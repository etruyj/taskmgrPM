//=============================================================================
// Tracelist.java
//	A list of traces (tasks) to complete for the day.
//
//=============================================================================

import java.sql.Date;
import java.sql.Time;

public class traceList
{
	private String msg;
	private int code;
	private trace[] traceList;
	private int listSize; // max size of the traceList;
	private int usedList; // the number of elements used by the trace list.

	public traceList(int size)
	{
		listSize = size;
		usedList = 0;
		traceList = new trace[listSize];
	}
	
	//================================================
	// Functions
	//================================================

	// Gettors - General
	public int getCode() { return code; }
	public String getMSG() { return msg; }
	public int getTraceCount() { return usedList; }	

	// Gettors - Trace Information
	public String getAccount(int traceNum) { return traceList[traceNum].account; }
	public boolean getCompleteStatus(int traceNum) { return traceList[traceNum].completed; }
	public String getContactName(int traceNum) { return traceList[traceNum].first_name + " " + traceList[traceNum].last_name; } 
	public String getDay(int traceNum) { return traceList[traceNum].day; }
	public int getDuration(int traceNum) { return traceList[traceNum].duration; }
	public String getProject(int traceNum) { return traceList[traceNum].project; }
	public String getSubject(int traceNum) { return traceList[traceNum].subject; }
	public String getText(int traceNum) { return traceList[traceNum].text; }
	public String getTime(int traceNum) { return traceList[traceNum].time; }
	public int getTraceID(int traceNum) { return traceList[traceNum].trace_id; }
	public boolean getUpdateStatus(int traceNum) { return traceList[traceNum].updated; }
	public boolean getUploadStatus(int traceNum) { return traceList[traceNum].uploaded; }

	// Settors
	public void addTrace(int length, String date, String timestamp, String acct, String first, String last, String proj, String subj, String txt, boolean comp)
	{
		if(usedList==listSize)
		{
			traceList = expandTraces();
		}

		traceList[usedList].duration = length;
		traceList[usedList].day = date;
		traceList[usedList].time = timestamp;
		traceList[usedList].account = acct;
		traceList[usedList].first_name = first;
		traceList[usedList].last_name = last;
		traceList[usedList].project = proj;
		traceList[usedList].subject = subj;
		traceList[usedList].text = txt;
		traceList[usedList].completed = comp;
		traceList[usedList].uploaded = false;
		traceList[usedList].updated = false;
		usedList++;
	}
	public void countTraces()
	{
		// This action can only be performed if traces
		// were returned. Otherwise, we'll make sure the
		// value is 0.
		
		if(code>0)
		{
			// Make the listSize equal to the usedList
			// value as I'm not sure how JAVA/GSON handles
			// array creation. Playing it safe, I'm assuming
			// it creates a perfectly sized array to the 
			// json.

			usedList = traceList.length;
			listSize = usedList; 
			
		}
		else
		{
			listSize = 0;
			usedList = 0;
		}
	}
	public trace[] expandTraces()
	{
		int newSize = 2 * listSize;
		trace[] newList = new trace[newSize];

		for(int i=0; i<usedList; i++)
		{
			newList[i].trace_id = traceList[i].trace_id;
			newList[i].duration = traceList[i].duration;
			newList[i].day = traceList[i].day;
			newList[i].time = traceList[i].time;
			newList[i].account = traceList[i].account;
			newList[i].first_name = traceList[i].first_name;
			newList[i].last_name = traceList[i].last_name;
			newList[i].project = traceList[i].project;
			newList[i].subject = traceList[i].subject;
			newList[i].text = traceList[i].text;
			newList[i].completed = traceList[i].completed;
			newList[i].uploaded = traceList[i].uploaded;
			newList[i].updated = traceList[i].updated;
		}
		return newList;
	}

	//================================================
	// Inner Classes
	// 	Trace - the building block of the trace list.
	//================================================
	private class trace
	{
		public int trace_id,  duration;
		String day;
	       	String time;
		String account, first_name, last_name, project;	
		String subject, text;
		boolean completed, uploaded, updated;

		private trace()
		{
			trace_id = 0;
			day = "0000-00-00";
			time = "00:00:00";
			account = "account";
			first_name = "first";
			last_name = "last";
			project = "project";
			subject = "subject";
			text = "body text";
			completed = uploaded = false;
			updated = true;
		}
			
	}

}
