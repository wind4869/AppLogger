package info.wind4869.applogger.Tools;

public class Process {

	public Process(String processName, String appLabel, String startTime, String endTime, String uid) {
		this.processName = processName;
		this.appLabel = appLabel;
		this.startTime = startTime;
		this.endTime = endTime;
		this.uid = uid;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLable) {
		this.appLabel = appLable;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return new StringBuffer(startTime).append(",").append(endTime).append(",")
				.append(processName).append(",").append(appLabel).append(",").append(uid).toString();
	}

	private String processName;
	private String appLabel;
	private String startTime;
	private String endTime;
	private String uid;
}
