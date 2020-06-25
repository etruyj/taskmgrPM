//======================================================================================
// Connector.java
// 	This class handles all the connection requests between the client and the server.
// 	It requires Google's gson, which is stored in the libraries sub-directory. 
//
// 	Compile
// 	> javac -d ../classes -cp ../libraries/gson-2.8.6.jar Connector.java
//======================================================================================

import com.google.gson.Gson;

import java.net.*;
import java.io.*;

public class Connector
{
	String address;
	URL server;
	URLConnection dbCon;
	Gson gson;

	//===================================================
	// Constructors
	//===================================================

	public Connector(String server)
	{
		address = "http://" + server + "/Services.php";
		gson = new Gson();
	}

	//===================================================
	// Functions
	//===================================================

	// API JSON Functions

	public AccountAPI decodeAllAccountsCall(String output)
	{
		return gson.fromJson(output, AccountAPI.class);
	}

	public ContactsAPI decodeContactsCall(String output)
	{
		return gson.fromJson(output, ContactsAPI.class);
	}

	public String decodeLoginCall(String output)
	{
		TokenAPI rJson = new TokenAPI();

		rJson = gson.fromJson(output, TokenAPI.class);

		if(rJson.getCode()>0)
		{
			return rJson.getToken();
		}
		else
		{
			System.out.println(rJson.getMsg());
			return "Incorrect";
		}
	}

	public ProjectsAPI decodeProjectCall(String output)
	{
		return gson.fromJson(output, ProjectsAPI.class);
	}

	public int decodeSaveCode(String output)
	{
		APISimpleResponse res = gson.fromJson(output, APISimpleResponse.class);
		return res.code;
	}

	public int decodeSaveID(String output)
	{
		APISimpleResponse res = gson.fromJson(output, APISimpleResponse.class);
		return res.id;
	}

	public String decodeSaveMSG(String output)
	{
		APISimpleResponse res = gson.fromJson(output, APISimpleResponse.class);
		return res.msg;
	}

	public TracesAPI decodeTracesCall(String output)
	{
		return gson.fromJson(output, TracesAPI.class);
	}

	public TraceInfoAPI decodeTraceInfoCall(String output)
	{
		return gson.fromJson(output, TraceInfoAPI.class);
	}

	public String generateAccountSearchString(String query, String token)
	{
		String accountString = "{\"cmd\":\"searchAccounts\", "
					+ "\"query\": \"" + query + "\", "
					+ "\"token\": \"" + token + "\"}";
		return accountString;
	}

	public String generateAllAccountsString(String token)
	{
		String accountString = "{\"cmd\":\"listAccounts\", "
					+ "\"token\": \"" + token + "\"}";
		return accountString;
	}

	public String generateContactsString(int acc_id, String token)
	{
		String contactsString = "{\"cmd\": \"listAccountContacts\", "
					+ "\"account_id\": \"" + acc_id + "\", "
				       	+ "\"token\": \"" + token + "\"}";
		return contactsString;		
	}

	public String generateLoginString(String user, String pass)
	{
		String loginString = "{\"cmd\": \"login\", "
					+ "\"user\":\"" + user + "\", "
					+ "\"pass\":\"" + pass + "\"}";
		return loginString;
	}

	public String generateProjectString(int acc_id, String token)
	{
		String projectString = "{\"cmd\": \"listAccountProjects\", "
					+ "\"account_id\": \"" + acc_id + "\", "
				       	+ "\"token\": \"" + token + "\"}";
		return projectString;			
	}

	public String generateSaveAccountString(int acc_id, String acc_name, String token)
	{
		String saveString = "{\"cmd\": \"saveAccount\", "
					+ "\"account_id\": \"" + acc_id + "\", "
					+ "\"name\": \"" + acc_name + "\", "
					+ "\"token\": \"" + token + "\"}";
		
		return saveString;
	}

