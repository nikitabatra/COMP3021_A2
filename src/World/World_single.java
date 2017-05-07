package World;

import static Warriors.Warrior_single.deadLocation;
import static Warriors.Warrior_single.deadWarrior;
import java.util.ArrayList;

import Warriors.WarriorType;
import Warriors.Warrior_single;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafxapplication1.FXMLDocumentController;
import javafxapplication1.SampleController_single;
import javafx.application.Application;
import javafx.stage.Stage;
import javafxapplication1.JavaFXApplication1;

public class World_single {
	public static Clock WorldClock;
        
	//Cities start from Red Headquarters and end with Blue Headquarters
	public static ArrayList<City_single> CityList;
        public static String timeCount = "";
        public static SampleController_single worldController;
        public static boolean checkOccupied = false;
                
                
        public static void main(String[] args) {
            Application.launch(JavaFXApplication1.class, args);
        }
	
	public World_single(SampleController_single worldController){
		this.worldController = worldController;
		//initialize clock 
		WorldClock = new Clock();
		
		//initialize Cities
		CityList = new ArrayList<City_single>();
		
		City_single c0 = new Headquarters_single(WorldProperty.RedProductionOrder, WorldProperty.RED, WorldProperty.InitLifeElements,0);
		CityList.add(c0);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_single c = new City_single(i);
			CityList.add(c);
		}
		
