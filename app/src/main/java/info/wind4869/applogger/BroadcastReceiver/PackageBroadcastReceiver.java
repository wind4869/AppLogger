package info.wind4869.applogger.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import info.wind4869.applogger.Tools.Utility;

/**
 * Created by wind on 16/2/24.
 */
public class PackageBroadcastReceiver extends BroadcastReceiver {

    public PackageBroadcastReceiver() {
        utility = new Utility();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) { // add package

            String packageName = intent.getDataString().split(":")[1];
            utility.writeRecordToExternalStorage((new StringBuilder()).append("+ ").
                    append(utility.getCurrentTime()).append(" ").append(packageName).toString());

            Log.d("AppLogger", packageName + " added");

        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) { // remove package

            String packageName = intent.getDataString().split(":")[1];
            utility.writeRecordToExternalStorage((new StringBuilder()).append("- ").
                    append(utility.getCurrentTime()).append(" ").append(packageName).toString());

            Log.d("AppLogger", packageName + " removed");
        }
    }

    private Utility utility;
}
