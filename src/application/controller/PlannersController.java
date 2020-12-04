package application.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.common.utils.SaveButtonGraphic;
import application.controller.model.PlannerViewModel;
import application.pojo.Planner;
import application.pojo.Status;
import application.pojo.TaskItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
public class PlannersController {
	
	public static Map<String, List<TaskItem>> userPlanners = new HashMap<String, List<TaskItem>>();
	public static List<TaskItem> itemsToRemove = new ArrayList<>();
	public static List<TaskItem> itemsToAdd = new ArrayList<>();
	ObservableList<TaskItem> plannerItems = FXCollections.observableArrayList();
	public static String currentPlanner;
	
	//Buttons
	@FXML
	private Button savePlanrChanges;
	@FXML
	private Button cancelPlanChanges;
	
	@FXML
	private Button addNewTaskBTN;
	
	@FXML
	private Button editPlnrBtn;
	//Combo Box's
	@FXML
	private ComboBox<Planner> openPlnrBox = new ComboBox<Planner>();
	@FXML
	private ComboBox<Status> newTaskStatus = new ComboBox<Status>();
	
	//Table View items
	@FXML
	private TableView<TaskItem> plannersTable;

	@FXML
	private TableColumn<TaskItem, String> TaskNameCol;

	@FXML
	private TableColumn<TaskItem, String> DateCol;

	@FXML
	private TableColumn<TaskItem, Status> StatusCol;

	@FXML
	private TableColumn<TaskItem, TaskItem> deleteCol;
	//Miscellaneous
	@FXML
	private VBox addTaskEdit; 
	@FXML
	private TextField addNewTaskName;

	@FXML
	private DatePicker newTaskDate;
	@FXML
	private Label curPlanLabel; 
	
	@FXML
	private TextField searchItems; 

	PlannerViewModel plannerModel = new PlannerViewModel();
	
	/*
	 * Edits certain nodes in the FXML file. 
	 * Generates an editable table to display the selected
	 * planner's items. 
	 * Creates a combobox with all the planner names. 
	 */
	@FXML
	public void initialize() {
		cancelPlanChanges.setVisible(false);
		savePlanrChanges.setVisible(false);
		addTaskEdit.setVisible(false);
		deleteCol.setVisible(false);
		if (!userPlanners.keySet().isEmpty()) {
			plannerModel.generatePlannerSeclection(openPlnrBox);

		}
	}

	//Handles the planner combobox action.
	//Table is filled with the selected planner's items. 
	@FXML
	private void openPlnrBoxAction(ActionEvent openPlrAction) {
		savePlanrChanges.setVisible(false);
		currentPlanner = openPlnrBox.getValue().getPlannerName();
		curPlanLabel.setText(currentPlanner); 
		plannerModel.generatePlannerTable(plannersTable, searchItems, TaskNameCol, DateCol, StatusCol, deleteCol, generatePlannerItems());
	}
	
	//Generates an ObservableList for the table
	private ObservableList<TaskItem> generatePlannerItems() {
			plannerItems.clear();
		for (TaskItem item : userPlanners.get(currentPlanner)) {
			plannerItems.add(item);
		}

		return plannerItems;
	}
	
	//When pressed, unhides items, makes the table available for edit
	@FXML
	private void editPlannerAction(ActionEvent editPlannerBtnAction)
	{
		plannersTable.setEditable(true);
		plannerModel.generateStatusSelection(newTaskStatus);
		addTaskEdit.setVisible(true);
		deleteCol.setVisible(true);
		savePlanrChanges.setVisible(true);
		cancelPlanChanges.setVisible(true);
	}

	//Handles new task action. Gets values from each input field
	//to update the current planner.
	@FXML
	private void newTaskBtnAction(ActionEvent addTaskAction) {
		TaskItem itemToAdd = new TaskItem();
		itemToAdd.setItemName(addNewTaskName.getText());
		itemToAdd.setDate(newTaskDate.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		itemToAdd.setStatus(newTaskStatus.getValue().getCode());
		plannerItems.add(itemToAdd);
		updatePlanner(false, itemToAdd);
	}

	//Either adds or removes items from a planner. 
	public void updatePlanner(boolean removeItem, TaskItem item) {
		if (!savePlanrChanges.isVisible()) {
			savePlanrChanges.setVisible(true);
		}
		if (removeItem) {
			plannerItems.remove(item);
			itemsToRemove.add(item);
		} else {
			userPlanners.get(currentPlanner).add(item);
		}
	}

	//Handles the save button actions. When pressed will remove items from the planner. 
	@FXML
	private void savePlanrChangesAction(ActionEvent e) {
		SaveButtonGraphic.editSaveButton(savePlanrChanges, ContentDisplay.GRAPHIC_ONLY);

		plannersTable.setEditable(false);
		addTaskEdit.setVisible(false);
		deleteCol.setVisible(false);
		savePlanrChanges.setVisible(false);
		cancelPlanChanges.setVisible(false);
		if (itemsToRemove != null) {
			for (TaskItem removeItem : itemsToRemove) {
				userPlanners.get(currentPlanner)
						.removeIf(item -> item.getItemName().equalsIgnoreCase(removeItem.getItemName()));
			}

		}
	}
	
	//Handles the cancel button actinos. When pressed, no changes are made to the current planner. 
	@FXML
	private void cancelPlanChangesAction(ActionEvent e)
	{
		plannersTable.setEditable(false);
		plannerModel.generateStatusSelection(newTaskStatus);
		addTaskEdit.setVisible(false);
		deleteCol.setVisible(false);
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setContentText("No changes made. Please reopen planner");
		alert.showAndWait();
		
		savePlanrChanges.setVisible(false);
		cancelPlanChanges.setVisible(false);
		
		
		
		
		
	}

		
}
