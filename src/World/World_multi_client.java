package World;

//import static Warriors.Warrior_multi.deadLocation;
//import static Warriors.Warrior_multi.deadWarrior;
import java.util.ArrayList;

import Warriors.WarriorType;
//import static Warriors.Warrior_multi.deadLocation;
//import static Warriors.Warrior_multi.deadWarrior;
//import Warriors.Warrior_multi;
import static Warriors.Warrior_multi_client.deadLocation;
import static Warriors.Warrior_multi_client.deadWarrior;

import Warriors.Warrior_multi_client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
import javafx.stage.Stage;
import javafxapplication1.JavaFXApplication1;

public class World_multi_client {
	public static Clock WorldClock;
        
	//Cities start from Red Headquarters and end with Blue Headquarters
	public static ArrayList<City_multi_client> CityList;
        public static String timeCount = "";
        //public static SampleController_multi_server multiWorldController;
        public static SampleController_multi_server serverWrorldController;
        public static SampleController_multi_client clientWorldController;
        public static boolean checkOccupied = false;
                
                
        public static void main(String[] args) {
            Application.launch(JavaFXApplication1.class, args);
        }
	
	public World_multi_client(SampleController_multi_client clientWorldController) throws IOException{

		this.clientWorldController = clientWorldController;
		//initialize clock 
		WorldClock = new Clock();
		
		//initialize Cities
		CityList = new ArrayList<City_multi_client>();
		
		City_multi_client c0 = new Headquarters_multi_client(WorldProperty.RedProductionOrder, WorldProperty.RED, WorldProperty.InitLifeElements,0);
		CityList.add(c0);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_multi_client c = new City_multi_client(i);
			CityList.add(c);
		}
		
