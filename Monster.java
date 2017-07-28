package Heros;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.CrowdControlAbility;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import PartyContainers.AI;
import PartyContainers.AiBattleReturnType;
import Statuses.StatusEffectAbility;

/**
 * Monster abstract class. 
 *  Includes a unique abstract method to be used by Monsters for individual battle behaviour.
 *  Includes various methods to select abilities or targets based on supplied parameters.
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
	 * This method will be used as part of the AI functionality to return an ability to use and a target.
	 * @param playerParty 
	 * @return an AiBattleReturnType. 
	 */
	public abstract AiBattleReturnType selectCommand(Collection<Hero> playerParty);
	
	/**
	 * Abstract method used by Monsters to determine actions during battle.  
	 * This method will be used to choose the Monsters ability to be used during it's turn.
	 * @param Collection<Ability> list of Abilities to be chosen from 
	 * @return an AiBattleReturnType. 
	 */
	//public abstract Ability pickAbility(Collection<Ability> abilities);
	
	/**
	 * Selects a target by determining which character has the highest or lowest of the specified stat.
	 * @param playerParty Collection of characters.
	 * @param stat A String used to specify which stat is to be evaluated.
	 * @param lower A boolean value.  If true the method will look for the character which has the lowest stat, else it will find the highest.
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
	 * Selects a target by determining which character has the highest or lowest of the specified stat.
	 * @param playerParty Collection of characters.
	 * @param stat A String used to specify which stat is to be evaluated.
	 * @param lower A boolean value.  If true the method will look for the character which has the lowest stat, else it will find the highest.
	 * @return Hero A character which has the highest or lowest stat.
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
	 * Selects a target by determining which character has the highest or lowest of the specified stat.
	 * @param playerParty Collection of characters.
	 * @param stat A String used to specify which stat is to be evaluated.
	 * @param lower A boolean value.  If true the method will look for the character which has the lowest stat, else it will find the highest.
	 * @return Hero A character which has the highest or lowest stat.
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
	 * Selects a target by determining which character has the highest or lowest of the specified stat.
	 * @param playerParty Collection of characters.
	 * @param stat A String used to specify which stat is to be evaluated.
	 * @param lower A boolean value.  If true the method will look for the character which has the lowest stat, else it will find the highest.
	 * @return Hero A character which has the highest or lowest stat.
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
	 * Picks current acting hero's highest damage attack.
	 * @param hero: Current acting hero.
	 * @return Current acting hero's highest damage attack.
	 */
	private OffensiveAbility pickHighestDamage()
	{
		// Pick Own best ability
		HashMap<String, Ability> abilities = this.getAbilities();
		
		int availableAB = this.getAbilityPoints();
		
		BaseAttack base = (BaseAttack) Ability.getAbility(this,"BaseAttack");
		int currentHighestDamage = base.getDamage();
		OffensiveAbility cmd = base;
		for(Ability ability : abilities.values())
		{
			if(ability instanceof OffensiveAbility)
			{
				OffensiveAbility offensive = (OffensiveAbility) ability;
				int currentDamage = offensive.getDamage();
				if((currentDamage > currentHighestDamage) && (offensive.getPointCost() < availableAB))
				{
					currentHighestDamage = currentDamage;
					cmd = offensive;
				}
			}
		}
		return cmd;
	}
	
	/**
	 * AI's method to scan it's the enemy's party to determine their strongest available attack to use.
	 * @param party: enemy's party
	 * @return Enemy's strongest available attack party wide
	 */
	private static OffensiveAbility pickHighestDamage(Collection<Hero> party)
	{
		// Scans entire party 
		int currentHighestDamage = 0;
		OffensiveAbility cmd = null;
		for(Hero playerHero : party)
		{
			HashMap<String, Ability> abilities = playerHero.getAbilities();
			Collection<Ability> currentAbilities = abilities.values();
			for(Ability ability : currentAbilities)
			{
				if(ability instanceof OffensiveAbility)
				{
					int availableAB = playerHero.getAbilityPoints();
					OffensiveAbility offensive = (OffensiveAbility) ability;
					int currentDamage = offensive.getDamage();
					if((currentDamage > currentHighestDamage) && (offensive.getPointCost() < availableAB))
					{
						currentHighestDamage = currentDamage;
						cmd = offensive;
					}
				}
			}	
		}
	
		return cmd;
	}
	
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
	
	public static Collection<Hero> getAvailableTargets(Collection<Hero> party) {
		Collection<Hero> availableTargets = new ArrayList<>();
		for (Hero currentTarget : party) {
			if (currentTarget.getHealth() > 0) {
				availableTargets.add(currentTarget);
			}
		}
		return availableTargets;
	}
	
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
	
	public void setHealRange(double percentage) { healRange = percentage; }
	public void setHealChance(double percentage) { healChance = percentage; }
	public void setRecoverPointsRange(double percentage) { recoverRange = percentage; }
	public void setRecoverPointsChance(double percentage) { recoverChance  = percentage; }
	public void setCureChance(double percentage) { cureChance  = percentage; }
	
}
