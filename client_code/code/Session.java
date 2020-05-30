//============================================================================
// Session.java
// 	This class stores all the session variables associated with the task
// 	manager process. These session include the current date, the username,
// 	first name, last name, and token.
//============================================================================

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Session
{
	// Variables
	String username, firstname, lastname, token, server;
	boolean loggedIn; // Is the user logged in?
	int returnCode; // The "error" code returned by API calls.
	DateFormat dFormat;
	Date today;
	String curDate;

	public Session()
	{
		dFormat = new SimpleDateFormat("yyyy-MM-dd");
		curDate = "YYYY-mm-dd";
		today = new Date();
		username = "none";
		firstname = "none";
		lastname = "none";
		token = "none";
		loggedIn = false;
		server = "none";
		returnCode = -99;
	}

	// Get Functions
	public String getCurDate() { return curDate; }
	public String getDate(){ return dFormat.format(today); }
	public String getFirstname() { return firstname; }
	public String getFullname() { return firstname + " " + lastname; }
	public String getLastname() { return lastname; }
	public boolean getLoggedIn() { return loggedIn; }
	public int getReturnCode() { return returnCode; }
	public String getServer() { return server; }
	public String getToken() { return token; }
	public String getUsername(){ return username; }

	// Set Functions
	public void setCurDate(String nDate) { curDate = nDate; }
	public void setFirstname(String first) { firstname = first; }
	public void setFullname(String first, String last)
	{
		firstname = first;
		lastname = last;
	}
	public void setLastname(String last) { lastname = last; }
	public void setLoggedIn(boolean status) { loggedIn = status; }
	public void setReturnCode(int code) { returnCode = code; }
	public void setServer(String host) { server = host; }
	public void setToken(String newToken){ token = newToken; }
	public void setUsername(String newUser){ username = newUser; }


}