		City_single c_last = new Headquarters_single(WorldProperty.BlueProductionOrder, WorldProperty.BLUE, WorldProperty.InitLifeElements,WorldProperty.NumberOfCity+1);
		CityList.add(c_last);
	}
	
        public static int warriorChosen;
        int keepCount = 0;
        public void runGame(String currentTime) throws InterruptedException, IOException{

                    if( keepCount <= WorldProperty.MaxMinutes/10 ){
			// :00 Produce Warriors on exact hours.
			if (WorldClock.getMinute() == 0){
                                worldController.submitWarrior.setDisable(false);
                                worldController.inputWarriorChosen.setDisable(false);
                                worldController.removeHQLE();
                                worldController.updatePage(currentTime);
                                worldController.startGameDisplay();
                                worldController.setWarriorChosenValue();
                                warriorChosen = worldController.warriorChosen;
                                
                                if(worldController.checkClicked == 1){
                                    worldController.submitWarrior.setDisable(true);
                                    worldController.inputWarriorChosen.setDisable(true);
                                }
                                
                                ((Headquarters_single)CityList.get(0)).tryToProduceWarrior();
				((Headquarters_single)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
                                
                                int blueWarriorType = ((Headquarters_single)CityList.get(0)).whichBlueWarrior;
                                int redWarriorType = ((Headquarters_single)CityList.get(WorldProperty.NumberOfCity+1)).whichRedWarrior;
                                boolean blueSuccess = ((Headquarters_single)CityList.get(0)).blueProductionSuccess;
                                boolean redSuccess = ((Headquarters_single)CityList.get(WorldProperty.NumberOfCity+1)).redProductionSuccess;			
                                
                                worldController.updateProduceWarriors(blueWarriorType, redWarriorType, blueSuccess, redSuccess);
			}
			// :10 March
			if (WorldClock.getMinute() == 10){
                                  worldController.removeWarriorLabel();
                                  worldController.updatePage(currentTime);
                                
				marchWarriors();
				
				//Check End Of Game
				Headquarters_single RedHeadquarters = (Headquarters_single) CityList.get(0);
				Headquarters_single BlueHeadquarters = (Headquarters_single) CityList.get(WorldProperty.NumberOfCity+1);
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
                                worldController.updatePage(currentTime);
				ProduceLifeElements();
                                worldController.updateProduceLE();
                                
			}
			
			// :30 Warriors Fetch Life Elements to their headquarters
			if (WorldClock.getMinute() == 30){
                                worldController.removeProduceLEdisplay();
                                worldController.updatePage(currentTime);
                                warriorsFetchLifeElementsFromCity();
			}
				
			// :40 Organize Battels (Core function.)
			if (WorldClock.getMinute() == 40){
                                worldController.removeFetchLEUpdate();
                                worldController.updatePage(currentTime);
				holdBattlesAndWorkAfterBattles();
			}
			
			// :50 Headquarters report Life Elements
			if (WorldClock.getMinute() == 50){
                                worldController.removeBattleSigns();
                                worldController.updatePage(currentTime);
				headquartersReportLifeElements();
                                
			}
                        timeCount = WorldClock.getTime();
                        keepCount++;
                    }
                        WorldClock.increase();	

	}
        
	public void holdBattlesAndWorkAfterBattles() {
		for (int index=1; index <= WorldProperty.NumberOfCity; index++){
			 City_single c = CityList.get(index);
                         c.organizeBattle();
                         if(c.checkIfBattle == true){
                             worldController.updateBattle(c.CityID);
                             worldController.showWinner(deadWarrior, deadLocation);
                         }        
		 }

		 //reward army.
		 Headquarters_single RedHeadquarters = (Headquarters_single) CityList.get(0);
		 Headquarters_single BlueHeadquarters = (Headquarters_single) CityList.get(WorldProperty.NumberOfCity+1);
		 RedHeadquarters.rewardArmy();
		 BlueHeadquarters.rewardArmy();
		 
		 //collectMoneyFromCity();
		 for (int i=1; i<= WorldProperty.NumberOfCity; i++){
			 City_single c = CityList.get(i);
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
		Headquarters_single RedHeadquarters = (Headquarters_single) CityList.get(0);
		Headquarters_single BlueHeadquarters = (Headquarters_single) CityList.get(WorldProperty.NumberOfCity+1);
                //redNumLE = RedHeadquarters.LifeElement;
                //blueNumLE = BlueHeadquarters.LifeElement;
		System.out.format("%s %d elements in red headquarter\n", WorldClock.getTime(),RedHeadquarters.LifeElement);
		System.out.format("%s %d elements in blue headquarter\n", WorldClock.getTime(),BlueHeadquarters.LifeElement);
                worldController.updateHQLE(RedHeadquarters.LifeElement, BlueHeadquarters.LifeElement);
                worldController.inputWarriorChosen.setDisable(false);
                worldController.submitWarrior.setDisable(false);
        }
        
        public static boolean blueWarriorFetchesLE = false;
        public static boolean redWarriorFetchesLE = false;
	//After March
	public void warriorsFetchLifeElementsFromCity() {
		Headquarters_single RedHeadquarters = (Headquarters_single) CityList.get(0);
		Headquarters_single BlueHeadquarters = (Headquarters_single) CityList.get(WorldProperty.NumberOfCity+1);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City_single c = CityList.get(i);
			//Empty City
			if (c.BlueWarriorStation.isEmpty() && c.RedWarriorStation.isEmpty()){
				continue;
			}
			//Red Fetch
			else if (c.BlueWarriorStation.isEmpty()){
				Warrior_single w = c.RedWarriorStation.get(0);
                                worldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(), w.WarriorNameCard,c.LifeElement);
				RedHeadquarters.addLifeElement(c.popLifeElements());
			}
			else if (c.RedWarriorStation.isEmpty()){

				Warrior_single w = c.BlueWarriorStation.get(0);
                                worldController.updateWarriorFetchesLE(c.CityID, w.WarriorNameCard, w.Party, c.LifeElement);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(),w.WarriorNameCard,c.LifeElement);
				BlueHeadquarters.addLifeElement(c.popLifeElements());
			} else {
				// Two warriors in this city.
			}
		}
	}

	public void ProduceLifeElements(){
		for (City_single c: CityList){
			if (!(c instanceof Headquarters_single)){
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
			City_single city = CityList.get(i);
			while (!city.RedWarriorStation.isEmpty()){
                                int redMoveTo = city.RedWarriorStation.get(0).Location + 1;                              
				city.RedWarriorStation.get(0).move();
                                worldController.updateWarriorMarch(WorldProperty.RED, redMoveTo);
			}
		}
		//March Blue Warriors.
		for (int i=1;i<=WorldProperty.NumberOfCity+1;i++){
			City_single city = CityList.get(i);
			while (!city.BlueWarriorStation.isEmpty()){
                                int blueMoveTo = city.BlueWarriorStation.get(0).Location - 1;
				city.BlueWarriorStation.get(0).move();
                                worldController.updateWarriorMarch(WorldProperty.BLUE, blueMoveTo);
			}
		}

		//Report March Information
		Headquarters_single RedHeadquarters = (Headquarters_single) CityList.get(0);
		Headquarters_single BlueHeadquarters = (Headquarters_single) CityList.get(WorldProperty.NumberOfCity+1);
		
		if (RedHeadquarters.checkNewArrival()){
			warriorReportMarch(RedHeadquarters, RedHeadquarters.getNewArrivedWarrior());
			RedHeadquarters.clearNewArrival();
		}
		for (int index=1; index<= WorldProperty.NumberOfCity; index++){
			City_single c = CityList.get(index);
			for (Warrior_single w:c.RedWarriorStation){
				warriorReportMarch(c,w);	
			}	
			for (Warrior_single w:c.BlueWarriorStation){
				warriorReportMarch(c,w);
			}
		}
		if (BlueHeadquarters.checkNewArrival()){
			warriorReportMarch(BlueHeadquarters, BlueHeadquarters.getNewArrivedWarrior());
			BlueHeadquarters.clearNewArrival();
		}			
	}
	
	public void warriorReportMarch(City_single c,Warrior_single w) {
		if (c instanceof Headquarters_single){
			//003:10 red lion 2 reached blue headquarter with 58 elements and force 50
			System.out.format("%s %s reached %s headquarter with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, WorldProperty.PartyNames[((Headquarters_single)c).getParty()], w.HP, w.AttackValue);
		}
		else {
			System.out.format("%s %s marched to city %d with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, c.CityID, w.HP, w.AttackValue);
		}
	}
	
}
