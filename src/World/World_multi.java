package World;

import static Warriors.Warrior_multi.deadLocation;
import static Warriors.Warrior_multi.deadWarrior;
import java.util.ArrayList;

import Warriors.WarriorType;
import Warriors.Warrior_multi;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafxapplication1.FXMLDocumentController;
import javafxapplication1.SampleController_multi_server;
import javafxapplication1.SampleController_multi_client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafxapplication1.JavaFXApplication1;

public class World_multi {
	public static Clock WorldClock;
        
	//Cities start from Red Headquarters and end with Blue Headquarters
	public static ArrayList<City_multi> CityList;
        public static String timeCount = "";
        //public static SampleController_multi_server multiWorldController;
        public static SampleController_multi_server serverWorldController;
        public static SampleController_multi_client clientWorldController;
        public static boolean checkOccupied = false;
                
                
        public static void main(String[] args) {
            Application.launch(JavaFXApplication1.class, args);
        }
	
        public static int clientWarrior;
	public World_multi(SampleController_multi_server serverWorldController) throws IOException{
        
            
		this.serverWorldController = serverWorldController;
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
        
        
	
        public static int serverWarriorChosen;
        public static int clientWarriorChosen;
        
        
        public static void connectClient(){
            new Thread( () -> {
                try {
                    ServerSocket serverSocket = new ServerSocket(6000);
                    Platform.runLater(() ->
                    System.out.println("This is server the new way"));

                    
                    Socket socket = serverSocket.accept();
                    System.out.println("Waiting for client input");

                    DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

                    while(true){
                        // get input from client
                        clientWarrior = inputFromClient.readInt();

                        //send time to client
                        outputToClient.writeChars("Output to client from server");

                        Platform.runLater(() -> {
                            System.out.println("Warrior chosen by client: "+clientWarrior);
                        });
                    }   
                }
                catch(IOException e){
                    e.printStackTrace();
                }

            }).start();
        }
        
        int keepCount = 0;
        
        public void runGame(String currentTime) throws InterruptedException, IOException{

                    if( keepCount <= WorldProperty.MaxMinutes/10 ){
			// :00 Produce Warriors on exact hours.
			if (WorldClock.getMinute() == 0){
                            
                            
                                serverWorldController.submitWarrior.setDisable(false);
                                serverWorldController.inputWarriorChosen.setDisable(false);
                                serverWorldController.removeHQLE();
                                serverWorldController.updatePage(currentTime);
                                serverWorldController.startGameDisplay();
                                serverWorldController.setWarriorChosenValue();
                                serverWarriorChosen = serverWorldController.warriorChosen;
                                clientWarriorChosen = clientWarrior;
                                
                                if(serverWorldController.checkClicked == 1){
                                    serverWorldController.submitWarrior.setDisable(true);
                                    serverWorldController.inputWarriorChosen.setDisable(true);
                                }
                                
                                ((Headquarters_multi)CityList.get(0)).tryToProduceWarrior();
				((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
                                
                                int blueWarriorType = ((Headquarters_multi)CityList.get(0)).whichBlueWarrior;
                                int redWarriorType = ((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).whichRedWarrior;
                                boolean blueSuccess = ((Headquarters_multi)CityList.get(0)).blueProductionSuccess;
                                boolean redSuccess = ((Headquarters_multi)CityList.get(WorldProperty.NumberOfCity+1)).redProductionSuccess;			
                                
                                serverWorldController.updateProduceWarriors(blueWarriorType, redWarriorType, blueSuccess, redSuccess);
			}
			// :10 March
			if (WorldClock.getMinute() == 10){
                                  serverWorldController.removeWarriorLabel();
                                  serverWorldController.updatePage(currentTime);
                                
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
                                serverWorldController.updatePage(currentTime);
				ProduceLifeElements();
                                serverWorldController.updateProduceLE();
                                
			}
			
			// :30 Warriors Fetch Life Elements to their headquarters
			if (WorldClock.getMinute() == 30){
                                serverWorldController.removeProduceLEdisplay();
                                serverWorldController.updatePage(currentTime);
                                warriorsFetchLifeElementsFromCity();
			}
				
			// :40 Organize Battels (Core function.)
			if (WorldClock.getMinute() == 40){
                                serverWorldController.removeFetchLEUpdate();
                                serverWorldController.updatePage(currentTime);
				holdBattlesAndWorkAfterBattles();
			}
			
			// :50 Headquarters report Life Elements
			if (WorldClock.getMinute() == 50){
                                serverWorldController.removeBattleSigns();
                                serverWorldController.updatePage(currentTime);
				headquartersReportLifeElements();
                                
			}
                        timeCount = WorldClock.getTime();
                        keepCount++;
                    }
                        WorldClock.increase();	

	}
        
	public void holdBattlesAndWorkAfterBattles() {
		for (int index=1; index <= WorldProperty.NumberOfCity; index++){
			 City_multi c = CityList.get(index);
                         c.organizeBattle();
                         if(c.checkIfBattle == true){
                             serverWorldController.updateBattle(c.CityID);
                             serverWorldController.showWinner(deadWarrior, deadLocation);
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
                serverWorldController.updateHQLE(RedHeadquarters.LifeElement, BlueHeadquarters.LifeElement);
                serverWorldController.inputWarriorChosen.setDisable(false);
                serverWorldController.submitWarrior.setDisable(false);
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
                                serverWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(), w.WarriorNameCard,c.LifeElement);
				RedHeadquarters.addLifeElement(c.popLifeElements());
			}
			else if (c.RedWarriorStation.isEmpty()){

				Warrior_multi w = c.BlueWarriorStation.get(0);
                                serverWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
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
                                serverWorldController.updateWarriorMarch(WorldProperty.RED, redMoveTo);
			}
		}
		//March Blue Warriors.
		for (int i=1;i<=WorldProperty.NumberOfCity+1;i++){
			City_multi city = CityList.get(i);
			while (!city.BlueWarriorStation.isEmpty()){
                                int blueMoveTo = city.BlueWarriorStation.get(0).Location - 1;
				city.BlueWarriorStation.get(0).move();
                                serverWorldController.updateWarriorMarch(WorldProperty.BLUE, blueMoveTo);
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
