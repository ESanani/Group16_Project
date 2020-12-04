package application.pojo;

/*
 * enum of the different Status' a task could be in. 
 * Used in certain nodes. 
 */

public enum Status {
	
	NEW("N", "New"), INPROGRESS("INP", "In Progress"), DONE("D", "Done");
	
	private String code;
	private String text;
	
	private Status(String code, String text)
	{
		this.code = code;
		this.text = text;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getText() {
		return text;
	}
	
	public static Status getByCode(String statusCode)
	{
		for(Status s : Status.values()) {
			if(s.code.equals(statusCode)) {
				return s;
			}
		}
		return null;
	}
	
	public static String getByStatus(String status)
	{
		for(Status s : Status.values())
		{
			if(s.text.equals(status)) {
				return s.code;
			}
		}
		return null; 
	}
	
	@Override
	public String toString() {
		return this.text;
	}
}
