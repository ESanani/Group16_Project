package application.pojo;

import javafx.beans.property.SimpleStringProperty;

/*
 * object to store all the fields of an item. Used in multiple nodes. 
 */
public class TaskItem {
	private final SimpleStringProperty plannerName; 
	private final SimpleStringProperty itemName;
	private final SimpleStringProperty date; 
	private String status;
	
	public TaskItem()
	{
		this.plannerName = new SimpleStringProperty(); 
		this.itemName = new SimpleStringProperty();
		this.date = new SimpleStringProperty();
	}

	public String getPlannerName() {
		return plannerName.get(); 
	}
	public String getItemName() {
		return itemName.get();
	}

	public String getDate() {
		return date.get();
	}

	public String getStatus() {
		return status; 
	}
	
	public void setPlannerName(String plannerName)
	{
		this.plannerName.set(plannerName);
	}
	
	public void setItemName(String itemName)
	{
		this.itemName.set(itemName);
	}
	
	public void setStatus(String status) {
		this.status = status; 
	}
	
	public void setDate(String date) {
		this.date.set(date);
	}
	
	public SimpleStringProperty itemNameProperty() {
		return itemName;
	}
	public SimpleStringProperty dateProperty() {
		return date;
	}

	public SimpleStringProperty plannerNameProperty() {
		return plannerName;
	}

}
