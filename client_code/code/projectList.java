//=========================================================================================
// projectList.java
//	This is the container list for decoding the resulting JSON from the 
//	listAccountContacts API call. 	
//=========================================================================================

public class projectList
{
	private String msg;
	private int code;
	private projects[] projectList;
	private int numProjects;

	//==========================================================
	// Functions
	//==========================================================
	
	public int count() 
	{ 
		if(code>0)
		{
			numProjects = projectList.length;
		}
		else
		{
			numProjects = 0;
		}
	
	       	return numProjects;
	}

	public String getProject(int i) 
	{ 
		return projectList[i].name;
	}

	public int getNumProjects() { return numProjects; }

	//==========================================================
	// Inner Class
	//==========================================================
	
	public class projects
	{
		public int id;
		public String name;

		public projects()
		{
			id = -1;
			name = "None";
		}
	}
}
