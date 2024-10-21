package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;

import javax.swing.*;
import java.awt.*;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ItemGui extends JPanel {
    private ItemPanel itemPanel;
    private JButton buttonEdit;
    private JButton buttonDelete;

    public ItemGui(EditAction editAction, DeleteAction deleteAction){
        initComponents();
        buttonEdit.setAction(editAction);
        buttonDelete.setAction(deleteAction);
    }

    private void initComponents() {
        itemPanel = new ItemPanel();

        buttonEdit = new JButton();
        buttonEdit.setBackground(Color.GRAY);
        buttonEdit.setFont(new Font(new JButton().getFont().getName(),Font.BOLD, 20));

        buttonDelete = new JButton();
        buttonDelete.setBackground(Color.GRAY);
        buttonDelete.setFont(new Font(new JButton().getFont().getName(),Font.BOLD, 20));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(itemPanel)
                .addComponent(buttonEdit)
                .addComponent(buttonDelete));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(itemPanel)
                .addComponent(buttonEdit,43,43,43)
                .addComponent(buttonDelete,43,43,43));
    }

    public void update(Item item) {
        itemPanel.update(item);
    }
}
