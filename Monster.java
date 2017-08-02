package Heros;

import java.util.ArrayList;
import java.util.Collection;
import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AiBattleReturnType;
import Statuses.StatusEffectAbility;

/**
 * Monster abstract class. 
 * This class will be used specifically for computer controlled Hero instances.
 * Includes various methods to select abilities or targets based on supplied parameters.
 * Includes various methods to supply information to the Monster based on its current state.
 * @author Andrew
 *
 */

public abstract class Monster extends Hero {
	private static final String HERO_NAME = "AI_Monster";
	protected static int experiencePerLevel = 100;
	protected int baseStrength;
	protected int strengthItemBonus;
	protected int attackPower;
	protected BaseAttack baseAttack;
	private static double healRange;		// Checks if HP is below this percentage to determine if it should run a heal chance
	private static double healChance;		// Percentage change the AI will use a healing item
	private static double recoverRange;	// Checks if AP is below this percentage to determine if it should run a heal chance
	private static double recoverChance;	// Percentage change the AI will use an ability points item
	private static double cureChance;	// Percentage change the AI will use a status item

	
	/**
	 * Constructor to create an instance of the Monster class.  This is no different from the Hero constructor.
	 * @param image: image to be used by the view
	 * @param experience: experience, not yet implemented
	 * @param level: level, not yet implements
	 * @param health: initial hero health, also sets the hero's maximum health
	 * @param abilityPoints: initial hero ability points, also sets the hero's maximum ability points 
	 * @param defenseRating: hero's defense rating
	 * @param speed: hero's speed rating, used for sorting to build initial battle queue order
	 * @param controlledBy: controlled by field, has to be a final constant from either HumanPlayer or AI, determines where control is passed in the battle system
	 */
	public Monster(String image, int experience, int level, int health, int abilityPoints, int defenseRating, int speed, String controlledBy) {
		super(image, experience, level, health, abilityPoints, defenseRating, speed, controlledBy, HERO_NAME);
	}
	
	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method will be used as part of the AI functionality to return an ability to use and a target (if applicable).
	 * @param Collection<Hero> A list of the Hero instances in the player's party
	 * @return AiBattleReturnType holding both a target (if applicable) and an ability to use. 
	 */
	public abstract AiBattleReturnType selectCommand(Collection<Hero> playerParty);
	
	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method specifics a double value to force certain actions.
	 * Used specifically for testing purposes.
	 * @param Collection<Hero> A list of the Hero instances in the player's party
	 * @param Double A value to control which type of ability will be selected.
	 * @return AiBattleReturnType holding both a target (if applicable) and an ability to use. 
	 */
	public AiBattleReturnType selectCommand(Collection<Hero> playerParty, double value){
		return null;
	}
	
	/**
	 * Selects a target by determining which character has the highest or lowest of the specified stat.
	 * Requires the Collection<Hero> argument to contain at least one instance or the method will return null.
	 * @param Collection<Hero> Collection of characters to be looked at.
	 * @param String A String used to specify which stat is to be evaluated.
	 * @param Boolean A boolean value.  If true the method will look for the character which has the lowest stat, else it will find the highest.
	 * @return Hero A character which has the highest or lowest stat.
	 */
	public static Hero selectByStat(Collection<Hero> playerParty, String stat, boolean lower) {
		Hero target = null;
		int statValue;
		if (lower) { statValue = Integer.MAX_VALUE; }
		else { statValue = Integer.MIN_VALUE; }
		for(Hero playerHero : playerParty) {
			if (playerHero.getHealth() > 0) {
				int currentStat = playerHero.getStat(stat);
				
				if (lower) {
					if (currentStat < statValue) {
						statValue = currentStat;
						target = playerHero;
					} 
				
				} else {
					if (currentStat > statValue) {
						statValue = currentStat;
						target = playerHero;
					}
				}
				
			}
		}
		return target;
	}
	
	/**
	 * Selects a target by determining which character is under a Crowd Control effect.
	 * Requires the Collection<Hero> argument to contain at least one instance or the method will return null.
	 * @param Collection<Hero> Collection of characters to be looked at.
	 * @return Hero A random character which is under a Crown Control effect.
	 */
	public static Hero selectIfCrowdControlled(Collection<Hero> playerParty) {
		Hero target = null;
		ArrayList<Hero> options = new ArrayList<>();
		for(Hero currentTarget : playerParty) {
			if (currentTarget.checkIfCrowdControlled()) { options.add(currentTarget); }
		}
		if (options.size() != 0) {
			target = pickRandom(options);
		}
		return target;
	}
	
	/**
	 * Selects a target by determining which character is not under a Crowd Control effect.
	 * Requires the Collection<Hero> argument to contain at least one instance or the method will return null.
	 * @param Collection<Hero> Collection of characters to be looked at.
	 * @return Hero A random character which is not under a Crown Control effect.
	 */
	public static Hero selectIfNotCrowdControlled(Collection<Hero> playerParty) {
		Hero target = null;
		ArrayList<Hero> options = new ArrayList<>();
		for(Hero currentTarget : playerParty) {
			if (!currentTarget.checkIfCrowdControlled()) { options.add(currentTarget); }
		}
		if (options.size() != 0) {
			target = pickRandom(options);
		}
		return target;
	}
	
