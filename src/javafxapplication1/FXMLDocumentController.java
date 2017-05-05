/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import Warriors.WarriorType;
import World.World;
import World.WorldProperty;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



/**
 *
 * @author nikitabatra
 */


public class FXMLDocumentController implements Initializable {
    
    @FXML Label displayTime;
//    public FXMLDocumentController(String timeLabel){
////        displayTime.setText(timeLabel);
//        System.out.println(timeLabel);
//    }
//    public void updatePage(String data){
//        myName.setText(data);
//        //System.out.println(data);
//    }
    
    @FXML
    private Label myName;
    private Label myID;
    int numLE, numT;
    int numCities = 5;
    int d_HP, n_HP, i_HP, l_HP, w_HP;
    int d_AV, n_AV, i_AV, l_AV, w_AV;

    
    @FXML private TextField inputLE;
    @FXML private TextField inputT;
    @FXML private TextField inputDHP;
    @FXML private TextField inputNHP;
    @FXML private TextField inputIHP;
    @FXML private TextField inputLHP;
    @FXML private TextField inputWHP;
    @FXML private TextField inputDAV;
    @FXML private TextField inputNAV;
    @FXML private TextField inputIAV;
    @FXML private TextField inputWAV;
    @FXML private TextField inputLAV;  
    
    @FXML ImageView hq1;
    @FXML ImageView hq2;
    @FXML ImageView city1;
    @FXML ImageView city2;
    @FXML ImageView city3;
    @FXML ImageView city4;
    @FXML ImageView city5;
    @FXML ImageView hq1Flag;
    @FXML ImageView hq2Flag;
    @FXML ImageView city1Flag;
    @FXML ImageView city2Flag;
    @FXML ImageView city3Flag;
    @FXML ImageView city4Flag;
    @FXML ImageView city5Flag;

    @FXML
    //private void handleFirstButtonAction(ActionEvent event) throws IOException { // choose options
    private void handleFirstButtonAction(ActionEvent event){
        System.out.println("You clicked the first button!");
        
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("input_parameters.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Input parameters");
                Scene scene1 = new Scene(root1, 400, 550);
                stage.setScene(scene1);
                stage.show();
                
        } catch(Exception e) {
           e.printStackTrace();
          }
//});
}
// 
//    @FXML
//    private void handleStartGameA1(ActionEvent event) throws InterruptedException, IOException{ // When startgame is selected
//        System.out.println("Game has been started!");
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("display_a1.fxml"));
//        
//        World w = new World();
//        w.runGame();
//    }
    
    @FXML
    private void handleInputParametersOption1(ActionEvent event) { // input page

        System.out.println("You clicked the input submit button!");
        // Hide window after click : 
        ((Node)(event.getSource())).getScene().getWindow().hide();
        try {
                numLE = parseInt(inputLE.getText());
                numT = parseInt(inputT.getText());
                d_HP = parseInt(inputDHP.getText());
                n_HP = parseInt(inputNHP.getText());
                i_HP = parseInt(inputIHP.getText());
                l_HP = parseInt(inputLHP.getText());
                w_HP = parseInt(inputWHP.getText());
                d_AV = parseInt(inputDAV.getText());
                n_AV = parseInt(inputNAV.getText());
                i_AV = parseInt(inputIAV.getText());
                l_AV = parseInt(inputLAV.getText());
                w_AV = parseInt(inputWAV.getText());

		boolean validLE = numLE >= 1 && numLE <= 1000;
		boolean validT = numT >= 0 && numT <= 10000;
			
		if (!(validLE && validT)){
			System.out.println("Error: M,T not valid.");
			System.exit(0);
		}
                System.out.println(numLE + " " + numT);
                        
                boolean validDHP = d_HP >= 0 && d_HP <= 100;
		boolean validNHP = n_HP >= 0 && n_HP <= 100;
                boolean validIHP = i_HP >= 0 && i_HP <= 100;
                boolean validLHP = l_HP >= 0 && l_HP <= 100;
                 boolean validWHP = w_HP >= 0 && w_HP <= 100;
                        
                 if(!(validDHP && validNHP && validIHP && validLHP && validWHP)){
                        System.out.println("Error: HP value input not valid.");
			System.exit(0);
                    }
                System.out.println(d_HP + " " + n_HP + " " + i_HP + " " + l_HP + " " + w_HP);
                        
                boolean validDAV = d_AV >= 0 && d_AV <= 100;
                boolean validNAV = n_AV >= 0 && n_AV <= 100;
                boolean validIAV = i_AV >= 0 && i_AV <= 100;
                boolean validLAV = l_AV >= 0 && l_AV <= 100;
                 boolean validWAV = w_AV >= 0 && w_AV <= 100;
                        
                if(!(validDHP && validNHP && validIHP && validLHP && validWHP)){
                        System.out.println("Error:  Attack Value input not valid.");
			System.exit(0);
                }
                System.out.println(d_AV + " " + n_AV + " " + i_AV + " " + l_AV + " " + w_AV);

                WarriorType.HP_LIST = new int[]{d_HP,n_HP,i_HP,l_HP,w_HP};
                WarriorType.ATTACK_LIST = new int[]{d_AV,n_AV,i_AV,l_AV,w_AV};
                WorldProperty.InitLifeElements = numLE;
                WorldProperty.MaxMinutes = numT;

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("display_a1.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("World of Warcraft: Assignment 1");
                Scene scene1 = new Scene(root1, 1000, 700);
                stage.setScene(scene1);                
                stage.show();
      
        } catch(Exception e) {
           System.out.println("Error: There should be 12 elements in total.");
           e.printStackTrace();
          }
    }
   
    @FXML
    private void handleSecondButtonAction(ActionEvent event) throws FileNotFoundException {
        System.out.println("You clicked the second button!");
        
        try {
                numLE = parseInt(inputLE.getText());
                numT = parseInt(inputT.getText());
                d_HP = parseInt(inputDHP.getText());
                n_HP = parseInt(inputNHP.getText());
                i_HP = parseInt(inputIHP.getText());
                l_HP = parseInt(inputLHP.getText());
                w_HP = parseInt(inputWHP.getText());
                d_AV = parseInt(inputDAV.getText());
                n_AV = parseInt(inputNAV.getText());
                i_AV = parseInt(inputIAV.getText());
                l_AV = parseInt(inputLAV.getText());
                w_AV = parseInt(inputWAV.getText());

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("single_player.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("World of Warcraft: Single-Player");
                
                Scene scene1 = new Scene(root1, 800, 600);
                stage.setScene(scene1);
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
    }
    
    @FXML
    private void handleThirdButtonAction(ActionEvent event) {
        System.out.println("You clicked the third button!");
        
        try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("multi_player.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("World of Warcraft: Multi-Player");
                Scene scene1 = new Scene(root1, 1000, 700);
                stage.setScene(scene1);
                
                stage.show();
        } catch(Exception e) {
           e.printStackTrace();
          }
    }
    
    //@FXML private ImageView imageview;
//    @FXML private ImageView imageview = null;
//    @FXML private Image image = null;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //image = new Image("flag_blue.png");
        System.out.println("Reaching initialize WOOOOHOOOOOOO");
        
        //ImageView imageview = null;
        //imageview.setImage(new Image("flag_red.png"));
        //imageview.setImage(new Image("flag_red.png"));
        //imageview.setImage(image);
        //hq1.setImage(img_hq1); 
        //hq1.setImage(img_hq1);
    }    
    
}
