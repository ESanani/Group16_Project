package application.common.utils;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/*
 * Class to change a save button's graphic / image
 */
public class SaveButtonGraphic {
	
	public static Image saveBtnImage= new Image("/application/images/icons8-checkmark-24.png");
	public static ImageView saveImgView = new ImageView(saveBtnImage); 
	public static final String saveBtnStyle = "-fx-background-color: #1ECD97;"; 
	public static final String unsavedBtnStyle = "-fx-background-color: transparent"; 
	public static final double setFitHeight = 24;
	public static final double setFitWidth = 23; 
	
	//Reverts to the original button style
	public static void setAsUnsavedBtn(Button unsavedButton)
	{
		unsavedButton.getGraphic().setDisable(true);
		unsavedButton.setContentDisplay(ContentDisplay.TEXT_ONLY); 
		unsavedButton.setStyle(unsavedBtnStyle);
		unsavedButton.setVisible(false);
		
	}
	//Removes the save button text, adds an image, recolors background
	public static void editSaveButton(Button saveButton, ContentDisplay graphicDisplay)
	{
		saveImgView.setFitHeight(setFitHeight);
		saveImgView.setFitWidth(setFitWidth);
		saveButton.setGraphic(saveImgView);
		saveButton.setContentDisplay(graphicDisplay);
		saveButton.setStyle(saveBtnStyle);
	}

}
