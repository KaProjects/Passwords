package org.kaleta.passman.data;

import org.kaleta.passman.data.model.Document;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public interface Manager {

    public void updateDocument(Document document) throws ManagerException;
    public Document retrieveDocument() throws ManagerException;
}
