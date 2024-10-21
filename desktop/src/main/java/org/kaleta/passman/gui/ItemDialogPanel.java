package org.kaleta.passman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ItemDialogPanel extends JPanel {
    private JDialog dialog;

    private JTextField textFieldName;
    private JTextField textFieldLogin;
    private JTextField textFieldPassword;
    private JTextField textFieldLink;

    private boolean confirmed;

    public ItemDialogPanel(JDialog dialog, boolean isCreate){
        this.dialog = dialog;
        initComponents();
        confirmed = false;
        textFieldName.setEnabled(isCreate);
    }

    private void initComponents() {
        JLabel labelName = new JLabel("Name: ");
        textFieldName = new JTextField();
        JLabel labelLogin = new JLabel("Login: ");
        textFieldLogin = new JTextField();
        JLabel labelPassword = new JLabel("Password: ");
        textFieldPassword = new JTextField();
        JLabel labelLink = new JLabel("Link: ");
        textFieldLink = new JTextField();

        JButton buttonOk = new JButton("OK");
        buttonOk.setBackground(Color.LIGHT_GRAY);
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = true;
                dialog.dispose();
            }
        });

        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.setBackground(Color.LIGHT_GRAY);
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dialog.dispose();
            }
        });

        this.setBackground(Color.LIGHT_GRAY);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(labelName, 75, 75, 75)
                        .addComponent(textFieldName, 200, 200, 200)
                        .addGap(5))
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(labelLogin, 75, 75, 75)
                        .addComponent(textFieldLogin, 200, 200, 200)
                        .addGap(5))
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(labelPassword, 75, 75, 75)
                        .addComponent(textFieldPassword, 200, 200, 200)
                        .addGap(5))
                .addGroup(layout.createSequentialGroup()
                        .addGap(5)
                        .addComponent(labelLink, 75, 75, 75)
                        .addComponent(textFieldLink, 200, 200, 200)
                        .addGap(5))
                .addGroup(layout.createSequentialGroup()
                        .addGap(75)
                        .addComponent(buttonOk)
                        .addGap(10)
                        .addComponent(buttonCancel)));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelName, 20, 20, 20)
                        .addComponent(textFieldName, 20, 20, 20))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelLogin, 20, 20, 20)
                        .addComponent(textFieldLogin, 20, 20, 20))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelPassword, 20, 20, 20)
                        .addComponent(textFieldPassword, 20, 20, 20))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelLink, 20, 20, 20)
                        .addComponent(textFieldLink, 20, 20, 20))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(buttonOk)
                        .addComponent(buttonCancel))
                .addGap(5));
    }

    public void setValues(String name, String login, String password, String link) {
        textFieldName.setText(name);
        textFieldLogin.setText(login);
        textFieldPassword.setText(password);
        textFieldLink.setText(link);
    }

    public String[] getValues() {
        return new String[]{textFieldName.getText(), textFieldLogin.getText(), textFieldPassword.getText(), textFieldLink.getText()};
    }

    public boolean isConfirmed(){
        return confirmed;
    }


}