	/**
	 * Selects a target by randomly choosing from a collection.
	 * Requires the Collection<Hero> argument to contain at least one instance or the method will return null.
	 * @param Collection<Hero> Collection of characters to be looked at.
	 * @return Hero A random character which is under a Crown Control effect.
	 */
	public static Hero selectRandomTarget(Collection<Hero> playerParty) {
		Hero target = null;
		ArrayList<Hero> options = new ArrayList<>();
		for(Hero currentTarget : playerParty) {
			options.add(currentTarget);
		}
		if (options.size() != 0) {
			target = pickRandom(options);
		}
		return target;
	}

	/**
	 * Returns a collection of Ability which can be used by the Monster.
	 * An Ability is usable if the Monster has enough Ability Points (AP >= Cost).
	 * @return Collection<Ability> A collection of usable Ability
	 */
	public Collection<Ability> getAvailableAbilities() {
		Collection<Ability> abilities = this.getAbilities().values();
		Collection<Ability> availableAbilities = new ArrayList<>();
		for (Ability a : abilities) {
			if (a.getPointCost() <= this.getAbilityPoints()) {
				availableAbilities.add(a);
			}
		}
		return availableAbilities;
	}
	
	/**
	 * Returns a collection of Hero which can be targetted by the Monster.
	 * A Hero is targetable if it is not considered dead (health > 0). 
	 * @return Collection<Hero> A collection of usable Ability
	 */
	public static Collection<Hero> getAvailableTargets(Collection<Hero> party) {
		Collection<Hero> availableTargets = new ArrayList<>();
		for (Hero currentTarget : party) {
			if (currentTarget.getHealth() > 0) {
				availableTargets.add(currentTarget);
			}
		}
		return availableTargets;
	}
	
	/**
	 * Returns a random ability that matches the type OffensiveAbility and not one of StatusEffectAbility.
	 * @param Collection<Ability> A collection of Ability to be considered.
	 * @return Ability A randomly selected Ability from the Ability that match the specified typing.
	 */
	public static Ability getOffensiveAbility(Collection<Ability> abilities) {
		ArrayList<Ability> options = new ArrayList<>();
		for(Ability currentAbility : abilities) {
			if (currentAbility instanceof OffensiveAbility && !(currentAbility instanceof StatusEffectAbility)) 
				options.add(currentAbility);
		}
		if (options.size() != 0) {
			return pickRandom(options);
		}
		return null;
	}
	
	/**
	 * Returns a random ability that matches the type StatusEffectAbility and not one of DefensiveAbility.
	 * @param Collection<Ability> A collection of Ability to be considered.
	 * @return Ability A randomly selected Ability from the Ability that match the specified typing.
	 */
	public static Ability getOffensiveStatusAbility(Collection<Ability> abilities) {
		ArrayList<Ability> options = new ArrayList<>();
		for(Ability currentAbility : abilities) {
			if (currentAbility instanceof StatusEffectAbility && !(currentAbility instanceof DefensiveAbility)) 
				options.add(currentAbility);
				}
			if (options.size() != 0) {
				return pickRandom(options);
			}
		return null;
	}
	
	/**
	 * Returns a random ability that matches the type DefensiveAbility.
	 * @param Collection<Ability> A collection of Ability to be considered.
	 * @return Ability A randomly selected Ability from the Ability that match the specified typing.
	 */
	public static Ability getDefensiveAbility(Collection<Ability> abilities) {
		ArrayList<Ability> options = new ArrayList<>();
		for(Ability currentAbility : abilities) {
			if (currentAbility instanceof DefensiveAbility) 
				options.add(currentAbility);
				}
			if (options.size() != 0) {
				return pickRandom(options);
			}
		return null;
	}
	
	/**
	 * Returns a random ability that matches the type CrowdControlAbility.
	 * @param Collection<Ability> A collection of Ability to be considered.
	 * @return Ability A randomly selected Ability from the Ability that match the specified typing.
	 */
	public static Ability getCrowdControlAbility(Collection<Ability> abilities) {
		ArrayList<Ability> options = new ArrayList<>();
		for(Ability currentAbility : abilities) {
			if (currentAbility instanceof CrowdControlAbility) 
				options.add(currentAbility);
				}
			if (options.size() != 0) {
				return pickRandom(options);
			}
		return null;
	}
	
	/**
	 * Picks a random element from an ArrayList
	 * @param options An ArrayList to choose an element from.
	 * @return An element from options.
	 */
	public static <T> T pickRandom(ArrayList<T> options) {
		int pick = (int) Math.floor(Math.random() * options.size());
		T choice = options.get(pick);
		return choice;
	}

	public double getHealRange() { return healRange; }
	public double getHealChance() { return healChance; }
	public double getRecoverPointsRange() { return recoverRange; }
	public double getRecoverPointsChance() { return recoverChance; }
	public double getCureChance() { return cureChance; }
	public BaseAttack getBaseAttack() { return this.baseAttack; };
	
	public void setHealRange(double percentage) { healRange = percentage; }
	public void setHealChance(double percentage) { healChance = percentage; }
	public void setRecoverPointsRange(double percentage) { recoverRange = percentage; }
	public void setRecoverPointsChance(double percentage) { recoverChance  = percentage; }
	public void setCureChance(double percentage) { cureChance  = percentage; }
	public final void setBaseAttack(BaseAttack baseAttack) {  this.baseAttack = baseAttack; };
	
}
