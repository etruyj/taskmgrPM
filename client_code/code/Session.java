//======================================================================================
// Session.java
// 	This class contains all the information relative to the connection to the server.
//======================================================================================

import java.text.SimpleDateFormat;
import java.util.Date;

public class Session
{
	private String username;
	private String token;
	private String today;
	private String currentDate;
	private String serverAddress;

	//==============================================
	// Constructor	
	//==============================================

	public Session()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
	
		username = "none";
		token = "none";
		serverAddress = "localhost";	
		currentDate = today = formatter.format(date);
	}

	//==============================================
	// Functions
	//==============================================
	
	// Get Functions
	public String getUsername() { return username; }
	public String getToken() { return token; }
	public String getToday() { return today; }
	public String getCurrentDate() { return currentDate; }
	public String getServerAddress() {return serverAddress; }

	// Set Functions
	public void setCurrentDate(String curDate) { currentDate = curDate; }
	public void setServerAddress(String serv) { serverAddress = serv; }
	public void setToken(String t) { token = t; }
	public void setUsername(String user) { username = user; }

	// API JSON Functions
	public String generateAllAccountsString()
	{
		String accountString = "{\"cmd\":\"listAccounts\", "
					+ "\"token\": \"" + token + "\"}";
		return accountString;
	}

	public String generateLoginString(String password)
	{
		String loginString = "{\"cmd\":\"login\", "
					+ "\"user\":\"" + username + "\","
					+ "\"pass\":\"" + password + "\"}";

		return loginString;
	}
}
