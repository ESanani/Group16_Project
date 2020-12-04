package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.common.utils.GenerateAnalytics;
import application.pojo.Status;
import application.pojo.TaskItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class DashboardController implements Initializable {

	@FXML
	private Pane pnlOverview;

	@FXML
	private Pane pnlNewWindow;

	@FXML
	private Button btnOverview;

	@FXML
	private Button btnPlanners;

	@FXML
	private Button btnNewPlanner;
	@FXML
	private Label totalLabel; 
	@FXML
	private Label newLabel; 
	@FXML
	private Label inProgLabel; 
	@FXML
	private Label completedLabel;
	@FXML
	private TextField overviewSearch;
	
	@FXML
	private TableView<TaskItem> dashTbl;
	@FXML
	private TableColumn<TaskItem, String> dashTblCol1;

	@FXML
	private TableColumn<TaskItem, String> dashTblCol2;

	@FXML
	private TableColumn<TaskItem, Status> dashTblCol3;

	/*
	 * Edits certain nodes in the FXML file. Generates an overview table of all tasks.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
			try {
				ObservableList<TaskItem> overviewItems = FXCollections.observableArrayList(GenerateAnalytics.plannerAnalysis("./testData/MOCK_DATA.csv"));
				FilteredList<TaskItem> filterData = new FilteredList<>(overviewItems, p -> true);
				
				overviewSearch.textProperty().addListener((observable, oldValue, newValue) -> {
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
				sortedData.comparatorProperty().bind(dashTbl.comparatorProperty());
				
				dashTblCol1.setCellValueFactory(celldata -> celldata.getValue().itemNameProperty());
				
				dashTblCol2.setCellValueFactory(celldata -> celldata.getValue().dateProperty());
				

				dashTblCol3.setCellValueFactory(new Callback<CellDataFeatures<TaskItem, Status>, ObservableValue<Status>>() {
					@Override
					public ObservableValue<Status> call(CellDataFeatures<TaskItem, Status> param) {
						TaskItem item = param.getValue();
						String statusCode = item.getStatus();
						Status status = Status.getByCode(statusCode);
						return new SimpleObjectProperty<Status>(status);
					}
				});
				
				dashTbl.setItems(sortedData);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			totalLabel.setText(Integer.toString(GenerateAnalytics.totalTaskCount));
			newLabel.setText(Integer.toString(GenerateAnalytics.newCount));
			inProgLabel.setText(Integer.toString(GenerateAnalytics.inProgressCount));
			completedLabel.setText(Integer.toString(GenerateAnalytics.completedCount));
	}

	//Used to change the Scene window/ hide elements on the current scene
	public void openWindow(String fxmlPath) {
		try {
			pnlOverview.setVisible(false);
			pnlNewWindow.setVisible(true);
			Pane newWindow = FXMLLoader.load(getClass().getResource(fxmlPath));
			pnlNewWindow.getChildren().setAll(newWindow);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Handles the button actions
	public void menuBtnClicks(ActionEvent actionEvent) {
		if (actionEvent.getSource() == btnOverview) {
			pnlNewWindow.setVisible(false);
			pnlOverview.setVisible(true);
			pnlOverview.setStyle("-fx-background-color : #02030A");
			pnlOverview.toFront();

		}
		if (actionEvent.getSource() == btnPlanners) {
			openWindow("../fxml/PlannerView.fxml");
		}

		if (actionEvent.getSource() == btnNewPlanner) {
			openWindow("../fxml/NewPlanner.fxml");
		}
	}

}
