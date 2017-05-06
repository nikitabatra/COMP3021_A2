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
    @FXML ImageView blueDragon;
    @FXML ImageView blueNinja;
    @FXML ImageView blueIceman;
    @FXML ImageView blueLion;
    @FXML ImageView blueWolf;
    @FXML ImageView redDragon;
    @FXML ImageView redNinja;
    @FXML ImageView redIceman;
    @FXML ImageView redLion;
    @FXML ImageView redWolf;
    
    @FXML ImageView hq1Blue;
    @FXML ImageView hq1Red;
    @FXML ImageView hq2Blue;
    @FXML ImageView hq2Red;
    
    @FXML ImageView city1Blue;
    @FXML ImageView city1Red;
    @FXML ImageView city2Blue;
    @FXML ImageView city2Red;
    @FXML ImageView city3Blue;
    @FXML ImageView city3Red;
    @FXML ImageView city4Blue;
    @FXML ImageView city4Red;
    @FXML ImageView city5Blue;
    @FXML ImageView city5Red;
    
    @FXML ImageView c1battle;
    @FXML ImageView c2battle;
    @FXML ImageView c3battle;
    @FXML ImageView c4battle;
    @FXML ImageView c5battle;
    
    @FXML ImageView c1bluewin;
    @FXML ImageView c1redwin;
    @FXML ImageView c2bluewin;
    @FXML ImageView c2redwin;
    @FXML ImageView c3bluewin;
    @FXML ImageView c3redwin;
    @FXML ImageView c4bluewin;
    @FXML ImageView c4redwin;
    @FXML ImageView c5bluewin;
    @FXML ImageView c5redwin;
    
    @FXML private Image image_hq1Flag = new Image("flag_blue.png");
    @FXML private Image image_hq2Flag = new Image("flag_red.png");
    @FXML private Image image_dragon_blue = new Image("dragon_blue.png");
    @FXML private Image image_ninja_blue = new Image("ninja_blue.png");
    @FXML private Image image_iceman_blue = new Image("iceman_blue.png");
    @FXML private Image image_lion_blue = new Image("lion_blue.png");
    @FXML private Image image_wolf_blue = new Image("wolf_blue.png");
    @FXML private Image image_dragon_red = new Image("dragon_red.png");
    @FXML private Image image_ninja_red = new Image("ninja_red.png");
    @FXML private Image image_iceman_red = new Image("iceman_red.png");
    @FXML private Image image_lion_red = new Image("lion_red.png");
    @FXML private Image image_wolf_red = new Image("wolf_red.png");
    
    @FXML private Image image_battle = new Image("battle.png");
    
    @FXML private Image image_winBattle = new Image("win.png");
    
    @FXML private Image tempBlue = null;
    @FXML private Image tempRed = null;

    @FXML private Label displayTime;
    @FXML private Label produceBlue;
    @FXML private Label produceRed;
    
    @FXML private Label C1LE;
    @FXML private Label C2LE;
    @FXML private Label C3LE;
    @FXML private Label C4LE;
    @FXML private Label C5LE;
    
    @FXML private Label redstatus_fetchLE;
    @FXML private Label bluestatus_fetchLE;
    
    
    
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
        String warriorNameBlue = WarriorType.WarriorNames[blueType];
        String warriorNameRed = WarriorType.WarriorNames[redType];
        produceBlue.setText(warriorNameBlue);
        produceRed.setText(warriorNameRed);  

        if(blueSuccess == true){
            if(warriorNameBlue == "dragon") hq1Blue.setImage(image_dragon_blue);
            if(warriorNameBlue == "ninja") hq1Blue.setImage(image_ninja_blue);
            if(warriorNameBlue == "iceman") hq1Blue.setImage(image_iceman_blue);
            if(warriorNameBlue == "lion") hq1Blue.setImage(image_lion_blue);
            if(warriorNameBlue == "wolf") hq1Blue.setImage(image_wolf_blue);
        }
        
        if(redSuccess == true){
            if(warriorNameRed == "dragon") hq2Red.setImage(image_dragon_red);
            if(warriorNameRed == "ninja") hq2Red.setImage(image_ninja_red);
            if(warriorNameRed == "iceman") hq2Red.setImage(image_iceman_red);
            if(warriorNameRed == "lion") hq2Red.setImage(image_lion_red);
            if(warriorNameRed == "wolf") hq2Red.setImage(image_wolf_red);
        }
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
    
    @FXML
    public void removeWarriorLabel(){
        produceBlue.setText("");
        produceRed.setText("");
    }
    
    @FXML
    public void updateWarriorMarch(int party, int location){

        if(party == WorldProperty.BLUE){

            if(location == 5){
                tempRed = hq2Red.getImage();
                hq2Red.setImage(null);
                city5Red.setImage(tempRed);
            }

            if(location == 4){
                tempRed = city5Red.getImage();
                city5Red.setImage(null);
                city4Red.setImage(tempRed);
            }

            if(location == 3){
                tempRed = city4Red.getImage();
                city4Red.setImage(null);
                city3Red.setImage(tempRed);
            }

            if(location == 2){
                tempRed = city3Blue.getImage();
                city3Red.setImage(null);
                city2Red.setImage(tempRed);
            }

            if(location == 1){
                tempRed = city2Blue.getImage();
                city2Red.setImage(null);
                city1Red.setImage(tempRed);
            }

            if(location == 0){
                tempRed = city1Blue.getImage();
                city1Red.setImage(null);
                hq1Red.setImage(tempRed);
            } 
        }

        if(party == WorldProperty.RED){

            if(location == 1){
                tempBlue = hq1Blue.getImage();
                hq1Blue.setImage(null);
                city1Blue.setImage(tempBlue);
            }

            if(location == 2){
                tempBlue = city1Blue.getImage();
                city1Blue.setImage(null);
                city2Blue.setImage(tempBlue);
            }

            if(location == 3){
                tempBlue = city2Blue.getImage();
                city2Blue.setImage(null);
                city3Blue.setImage(tempBlue);
            }

            if(location == 4){
                tempBlue = city3Blue.getImage();
                city3Blue.setImage(null);
                city4Blue.setImage(tempBlue);
            }

            if(location == 5){
                tempBlue = city4Blue.getImage();
                city4Blue.setImage(null);
                city5Blue.setImage(tempBlue);
            }

            if(location == 6){
                tempBlue = city5Blue.getImage();
                city5Blue.setImage(null);
                hq2Blue.setImage(tempBlue);
            }  
        }
    }
    
    @FXML
    public void updateProduceLE(){
        C1LE.setText("+10");
        C2LE.setText("+10");
        C3LE.setText("+10");
        C4LE.setText("+10");
        C5LE.setText("+10");
    }
    
    @FXML
    public void removeProduceLEdisplay(){
        C1LE.setText("");
        C2LE.setText("");
        C3LE.setText("");
        C4LE.setText("");
        C5LE.setText("");
    }
       
    @FXML
    public void updateWarriorFetchesLE(int cID, String name, int party, int numLE){
        
        if(party == WorldProperty.RED){
            redstatus_fetchLE.setText(name + " earned " + numLE + " life elements for his headquarter");  
        }
        
        if(party == WorldProperty.BLUE){
            bluestatus_fetchLE.setText(name + " earned " + numLE + " life elements for his headquarter");  
        }
    }
    
    @FXML
    public void updateBattle(int cID){
        if(cID == 1){
            c1battle.setImage(image_battle);
        }
        
        if(cID == 2){
            c2battle.setImage(image_battle);
        }
        
        if(cID == 3){
            c3battle.setImage(image_battle);
        }
        
        if(cID == 4){
            c4battle.setImage(image_battle);
        }
        
        if(cID == 5){
            c5battle.setImage(image_battle);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}