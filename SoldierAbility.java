package BattleCommands;

import java.awt.Image;

import GridGUI.BattleController;
import GridGUI.ImagePreparation;
import Heros.Hero;
import Statuses.BuffStatuses;
import Statuses.CrowdControlStatus;
import Statuses.OffensiveStatusesPerTurn;
import Statuses.Status;
import Statuses.StatusEffectAbility;

/**
 * Contains many inner classes of Soldier type abilities.
 * @author Kevin
 *
 */
public abstract class SoldierAbility extends Ability {
	
	private static final String BELONGS_TO_CLASS = "SOLDIER";
	
	/**
	 * HamString class is an OffensiveAbility. Note the absence of the useBattleCommand method.
	 * Since this is a pure OffensiveAbility there is no need to redefine this method, it will immediately call OffensiveAbiltiies
	 * useBattleCommand.
	 * @author Kevin
	 *
	 */
	public static class HamString extends OffensiveAbility {
		public static final String NAME = "HamString";
		private static final int ABILITY_POINTS_COST = 4;
		private static final int BASE_DAMAGE = 6;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/dagger.png", ABILITY_IMAGE_WIDTH, ABILITY_SWORD_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public HamString()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(HamString.NAME);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
	
	}
	
	/**
	 * Lunge class is an OffensiveAbility. Note the absence of the useBattleCommand method.
	 * Since this is a pure OffensiveAbility there is no need to redefine this method, it will immediately call OffensiveAbiltiies
	 * useBattleCommand.
	 * @author Kevin
	 *
	 */
	public static class Lunge extends OffensiveAbility {
		public static final String NAME = "Lunge";
		private static final int ABILITY_POINTS_COST = 4;
		private static final int BASE_DAMAGE = 5;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/dagger.png", ABILITY_IMAGE_WIDTH, ABILITY_SWORD_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public Lunge()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(Lunge.NAME);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
	}
	
	/**
	 * Snipe class is an OffensiveAbility. Note the absence of the useBattleCommand method.
	 * Since this is a pure OffensiveAbility there is no need to redefine this method, it will immediately call OffensiveAbiltiies
	 * useBattleCommand.
	 * @author Kevin
	 *
	 */
	public static class Snipe extends OffensiveAbility {
		public static final String NAME = "Snipe";
		private static final int ABILITY_POINTS_COST = 4;
		private static final int BASE_DAMAGE = 5;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/dagger.png", ABILITY_IMAGE_WIDTH, ABILITY_SWORD_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public Snipe()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(Snipe.NAME);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
	}
	
	/**
	 * MultiShot class is an OffensiveAbility. Note the absence of the useBattleCommand method.
	 * Since this is a pure OffensiveAbility there is no need to redefine this method, it will immediately call OffensiveAbiltiies
	 * useBattleCommand.
	 * @author Kevin
	 *
	 */
	public static class MultiShot extends OffensiveAbility {
		public static final String NAME = "MultiShot";
		private static final int ABILITY_POINTS_COST = 3;
		private static final int BASE_DAMAGE = 4;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/dagger.png", ABILITY_IMAGE_WIDTH, ABILITY_SWORD_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public MultiShot()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(MultiShot.NAME);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
	}
	
	/**
	 * PoisonShot class is an OffensiveAbility that implements the StatusEffectAbility interface.
	 * By implementing the StatusEffectAbility interface it must provide a method getAbilityStatus()
	 * If this ability is implemented properly in it's useBattleCommand method it should call, clone, and then 
	 * apply getAbilityStatus to it's enemy.
	 * @author Kevin
	 *
	 */
	public static class PoisonShot extends OffensiveAbility implements StatusEffectAbility{
		public static final String NAME = "PoisonShot";
		private static final int ABILITY_POINTS_COST = 3;
		private static final int BASE_DAMAGE = 2;
		private Status poison;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/poison_drop.png", ABILITY_IMAGE_WIDTH, ABILITY_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public PoisonShot()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(PoisonShot.NAME);
			this.poison = new OffensiveStatusesPerTurn.Poison(-2,2,2);
		}
		
		/**
		 * Calls the regular OffensiveAbility useBattleCommand to apply the base damage, 
		 * then applies the status to the target.
		 * @param hero: current acting hero
		 * @param target: target
		 */
		@Override
		public void useBattleCommand(Hero hero, Hero target) {
			super.useBattleCommand(hero, target);
			// Add status
			// Is cloned within the status
			applyAbilityStatus(target);
		}
		
		/**
		 * Does the regular base damage the same as OffensiveAbility useBattleCommand, 
		 * then applies the status to the target.
		 * @param hero: current acting hero
		 * @param target: target
		 */
		@Override
		public void useBattleCommand(Hero hero, BattleController controller) {
			Hero target = controller.signalShowTargetOptions();
			hero.checkIfEnoughAP(this);
			int otherDefenseRating = target.getDefenseRating();
			int damageDone = this.getDamage() - otherDefenseRating;
			if(damageDone <= 0)
			{
				damageDone = 1;
			}
			int otherHealth = target.getHealth();
			int newHealth = otherHealth - damageDone;
			controller.animateBattleCommand(target, this.getAnimationImage(),true);
			System.out.println("AI" + " health change: " + target.getHealth());
			hero.setAbilityPoints(hero.getAbilityPoints() - this.getPointCost());
			target.setHealth(newHealth);
			System.out.println("AI" + " health Change: " + target.getHealth());
			// Add status
			// Is cloned within the status
			applyAbilityStatus(target);
		}
		
		@Override
		public void useBattleCommand(Hero hero, Hero target, BattleController controller) {
			super.useBattleCommand(hero, target, controller);
			// Add status
			// Is cloned within the status
			applyAbilityStatus(target);
		}
		
