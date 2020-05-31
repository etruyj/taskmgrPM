//=======================================================================================
// APIResponseContainer.java
// 	This is a super class for defining the core functionality of the API containers.
// 	This class shouldn't be used to hold actual data.
//=======================================================================================

public class APIResponseContainer
{
	private String msg = "NONE";
	private int code = -99;
	private int numElements = 0;

	//==================================================================
	// FUNCTIONS
	//==================================================================
	
	public String getMsg() { return msg; }
	public int getCode() { return code; }
	public int getNum() { return numElements; }
	public void setNum(int num) { numElements = num; }
}
