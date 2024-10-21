package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class EditAction extends AbstractAction {
    private Editable editable;
    private Item item;

    public EditAction(Editable editable, Item item){
        super("Edit");
        this.editable = editable;
        this.item = item;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog dialog = new JDialog((Frame) editable, "Edit " + item.getName(),JDialog.DEFAULT_MODALITY_TYPE);
        ItemDialogPanel dialogPanel = new ItemDialogPanel(dialog, false);
        dialogPanel.setValues(item.getName(), item.getLogin(), item.getPassword(), item.getLink());
        dialog.setContentPane(dialogPanel);
        dialog.pack();
        dialog.setVisible(true);

        if(dialogPanel.isConfirmed()){
            item.setLogin(dialogPanel.getValues()[1]);
            item.setPassword(dialogPanel.getValues()[2]);
            item.setLink(dialogPanel.getValues()[3]);
            editable.editItem(item);
        }
    }
}
