package org.gallery.demo.common.util;

import java.awt.*;

public class CommonUtil {

    public static boolean isLinuxSystem() {
        String os = System.getProperty("os.name");
        return os.equalsIgnoreCase("Linux");
    }

    public static boolean isWindowsSystem() {
        String os = System.getProperty("os.name");
        return os.equalsIgnoreCase("Windows");
    }

    public static Dimension getScreenDimension(){
       return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
