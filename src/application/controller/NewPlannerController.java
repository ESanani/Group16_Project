package application.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.common.utils.SaveButtonGraphic;
import application.pojo.Status;
import application.pojo.TaskItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class NewPlannerController {

	@FXML
	private TextField newPlannerTask;

	@FXML
	private TextField newPlannerName;

	@FXML
	private DatePicker newPlannerDatePicker;

	@FXML
	private ComboBox<Status> newPlannerStatusBox = new ComboBox<Status>();

	@FXML
	private Button addTaskBTN;

	@FXML
	private Button savePlnrBTN;

	@FXML
	private VBox newItems = null;

	@FXML
	private TableView<TaskItem> itemsTable;

	@FXML
	private TableColumn<TaskItem, String> TaskNameCol;

	@FXML
	private TableColumn<TaskItem, String> DateCol;

	@FXML
	private TableColumn<TaskItem, Status> TaskStatusCol;

	ObservableList<TaskItem> list = FXCollections.observableArrayList();
	private static String PlannerName;

	/*
	 * Edits certain nodes in the FXML file. Generates a editable table to store/edit
	 * a new planner item. 
	 */
	@FXML
	public void initialize() {
		for (Status s : Status.values()) {
			newPlannerStatusBox.getItems().add(Status.getByCode(s.getCode()));

		}
		savePlnrBTN.setVisible(false);
		itemsTable.setEditable(true);

		// Task Name
		TaskNameCol.setCellValueFactory(celldata -> celldata.getValue().itemNameProperty());
		TaskNameCol.setCellFactory(TextFieldTableCell.<TaskItem>forTableColumn());

		TaskNameCol.setOnEditCommit((CellEditEvent<TaskItem, String> event) -> {
			TablePosition<TaskItem, String> pos = event.getTablePosition();

			String newTaskName = event.getNewValue();
			int row = pos.getRow();
			TaskItem newTask = event.getTableView().getItems().get(row);

			newTask.setItemName(newTaskName);
		});

		// Date
		DateCol.setCellValueFactory(celldata -> celldata.getValue().dateProperty());
		DateCol.setCellFactory(TextFieldTableCell.<TaskItem>forTableColumn());

		DateCol.setOnEditCommit((CellEditEvent<TaskItem, String> event) -> {
			TablePosition<TaskItem, String> pos = event.getTablePosition();

			String newDate = event.getNewValue();
			int row = pos.getRow();
			TaskItem newTask = event.getTableView().getItems().get(row);

			newTask.setDate(newDate);
		});
		// Status
		ObservableList<Status> statusList = FXCollections.observableArrayList(Status.values());

		TaskStatusCol.setCellValueFactory(new Callback<CellDataFeatures<TaskItem, Status>, ObservableValue<Status>>() {
			@Override
			public ObservableValue<Status> call(CellDataFeatures<TaskItem, Status> param) {
				TaskItem item = param.getValue();

				String statusCode = item.getStatus();
				Status status = Status.getByCode(statusCode);
				return new SimpleObjectProperty<Status>(status);
			}
		});

		TaskStatusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusList));

		TaskStatusCol.setOnEditCommit((CellEditEvent<TaskItem, Status> event) -> {
			TablePosition<TaskItem, Status> pos = event.getTablePosition();

			Status newStatus = event.getNewValue();

			int row = pos.getRow();
			TaskItem item = event.getTableView().getItems().get(row);
			item.setStatus(newStatus.getCode());
		});
	}

	//Populates an ObservableList of new items for the Table. 
	private ObservableList<TaskItem> generatePlannerEntry() {
		TaskItem newItem = new TaskItem();
		newItem.setItemName(newPlannerTask.getText());
		newItem.setDate(newPlannerDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")));
		newItem.setStatus(newPlannerStatusBox.getValue().getCode());
		list.add(newItem);

		return list;
	}
	
	//Handles the new planner button action.
	//Clears all the input fields.
	@FXML
	public void newPlnrBtnAction(ActionEvent newBtnAction)
	{
		newPlannerName.clear();
		itemsTable.getItems().clear();
		SaveButtonGraphic.setAsUnsavedBtn(savePlnrBTN);
	}

	//Handles button action to add to table
	@FXML
	public void addItemAction(ActionEvent actionEvent) {
		
		if(!savePlnrBTN.isVisible()) {
			savePlnrBTN.setVisible(true);
		}

		itemsTable.setItems(generatePlannerEntry());
		newPlannerTask.clear();
		newPlannerDatePicker.getEditor().clear();
		newPlannerStatusBox.valueProperty().set(null);

	}

	//Saves the new planner items. 
	@FXML
	public void savePlannerAction(ActionEvent saveActionEvent) {

		SaveButtonGraphic.editSaveButton(savePlnrBTN, ContentDisplay.GRAPHIC_ONLY);

		PlannerName = newPlannerName.getText(); 
		
		List<TaskItem> newTaskItems = new ArrayList<>();
		boolean plannerExists = PlannersController.userPlanners.containsKey(PlannerName);

		for (TaskItem newItem : itemsTable.getItems()) {
			if (plannerExists) {
				PlannersController.userPlanners.get(PlannerName).add(newItem);
			} else {
				newTaskItems.add(newItem);
			}

		}
		if (!PlannersController.userPlanners.containsKey(PlannerName)) {
			PlannersController.userPlanners.put(PlannerName, newTaskItems);
		}

	}


}
