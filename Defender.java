package Heros;

import java.util.Collection;

import BattleCommands.Ability;
import PartyContainers.AiBattleReturnType;


/**
 * Defender Monster type. 
 *  Defines the method selectCommand for this specific Monster type.
 *  Will use abilities as such:
 *  25% chance to use an OffensiveAbility
 *  	Target defined by lowest health
 *  25% chance to use a CrowdControlAbility
 *  	Target defined by highest health
 *  30% chance to use a DefensiveAbility
 *  20% chance to use baseAttack
 *  	Target defined by lowest health
 * @author Andrew
 */
public abstract class Defender extends Monster {

	/**
	 * Constructor to create an instance of the Defender class.
	 * This constructor will specify values for a Monster to check when to use items.
	 * @param experience: experience, not yet implemented
	 * @param level: level, not yet implements
	 * @param health: initial hero health, also sets the hero's maximum health
	 * @param abilityPoints: initial hero ability points, also sets the hero's maximum ability points 
	 * @param defenseRating: hero's defense rating
	 * @param speed: hero's speed rating, used for sorting to build initial battle queue order
	 * @param controlledBy: controlled by field, has to be a final constant from either HumanPlayer or AI, determines where control is passed in the battle system
	 */
	public Defender(String image, int experience, int level, int health, int abilityPoints, int defenseRating,
			int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy);
		setHealRange(0.5);
		setHealChance(0.75);
		setRecoverPointsRange(0.3);
		setRecoverPointsChance(0.3);
		setCureChance(0.5);
	}

	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method will be used as part of the AI functionality to return an ability to use and a target (if applicable).
	 * @param Collection<Hero> A list of the Hero instances in the player's party
	 * @return AiBattleReturnType holding both a target (if applicable) and an ability to use. 
	 */
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty) {
			
			Hero target = null;
			Ability ability = null;
			
			Collection<Ability> availableAbilities = getAvailableAbilities();
			Collection<Hero> availableTargets = getAvailableTargets(playerParty);
			
			if (availableAbilities.size() < 2) {
				ability = this.getBaseAttack();
				target = selectByStat(availableTargets,"health",true); //Lowest Health
				return new AiBattleReturnType(target, ability);
			}
			else {
				double random = Math.random();
				//System.out.println(random);
				if(random > 0.75) {
					ability = getOffensiveAbility(availableAbilities);
					if (ability == null) {
						ability = getOffensiveStatusAbility(availableAbilities);
					}
					target = selectByStat(availableTargets,"health",true); //Lowest Health
				}
				else if(random > 0.50) {
					ability = getCrowdControlAbility(availableAbilities);
					target = selectByStat(availableTargets,"abilityPoints",false); //Highest abilityPoints
				}
				else if(random > 0.20) {
					ability = getDefensiveAbility(availableAbilities);
				}
			}
			if (ability == null) { 
				ability = this.getBaseAttack();
				target = selectByStat(availableTargets,"health",true); //Lowest Health 
			}
			return new AiBattleReturnType(target, ability);
		}
	
	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method specifics a double value to force certain actions.
	 * Used specifically for testing purposes.
	 * @param Collection<Hero> A list of the Hero instances in the player's party
	 * @param Double A value to control which type of ability will be selected.
	 * @return AiBattleReturnType holding both a target (if applicable) and an ability to use. 
	 */
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty, double value) {
		
		Hero target = null;
		Ability ability = null;
		
		Collection<Ability> availableAbilities = getAvailableAbilities();
		Collection<Hero> availableTargets = getAvailableTargets(playerParty);
		
		if (availableAbilities.size() < 2) {
			ability = this.getBaseAttack();
			target = selectByStat(availableTargets,"health",true); //Lowest Health
			return new AiBattleReturnType(target, ability);
		}
		else {
			double random = value;
			//System.out.println(random);
			if(random > 0.75) {
				ability = getOffensiveAbility(availableAbilities);
				if (ability == null) {
					ability = getOffensiveStatusAbility(availableAbilities);
				}
				target = selectByStat(availableTargets,"health",true); //Lowest Health
			}
			else if(random > 0.50) {
				ability = getCrowdControlAbility(availableAbilities);
				target = selectByStat(availableTargets,"abilityPoints",false); //Highest abilityPoints
			}
			else if(random > 0.20) {
				ability = getDefensiveAbility(availableAbilities);
			}
		}
		if (ability == null) { 
			ability = this.getBaseAttack();
			target = selectByStat(availableTargets,"health",true); //Lowest Health 
		}
		return new AiBattleReturnType(target, ability);
	}
}