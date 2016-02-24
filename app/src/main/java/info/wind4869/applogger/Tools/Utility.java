package info.wind4869.applogger.Tools;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by wind on 16/2/24.
 */
public class Utility {

    public Utility() {
        // get file path in external storage(sdCard)
        File sdCardDir = Environment.getExternalStorageDirectory();
        if (sdCardDir != null) {
            filePath = sdCardDir.toString() + "/applog.txt";  // fileName = "/applog.txt"
        } else {
            throw new RuntimeException("sd card not found");
        }
    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return new StringBuffer(String.valueOf(year))
                .append("-").append(month).append("-").append(day).append(" ")
                .append(hour).append(":").append(minute).append(":").append(second)
                .toString();
    }

    public void writeRecordToExternalStorage(String record) {
        writeRecordToFile(record, filePath);
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

    public String filePath;
}
