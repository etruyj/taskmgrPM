//=============================================================================
// accountList.java
// 	A container class for decoding the resulting JSON from the 
// 	getAllAccounts API call.
//=============================================================================

public class accountList
{
	private String msg;
	private int code;
	private account[] accountList;
	private int numAccounts;

	//======================================================
	// Functions
	//======================================================

	public int count()
	{
		if(code>0)
		{
			numAccounts = accountList.length;
		}
		else
		{
			numAccounts = 0;
		}

		return numAccounts;
	}

	public int getAccountID(String account_name)
	{
		int acc_id = -1;

		for(int i=0; i<numAccounts; i++)
		{
			if(accountList[i].account==account_name)
			{
				acc_id = accountList[i].id;
			}
		}

		return acc_id;
	}

	public String getAccountName(int i) { return accountList[i].account; }
	public int getNumAccounts() { return numAccounts; }

	//======================================================
	// Inner Classes
	// 	Accounts
	//======================================================
	
	private class account
	{
		public String account;
		public int id;

		private account()
		{
			account = "none";
			id = -1;
		}
	}
}
