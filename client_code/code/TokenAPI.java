//=====================================================================================
// tokenAPI.java
// 	This is a container class that captures the token from the login attempt. The
// 	error code is checked to make sure the connection is successful. If so, the token
// 	is fed back to be stored in the Session var.
//=====================================================================================

public class TokenAPI extends APIResponseContainer
{
	String token="none";

	public String getToken() { return token; }
}
