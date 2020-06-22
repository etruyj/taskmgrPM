//=====================================================================================
// TracesAPI.java
// 	This is a container class that captures the trace information. It also provides
// 	functionality to determine the id of the corresponding value. This id can be used
// 	for subsequent queries as well as filling in trace information when it's being
// 	written.
//=====================================================================================

public class TracesAPI extends APIResponseContainer
{
	private traceSummary[] traceList;

	//================================================
	// Functions
	//================================================

	public void count() 
	{
		if(this.getCode()>0)
       		{	       
			this.setNum(traceList.length);
		}
		else
		{
			this.setNum(0);
		}	
	}
	public boolean getCompleted(int i) { return traceList[i].completed; }
	public String getDate(int i) { return traceList[i].date; }
	public String getSubject(int i) { return traceList[i].subject; }
	public String getTime(int i) { return traceList[i].time; }
	public int getTraceID(int i) { return traceList[i].trace_id; }
	public String getType(int i) { return traceList[i].type; }


	//================================================
	// Inner Class
	//================================================

	private class traceSummary
	{
		public int trace_id = -99;
		public String date = "yyyy-MM-dd";
		public String time = "00:00";
		public String duration = "0";
		public String type = "none";
		public String subject  = "none";
		public boolean completed = false;
	}
}
