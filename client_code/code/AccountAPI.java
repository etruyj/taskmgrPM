//=====================================================================================
// AccountAPI.java
// 	This is a container class that captures the account information. It also provides
// 	functionality to determine the id of the corresponding value. This id can be used
// 	for subsequent queries as well as filling in trace information when it's being
// 	written.
//=====================================================================================

public class AccountAPI extends APIResponseContainer
{
	private account[] accountList;

	//================================================
	// Functions
	//================================================

	public void count() 
	{
		if(this.getCode()>0)
       		{	       
			this.setNum(accountList.length);
		}
		else
		{
			this.setNum(0);
		}	
	}
	public int getAccountID(int i) { return accountList[i].id; }
	public int getAccountIDbyName(String acc_name)
	{
		int index = -1;

		for(int i=0; i<this.getNum(); i++)
		{
			if(accountList[i].name.equals(acc_name))
			{
				index = i;
			}
		}

		if(index>=0)
		{
			return getAccountID(index);
		}
		else
		{
			return index;
		}
	}
	public String getAccountName(int i) { return accountList[i].name; }

	//================================================
	// Inner Class
	//================================================

	private class account
	{
		public String name = "none";
		public int id = -99;
	}
}
