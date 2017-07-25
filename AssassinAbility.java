package BattleCommands;

import java.awt.Image;

import GridGUI.BattleController;
import GridGUI.ImagePreparation;
import Heros.Hero;
import Statuses.OffensiveStatusesNotPerTurn;
import Statuses.OffensiveStatusesPerTurn;
import Statuses.Status;
import Statuses.StatusEffectAbility;

/**
 * Contains many inner classes of assassin type abilities.
 * @author Kevin
 *
 */
public abstract class AssassinAbility extends Ability {

	/**
	 * Final constant string declaring which class these abilities belong to.
	 */
	private static final String BELONGS_TO_CLASS = "ASSASSIN";
	
	/**
	 * PoisonBlade class is an OffensiveAbility that implements the StatusEffectAbility interface.
	 * By implementing the StatusEffectAbility interface it must provide a method getAbilityStatus()
	 * If this ability is implemented properly in it's useBattleCommand method it should call, clone, and then 
	 * apply getAbilityStatus to it's enemy.
	 * @author Kevin
	 *
	 */
	public static class PoisonBlade extends OffensiveAbility implements StatusEffectAbility{
		
		public static final String NAME = "PoisonBlade";
		private static final int ABILITY_POINTS_COST = 4;
		private static final int BASE_DAMAGE = 2;
		private static final Image ANIMATION_IMAGE  = ImagePreparation.getInstance().prepImage("/poison_drop.png", Ability.ABILITY_IMAGE_WIDTH, Ability.ABILITY_IMAGE_HEIGHT);
				
		private Status poison;
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 * Also creates the poison status.
		 */
		public PoisonBlade()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(PoisonBlade.NAME);
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
		 * then applies the status to the target. Includes animation.
		 * @param hero  current acting hero
		 * @param controller the controller to display the animation 
		 */
		@Override
		public void useBattleCommand(Hero hero, BattleController controller) {
			Hero target = controller.signalShowTargetOptions();
			super.useBattleCommand(hero, target);
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
			return AssassinAbility.BELONGS_TO_CLASS;
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
	}
	
	/**
	 * ExposeWeakness class is an OffensiveAbility that implements the StatusEffectAbility interface.
	 * By implementing the StatusEffectAbility interface it must provide a method getOffensiveStatus()
	 * If this ability is implemented properly in it's useBattleCommand method it should call, clone, and then 
	 * apply getOffensiveStatus to it's enemy.
	 * @author Kevin
	 *
	 */
	public static class ExposeWeakness extends OffensiveAbility implements StatusEffectAbility{
		
		public static final String NAME = "ExposeWeakness";
		private Status decreaseDefense;
		private static final int ABILITY_POINTS_COST = 4;
		private static final int BASE_DAMAGE = 1;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/red_minus.png", ABILITY_IMAGE_WIDTH, ABILITY_IMAGE_HEIGHT);
		
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 * Also creates the decreaseDefense status.
		 */
		public ExposeWeakness()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE);
			this.setName(ExposeWeakness.NAME);
			this.decreaseDefense = new OffensiveStatusesNotPerTurn.LowerDefense(-3,2,2);
		}
		
		/**
		 * Calls the regular OffensiveAbility useBattleCommand to apply the base damage, 
		 * then applies the offensive status to the target.
		 * @param hero: current acting hero
		 * @param target: target
		 */
		@Override
		public void useBattleCommand(Hero hero, Hero target) {
			super.useBattleCommand(hero, target);
			// Add status
			// Is cloned within the status
			Status newDecreaseDefense = applyAbilityStatus(target);
			// Expose should tick immediately so update with new status 
			newDecreaseDefense.updateStatus(target);
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
			Status newDecreaseDefense = applyAbilityStatus(target);
			// Expose should tick immediately so update with new status 
			newDecreaseDefense.updateStatus(target);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return AssassinAbility.BELONGS_TO_CLASS;
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
			return this.decreaseDefense.addStatus(target);
		}
	}
	
	/**
	 * ThrowKnives class is an OffensiveAbility. Note the absence of the useBattleCommand method.
	 * Since this is a pure OffensiveAbility there is no need to redefine this method, it will immediately call OffensiveAbiltiies
	 * useBattleCommand.
	 * @author Kevin
	 *
	 */
	public static class ThrowKnives extends OffensiveAbility {
		
		public static final String NAME = "ThrowKnives";
		private static final int ABILITY_POINTS_COST = 2;
		private static final int BASE_DAMAGE = 4;
		private static final Image ANIMATION_IMAGE = ImagePreparation.getInstance().prepImage("/dagger.png", ABILITY_IMAGE_WIDTH, ABILITY_SWORD_IMAGE_HEIGHT);
	
		/**
		 * Default constructor that sets the ability point cost and damage done.
		 */
		public ThrowKnives()
		{
			super(ABILITY_POINTS_COST,BASE_DAMAGE, ANIMATION_IMAGE );
			this.setName(ThrowKnives.NAME);
		}
		
		/**
		 * Returns the class string that owns these abilities.
		 * @return Class string that owns these abilities.
		 */
		@Override
		public String getClassOwner() {
			return AssassinAbility.BELONGS_TO_CLASS;
		}
	}
}
