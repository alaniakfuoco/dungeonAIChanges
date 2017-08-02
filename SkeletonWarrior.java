package Heros;

import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.OffensiveAbility;
import BattleCommands.SoldierAbility;
import BattleCommands.SoldierAbility.Defend;

/**
 * SkeletonWarrior hero class. Sets base level 1 SkeletonWarrior fields. 
 * Checks to insure the class only has Soldier Abilities.
 * @author Kevin
 *
 */
public class SkeletonWarrior extends Controller {
	private static final String HERO_NAME = "Skeleton Warrior";
	private static final String IMAGE = "/warrior.gif";
	private OffensiveAbility hamString = new SoldierAbility.HamString();
	private OffensiveAbility shieldBash = new SoldierAbility.ShieldBash();
	
	/**
	 * Default SkeletonWarrior constructor
	 * @param controlledBy who will control this character, player or AI?
	 */
	public SkeletonWarrior(String controlledBy)
	{
		// Set experience to 0, level 0, health 6, AB 5, defense 2, speed 2
		super(SkeletonWarrior.IMAGE,0,0/SkeletonWarrior.experiencePerLevel,6,5,2,1,controlledBy);
		this.baseStrength = 3;
		this.strengthItemBonus = 1;
		this.attackPower = this.baseStrength + this.strengthItemBonus;
		this.baseAttack = new BaseAttack(this.attackPower);
		Defend SkeletonWarriorDefend = new SoldierAbility.Defend(3);
		this.setAbilityCheckType(baseAttack);
		this.setAbilityCheckType(hamString);
		this.setAbilityCheckType(SkeletonWarriorDefend);
		this.setAbilityCheckType(shieldBash);
		this.setBaseAttack(baseAttack);
		this.setName(HERO_NAME);
		this.setAttackPower(attackPower);
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
		return SkeletonWarrior.IMAGE;
	}
}
