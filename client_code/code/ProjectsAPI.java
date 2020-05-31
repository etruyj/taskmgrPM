//=====================================================================================
// ProjectAPI.java
// 	This is a container class that captures the project information. It also provides
// 	functionality to determine the id of the corresponding value. This id can be used
// 	for subsequent queries as well as filling in trace information when it's being
// 	written.
//=====================================================================================

public class ProjectsAPI extends APIResponseContainer
{
	private project[] projectList;

	//================================================
	// Functions
	//================================================

	public void count() 
	{
	       	if(this.getCode()>0)
		{	
			this.setNum(projectList.length); 
		}
		else
		{
			this.setNum(0);
		}
	}
	public int getProjectID(int i) { return projectList[i].id; }
	public int getProjectIDbyName(String prj_name)
	{
		int index = -1;

		for(int i=0; i<this.getNum(); i++)
		{
			if(projectList[i].name.equals(prj_name))
			{
				index = i;
			}
		}

		if(index>=0)
		{
			return getProjectID(index);
		}
		else
		{
			return index;
		}
	}
	public String getProjectName(int i) { return projectList[i].name; }

	//================================================
	// Inner Class
	//================================================

	private class project
	{
		public String name = "none";
		public int id = -99;
	}
}
