/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

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

    @FXML private ImageView hq1Flag;
    @FXML private ImageView hq2Flag;
    @FXML private Label displayTime;
    
    
    @FXML private Image image_hq1Flag = new Image("flag_blue.png");
    @FXML private Image image_hq2Flag = new Image("flag_red.png");
    
    @FXML
    public void updatePage(String data){
        displayTime.setText(data);
        System.out.println("Entered update page"); 
    }
    
    @FXML
    private void handleStartGameA1(ActionEvent event) throws InterruptedException, IOException{ // When startgame is selected
        System.out.println("Game has been started!");
        displayTime.setText("We will have the time here");
        
        Thread t = new Thread();
        if (this.WorldObject == null)
                this.WorldObject = new World(this);
        //w.runGame();
        
        String currentTime = this.WorldObject.WorldClock.getTime();
        System.out.println("TIME BEFOREEEEEEEEEE" + currentTime);
        this.WorldObject.runGame(currentTime);
        
//		for (int minute=0; minute<=WorldProperty.MaxMinutes/10; minute++){
//                        
//			// :00 Produce Warriors on exact hours.
//			if (WorldClock.getMinute() == 0){
//                                
//                                t.sleep(500);
//                                
//                                 Platform.runLater(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        displayTime.setText(WorldClock.getTime());
//                                    }
//                                });
//                                
//				((Headquarters)CityList.get(0)).tryToProduceWarrior();
//				((Headquarters)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
//			}
//			// :10 March
//			if (WorldClock.getMinute() == 10){
//                            //worldController.updatePage(WorldClock.getTime());
//                            
//                                t.sleep(500);
//                                
//
//                                 
//				w.marchWarriors();
//				
//				//Check End Of Game
//				Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
//				Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
//				boolean RedOcuupied = RedHeadquarters.checkOccupied();
//				boolean BlueOcuupied = BlueHeadquarters.checkOccupied();
//				if ( RedOcuupied || BlueOcuupied ){
//					return;
//				}
//				
//			}
//                       
//			// :20 Produce Life Elements
//			if (WorldClock.getMinute() == 20){
//                                displayTime.setText(WorldClock.getTime());
//				w.ProduceLifeElements();
//			}
//			
//			// :30 Warriors Fetch Life Elements to their headquarters
//			if (WorldClock.getMinute() == 30){
//                                //displayTime.setText(WorldClock.getTime());
//                                System.out.println("CHECK IF WARRIOR FETCHES: " + warriorFetchesElements);
//                                if(warriorFetchesElements == true){
//                                    t.sleep(500);
//                                    
//                                 Platform.runLater(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        displayTime.setText(WorldClock.getTime());
//                                    }
//                                });
//                                }
//                                w.warriorsFetchLifeElementsFromCity();
//			}
//				
//			// :40 Organize Battels (Core function.)
//			if (WorldClock.getMinute() == 40){
//                                //displayTime.setText(WorldClock.getTime());
//                                System.out.println("CHECK IF NO BATTLE: " + checkIfNoBattle);
//                                if(checkIfNoBattle == false){
//                                    t.sleep(500);
//                                    
//                                 Platform.runLater(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        displayTime.setText(WorldClock.getTime());
//                                    }
//                                });
//                                    
//                                    w.holdBattlesAndWorkAfterBattles();
//                                }
//				w.holdBattlesAndWorkAfterBattles();
//			}
//			
//			// :50 Headquarters report Life Elements
//			if (WorldClock.getMinute() == 50){
//                                //displayTime.setText(WorldClock.getTime());
//                                t.sleep(500);
//                                
//                                 Platform.runLater(new Runnable() {
//
//                                    @Override
//                                    public void run() {
//                                        displayTime.setText(WorldClock.getTime());
//                                    }
//                                });
//                                
//                                
//				w.headquartersReportLifeElements();	
//			}
//                        timeCount = WorldClock.getTime();
//			WorldClock.increase();
//                }
        
    }
    
    @FXML
    private void handleMoveForward(ActionEvent event) throws InterruptedException, IOException{ // When startgame is selected
        System.out.println("Game moving forward!");
        //displayTime.setText("We will have the time here");

    }   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Reached Sample Controller! WHOOOOOOOOPSSSSSSS");
        hq1Flag.setImage(image_hq1Flag);
        hq2Flag.setImage(image_hq2Flag);
    }
}