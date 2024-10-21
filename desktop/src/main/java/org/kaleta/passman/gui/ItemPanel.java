package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ItemPanel extends JPanel {

    private TitledBorder border;
    private JLabel labelLogin;
    private JButton buttonShowPassword;
    private String password;
    private JLabel labelLink;

    public ItemPanel(){
        initComponents();
    }

    private void initComponents() {
        this.setBackground(Color.GRAY);

        border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK,1), "---");
        this.setBorder(border);

        labelLogin = new JLabel(" ---");
        labelLogin.setBackground(Color.ORANGE);
        labelLogin.setOpaque(true);

        buttonShowPassword = new JButton("Show Password");
        buttonShowPassword.setBackground(Color.ORANGE);
        buttonShowPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ItemPanel.this, password, "Password", JOptionPane.PLAIN_MESSAGE);
            }
        });

        password = "---";

        labelLink = new JLabel(" ---");
        labelLink.setBackground(Color.ORANGE);
        labelLink.setOpaque(true);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(labelLogin, 175, 175, 175)
                .addGap(5)
                .addComponent(buttonShowPassword)
                .addGap(5)
                .addComponent(labelLink, 225, 225, 225));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(labelLogin,20,20,20)
                .addComponent(buttonShowPassword,20,20,20)
                .addComponent(labelLink,20,20,20));
    }

    public void update(Item item){
        border.setTitle(item.getName());
        labelLogin.setText(" " + item.getLogin());
        password = item.getPassword();
        labelLink.setText(" " + item.getLink());
        this.repaint();
    }
}
