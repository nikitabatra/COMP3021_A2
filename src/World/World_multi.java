package World;


import static Warriors.Warrior_multi.deadLocation;
import static Warriors.Warrior_multi.deadWarrior;
import java.util.ArrayList;

import Warriors.WarriorType;
import Warriors.Warrior_multi;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafxapplication1.FXMLDocumentController;
import javafxapplication1.SampleController_multi;
import javafx.application.Application;
import javafx.stage.Stage;
import javafxapplication1.GreetingServer;
import javafxapplication1.GreetingClient;
import javafxapplication1.JavaFXApplication1;

public class World_multi {
	public static Clock WorldClock;
        
	//Cities start from Red Headquarters and end with Blue Headquarters
	public static ArrayList<City_multi> CityList;
        public static String timeCount = "";
        public static SampleController_multi multiWorldController;
        public static FXMLDocumentController cont;
        public static boolean checkOccupied = false;
        protected ServerSocket serverSocket = null;
        
                
        protected DataInputStream incomingData;
        protected DataOutputStream outgoingData;
        public static void main(String[] args) {
            Application.launch(JavaFXApplication1.class, args);
        }
	
	public World_multi(SampleController_multi multiWorldController) throws IOException {
                
                
		this.multiWorldController = multiWorldController;
		//initialize clock 
		WorldClock = new Clock();
		
		//initialize Cities
		CityList = new ArrayList<City_multi>();
		
		City_multi c0 = new Headquarters_multi(WorldProperty.RedProductionOrder, WorldProperty.RED, WorldProperty.InitLifeElements,0);
		CityList.add(c0);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_multi c = new City_multi(i);
			CityList.add(c);
		}
		
