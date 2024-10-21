package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;
import org.kaleta.passman.gui.log.MyFormatter;
import org.kaleta.passman.service.Service;
import org.kaleta.passman.service.ServiceFailureException;
import org.kaleta.passman.service.ServiceImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class Gui extends JFrame implements Editable{
    private static final Logger LOG = Logger.getLogger(Gui.class.getName());
    private static final String VERSION = "1.0";
    private static final String NAME = "Password Manager";
    private Service service;

    public Gui(){
        applySettingsBeforeInit();
        initService();
        initComponents();
    }

    private void applySettingsBeforeInit(){
        try {
            String dataDirPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/";
            File dataDir = new File(dataDirPath);
            if (!dataDir.exists()){
                dataDir.mkdir();
            }

            File file = new File(dataDirPath + "log.log");
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileHandler fh = new FileHandler(file.getCanonicalPath(),true);
            SimpleFormatter formatter = new MyFormatter();
            fh.setFormatter(formatter);
            LOG.addHandler(fh);
            LOG.setLevel(Level.INFO);
            LOG.setUseParentHandlers(false);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initService(){
        service = new ServiceImpl();
        String password = JOptionPane.showInputDialog("Enter your global password.");
        if (password == null){
            System.exit(-1);
        }
        try {
            service.setPassword(password);
        } catch (IllegalAccessException  e) {
            LOG.warning("Unauthorized Access Attempt");
            GuiUtils.showAccessDeniedDialogAndExit(this);
        }
    }

    private void initComponents() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        JMenu menuHelp = new JMenu("Help");
        menuBar.add(menuHelp);

        JMenuItem menuItemAbout = new JMenuItem("About");
        menuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("name: " + NAME + "\n");
                sb.append("version: " + VERSION + "\n\n");
                sb.append("developer: Stanislav Kaleta \n");
                sb.append("contact: kstanleykale@gmail.com \n\n");
                sb.append("testers: Stanislav Kaleta, Ludmila Florekova \n\n");
                sb.append("\u00a9 2015 Stanislav Kaleta All rights reserved");

                JOptionPane.showMessageDialog(Gui.this, sb.toString(), "About", JOptionPane.PLAIN_MESSAGE);
            }
        });
        menuHelp.add(menuItemAbout);

        JMenuItem menuItemNewRecord = new JMenuItem("New Record...");
        menuItemNewRecord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(Gui.this, "Add New Record",JDialog.DEFAULT_MODALITY_TYPE);
                ItemDialogPanel dialogPanel = new ItemDialogPanel(dialog, true);
                dialog.setContentPane(dialogPanel);
                dialog.pack();
                dialog.setVisible(true);

                if(dialogPanel.isConfirmed()){
                    Item item = new Item();
                    item.setName(dialogPanel.getValues()[0]);
                    item.setLogin(dialogPanel.getValues()[1]);
                    item.setPassword(dialogPanel.getValues()[2]);
                    item.setLink(dialogPanel.getValues()[3]);
                    Gui.this.addItem(item);
                }
            }
        });
        menuFile.add(menuItemNewRecord);

        JMenuItem menuItemChangePassword = new JMenuItem("Change Global Password...");
        menuItemChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        String password = JOptionPane.showInputDialog("Enter new global password");
                        if (password != null){
                            try {
                                service.changePassword(password);
                            } catch (ServiceFailureException sfe) {
                                LOG.severe(sfe.getMessage());
                                GuiUtils.showErrorDialogWithInfo(Gui.this, sfe.getMessage());
                            } catch (IllegalAccessException iae) {
                                LOG.warning("Unauthorized Access Attempt");
                                GuiUtils.showAccessDeniedDialogAndExit(Gui.this);
                            }
                        }
                        return null;
                    }
                }.execute();
            }
        });
        menuFile.add(menuItemChangePassword);

        menuFile.add(new JSeparator());

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuFile.add(menuItemExit);


        this.setTitle(NAME + " " + VERSION);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        List<Item> itemList = new ArrayList<>();
        try {
            itemList.addAll(service.retrieveData());
        } catch (IllegalAccessException e) {
            LOG.warning("Unauthorized Access Attempt");
            GuiUtils.showAccessDeniedDialogAndExit(this);
        } catch (ServiceFailureException e) {
            LOG.severe(e.getMessage());
            GuiUtils.showErrorDialogWithInfo(this, e.getMessage());
        }

        /*TODO scrollpane*/

        updateGui(itemList);


    }

    private void updateGui(List<Item> itemList){
        this.getContentPane().removeAll();
        for (Item item : itemList) {
            EditAction editAction = new EditAction(this, item);
            DeleteAction deleteAction = new DeleteAction(this, item);
            ItemGui itemGui = new ItemGui(editAction, deleteAction);
            itemGui.update(item);
            this.add(itemGui);
        }
        this.repaint();
        this.pack();
    }

    @Override
    public void addItem(final Item item){
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    List<Item> itemList = service.retrieveData();
                    itemList.add(item);
                    service.updateData(itemList);
                    updateGui(itemList);
                } catch (ServiceFailureException e) {
                    LOG.severe(e.getMessage());
                    GuiUtils.showErrorDialogWithInfo(Gui.this, e.getMessage());
                } catch (IllegalAccessException e) {
                    LOG.warning("Unauthorized Access Attempt");
                    GuiUtils.showAccessDeniedDialogAndExit(Gui.this);
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void editItem(final Item item) {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    List<Item> itemList = service.retrieveData();

                    int updated = 0;
                    for (Item item1 : itemList){
                        if (item1.getName().equals(item.getName())){
                            updated++;
                            item1.setLogin(item.getLogin());
                            item1.setPassword(item.getPassword());
                            item1.setLink(item.getLink());
                        }
                    }
                    if (updated != 1){
                        String msg = "more or none record with name \"" + item.getName() + "\" !";
                        LOG.severe(msg);
                        GuiUtils.showErrorDialogWithInfo(Gui.this, msg);
                        return null;
                    }

                    service.updateData(itemList);
                    updateGui(itemList);
                } catch (ServiceFailureException e) {
                    LOG.severe(e.getMessage());
                    GuiUtils.showErrorDialogWithInfo(Gui.this, e.getMessage());
                } catch (IllegalAccessException e) {
                    LOG.warning("Unauthorized Access Attempt");
                    GuiUtils.showAccessDeniedDialogAndExit(Gui.this);
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void deleteItem(final Item item) {
        new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    List<Item> itemList = service.retrieveData();

                    itemList.remove(item);

                    service.updateData(itemList);
                    updateGui(itemList);
                } catch (ServiceFailureException e) {
                    LOG.severe(e.getMessage());
                    GuiUtils.showErrorDialogWithInfo(Gui.this, e.getMessage());
                } catch (IllegalAccessException e) {
                    LOG.warning("Unauthorized Access Attempt");
                    GuiUtils.showAccessDeniedDialogAndExit(Gui.this);
                }
                return null;
            }
        }.execute();
    }

    public static void main(String[] args) {
        new Gui().setVisible(true);
    }
}
