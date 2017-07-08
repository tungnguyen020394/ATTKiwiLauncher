package com.att.kiwilauncher.xuly;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by admin on 7/3/2017.
 */

public class AppInfoHelper {
    public static boolean capNhatVersion(String packageName, int versionCode, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            if (pi.versionCode < versionCode) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getApkFileName(String url){
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }
    public static String getPackageName(String label, Context context) {
        String packageName = "";
        List<ApplicationInfo> listApplicationInfo = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i = 0; i < listApplicationInfo.size(); i++) {
            if (context.getPackageManager().getApplicationLabel(listApplicationInfo.get(i)).toString().trim().toLowerCase().replace(" ", "").equals(
                    label.trim().toLowerCase().replace(" ", ""))) {
                packageName = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA).get(i).packageName;
                //Toast.makeText(context, "Ok-" + packageName, Toast.LENGTH_LONG).show();
                break;
            }
        }
        return packageName;
    }

    public void launchApp(String packageName, Context context) {
        Intent intent = new Intent();
        intent.setPackage(packageName);

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        if (resolveInfos.size() > 0) {
            ResolveInfo launchable = resolveInfos.get(0);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);

            context.startActivity(i);
        }
    }
}
