package BattleCommands;

import java.awt.Image;

import GridGUI.BattleController;
import Heros.Hero;

/**
 * OffensiveAbility is am abstract class that extends Ability. OffensiveAbility will
 * damage and / or apply a status effect to a target. 
 * Why abstract? We don't yet know what class the ability belongs to.
 * @author Kevin
 *
 */
public abstract class OffensiveAbility extends Ability{

	private int damage;
	private Image animationImage;
	
	/**
	 * OffensiveAbility two parameter constructor, sets ability point cost and damage.
	 * @param animationImage TODO
	 * @param pointCost: ability point cost of ability
	 * @param damage: damage done by ability
	 */
	public OffensiveAbility(int pointCost, int damage, Image animationImage)
	{
		super.setPointCost(pointCost);
		this.setDamage(damage);
		this.animationImage = animationImage;
	}
	
	/**
	 * OffensiveAbility single parameter constructor, just sets damage.
	 * @param animationImage TODO
	 * @param effectStrength: damage done by ability.
	 */
	public OffensiveAbility(int effectStrength, Image animationImage)
	{
		this.damage = effectStrength;
		this.animationImage = animationImage;
	}
	
	
	/**
	 * Returns the class string that owns these abilities.
	 * @return Class string that owns these abilities.
	 */
	public abstract String getClassOwner();
	
	/**
	 * All offensive abilities will be applying damage to a target. This this method computes
	 * the damage done to the target by taking the difference between their defense rating and the 
	 * abilities damage done. If the target's defense rating is higher than the damage done by this ability
	 * the damage done is 1. This method also updates the ability points of the current acting hero.
	 * @param hero: current acting hero
	 * @param target: target hero
	 * @throws IllegalArgumentException: if hero doesn't have enough ability points.
	 */
	public void useBattleCommand(Hero hero, Hero target)
	{
		hero.checkIfEnoughAP(this);
        int otherDefenseRating = target.getDefenseRating();
        int damageDone = this.getDamage() - otherDefenseRating;
        if(damageDone <= 0)
        {
            damageDone = 1;
        }
        int otherHealth = target.getHealth();
        int newHealth = otherHealth - damageDone;
        System.out.println("AI" + " health change: " + target.getHealth());
        hero.setAbilityPoints(hero.getAbilityPoints() - this.getPointCost());
        target.setHealth(newHealth);
        System.out.println("AI" + " health Change: " + target.getHealth());
	}
	
	/**
	 * All offensive abilities will be applying damage to a target. This this method computes
	 * the damage done to the target by taking the difference between their defense rating and the 
	 * abilities damage done. If the target's defense rating is higher than the damage done by this ability
	 * the damage done is 1. This method also updates the ability points of the current acting hero.
	 * @param controller battle controller to animate.
	 * @param hero: current acting hero
	 * @throws IllegalArgumentException: if hero doesn't have enough ability points.
	 */
	public void useBattleCommand(Hero hero, BattleController controller)
	{
		Hero target = controller.signalShowTargetOptions();
		useBattleCommand(hero,target);
		controller.animateBattleCommand(target, this.getAnimationImage(), true);
	}
	
	/**
	 * All offensive abilities will be applying damage to a target. This this method computes
	 * the damage done to the target by taking the difference between their defense rating and the 
	 * abilities damage done. If the target's defense rating is higher than the damage done by this ability
	 * the damage done is 1. This method also updates the ability points of the current acting hero.
	 * @param controller battle controller to animate.
	 * @param hero: current acting hero
	 * @param target: target hero
	 * @throws IllegalArgumentException: if hero doesn't have enough ability points.
	 */
	
	public void useBattleCommand(Hero hero, Hero target, BattleController controller)
	{
		useBattleCommand(hero,target);
		controller.animateBattleCommand(target, this.getAnimationImage(), true);
		
	}

	
	/**
	 * Evaluate the amount of damage done. 
	 * Evaluated as the amount of damage this ability does - the target's defense rating.
	 * If their defense is higher than the amount of damage done, the attack will do 1 damage.
	 * This is used for JUnit testing only, all other evaluation is done within the useBattleCommand command.
	 * @param target hero you wish to attack
	 * @param offensiveAbility ability to wish to use
	 * @return the amount of damage done
	 */
	public static int evaluateDamage(Hero target, OffensiveAbility offensiveAbility)
	{
		int damageDone = 1;
		int potentialDamage = offensiveAbility.getDamage() - target.getDefenseRating() ;
		if (potentialDamage > 0) { damageDone = potentialDamage; }
		return damageDone;
	}
	
	/**
	 * Set the damage done by this ability
	 * @param damage: damage done by this ability,
	 */
	public final void setDamage(int damage) {
		this.damage = damage;
	}
	
	/**
	 * Get the damage done by this ability.
	 * @return Damage done by this ability.
	 */
	public final int getDamage()
	{
		return this.damage;
	}
	
	/**
	 * Get this abilities animation image
	 * @return This abilities animation image. 
	 */
	public Image getAnimationImage() {
		return animationImage;
	}
}