		/**
		 * Applies and returns a clone of an abilities Status effect.
		 * If this interface is implemented properly in the corresponding abilities 
		 * useBattleCommand method it should applyAbilityStatus to it's enemy.
		 * @param target target to apply status effect to
		 * @return Abilities status effect
		 */
		@Override
		public Status applyAbilityStatus(Hero target)
		{
			return this.poison.addStatus(target);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
	}
	
	/**
	 * Defend class is an DefensiveAbility that implements the StatusEffectAbility interface.
	 * By implementing the StatusEffectAbility interface it must provide a method getAbilityStatus()
	 * If this ability is implemented properly in it's useBattleCommand method it should call, clone, and then 
	 * apply getAbilityStatus to the current acting hero.
	 * @author Kevin
	 *
	 */
	public static class Defend extends DefensiveAbility implements StatusEffectAbility{
		public static final String NAME = "Defend";
		private static final String STAT_AFFECTED = "defenseRating";
		private static final int ABILITY_POINTS_COST = 0;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/green_arrow.png", ABILITY_IMAGE_WIDTH, DEFEND_ABILITY_IMAGE_HEIGHT);
		private Status defend;
		
		/**
		 * Default constructor that sets the ability point cost, stat affected, effect strength, 
		 * and creates the defend status.
		 * @param effectStrength: how much defense rating is added to a character before their next turn.
		 */
		public Defend(int effectStrength)
		{
			super(ABILITY_POINTS_COST,Defend.STAT_AFFECTED, effectStrength, ANIMATION_IMAGE);
			this.setName(Defend.NAME);
			this.defend = new BuffStatuses.Defend(3,1,1);
		}
		
		/**
		 * Applies the defensive status to the current acting hero.
		 * @param hero: current acting hero
		 * @param target: target, should always be null as can only use on self
		 */
		@Override
		public void useBattleCommand(Hero hero, Hero other)
		{
			Status newDefend = applyAbilityStatus(hero);
			// Only want to update the defensive status, all other statuses are updated at the beginning of the turn 
			// Use the new status that is within the hero's statuses Map
			newDefend.updateStatus(hero);
		}
		
		/**
		 * Applies the defensive status to the current acting hero with animation.
		 * @param hero current acting hero
		 * @param controller controller to animate action 
		 */
		@Override
		public void useBattleCommand(Hero hero, BattleController controller)
		{
			// Apply to self 
			Status newDefend = applyAbilityStatus(hero);
			// Only want to update the defensive status, all other statuses are updated at the beginning of the turn 
			newDefend.updateStatus(hero,controller);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
		
		/**
		 * Applies and returns a clone of an abilities Status effect.
		 * If this interface is implemented properly in the corresponding abilities 
		 * useBattleCommand method it should applyAbilityStatus to it's enemy.
		 * @param target target to apply status effect to
		 * @return Abilities status effect
		 */
		@Override
		public Status applyAbilityStatus(Hero target)
		{
			return this.defend.addStatus(target);
		}
		
	}
	
	/**
	 * SheildBash class is an OffensiveAbility that implements the StatusEffectAbility and CrowdControlAbility interfaces.
	 * By implementing the StatusEffectAbility interface it must provide a method getAbilityStatus()
	 * If this ability is implemented properly in it's useBattleCommand method it should call, clone, and then 
	 * apply getAbilityStatus to it's enemy. CrowdControlAbility interface ONLY contains a constant stating that the created status
	 * will affect an enemies turn. No methods are required to be implemented. 
	 * @author Kevin
	 *
	 */
	public static class ShieldBash extends OffensiveAbility implements StatusEffectAbility, CrowdControlAbility{
		public static final String NAME = "ShieldBash";
		private Status stunned;
		private static final int BASE_DAMAGE = 2;
		private static final int ABILITY_POINTS_COST = 3;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/Stunned.png", ABILITY_IMAGE_WIDTH, ABILITY_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability points cost, base damage, stat affected, 
		 * and creates the stunned status.
		 */
		public ShieldBash()
		{
			super(ABILITY_POINTS_COST, BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(ShieldBash.NAME);
			this.stunned = new CrowdControlStatus(ShieldBash.NAME,this.getDamage(),2,2);
		}
		
		/**
		 * Calls the regular OffensiveAbility useBattleCommand to apply the base damage, 
		 * then applies the status to the target.
		 * @param hero: current acting hero
		 * @param target: target
		 */
		@Override
		public void useBattleCommand(Hero hero, Hero target) {
			super.useBattleCommand(hero, target);
			// Add status
			// Status is cloned in method 
			stunned.addStatus(target);
		}
		
		/**
		 * Does the regular base damage the same as OffensiveAbility useBattleCommand, 
		 * then applies the status to the target.
		 * @param hero: current acting hero
		 * @param target: target
		 */
		@Override
		public void useBattleCommand(Hero hero, BattleController controller) {
			Hero target = controller.signalShowTargetOptions();
			super.useBattleCommand(hero, target);
			// Add status
			// Is cloned within the status
			applyAbilityStatus(target);
		}
		
		@Override
		public void useBattleCommand(Hero hero, Hero target, BattleController controller) {
			super.useBattleCommand(hero, target, controller);
			// Add status
			// Is cloned within the status
			applyAbilityStatus(target);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return SoldierAbility.BELONGS_TO_CLASS;
		}
		
		/**
		 * Gets the status associated with this ability that implements StatusEffectAbility.
		 * @return The status associated with this ability that implements StatusEffectAbility.
		 */
		public Status applyAbilityStatus(Hero target){
			return this.stunned.addStatus(target);
		}
	}
}