		City_multi_client c_last = new Headquarters_multi_client(WorldProperty.BlueProductionOrder, WorldProperty.BLUE, WorldProperty.InitLifeElements,WorldProperty.NumberOfCity+1);
		CityList.add(c_last);
	}

	
        public static int serverWarriorChosen;
        public static int clientWarriorChosen;
        int keepCount = 0;
        
        public static DataInputStream fromServer;
        public static DataOutputStream toServer;
        
        public static boolean continueToPlay = true;
        
        public static void connectToServer(){
            
            try {
                Socket socket = new Socket("localhost", 6000);
                fromServer = new DataInputStream(socket.getInputStream());
                toServer = new DataOutputStream(socket.getOutputStream());
            }
            
            catch(IOException e){
                e.printStackTrace();
            }
            
            new Thread(() -> {
               try{
                   int serverWarrior = fromServer.readInt();
                   
                   while(continueToPlay){
                       waitForServerAction();
                       sendMove();
                       receiveInfoFromServer();
                   }
               }
               
               catch(Exception ex){
                   ex.printStackTrace();
               }
            });
            
        }
        
        public static boolean waiting = false;
        
        public static void waitForServerAction() throws InterruptedException{
            while(waiting){
                Thread.sleep(1000);
            }
            
            waiting = true;
        }
        
        public static void sendMove() throws IOException{
            toServer.writeInt(clientWarriorChosen);
        }
        
        public static void receiveInfoFromServer() throws IOException{
            serverWarriorChosen = fromServer.readInt();
            waiting = false;
        }
        
        public void runGame(String currentTime) throws InterruptedException, IOException{

                    if( keepCount <= WorldProperty.MaxMinutes/10 ){
			// :00 Produce Warriors on exact hours.
			if (WorldClock.getMinute() == 0){
                                clientWorldController.submitWarrior.setDisable(false);
                                clientWorldController.inputWarriorChosen.setDisable(false);
                                clientWorldController.removeHQLE();
                                clientWorldController.updatePage(currentTime);
                                clientWorldController.startGameDisplay();
                                clientWorldController.setWarriorChosenValue();
                                clientWarriorChosen = clientWorldController.warriorChosen;
                                
                                if(clientWorldController.checkClicked == 1){
                                    clientWorldController.submitWarrior.setDisable(true);
                                    clientWorldController.inputWarriorChosen.setDisable(true);
                                }
                                
                                ((Headquarters_multi_client)CityList.get(0)).tryToProduceWarrior();
				((Headquarters_multi_client)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
                                
                                int blueWarriorType = ((Headquarters_multi_client)CityList.get(0)).whichBlueWarrior;
                                int redWarriorType = ((Headquarters_multi_client)CityList.get(WorldProperty.NumberOfCity+1)).whichRedWarrior;
                                boolean blueSuccess = ((Headquarters_multi_client)CityList.get(0)).blueProductionSuccess;
                                boolean redSuccess = ((Headquarters_multi_client)CityList.get(WorldProperty.NumberOfCity+1)).redProductionSuccess;			
                                
                                clientWorldController.updateProduceWarriors(blueWarriorType, redWarriorType, blueSuccess, redSuccess);
			}
			// :10 March
			if (WorldClock.getMinute() == 10){
                                  clientWorldController.removeWarriorLabel();
                                  clientWorldController.updatePage(currentTime);
                                
				marchWarriors();
				
				//Check End Of Game
				Headquarters_multi_client RedHeadquarters = (Headquarters_multi_client) CityList.get(0);
				Headquarters_multi_client BlueHeadquarters = (Headquarters_multi_client) CityList.get(WorldProperty.NumberOfCity+1);
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
                                clientWorldController.updatePage(currentTime);
				ProduceLifeElements();
                                clientWorldController.updateProduceLE();
                                
			}
			
			// :30 Warriors Fetch Life Elements to their headquarters
			if (WorldClock.getMinute() == 30){
                                clientWorldController.removeProduceLEdisplay();
                                clientWorldController.updatePage(currentTime);
                                warriorsFetchLifeElementsFromCity();
			}
				
			// :40 Organize Battels (Core function.)
			if (WorldClock.getMinute() == 40){
                                clientWorldController.removeFetchLEUpdate();
                                clientWorldController.updatePage(currentTime);
				holdBattlesAndWorkAfterBattles();
			}
			
			// :50 Headquarters report Life Elements
			if (WorldClock.getMinute() == 50){
                                clientWorldController.removeBattleSigns();
                                clientWorldController.updatePage(currentTime);
				headquartersReportLifeElements();
                                
			}
                        timeCount = WorldClock.getTime();
                        keepCount++;
                    }
                        WorldClock.increase();	

	}
        
	public void holdBattlesAndWorkAfterBattles() {
		for (int index=1; index <= WorldProperty.NumberOfCity; index++){
			 City_multi_client c = CityList.get(index);
                         c.organizeBattle();
                         if(c.checkIfBattle == true){
                             clientWorldController.updateBattle(c.CityID);
                             clientWorldController.showWinner(deadWarrior, deadLocation);
                         }        
		 }

		 //reward army.
		 Headquarters_multi_client RedHeadquarters = (Headquarters_multi_client) CityList.get(0);
		 Headquarters_multi_client BlueHeadquarters = (Headquarters_multi_client) CityList.get(WorldProperty.NumberOfCity+1);
		 RedHeadquarters.rewardArmy();
		 BlueHeadquarters.rewardArmy();
		 
		 //collectMoneyFromCity();
		 for (int i=1; i<= WorldProperty.NumberOfCity; i++){
			 City_multi_client c = CityList.get(i);
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
		Headquarters_multi_client RedHeadquarters = (Headquarters_multi_client) CityList.get(0);
		Headquarters_multi_client BlueHeadquarters = (Headquarters_multi_client) CityList.get(WorldProperty.NumberOfCity+1);
                //redNumLE = RedHeadquarters.LifeElement;
                //blueNumLE = BlueHeadquarters.LifeElement;
		System.out.format("%s %d elements in red headquarter\n", WorldClock.getTime(),RedHeadquarters.LifeElement);
		System.out.format("%s %d elements in blue headquarter\n", WorldClock.getTime(),BlueHeadquarters.LifeElement);
                clientWorldController.updateHQLE(RedHeadquarters.LifeElement, BlueHeadquarters.LifeElement);
                clientWorldController.inputWarriorChosen.setDisable(false);
                clientWorldController.submitWarrior.setDisable(false);
        }
        
        public static boolean blueWarriorFetchesLE = false;
        public static boolean redWarriorFetchesLE = false;
	//After March
	public void warriorsFetchLifeElementsFromCity() {
		Headquarters_multi_client RedHeadquarters = (Headquarters_multi_client) CityList.get(0);
		Headquarters_multi_client BlueHeadquarters = (Headquarters_multi_client) CityList.get(WorldProperty.NumberOfCity+1);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_multi_client c = CityList.get(i);
			//Empty City
			if (c.BlueWarriorStation.isEmpty() && c.RedWarriorStation.isEmpty()){
				continue;
			}
			//Red Fetch
			else if (c.BlueWarriorStation.isEmpty()){
				Warrior_multi_client w = c.RedWarriorStation.get(0);
                                clientWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(), w.WarriorNameCard,c.LifeElement);
				RedHeadquarters.addLifeElement(c.popLifeElements());
			}
			else if (c.RedWarriorStation.isEmpty()){

				Warrior_multi_client w = c.BlueWarriorStation.get(0);
                                clientWorldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(),w.WarriorNameCard,c.LifeElement);
				BlueHeadquarters.addLifeElement(c.popLifeElements());
			} else {
				// Two warriors in this city.
			}
		}
	}

	public void ProduceLifeElements(){
		for (City_multi_client c: CityList){
			if (!(c instanceof Headquarters_multi_client)){
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
			City_multi_client city = CityList.get(i);
			while (!city.RedWarriorStation.isEmpty()){
                                int redMoveTo = city.RedWarriorStation.get(0).Location + 1;                              
				city.RedWarriorStation.get(0).move();
                                clientWorldController.updateWarriorMarch(WorldProperty.RED, redMoveTo);
			}
		}
		//March Blue Warriors.
		for (int i=1;i<=WorldProperty.NumberOfCity+1;i++){
			City_multi_client city = CityList.get(i);
			while (!city.BlueWarriorStation.isEmpty()){
                                int blueMoveTo = city.BlueWarriorStation.get(0).Location - 1;
				city.BlueWarriorStation.get(0).move();
                                clientWorldController.updateWarriorMarch(WorldProperty.BLUE, blueMoveTo);
			}
		}

		//Report March Information
		Headquarters_multi_client RedHeadquarters = (Headquarters_multi_client) CityList.get(0);
		Headquarters_multi_client BlueHeadquarters = (Headquarters_multi_client) CityList.get(WorldProperty.NumberOfCity+1);
		
		if (RedHeadquarters.checkNewArrival()){
			warriorReportMarch(RedHeadquarters, RedHeadquarters.getNewArrivedWarrior());
			RedHeadquarters.clearNewArrival();
		}
		for (int index=1; index<= WorldProperty.NumberOfCity; index++){
			City_multi_client c = CityList.get(index);
			for (Warrior_multi_client w:c.RedWarriorStation){
				warriorReportMarch(c,w);	
			}	
			for (Warrior_multi_client w:c.BlueWarriorStation){
				warriorReportMarch(c,w);
			}
		}
		if (BlueHeadquarters.checkNewArrival()){
			warriorReportMarch(BlueHeadquarters, BlueHeadquarters.getNewArrivedWarrior());
			BlueHeadquarters.clearNewArrival();
		}			
	}
	
	public void warriorReportMarch(City_multi_client c,Warrior_multi_client w) {
		if (c instanceof Headquarters_multi_client){
			//003:10 red lion 2 reached blue headquarter with 58 elements and force 50
			System.out.format("%s %s reached %s headquarter with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, WorldProperty.PartyNames[((Headquarters_multi_client)c).getParty()], w.HP, w.AttackValue);
		}
		else {
			System.out.format("%s %s marched to city %d with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, c.CityID, w.HP, w.AttackValue);
		}
	}
        
        
	
}
