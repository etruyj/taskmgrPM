//=============================================================================
// TraceDetails
// 	A container class to hold the results of the listTaskDetails API call
// 	and fed into the taskDetailPane
//=============================================================================

public class traceInfo
{
	private String msg;
	private int code;
	private int trace_id;
	private String date;
	private String time;
	private int duration;
	private String type;
	private String account;
	private String first_name;
	private String last_name;
	private String project_name;
	private String subject;
	private String text;
	private boolean completed;

	//==========================================
	// Functions
	//==========================================

	public String getMsg() { return msg; }
	public int getCode() { return code; }
	public int getTraceID() { return trace_id; }
	public String getDate() { return date; }
	public String getTime() { return time; }
	public int getDuration() { return duration; }
	public String getType() { return type; }
	public String getAccount() { return account; }
	public String getFirstName() { return first_name; }
	public String getLastName() { return last_name; }
	public String getProject() { return project_name; }
	public String getSubject() { return subject; }
	public String getText() { return text; }
	public boolean getCompleted() { return completed; }	

	public void newTrace(Session sesh)
	{
		msg = "Empty trace created.";
		code = 2;
		trace_id = -1;
		date = sesh.getCurDate();
		time = "13:00";
		duration = 5;
		type = "none";
		account = "none";
		first_name = "Fox";
		last_name = "Mulder";
		project_name = "Anastasi";
		subject = "none";
		text = "";
		completed = false;
	}
}
