package org.kaleta.passman.service;

import org.kaleta.passman.entity.Item;

import java.util.List;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public interface Service {

    public void setPassword(String password) throws IllegalAccessException;

    public void changePassword(String newPassword) throws IllegalAccessException, ServiceFailureException;

    public List<Item> retrieveData() throws IllegalAccessException, ServiceFailureException;

    public void updateData(List<Item> itemList) throws IllegalAccessException, ServiceFailureException;
}
