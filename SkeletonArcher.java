package Heros;

import java.util.ArrayList;
import java.util.Collection;
import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.OffensiveAbility;
import BattleCommands.SoldierAbility;
import PartyContainers.AI;
import PartyContainers.AiBattleReturnType;

/**
 * SkeletonArcher hero class. Sets base level 1 SkeletonArcher fields. 
 * Checks to insure the class only has Soldier Abilities.
 * @author Kevin
 *
 */

public class SkeletonArcher extends Monster{
	private static int experiencePerLevel = 100;
	private static final String IMAGE = "/archer.gif";
	private int baseStrength;
	private int strengthItemBonus;
	private int attackPower;
	private BaseAttack baseAttack;
	private OffensiveAbility snipe = new SoldierAbility.Snipe();
	private OffensiveAbility multiShot = new SoldierAbility.MultiShot();
	private OffensiveAbility poisonShot = new SoldierAbility.PoisonShot();
	
	/**
	 * Default SkeletonArcher constructor
	 * @param controlledBy: who will control this character, player or AI?
	 */
	public SkeletonArcher(String controlledBy)
	{
		// Set experience to 0, level 0, health 6, AB 5, defense 2, speed 2
		super(SkeletonArcher.IMAGE,0,0/SkeletonArcher.experiencePerLevel,6,5,3,1,controlledBy);
		this.baseStrength = 3;
		this.strengthItemBonus = 1;
		this.attackPower = this.baseStrength + this.strengthItemBonus;
		this.baseAttack = new BaseAttack(this.attackPower);
		this.setAbilityCheckType(baseAttack);
		this.setAbilityCheckType(snipe);
		this.setAbilityCheckType(poisonShot);
		this.setAbilityCheckType(multiShot);
	}
	
	/**
	 * Set this hero's abilities, used to insure they only receive SoldierAbilities.
	 */
	@Override
	public void setAbilityCheckType(Ability ability) {
		if(ability.getClassOwner().equals("ALL") || ability.getClassOwner().equals("SOLDIER"))
		{
			ability.setAbility(this);
		}
	}
	
	/**
	 * Get the image path to be displayed by the view.
	 * @return image path
	 */
	public String getImage() {
		return SkeletonArcher.IMAGE;
	}
	
	public static void main(String[] args){
	}

	@Override
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
		
		Hero target = null;
		Ability ability = null;
		
		Collection<Ability> abilities = this.getAbilities().values();
		Collection<Ability> availableAbilities = new ArrayList<>();
		for (Ability a : abilities) {
			if (a.getPointCost() < this.getAbilityPoints()) {
				availableAbilities.add(a);
			}
		}
		
		if (availableAbilities.size() == 1) {
			ability = baseAttack;
			target = AI.selectByStat(playerParty,"health",true); //Lowest Health
		}
		else {
			double random = Math.random();
			if (random > 0.75 && availableAbilities.contains(snipe)) {
				ability = snipe;
				target = AI.selectByStat(playerParty,"health",true); //Lowest Health
			} else if (random > 0.5 && availableAbilities.contains(poisonShot)) {
				ability = poisonShot;
				target = AI.selectByStat(playerParty,"defenseRating",false);  //Highest Armour
			} else if (random > 0.25 && availableAbilities.contains(multiShot)) {
				ability = multiShot;
				target = AI.selectByStat(playerParty,"health",false); //Highest Health
			} else {
				ability = baseAttack;
				target = AI.selectByStat(playerParty,"health",true); //Lowest Health
			}
		}
		
		return new AiBattleReturnType(target, ability);
	}

}
