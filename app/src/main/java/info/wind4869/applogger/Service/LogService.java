package info.wind4869.applogger.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import info.wind4869.applogger.Tools.Process;

public class LogService extends Service {

	public LogService() {
		numOfInstalledApp = 0;
		prevProcess = null;
		curProcess = null;

		nameInfoMap = new HashMap<>();

		// get file path in sdCard
		File sdCardDir = Environment.getExternalStorageDirectory();
		if (sdCardDir != null) {
			filePath = sdCardDir.toString() + fileName;
		} else {
			throw new RuntimeException("sd card not found");
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("AppLogger", "LogService onCreate");

		Logging(); // start the logging thread
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("AppLogger", "LogService onStartCommand");

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		Log.d("AppLogger", "LogService onDestroy");

		// restart service
        Intent intent = new Intent(this, LogService.class);
        startService(intent);
    }

	private void Logging() {
		Log.d("AppLogger", "start logging ... ");

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					initOtherMembers(); // init other members
					logAppUsages(); // the main routine
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.e("AppLogger", "InterruptedException in log thread");
					}
				}
			}
		}).start();
	}

	private void initOtherMembers() {
		// get ActivityManager
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		// get PackageManager
		packageManager = getPackageManager();

		// get ApplicationInfo of all applications installed
		applicationInfos = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);

		// update the <String(processName): ApplicationInfo> map
		if (applicationInfos.size() != numOfInstalledApp) {
			Log.d("AppLogger", "number of app changed");

			numOfInstalledApp = applicationInfos.size();
			for (ApplicationInfo ai : applicationInfos) {
				nameInfoMap.put(ai.processName, ai);
			}
		}
	}

	private void logAppUsages() {
		// get name of current process (deprecated after android 5.0)
		String processName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();

		if(!processName.equals("info.wind4869.applogger") && !processName.equals("android")) {

			if (prevProcess == null) {
				curProcess = createProcess(processName);
				if (curProcess == null) return;

				prevProcess = curProcess; // pay attention!

				Log.d("AppLogger", curProcess.getAppLabel() + " starts at " + curProcess.getStartTime());

			} else if (!processName.equals(prevProcess.getProcessName())) {
				curProcess = createProcess(processName);
				if (curProcess == null) return;

				// end of previous process
				prevProcess.setEndTime(curProcess.getStartTime());
				Log.d("AppLogger", prevProcess.getAppLabel() + " ends at " + curProcess.getStartTime());

				// write previous process record to file
				writeRecordToFile(prevProcess.toString(), filePath);

				// start of current process
				prevProcess = curProcess; // pay attention!
				Log.d("AppLogger", curProcess.getAppLabel() + " starts at " + curProcess.getStartTime());
			}
		}
	}

	private Process createProcess(String processName) {
		// get ApplicationInfo by processName from nameInfoMap
		ApplicationInfo applicationInfo = nameInfoMap.get(processName);
		if (applicationInfo == null) return null;

		// get appLabel and uid from ApplicationInfo
		String appLabel = applicationInfo.loadLabel(packageManager).toString();
		String uid = String.valueOf(applicationInfo.uid);

		// get current time
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		String startTime = new StringBuffer(String.valueOf(year))
				.append("-").append(month).append("-").append(day).append(" ")
				.append(hour).append(":").append(minute).append(":").append(second)
				.toString();

		return new Process(processName, appLabel, startTime, null, uid);
	}

	private void writeRecordToFile(String record, String fileName) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName, true);
			fileWriter.write(record + "\n");
			fileWriter.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e1) {
					throw new RuntimeException("fail to close file");
				}
			}
		}
	}

	private int numOfInstalledApp;
	private Process prevProcess;
	private Process curProcess;

	private HashMap<String, ApplicationInfo> nameInfoMap;
	private String filePath;

	private ActivityManager activityManager;
	private PackageManager packageManager;
	List<ApplicationInfo> applicationInfos;

	private static final String fileName = "/applog.txt";
}
