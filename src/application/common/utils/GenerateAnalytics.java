package application.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import application.controller.PlannersController;
import application.pojo.Status;
import application.pojo.TaskItem;

public class GenerateAnalytics {
	
	public static int totalTaskCount = 0;
	public static int completedCount = 0;
	public static int inProgressCount = 0; 
	public static int newCount = 0; 
	public static List<TaskItem> plannerItems = new ArrayList<TaskItem>(); 
	
	/*
	 * Method to read the last two months of user data from the test data file.
	 * Reads from a .csv file.
	 * Actual data would be saved/read from a database or through serialization/
	 */
	public static List<TaskItem> plannerAnalysis(String filePath) throws IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 
		sdf.setLenient(false);
		//Current Date
		Calendar currentDate = Calendar.getInstance();
		//2 Months from today
		Calendar twoMonthsBefore = Calendar.getInstance();
		twoMonthsBefore.add(Calendar.MONTH, -2);
		
		
		File dataFile = new File(filePath); 
		BufferedReader buffer = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF8")); 
		
		String line = buffer.readLine();

		List<TaskItem> inRangeItems = new ArrayList<TaskItem>();
		while((line = buffer.readLine()) != null)
		{
			String[] itemAttributes = line.split(","); 
			
			try {
				Date datetoCheck = sdf.parse(itemAttributes[0]);
				if(dateInRange(datetoCheck, twoMonthsBefore,currentDate))
				{
					totalTaskCount++;
					inRangeItems.add(createItem(itemAttributes)); 
					
				}
				
				line = buffer.readLine(); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		buffer.close();
		
		return inRangeItems;
	}
	
	//Method to check if an items date is within the last two months. 
	private static boolean dateInRange(Date dateToCheck, Calendar startDate, Calendar endDate)
	{
		if(dateToCheck.after(startDate.getTime()) && dateToCheck.before(endDate.getTime()))
		{
			return true; 
		}
		
		return false;
	}
	
	//Create a new object with planner data
	private static TaskItem createItem(String[] csvData)
	{
		String date = csvData[0];
		String plannerName = csvData[1];
		String taskName = csvData[2];
		String status = csvData[3];
		switch(status) {
			case "New":
				newCount++;
				break;
			case "In Progress":
				inProgressCount++;
				break;
			case "Done":
				completedCount++;
			break;
			default:
			break;
				
		}
		TaskItem item = new TaskItem();
		item.setPlannerName(plannerName); item.setDate(date);
		item.setItemName(taskName); item.setStatus(Status.getByStatus(status));
		getAllPlanners(item); 
		return item; 
	}
	
	//Populates a Map<String,List<TaskItem>> for later use.
	public static void getAllPlanners(TaskItem item)
	{
		if(!PlannersController.userPlanners.containsKey(item.getPlannerName()))
		{
			plannerItems = new ArrayList<TaskItem>();
			plannerItems.add(item);
			PlannersController.userPlanners.put(item.getPlannerName(), plannerItems); 
		}
		else {
			PlannersController.userPlanners.get(item.getPlannerName()).add(item);
		}
	}


}
