package World;

import Warriors.Dragon;
import Warriors.Iceman;
import Warriors.Lion;
import Warriors.Ninja;
import Warriors.Warrior;
import Warriors.WarriorType;
import Warriors.Wolf;

public class Headquarters extends City{
	private int[] ProductionOrder;
	private int WarriorIndex = 0;
	private int ProductionID = 1; //Production ID starts from 1
	private int Party;
	private boolean HasNewArrival;
	
	public int getParty(){
		return Party;
	}
	
	public void addLifeElement(int e){
		this.LifeElement += e;
	}
	
	public void increaseLifeElement(int n){
		LifeElement += n;
	}
	public Headquarters(int[] order, int party, int initLifeElements, int cityID){
		super(cityID);
		ProductionOrder = new int[5];
		System.arraycopy(order, 0, ProductionOrder, 0, order.length);
		LifeElement = initLifeElements;
		this.Party = party;
		this.HasNewArrival = false;
	}
	public void showOrder(){
		for (int i:ProductionOrder){
			System.out.println(i);
		}
	}
	
	public void setNewArrival(){
		this.HasNewArrival = true;
	}
	
	public void clearNewArrival(){
		this.HasNewArrival = false;
	}
	
	public Warrior getNewArrivedWarrior(){
		if (this.Party == WorldProperty.RED)
			return this.BlueWarriorStation.get(this.BlueWarriorStation.size()-1);
		if (this.Party == WorldProperty.BLUE)
			return this.RedWarriorStation.get(this.RedWarriorStation.size()-1);
		System.err.println("Error in Headquarters: NO such party");
		return null;
	}
	
	public boolean checkNewArrival(){
		return this.HasNewArrival;
	}
	
	/*
	 * return true if enough elements,
	 * return false otherwise.
	 */
	public boolean rewardWarrior(Warrior Object,int RewardNumber){
		if (this.LifeElement >= RewardNumber){
			this.LifeElement -= RewardNumber;
			Object.HP += RewardNumber;
			return true;
		} else {
			return false;
		}
		
	}
	
	//TODO: Both Headquarters reward army.
	/*
	 * return true if enough elements,
	 * return false otherwise.
	 */
	public void rewardArmy(){
		//Red party reward.
		if (this.Party == WorldProperty.RED){
			for (int i=WorldProperty.NumberOfCity; i>=1; i--){
				City c = World.CityList.get(i);
				if (c.Status_AfterBattle == true && (!c.RedWarriorStation.isEmpty()) && c.BlueWarriorStation.isEmpty() ){
					Warrior w = c.RedWarriorStation.get(0);
					this.rewardWarrior(w, WorldProperty.rewardNumber);
				}
			}
		}
		//Blue party reward.
		if (this.Party == WorldProperty.BLUE){
			for (int i=1;i<=WorldProperty.NumberOfCity;i++){
				City c = World.CityList.get(i);
				if (c.Status_AfterBattle == true && c.RedWarriorStation.isEmpty() && (!c.BlueWarriorStation.isEmpty()) ){
					Warrior w = c.BlueWarriorStation.get(0);
					this.rewardWarrior(w, WorldProperty.rewardNumber);
				}
			}
		}
	}
	
	
	
	//DRAGON = 0;
	//NINJA = 1;
	//ICEMAN = 2;
	//LION = 3;
	//WOLF = 4;
	public boolean tryToProduceWarrior(){
		int warriorType = ProductionOrder[WarriorIndex];
		int HPValue = WarriorType.HP_LIST[warriorType];
		if (this.LifeElement >= HPValue){
			LifeElement -= HPValue;
			return produceWarrior(warriorType);
		} else {
//			System.out.println("Not enought Life Element");
			return false;
		}
	}
	
	/**
	 * @param warriorType
	 * @return
	 */
	private boolean produceWarrior(int warriorType) {
		Warrior w;
		switch (warriorType){
			case 0:
				w = new Dragon(warriorType, this.Party, this.ProductionID);
				break;
			case 1:
				w = new Ninja(warriorType, this.Party, this.ProductionID);
				break;
			case 2:
				w = new Iceman(warriorType, this.Party, this.ProductionID);
				break;
			case 3:
				w = new Lion(warriorType, this.Party, this.ProductionID);
				break;
			case 4:
				w = new Wolf(warriorType, this.Party, this.ProductionID);
				break;
			default:
				w = null;
				System.err.println("Error: No such Warrior Type in Headquarters.java");
				return false;
		}
		
		if (this.Party == WorldProperty.RED){
			RedWarriorStation.add(w);
		} else if (this.Party == WorldProperty.BLUE){
			BlueWarriorStation.add(w);
		} else {
			System.err.println("Error: Undefined warrior type.");
			return false;
		}
		this.WarriorIndex = (this.WarriorIndex + 1) % 5;
		this.ProductionID ++;
		
		return true;
	}
	
	/*
	 * return false for not occupied.
	 * return true for occupied.
	 */
	public boolean checkOccupied(){
		if (this.Party == WorldProperty.RED && this.BlueWarriorStation.size() >= 2){
			//003:10 blue headquarter was taken
			System.out.format("%s red headquarter was taken\n", World.WorldClock.getTime());
			return true;
		}
		if (this.Party == WorldProperty.BLUE && this.RedWarriorStation.size() >= 2){
			System.out.format("%s blue headquarter was taken\n", World.WorldClock.getTime());	
			return true;
		}
		return false;
	}
	
}
