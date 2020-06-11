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
			System.out.println(name[1] + " " + name[0]);

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
	public String getPosition(int i) { return contactList[i].last; }

	//================================================
	// Inner Class
	//================================================

	private class contact
	{
		public String first = "Jean-Luc";
		public String last = "Picard";
		public String position = "Captain";
		public int id = -99;
	}
}