	public String generateSaveContactString(int con_id, int acc_id, String firstName, String lastName, String position, String email, String phone, boolean includeInSearch, String token)
	{
		// Define the incValue for passing an int to MySQL via
		// PHP. MySQL's tinyint does not like true/false values
		// and conversely, Java's checkboxes are crabby with 1/0
		// values. For ease of use, this translation is performed
		// at the borders.

		int incValue = 0;
		if(includeInSearch)
		{
			incValue = 1;
		}
		
		String saveString = "{\"cmd\": \"saveContact\", "
					+ "\"contact_id\": \"" + con_id + "\", "
					+ "\"account_id\": \"" + acc_id + "\", "
					+ "\"firstName\": \"" + firstName + "\", "
					+ "\"lastName\": \"" + lastName + "\", "
					+ "\"position\": \"" + position + "\", "
					+ "\"email\": \"" + email + "\", "
					+ "\"phone\": \"" + phone + "\", "
					+ "\"toInclude\": \"" + incValue + "\", "
					+ "\"token\": \"" + token + "\"}";
		
		return saveString;
	}

	public String generateSaveProjectString(int proj_id, int acc_id, String proj_name, String token)
	{
		String saveString = "{\"cmd\": \"saveProject\", "
					+ "\"project_id\": \"" + proj_id + "\", "
					+ "\"account_id\": \"" + acc_id + "\", "
					+ "\"name\": \"" + proj_name + "\", "
					+ "\"token\": \"" + token + "\"}";
		
		return saveString;
	}

	public String generateSaveTraceString(int trace_id, int acc_id, int cont_id, int proj_id, int durs, String date, String time, String type, String subj, String info, boolean status, String token)
	{
		int intBool;
		if(status) { intBool=1; }
	       	else	{ intBool=0;	}	

		String saveString = "{\"cmd\": \"saveTrace\", "
				+ "\"trace_id\": \"" + trace_id + "\", "
				+ "\"date\": \"" + date + "\", "
				+ "\"time\": \"" + time + "\", "
				+ "\"account_id\": \"" + acc_id + "\", "
				+ "\"contact_id\": \"" + cont_id + "\", "
				+ "\"project_id\": \"" + proj_id + "\", "
				+ "\"duration\": \"" + durs + "\", "
				+ "\"type\": \"" + type + "\", "
				+ "\"subject\": \"" + subj + "\", "
				+ "\"text\": \"" + info + "\", "
				+ "\"completed\": \"" + intBool + "\", "
				+ "\"token\": \"" + token + "\"}";

		return saveString;
	}

	public String generateTraceContactsString(int acc_id, String token)
	{
		String contactsString = "{\"cmd\": \"listAccountContacts\", "
					+ "\"account_id\": \"" + acc_id + "\", "
				       	+ "\"token\": \"" + token + "\"}";
		return contactsString;		
	}

	public String generateTracebyDateString(String date, String token)
	{
		String tracesString = "{\"cmd\": \"listTraces\", "
					+ "\"day\": \"" + date + "\", "
				       	+ "\"token\": \"" + token + "\"}";
		return tracesString;
	}

	public String generateTraceInfoString(int id, String token)
	{
		String tracesString = "{\"cmd\": \"listTraceDetails\", "
					+ "\"trace_id\": \"" + id + "\", "
				       	+ "\"token\": \"" + token + "\"}";
		return tracesString;
	}

	// API call functions

	public String getResponse(String jsonInput)
	{
		String response = "EMPTY";
		String output = "EMPTY";

		try
		{
			server = new URL(address);

			URLConnection cxn = server.openConnection();
			cxn.setDoOutput(true);

			OutputStreamWriter out = new OutputStreamWriter(cxn.getOutputStream());

			out.write(jsonInput);
			out.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(cxn.getInputStream()));
		
			while((response = in.readLine()) != null)
			{
				// System.out.println(response) is
				//  for error handling API calls. Comment
				//  out before each version release or upload.
				//System.out.println(response);
				output = response;
				
			}
	
			in.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return output;
	}

	public String postAPIRequest(String input)
	{
		String jsonOutput = "{\"msg\":\"Connection Failed\", \"code\": \"-1\"}";

		try
		{
			jsonOutput = getResponse(input);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

		return jsonOutput;
	}

	public void setServer(String host)
	{
		address = "http://" + host + "/Services.php";
	}

	//====================================================
	// INNER CLASSES
	//====================================================
	
	private class APISimpleResponse
	{
		// This is just a lightweight variable grouping
		// for parsing the success and failure of save
		// queries
		public String msg;
		public int code;
		public int id; // Stores the new id for newly created items
	}
}
