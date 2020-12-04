package application.controller.model;

import application.controller.PlannersController;
import application.pojo.Planner;
import application.pojo.Status;
import application.pojo.TaskItem;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class PlannerViewModel {
	
	PlannersController plannerControl;
	
	//Populates the combo box with all of the planners
	public void generatePlannerSeclection(ComboBox<Planner> openPlnrBox)
	{
		ObservableList<Planner> l = FXCollections.observableArrayList();
		for(String plan : PlannersController.userPlanners.keySet())
		{
			l.add(new Planner(plan)); 
		}
		
		StringConverter<Planner> converter = new StringConverter<Planner>() {
			@Override
			public String toString(Planner plan) {
				return plan.getPlannerName();
			}
			
			@Override
			public Planner fromString(String string) {
				return null; 
			}
		};
		openPlnrBox.setConverter(converter);
		openPlnrBox.setItems(l);

		
		openPlnrBox.setCellFactory(new Callback<ListView<Planner>, ListCell<Planner>>() {
			@Override
			public ListCell<Planner> call(ListView<Planner> param) {
				return new ListCell<Planner>() {
					@Override
					public void updateItem(Planner item, boolean empty) {
						super.updateItem(item, empty);
						if(!empty) {
							setText(item.getPlannerName());
							setGraphic(null);
						} else {
							setText(null);
						}
					}
				}; 
			}
		});
	}
	
	//Populates the Status combo box with Status'
	public void generateStatusSelection(ComboBox<Status> newTaskStatus) {
		for (Status s : Status.values()) {
			newTaskStatus.getItems().add(Status.getByCode(s.getCode()));
		}
	}
	
	//Creates a table with all of the current planners items, a search bar and handles the 
	//delete actions. 
	public void generatePlannerTable(TableView<TaskItem> plannersTable, TextField searchItems, TableColumn<TaskItem, String> TaskNameCol,
			TableColumn<TaskItem, String> DateCol,TableColumn<TaskItem, Status> StatusCol,TableColumn<TaskItem, TaskItem> deleteCol, ObservableList<TaskItem> plannerItems)
	{
		FilteredList<TaskItem> filterData = new FilteredList<>(plannerItems, p -> true);
		
		searchItems.textProperty().addListener((observable, oldValue, newValue) -> {
			filterData.setPredicate(task -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (task.getItemName().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				}
				return false;
			});
		});
		
		SortedList<TaskItem> sortedData = new SortedList<>(filterData); 
		sortedData.comparatorProperty().bind(plannersTable.comparatorProperty());
		
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

		StatusCol.setCellValueFactory(new Callback<CellDataFeatures<TaskItem, Status>, ObservableValue<Status>>() {
			@Override
			public ObservableValue<Status> call(CellDataFeatures<TaskItem, Status> param) {
				TaskItem item = param.getValue();

				String statusCode = item.getStatus();
				Status status = Status.getByCode(statusCode);
				return new SimpleObjectProperty<Status>(status);
			}
		});

		StatusCol.setCellFactory(ComboBoxTableCell.forTableColumn(statusList));

		StatusCol.setOnEditCommit((CellEditEvent<TaskItem, Status> event) -> {
			TablePosition<TaskItem, Status> pos = event.getTablePosition();

			Status newStatus = event.getNewValue();

			int row = pos.getRow();
			TaskItem item = event.getTableView().getItems().get(row);
			item.setStatus(newStatus.getCode());
		});

		// delete col
		deleteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));

		deleteCol.setCellFactory(param -> new TableCell<TaskItem, TaskItem>() {
			private final Button deleteTaskBTN = new Button();
			private final Image deleteIMG = new Image("/application/images/icons8-remove-16.png");
			private final ImageView imgView = new ImageView(deleteIMG);


			@Override
			protected void updateItem(TaskItem item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null) {
					setGraphic(null);
					return;
				}
				deleteTaskBTN.setGraphic(imgView);
				deleteTaskBTN.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				deleteTaskBTN.setStyle("-fx-background-color: transparent;");
				setGraphic(deleteTaskBTN);
				deleteTaskBTN.setOnAction(event -> removeItems(item));
			}
			
			protected void removeItems(TaskItem item) {
				
				PlannersController.itemsToRemove.add(item); 
				plannerItems.remove(item); 
			}
		});
	
		plannersTable.setItems(sortedData);
	}

}
