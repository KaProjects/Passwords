package org.kaleta.passman.gui;

import org.kaleta.passman.entity.Item;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public interface Editable {

    public void addItem(Item item);

    public void editItem(Item item);

    public void deleteItem(Item item);
}
