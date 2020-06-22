//=====================================================================================
// TraceInfoAPI.java
// 	This is a container class that captures the trace details. It also provides
// 	functionality to determine the id of the corresponding value. This id can be used
// 	for subsequent queries as well as filling in trace information when it's being
// 	written.
//=====================================================================================

public class TraceInfoAPI extends APIResponseContainer
{
	public int trace_id = -99;
	public String date = "yyyy-MM-dd";
	public String time = "00:00";
	public String duration = "0";
	public String type = "test";
	public String account = "no account";
	public String first_name = "Dana";
	public String last_name = "Sculley";
	public String project_name = "no project";
	public String subject = "much to do about nothing";
	public String text = "no much detail here";
	public boolean completed = false;

	//================================================
	// Functions
	//================================================
	
	// Gettors
	public int getTraceID() { return trace_id; }
	public String getDate() { return date; }
	public String getTime() { return time; }
	public String getDuration() { return duration; }
	public String getType() { return type; }
	public String getAccount() { return account; }
	public String getContact() { return last_name + ", " + first_name; }
	public String getProject() { return project_name; }
	public String getSubject() { return subject; }
	public String getText() { return text; }
	public boolean getCompleted() { return completed; }

	// Settors
	public void setTraceID(int id) { trace_id = id; }
	public void setDate(String day) { date = day; }
	public void setTime(String t) { time = t; }
	public void setType(String t) { type = t; }
	public void setDuration(String d) { duration = d; }
	public void setAccount(String a) { account = a; }
	public void setContact(String fullName)
	{
		String[] name = fullName.split(", ");
		if(name.length>0)
		{
			last_name = name[0];
			first_name = name[1];
		}
		else
		{
			last_name = "Riker";
			first_name = "John";
		}

	}
	public void setProject(String p) { project_name = p; }
	public void setSubject(String s) { subject = s; }
	public void setText(String t) { text = t; }
	public void setComplete(boolean c) { completed = c; }

	// Other functions
	
	public void newDetails()
	{
		trace_id = -99;
		date = "2020-04-02";
		time = "00:00";
		duration = "5";
		type = "Email";
		account = "Account Name";
		first_name = "Dana";
		last_name = "Sculley";
		project_name = "Project Name";
		subject = "";
		text = "";
		completed = false;
	}	

	public String generateUploadString(int acc_id, int cont_id, int proj_id)
	{
		String uploadString = "{\"cmd\": \"uploadTrace\", "
						+ "\"trace_id\": " + trace_id + ", "
						+ "\"date\": " + date + ", "
						+ "\"time\": " + time + ", "
						+ "\"duration\": " + duration + ", "
						+ "\"type\": " + type + ", "
						+ "\"account_id\": " + acc_id + ", "
						+ "\"contact_id\": " + cont_id + ", "
						+ "\"project_id\": " + proj_id + ", "
						+ "\"subject\": " + subject + ", "
						+ "\"text\": " + text + "\"}"; 
		return uploadString;
	}

}
