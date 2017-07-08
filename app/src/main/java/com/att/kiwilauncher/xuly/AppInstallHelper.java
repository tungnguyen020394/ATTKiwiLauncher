package com.att.kiwilauncher.xuly;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 7/3/2017.
 */

public class AppInstallHelper {
    public static List<ApplicationInfo> getListInstalledApplication(Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        return applicationInfoList;
    }
    /* public static boolean checkInstalledApplication(String label, Context context) {
         List<ApplicationInfo> applicationInfoList = new ArrayList<>();
         applicationInfoList = getListInstalledApplication(context);
         for (int i = 0; i < applicationInfoList.size(); i++) {
             String appLabel = (String) context.getPackageManager().getApplicationLabel(applicationInfoList.get(i));
             if (appLabel.trim().toLowerCase().replace(" ", "").equals(label.trim().toLowerCase().replace(" ", ""))) {
                 return true;
             }
         }
         return false;
     }*/
    public static boolean checkInstalledApplication(String packageName, Context context) {
        List<ApplicationInfo> applicationInfoList = getListInstalledApplication(context);
        String installedPackageName = "";
        for (int i = 0; i < applicationInfoList.size(); i++) {
            installedPackageName = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA).get(i).packageName;
            if (installedPackageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void InstallAPK(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String command;
                command = "pm install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void installApp(Context context, String apkFileName) {
        String pathToDownloadFolder = "/KiwiStore/download/";
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        install.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory() + pathToDownloadFolder + apkFileName)),
                "application/vnd.android.package-archive");
        context.startActivity(install);
    }

    public static void uninstallApp(String packageName, Context context) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }


    public static String getFileZipName(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }
}
