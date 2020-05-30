//=============================================================================
// Connector.java
// 	This class contains all the curl functionality associated with 
// 	managing the connection between this application and the remote
// 	API. Operations include CURL HTTP requests, JSON encoding/decoding,
// 	and password hashing.
//
// 	Variables
// 	-- Host: the location of the server
// 	-- Token: the authorization string associated with the session.
//============================================================================

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class Connector
{
	private URL url;

	public Connector(String host) throws IOException
	{
		Gson gson = new Gson();
		String hostString = "http://" + host + "/Services.php";
		url = new URL(hostString);
	}

	public String postJSON(String jsonInputString) throws IOException
	{
		// Open the connection
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);

		// Create JSON string
		// For Testing purpose
//		String jsonInputString = "{\"user\": \"etruyj\", \"pass\": \"testpassword1\", \"cmd\": \"login\"}";

		// Write to Server
		try(OutputStream os = con.getOutputStream())
		{
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		// Read server response
		try(BufferedReader br = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "utf-8")))
		{
			StringBuilder response = new StringBuilder();
			String responseLine = null;

			while((responseLine = br.readLine()) != null)
			{
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}	
}
