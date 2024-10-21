package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class DeleteAction extends AbstractAction {
    private Editable editable;
    private Item item;

    public DeleteAction(Editable editable, Item item){
        super("Delete");
        this.editable = editable;
        this.item = item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog((Component) editable,
                "Are you sure you want to delete " + item.getName() + " record?",
                "Deleting " + item.getName() + " record",
                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if (result == 0){
            editable.deleteItem(item);
        }
    }
}
