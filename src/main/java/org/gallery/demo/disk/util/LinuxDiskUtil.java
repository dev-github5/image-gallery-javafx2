package org.gallery.demo.disk.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LinuxDiskUtil {

    public static final String MEDIA_FS = "/media";

    public static List<String> getDrivesRootPath() {
        List<String> driveRootPaths = new ArrayList<>();
        for (String mountPoint : readMountEntries()) {
            String[] mount = mountPoint.split("\t");
            if (mount[1].contains(MEDIA_FS)) {
                //System.err.println("MountPoint" + mount[1]);
                driveRootPaths.add(mount[0]+":"+mount[1]);
            }
        }
        return driveRootPaths;
    }

    private static List<String> readMountEntries() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/mounts"), "UTF-8"));
            String response;
            StringBuilder stringBuilder = new StringBuilder();
            while ((response = bufferedReader.readLine()) != null) {
                stringBuilder.append(response.replaceAll(" +", "\t") + "\n");
            }
            bufferedReader.close();
            return new ArrayList<>(Arrays.asList(stringBuilder.toString().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
