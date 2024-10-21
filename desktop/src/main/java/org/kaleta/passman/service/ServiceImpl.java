package org.kaleta.passman.service;

import org.kaleta.passman.data.Manager;
import org.kaleta.passman.data.ManagerException;
import org.kaleta.passman.data.XmlManager;
import org.kaleta.passman.data.model.Document;
import org.kaleta.passman.entity.Item;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class ServiceImpl implements Service{
    private String privateKey;
    private Manager manager;

    public ServiceImpl(){
        privateKey = null;
        manager = new XmlManager();
        checkFirstUse();
    }

    @Override
    public void setPassword(String password) throws IllegalAccessException {
        try {
            password = normalizePassword(password);
            String encryptedPassword = manager.retrieveDocument().getPassword();

            String decryptedPassword = decrypt(encryptedPassword, password);
            if (decryptedPassword.equals(password)){
                privateKey = password;
            } else {
                privateKey = null;
                throw new IllegalAccessException();
            }
        } catch (ManagerException | ServiceFailureException e) {
            throw new IllegalAccessException();
        }

    }

    @Override
    public void changePassword(String newPassword) throws IllegalAccessException, ServiceFailureException {
        if (privateKey == null){
            throw new IllegalAccessException();
        }

        newPassword = normalizePassword(newPassword);

        List<Item> itemList = retrieveData();
        Document document = new Document();
        document.setPassword(encrypt(newPassword, newPassword));
        try {
            manager.updateDocument(document);
        } catch (ManagerException e) {
            throw new ServiceFailureException(e);
        }
        setPassword(newPassword);
        updateData(itemList);
    }

    @Override
    public List<Item> retrieveData() throws IllegalAccessException, ServiceFailureException {
        if (privateKey == null){
            throw new IllegalAccessException();
        }

        List<Item> decryptedDataList = new ArrayList<>();
        List<Document.Data> encryptedDataList = null;
        try {
            encryptedDataList = manager.retrieveDocument().getDataList();
        } catch (ManagerException e) {
            throw new ServiceFailureException(e);
        }

        for (Document.Data data : encryptedDataList){
            String decryptedData = decrypt(data.getHash(), privateKey);
            Item item = deconstructAttributeString(decryptedData);
            decryptedDataList.add(item);
        }

        return decryptedDataList;
    }

    @Override
    public void updateData(List<Item> itemList) throws IllegalAccessException, ServiceFailureException {
        if (privateKey == null){
            throw new IllegalAccessException();
        }

        List<Document.Data> dataList = new ArrayList<>();

        for (Item item : itemList){
            String dataString = constructAttributeString(item);
            String encryptedData = encrypt(dataString, privateKey);

            Document.Data data = new Document.Data();
            data.setHash(encryptedData);
            dataList.add(data);
        }

        try {
            Document document = manager.retrieveDocument();
            document.getDataList().clear();
            document.getDataList().addAll(dataList);
            manager.updateDocument(document);
        } catch (ManagerException e) {
            throw new ServiceFailureException(e);
        }
    }

    private String decrypt(String encryptedString, String privateKey) throws ServiceFailureException {
        byte[] outputBytes;
        try {
            Key key = new SecretKeySpec(privateKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] inputBytes = new BASE64Decoder().decodeBuffer(encryptedString);
            outputBytes = cipher.doFinal(inputBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException e) {
            throw new ServiceFailureException(e);
        }
        return new String(outputBytes);
    }

    private String encrypt(String stringToEncrypt, String privateKey) throws ServiceFailureException {
        byte[] outputBytes;
        try {
            Key key = new SecretKeySpec(privateKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            outputBytes = cipher.doFinal(stringToEncrypt.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ServiceFailureException(e);
        }
        return new BASE64Encoder().encode(outputBytes);
    }

    public String normalizePassword(String password) throws ServiceFailureException {
        // n from <4,16> -> 16 bytes
        if (password.length() < 4 || password.length() > 16){
            throw new ServiceFailureException("password length is not in interval <4,16>");
        }

        String normalizedPassword = "";
        for (int i=0;i<16-password.length();i++){
            normalizedPassword += "$";
        }
        normalizedPassword += password;

        return normalizedPassword;
    }

    private String constructAttributeString(Item item) throws ServiceFailureException {
        String name = item.getName();
        if (name.length() > 30){
            throw new ServiceFailureException("name length > 30");
        }
        String nameOutput ="";
        for (int i=0;i<30-name.length();i++){
            nameOutput += "$";
        }
        nameOutput += name;

        String login = item.getLogin();
        if (login.length() > 30){
            throw new ServiceFailureException("login length > 30");
        }
        String loginOutput ="";
        for (int i=0;i<30-login.length();i++){
            loginOutput += "$";
        }
        loginOutput += login;

        String password = item.getPassword();
        if (password.length() > 30){
            throw new ServiceFailureException("password length > 30");
        }
        String passwordOutput ="";
        for (int i=0;i<30-password.length();i++){
            passwordOutput += "$";
        }
        passwordOutput += password;

        String link = item.getLink();
        if (link.length() > 30){
            throw new ServiceFailureException("link length > 30");
        }
        String linkOutput ="";
        for (int i=0;i<30-link.length();i++){
            linkOutput += "$";
        }
        linkOutput += link;

        return nameOutput + loginOutput + passwordOutput + linkOutput;
    }

    private Item deconstructAttributeString(String attributeString) throws ServiceFailureException {
        if (attributeString.length() != 120){
            throw new ServiceFailureException("attribute string length is not 120");
        }
        String name = attributeString.substring(0, 30).replace("$", "");
        String login = attributeString.substring(30,60).replace("$", "");
        String password = attributeString.substring(60,90).replace("$", "");
        String link = attributeString.substring(90,120).replace("$", "");

        Item item = new Item();
        item.setName(name);
        item.setLogin(login);
        item.setPassword(password);
        item.setLink(link);

        return item;
    }

    private void checkFirstUse() {
        String dataDirPath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/";
        File dataDir = new File(dataDirPath);
        if (!dataDir.exists()){
            dataDir.mkdir();
        }

        String fileName = "db.xml";
        File file = new File(dataDirPath + fileName);
        if (!file.exists()){
            try {
                file.createNewFile();

                String globalPassword = JOptionPane.showInputDialog("Insert your global password (from 4 to 16 characters).");
                globalPassword = normalizePassword(globalPassword);
                Document document = new Document();
                String encryptedGlobalPassword = encrypt(globalPassword, globalPassword);
                document.setPassword(encryptedGlobalPassword);
                manager.updateDocument(document);
                setPassword(globalPassword);


            } catch (IOException | ManagerException | ServiceFailureException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
