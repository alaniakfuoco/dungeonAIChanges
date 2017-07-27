package Heros;

import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.OffensiveAbility;
import BattleCommands.SoldierAbility;
import BattleCommands.SoldierAbility.Defend;

/**
 * SkeletonSpearMan hero class. Sets base level 1 SkeletonSpearMan fields. 
 * Checks to insure the class only has Soldier Abilities.
 * @author Kevin
 *
 */
public class SkeletonSpearMan extends Punisher {
	private static final String IMAGE = "/spear.gif";
	private OffensiveAbility lunge = new SoldierAbility.Lunge();
	private OffensiveAbility shieldBash = new SoldierAbility.ShieldBash();
	
	/**
	 * Default SkeletonSpearMan constructor
	 * @param controlledBy: who will control this character, player or AI?
	 */
	public SkeletonSpearMan(String controlledBy)
	{
		// Set experience to 0, level 0, health 6, AB 5, defense 2, speed 2
		super(SkeletonSpearMan.IMAGE,0,0/SkeletonSpearMan.experiencePerLevel,6,5,2,1,controlledBy);
		this.baseStrength = 3;
		this.strengthItemBonus = 1;
		this.attackPower = this.baseStrength + this.strengthItemBonus;
		this.baseAttack = new BaseAttack(this.attackPower);
		Defend SkeletonSpearManDefend = new SoldierAbility.Defend(3);
		this.setAbilityCheckType(baseAttack);
		this.setAbilityCheckType(lunge);
		this.setAbilityCheckType(SkeletonSpearManDefend);
		this.setAbilityCheckType(shieldBash);
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
		return SkeletonSpearMan.IMAGE;
	}

}

