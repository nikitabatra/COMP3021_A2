package Warriors;

public class Iceman_single extends Warrior_single {

	public Iceman_single(int Type, int Party, int ProductionID) {
		super(Type, Party, ProductionID);
		
	}
	
	public boolean move(){
		super.move();
		if (MovedDistance % 2 == 0){
			HP=HP-9<=0?1:HP-9;
			this.AttackValue += 20;
		}
		
		return true;
	}

}
