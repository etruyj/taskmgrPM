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
/*	public int getContactIDbyName(String cnt_name)
	{
		int index = -1;

		for(int i=0; i<this.getNum(); i++)
		{
			if(contactList[i].name.equals(cnt_name))
			{
				index = i;
			}
		}

		if(index>=0)
		{
			return getContactID(index);
		}
		else
		{
			return index;
		}
	}
*/	public String getFirstName(int i) { return contactList[i].first; }
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