		City_multi c_last = new Headquarters_multi(WorldProperty.BlueProductionOrder, WorldProperty.BLUE, WorldProperty.InitLifeElements,WorldProperty.NumberOfCity+1);
		CityList.add(c_last);
	}
	
        public static int warriorChosen;
        public static int clientWarriorListen;
        
        int keepCount = 0;
        public void runGame(String currentTime) throws InterruptedException, IOException{  

            if( keepCount <= WorldProperty.MaxMinutes/10 ){
			// :00 Produce Warriors on exact hours.
			if (WorldClock.getMinute() == 0){
                                multiWorldController.submitWarrior.setDisable(false);
                                multiWorldController.inputWarriorChosen.setDisable(false);
                                multiWorldController.removeHQLE();
                                multiWorldController.updatePage(currentTime);
                                multiWorldController.startGameDisplay();
                                multiWorldController.setWarriorChosenValue();
                                warriorChosen = multiWorldController.warriorChosen;
                                
                                if(multiWorldController.checkClicked == 1){
                                    multiWorldController.submitWarrior.setDisable(true);
                                    multiWorldController.inputWarriorChosen.setDisable(true);
                                }
                                
                                ((Headquarters_multi)CityList.get(0)).tryToProduceWarrior();
				((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
                                
                                int blueWarriorType = ((Headquarters_multi)CityList.get(0)).whichBlueWarrior;
                                int redWarriorType = ((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).whichRedWarrior;
                                boolean blueSuccess = ((Headquarters_multi)CityList.get(0)).blueProductionSuccess;
                                boolean redSuccess = ((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).redProductionSuccess;			
                                
                                multiWorldController.updateProduceWarriors(blueWarriorType, redWarriorType, blueSuccess, redSuccess);
			}
			// :10 March
			if (WorldClock.getMinute() == 10){
                                  multiWorldController.removeWarriorLabel();
                                  multiWorldController.updatePage(currentTime);
                                
				marchWarriors();
				
				//Check End Of Game
				Headquarters_multi RedHeadquarters = (Headquarters_multi) CityList.get(0);
				Headquarters_multi BlueHeadquarters = (Headquarters_multi) CityList.get(WorldProperty.NumberOfCity+1);
				boolean RedOcuupied = RedHeadquarters.checkOccupied();
				boolean BlueOcuupied = BlueHeadquarters.checkOccupied();
				if ( RedOcuupied || BlueOcuupied ){
					checkOccupied = true;
				}
				if ( RedOcuupied || BlueOcuupied ){
					return;
				}
				
			}
			// :20 Produce Life Elements
			if (WorldClock.getMinute() == 20){
                                multiWorldController.updatePage(currentTime);
				ProduceLifeElements();
                                multiWorldController.updateProduceLE();
                                
			}
			
			// :30 Warriors Fetch Life Elements to their headquarters
			if (WorldClock.getMinute() == 30){
                                multiWorldController.removeProduceLEdisplay();
                                multiWorldController.updatePage(currentTime);
                                warriorsFetchLifeElementsFromCity();
			}
				
			// :40 Organize Battels (Core function.)
			if (WorldClock.getMinute() == 40){
                                multiWorldController.removeFetchLEUpdate();
                                multiWorldController.updatePage(currentTime);
				holdBattlesAndWorkAfterBattles();
			}
			
			// :50 Headquarters report Life Elements
			if (WorldClock.getMinute() == 50){
                                multiWorldController.removeBattleSigns();
                                multiWorldController.updatePage(currentTime);
				headquartersReportLifeElements();
                                
			}
                        timeCount = WorldClock.getTime();
                        keepCount++;
                    }
                        WorldClock.increase();	
                        //output.write(time)
                        
//                        
//                        this.outgoingData = new DataOutputStream(server.getOutputStream());
//                        outgoingData.writeUTF("Time is : "  + WorldClock.getTime() + "\nGoodbye!");
//                        
////                        server.close(); 
//                        serverSocket.close();
        }
	public void holdBattlesAndWorkAfterBattles() {
		for (int index=1; index <= WorldProperty.NumberOfCity; index++){
			 City_multi c = CityList.get(index);
                         c.organizeBattle();
                         if(c.checkIfBattle == true){
                             multiWorldController.updateBattle(c.CityID);
                             multiWorldController.showWinner(deadWarrior, deadLocation);
                         }        
		 }

		 //reward army.
		 Headquarters_multi RedHeadquarters = (Headquarters_multi) CityList.get(0);
		 Headquarters_multi BlueHeadquarters = (Headquarters_multi) CityList.get(WorldProperty.NumberOfCity+1);
		 RedHeadquarters.rewardArmy();
		 BlueHeadquarters.rewardArmy();
		 
		 //collectMoneyFromCity();
		 for (int i=1; i<= WorldProperty.NumberOfCity; i++){
			 City_multi c = CityList.get(i);
			 c.payTribute();
		 }
	}

	/**
	 * 
	 */
        
        public static int redNumLE;
        public static int blueNumLE;
	public void headquartersReportLifeElements() {
		//000:50 100 elements in red headquarter
		Headquarters_multi RedHeadquarters = (Headquarters_multi) CityList.get(0);
		Headquarters_multi BlueHeadquarters = (Headquarters_multi) CityList.get(WorldProperty.NumberOfCity+1);
                //redNumLE = RedHeadquarters.LifeElement;
                //blueNumLE = BlueHeadquarters.LifeElement;
		System.out.format("%s %d elements in red headquarter\n", WorldClock.getTime(),RedHeadquarters.LifeElement);
		System.out.format("%s %d elements in blue headquarter\n", WorldClock.getTime(),BlueHeadquarters.LifeElement);
                multiWorldController.updateHQLE(RedHeadquarters.LifeElement, BlueHeadquarters.LifeElement);
                multiWorldController.inputWarriorChosen.setDisable(false);
                multiWorldController.submitWarrior.setDisable(false);
        }
        
        public static boolean blueWarriorFetchesLE = false;
        public static boolean redWarriorFetchesLE = false;
	//After March
	public void warriorsFetchLifeElementsFromCity() {
		Headquarters_multi RedHeadquarters = (Headquarters_multi) CityList.get(0);
		Headquarters_multi BlueHeadquarters = (Headquarters_multi) CityList.get(WorldProperty.NumberOfCity+1);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_multi c = CityList.get(i);
			//Empty City
			if (c.BlueWarriorStation.isEmpty() && c.RedWarriorStation.isEmpty()){
				continue;
			}
			//Red Fetch
			else if (c.BlueWarriorStation.isEmpty()){
				Warrior_multi w = c.RedWarriorStation.get(0);
                                multiWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(), w.WarriorNameCard,c.LifeElement);
				RedHeadquarters.addLifeElement(c.popLifeElements());
			}
			else if (c.RedWarriorStation.isEmpty()){

				Warrior_multi w = c.BlueWarriorStation.get(0);
                                multiWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(),w.WarriorNameCard,c.LifeElement);
				BlueHeadquarters.addLifeElement(c.popLifeElements());
			} else {
				// Two warriors in this city.
			}
		}
	}

	public void ProduceLifeElements(){
		for (City_multi c: CityList){
			if (!(c instanceof Headquarters_multi)){
				c.produceLifeElement();
			}
		}
	}

	//TODO: modify operation on CityList to Warrior.move()
        public static int blueMoveTo;
        public static int redMoveTo;
	public void marchWarriors(){
            
		//March Red Warriors.		
		for (int i=WorldProperty.NumberOfCity;i>=0;i--){
			City_multi city = CityList.get(i);
			while (!city.RedWarriorStation.isEmpty()){
                                int redMoveTo = city.RedWarriorStation.get(0).Location + 1;                              
				city.RedWarriorStation.get(0).move();
                                multiWorldController.updateWarriorMarch(WorldProperty.RED, redMoveTo);
			}
		}
		//March Blue Warriors.
		for (int i=1;i<=WorldProperty.NumberOfCity+1;i++){
			City_multi city = CityList.get(i);
			while (!city.BlueWarriorStation.isEmpty()){
                                int blueMoveTo = city.BlueWarriorStation.get(0).Location - 1;
				city.BlueWarriorStation.get(0).move();
                                multiWorldController.updateWarriorMarch(WorldProperty.BLUE, blueMoveTo);
			}
		}

		//Report March Information
		Headquarters_multi RedHeadquarters = (Headquarters_multi) CityList.get(0);
		Headquarters_multi BlueHeadquarters = (Headquarters_multi) CityList.get(WorldProperty.NumberOfCity+1);
		
		if (RedHeadquarters.checkNewArrival()){
			warriorReportMarch(RedHeadquarters, RedHeadquarters.getNewArrivedWarrior());
			RedHeadquarters.clearNewArrival();
		}
		for (int index=1; index<= WorldProperty.NumberOfCity; index++){
			City_multi c = CityList.get(index);
			for (Warrior_multi w:c.RedWarriorStation){
				warriorReportMarch(c,w);	
			}	
			for (Warrior_multi w:c.BlueWarriorStation){
				warriorReportMarch(c,w);
			}
		}
		if (BlueHeadquarters.checkNewArrival()){
			warriorReportMarch(BlueHeadquarters, BlueHeadquarters.getNewArrivedWarrior());
			BlueHeadquarters.clearNewArrival();
		}			
	}
	
	public void warriorReportMarch(City_multi c,Warrior_multi w) {
		if (c instanceof Headquarters_multi){
			//003:10 red lion 2 reached blue headquarter with 58 elements and force 50
			System.out.format("%s %s reached %s headquarter with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, WorldProperty.PartyNames[((Headquarters_multi)c).getParty()], w.HP, w.AttackValue);
		}
		else {
			System.out.format("%s %s marched to city %d with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, c.CityID, w.HP, w.AttackValue);
		}
	}
	
}
