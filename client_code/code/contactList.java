//=========================================================================================
// contactList.java
//	This is the container list for decoding the resulting JSON from the 
//	listAccountContacts API call. 	
//=========================================================================================

public class contactList
{
	private String msg;
	private int code;
	private contacts[] contactList;
	private int numContacts;

	//==========================================================
	// Functions
	//==========================================================
	
	public int count() 
	{ 
		if(code>0)
		{
			numContacts = contactList.length;
		}
		else
		{
			numContacts = 0;
		}
	
	       	return numContacts;
	}

	public String getFullName(int i) 
	{ 
		return contactList[i].last + ", " + contactList[i].first;
	}

	public int getNumContacts() { return numContacts; }

	//==========================================================
	// Inner Class
	//==========================================================
	
	public class contacts
	{
		public int id;
		public String first;
		public String last;
		public String position;

		public contacts()
		{
			id = -1;
			first = "John";
			last = "Doe";
			position = "unemployed";
		}
	}
}
