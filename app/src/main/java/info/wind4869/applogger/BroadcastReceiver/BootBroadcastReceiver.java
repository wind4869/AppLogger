package info.wind4869.applogger.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import info.wind4869.applogger.Service.LogService;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("AppLogger", "boot completed");

            // start service
            Intent serviceIntent = new Intent(context, LogService.class);
            context.startService(serviceIntent);
        }
    }
}
