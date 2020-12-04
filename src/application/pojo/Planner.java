package application.pojo;

import javafx.beans.property.SimpleStringProperty;

/*
 * Object to store the names of planners. Object is used in certain nodes. 
 */
public class Planner {

	private final SimpleStringProperty plannerName = new SimpleStringProperty(); 
	
	public Planner(String plannerName)
	{
		this.plannerName.set(plannerName);
	}
	
	public String getPlannerName() {
		return plannerName.get();
	}
	
	public void setPlannerName(String plannerName) {
		this.plannerName.set(plannerName);
	}
	public SimpleStringProperty plannerNameProperty() {
		return plannerName; 
	}
}
