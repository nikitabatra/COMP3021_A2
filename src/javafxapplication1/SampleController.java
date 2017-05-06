/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import Warriors.WarriorType;
import World.Headquarters;
import World.World;
import static World.World.CityList;
import static World.World.WorldClock;
import static World.World.checkIfNoBattle;
import static World.World.timeCount;
import static World.World.warriorFetchesElements;
import static World.World.worldController;
import World.WorldProperty;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author nikitabatra
 */
public class SampleController implements Initializable {
    
    World WorldObject = null;
    public SampleController getSampleController(){
        return this;
    }

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
    
    @FXML private Label displayTime;
    @FXML private Label produceBlue;
    @FXML private Label produceRed;
    
    
    @FXML private Image image_hq1Flag = new Image("flag_blue.png");
    @FXML private Image image_hq2Flag = new Image("flag_red.png");
    
    @FXML
    public void updatePage(String data){
        displayTime.setText(data);
        System.out.println("Entered update page"); 
    }
    
    @FXML
    public void startGameDisplay(){
        hq1Flag.setImage(image_hq1Flag);
        hq2Flag.setImage(image_hq2Flag);
    }
    
    @FXML
    public void updateProduceWarriors(int blueType, int redType, boolean blueSuccess, boolean redSuccess){
        produceBlue.setText(WarriorType.WarriorNames[blueType]);
        produceRed.setText(WarriorType.WarriorNames[redType]);   
    }
    
    @FXML
    private void handleStartGameA1(ActionEvent event) throws InterruptedException, IOException{ // When startgame is selected
        displayTime.setText("Game End");
        
        if(WorldObject.checkOccupied == true){
            System.out.println("Game has been ended!");
            displayTime.setDisable(true);
            return;
        }
        
        if (this.WorldObject == null){
                this.WorldObject = new World(this);
                System.out.println("Game has been started!");
        }
        String currentTime = this.WorldObject.WorldClock.getTime();
        this.WorldObject.runGame(currentTime);    
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}