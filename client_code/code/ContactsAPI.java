//=====================================================================================
// ContactsAPI.java
// 	This is a container class that captures the contact information. It also provides
// 	functionality to determine the id of the corresponding value. This id can be used
// 	for subsequent queries as well as filling in trace information when it's being
// 	written.
//=====================================================================================

public class ContactsAPI extends APIResponseContainer
{
	private contact[] contactList;

	//================================================
	// Functions
	//================================================

	public void count() 
	{
	       	if(this.getCode()>0)
		{	
			this.setNum(contactList.length);
		}
		else
		{
			this.setNum(0);
		}	
	}
	public int getContactID(int i) { return contactList[i].id; }
	public int getContactIDbyName(String cnt_name)
	{
		// Split the full name back to the individual pieces. 
		// Scan through the dropdown to find the right values.
		int index = -1;
		String[] name = cnt_name.split(", ");
		
		if(name.length > 0)
		{
			for(int i=0; i<this.getNum(); i++)
			{
				if(contactList[i].last.equals(name[0]))
				{
					if(contactList[i].first.equals(name[1]))
					{
						index = contactList[i].id;
					}
				}
			}

		}
		else
		{
			System.out.println(cnt_name);
		}

		return index;
	}
	public String getFirstName(int i) { return contactList[i].first; }
	public String getFullName(int i) { return contactList[i].last + ", " + contactList[i].first; }
	public String getLastName(int i) { return contactList[i].last; }
	public String getPosition(int i) { return contactList[i].position; }
	public String getEmail(int i) { return contactList[i].email; }
	public String getPhone(int i) { return contactList[i].phone; }
	public boolean getIncludeInSearch(int i) { return contactList[i].includeInSearch; }

	public void setFirstName(int i, String fname) { contactList[i].first = fname; }
	public void setLastName(int i, String lname) { contactList[i].last = lname; }
	public void setPosition(int i, String pos) { contactList[i].position = pos; }
	public void setEmail(int i, String em) { contactList[i].email = em; }
	public void setPhone(int i, String ph) { contactList[i].phone = ph; }
	public void setIncludeInSearch(int i, boolean toInc) { contactList[i].includeInSearch = toInc; }

	//================================================
	// Inner Class
	//================================================

	private class contact
	{
		public String first = "Jean-Luc";
		public String last = "Picard";
		public String position = "Captain";
		public String email = "this@example.com";
		public String phone = "(555) 867-5309";
		public boolean includeInSearch = false;
		public int id = -99;
	}

}
