package World;

import java.util.ArrayList;

import Warriors.Warrior;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafxapplication1.FXMLDocumentController;
import javafxapplication1.SampleController;
import javafx.application.Application;
import javafxapplication1.JavaFXApplication1;

public class World {
	public static Clock WorldClock;
	//Cities start from Red Headquarters and end with Blue Headquarters
	public static ArrayList<City> CityList;
        public static String timeCount = "";
        
        public static void main(String[] args) {
            Application.launch(JavaFXApplication1.class, args);
    }
	
	public World(){
		
		//initialize clock
		WorldClock = new Clock();
		
		//initialize Cities
		CityList = new ArrayList<City>();
		
		City c0 = new Headquarters(WorldProperty.RedProductionOrder, WorldProperty.RED, WorldProperty.InitLifeElements,0);
		CityList.add(c0);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City c = new City(i);
			CityList.add(c);
		}
		
		City c_last = new Headquarters(WorldProperty.BlueProductionOrder, WorldProperty.BLUE, WorldProperty.InitLifeElements,WorldProperty.NumberOfCity+1);
		CityList.add(c_last);
	
	}
	
        public void runGame() throws InterruptedException, IOException{
		for (int minute=0; minute<=WorldProperty.MaxMinutes/10; minute++){
			// :00 Produce Warriors on exact hours.
			if (WorldClock.getMinute() == 0){
                                Thread.sleep(500);

				((Headquarters)CityList.get(0)).tryToProduceWarrior();
				((Headquarters)CityList.get(WorldProperty.NumberOfCity+1)).tryToProduceWarrior();
			}
			// :10 March
			if (WorldClock.getMinute() == 10){
                                Thread.sleep(500);
                                
				marchWarriors();
				
				//Check End Of Game
				Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
				Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
				boolean RedOcuupied = RedHeadquarters.checkOccupied();
				boolean BlueOcuupied = BlueHeadquarters.checkOccupied();
				if ( RedOcuupied || BlueOcuupied ){
					return;
				}
				
			}
			// :20 Produce Life Elements
			if (WorldClock.getMinute() == 20){
				ProduceLifeElements();
			}
			
			// :30 Warriors Fetch Life Elements to their headquarters
			if (WorldClock.getMinute() == 30){
                                System.out.println("CHECK IF WARRIOR FETCHES: " + warriorFetchesElements);
                                if(warriorFetchesElements == true){
                                    Thread.sleep(500);
                                }
                                warriorsFetchLifeElementsFromCity();
			}
				
			// :40 Organize Battels (Core function.)
			if (WorldClock.getMinute() == 40){

                                System.out.println("CHECK IF NO BATTLE: " + checkIfNoBattle);
                                if(checkIfNoBattle == false){
                                    Thread.sleep(500);
                                    holdBattlesAndWorkAfterBattles();
                                }

				holdBattlesAndWorkAfterBattles();
			}
			
			// :50 Headquarters report Life Elements
			if (WorldClock.getMinute() == 50){
                                Thread.sleep(500);
                                
				headquartersReportLifeElements();	
			}
                        timeCount = WorldClock.getTime();
			WorldClock.increase();
//                        FXMLLoader fxmlLoader = new FXMLLoader();
//                        fxmlLoader.setLocation(getClass().getResource("display_a1.fxml"));
//                        SampleController sc;
//                        sc.getSampleController();
//                        fxmlLoader.setController(sc);
//                        SampleController sc = (SampleController) fxmlLoader.getController();
//                        sc.updatePage("NCWEHBVJHERBVJKHERBVHKERBKVEHJRBVEJKRBVHJ");
                        //sc.updatePage(WorldClock.getTime());
                        
                        FXMLLoader fxmlLoader = new FXMLLoader();
                        SampleController sc = new SampleController();
                        fxmlLoader.setController(sc);
                        fxmlLoader.setLocation(getClass().getResource("display_a1.fxml"));
                        //sc.updatePage("NCWEHBVJHERBVJKHERBVHKERBKVEHJRBVEJKRBVHJ");
                        //Parent root = fxmlLoader.load();
                        //sc.updatePage(WorldClock.getTime());
		}
	}
        
        boolean checkIfNoBattle = true;
	/**
	 * 
	 */
	private void holdBattlesAndWorkAfterBattles() {
		for (int index=1; index <= WorldProperty.NumberOfCity; index++){
			 City c = CityList.get(index);
                         if(c.organizeBattle() == false){
                             checkIfNoBattle = false;
                         }
			 //c.organizeBattle();
		 }

		 //reward army.
		 Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
		 Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
		 RedHeadquarters.rewardArmy();
		 BlueHeadquarters.rewardArmy();
		 
		 //collectMoneyFromCity();
		 for (int i=1; i<= WorldProperty.NumberOfCity; i++){
			 City c = CityList.get(i);
			 c.payTribute();
		 }
	}

	/**
	 * 
	 */
	private void headquartersReportLifeElements() {
		//000:50 100 elements in red headquarter
		Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
		Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
		System.out.format("%s %d elements in red headquarter\n", WorldClock.getTime(),RedHeadquarters.LifeElement);
		System.out.format("%s %d elements in blue headquarter\n", WorldClock.getTime(),BlueHeadquarters.LifeElement);
	}
        
        boolean warriorFetchesElements = true;
	//After March
	private void warriorsFetchLifeElementsFromCity() {
		Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
		Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
		
		for (int i=1;i<=WorldProperty.NumberOfCity;i++){
			City c = CityList.get(i);
			//Empty City
			if (c.BlueWarriorStation.isEmpty() && c.RedWarriorStation.isEmpty()){
				continue;
			}
			//Red Fetch
			else if (c.BlueWarriorStation.isEmpty()){
				Warrior w = c.RedWarriorStation.get(0);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(),w.WarriorNameCard,c.LifeElement);
				RedHeadquarters.addLifeElement(c.popLifeElements());
			}
			else if (c.RedWarriorStation.isEmpty()){
				Warrior w = c.BlueWarriorStation.get(0);
				//000:30 red iceman 1 earned 10 elements for his headquarter
				System.out.format("%s %s earned %d elements for his headquarter\n", WorldClock.getTime(),w.WarriorNameCard,c.LifeElement);
				BlueHeadquarters.addLifeElement(c.popLifeElements());
			} else {
                            warriorFetchesElements = false;
				// Two warriors in this city.
			}
		}
	}
	
	
	public void ProduceLifeElements(){
		for (City c: CityList){
			if (!(c instanceof Headquarters)){
				c.produceLifeElement();
			}
		}
	}
	
	
	//TODO: modify operation on CityList to Warrior.move()
	public void marchWarriors(){
		//March Red Warriors.		
		for (int i=WorldProperty.NumberOfCity;i>=0;i--){
			City city = CityList.get(i);
			while (!city.RedWarriorStation.isEmpty()){
				city.RedWarriorStation.get(0).move();
			}
		}
		//March Blue Warriors.
		for (int i=1;i<=WorldProperty.NumberOfCity+1;i++){
			City city = CityList.get(i);
			while (!city.BlueWarriorStation.isEmpty()){
				city.BlueWarriorStation.get(0).move();
			}
		}	
		//Report March Information
		Headquarters RedHeadquarters = (Headquarters) CityList.get(0);
		Headquarters BlueHeadquarters = (Headquarters) CityList.get(WorldProperty.NumberOfCity+1);
		
		if (RedHeadquarters.checkNewArrival()){
			warriorReportMarch(RedHeadquarters, RedHeadquarters.getNewArrivedWarrior());
			RedHeadquarters.clearNewArrival();
		}
		for (int index=1; index<= WorldProperty.NumberOfCity; index++){
			City c = CityList.get(index);
			for (Warrior w:c.RedWarriorStation){
				warriorReportMarch(c,w);	
			}	
			for (Warrior w:c.BlueWarriorStation){
				warriorReportMarch(c,w);
			}
		}
		if (BlueHeadquarters.checkNewArrival()){
			warriorReportMarch(BlueHeadquarters, BlueHeadquarters.getNewArrivedWarrior());
			BlueHeadquarters.clearNewArrival();
		}			
	}
	
	private void warriorReportMarch(City c,Warrior w) {
		if (c instanceof Headquarters){
			//003:10 red lion 2 reached blue headquarter with 58 elements and force 50
			System.out.format("%s %s reached %s headquarter with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, WorldProperty.PartyNames[((Headquarters)c).getParty()], w.HP, w.AttackValue);
		}
		else {
			System.out.format("%s %s marched to city %d with %d elements and force %d\n", 
					WorldClock.getTime(), w.WarriorNameCard, c.CityID, w.HP, w.AttackValue);
		}
	}
	
}
