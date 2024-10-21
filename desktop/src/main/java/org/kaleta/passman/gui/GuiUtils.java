package org.kaleta.passman.gui;

import javax.swing.*;
import java.awt.*;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class GuiUtils {

    public static void showAccessDeniedDialogAndExit(Component component){
        JOptionPane.showMessageDialog(component, "Access denied!", "Wrong Password!", JOptionPane.ERROR_MESSAGE);
        System.exit(-1);
    }

    public static void showErrorDialogWithInfo(Component component, String message){
        JOptionPane.showMessageDialog(component, "Internal Error occurred!\nInfo:\n" + message, "Error!", JOptionPane.ERROR_MESSAGE);
    }

}
