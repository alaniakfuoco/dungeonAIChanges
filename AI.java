package PartyContainers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import BattleCommands.Ability;
import BattleCommands.BaseAttack;
import BattleCommands.BattleCommand;
import BattleCommands.HealthItem;
import BattleCommands.Item;
import BattleCommands.OffensiveAbility;
import BattleCommands.StatusItem;
import GridGUI.BattleController;
import Heros.Hero;
import Heros.Monster;
import Heros.SkeletonArcher;
import Heros.SkeletonBoss;
import Heros.SkeletonSpearMan;
import Heros.SkeletonWarrior;
import RPG_Exceptions.MaximumStatException;
import RPG_Exceptions.NotAfflictedWithStatusException;
import Statuses.Status;
/**
 * Artificial Intelligence class, will scan the human opponent and make a decision as which move to make.
 * @author Kevin, Andrew 
 *
 */

public class AI extends Player{
	// Public constant which defines who to give control to in the battle system
	public static final String CONTROLLER = "AI";
	private BattleController controller;
	
	/**
	 * Constructor which creates the default party.
	 */
	public AI()
	{
		super();
		makeDefaultParty();
	}
	
	/**
	 * Creates the default AI party uses Heros specific to the AI.
	 */
	public void makeDefaultParty()
	{
		ArrayList<Hero> partyArray = new ArrayList<Hero>();
		partyArray.add(new SkeletonBoss(AI.CONTROLLER));
		partyArray.add(new SkeletonSpearMan(AI.CONTROLLER));
		partyArray.add(new SkeletonArcher(AI.CONTROLLER));
		partyArray.add(new SkeletonWarrior(AI.CONTROLLER));
		for(Hero hero : partyArray)
		{
			super.getParty().put(hero.getClass().getName(), hero);
		}
	}
	
	/**
	 * The function used to determine what the AI player is going to do. This is a multiple step process in which the AI....
	 * 1) Checks to see if it's health is 30% or less (compared to it's maximum health) and will use a potion 50% of the time
	 * 2) If it is poisoned it will use an antidote, 75% of the time
	 * 3) If it does not use an item it will call it's own function 'selectCommand' which selects a target and ability to be used
	 * @param monster The current acting character
	 * @param player The enemy's player to access their party
	 * @return AiBattleReturnType What is this? A class specifically made so that we can return two types of values by setting them as fields in this class.
	 * This class stores the target and AI ability used to be published to the view. 
	 * @throws MaximumStatException if the AI attempts to use a potion or ability when it would make no statistical difference to their hero 
	 * @throws NotAfflictedWithStatusException 
	 */
	public AiBattleReturnType scan(Monster monster, Player player) throws MaximumStatException
	{
		
		AiBattleReturnType result;
		TreeMap<String, Hero> party = player.getParty();
		Collection<Hero> playerParty = party.values();
		
		/*OffensiveAbility playerHighestDamage = pickHighestDamage(playerParty);
		
		if(hero.getHealth() != hero.getMaxHealth())
		{
			if((playerHighestDamage.getDamage()-hero.getDefenseRating()) > hero.getHealth())
			{
				Item item = pickHealingItem();
				if(item != null)
				{
					System.out.println("AI using item: " + item.toString());
					item.useBattleCommand(hero, null);
					AiBattleReturnType result = new AiBattleReturnType(null,item);
					return result;
				}
			}
		}
		*/
		
		//return new AiBattleReturnType(null,pickAntidote());
		
		// Monster will use healing items 50% of the time when health is below 30% (If such an item exists).
		double currentHealth = (double) monster.getHealth();
		double maxHealth = (double) monster.getMaxHealth();
		if ((currentHealth / maxHealth) <= 0.30) {
			double random = Math.random();
			if (random > 0.5) {
				Item item = pickHealingItem();
				if(item != null)
				{
					System.out.println("AI using item: " + item.toString());
					Hero target = null;
					if(controller != null)
					{
						item.useBattleCommand(monster, controller);
					}
					else
					{
						item.useBattleCommand(monster, target);
					}
					result = new AiBattleReturnType(null,item);
					return result;
				}
			}
		}
		
		// Monster will use an status curing item 75% of the time when under a status (If such an item exists).
		Collection<Status> status = monster.getStatuses().values();

		if (status.size() != 0) {
			//String statusAffliction = setStatusCure(status);
			
			double random = Math.random();
			if (random > 0.25) {
				Item item = setStatusCure(status);
				if(item != null)
				{
					System.out.println("AI using item: " + item.toString());
					Hero target = null;
					if(controller != null)
					{
						item.useBattleCommand(monster, controller);
					}
					else
					{
						item.useBattleCommand(monster, target);
					}
					result = new AiBattleReturnType(null,item);
					return result;
				}
			}
		}
		
		

		// Attack if no need to heal/recover
		result = monster.selectCommand(playerParty);
		
		System.out.println("AI using ability: " + result.getCmd().toString());
		System.out.println(result.getTarget().getClass());
		
		BattleCommand ability = result.getCmd();
		Hero target = result.getTarget();
		if(controller != null)
		{
			if (ability instanceof OffensiveAbility) {
				OffensiveAbility useAbility = (OffensiveAbility) ability;
				useAbility.useBattleCommand(monster, target, controller);
			} else {
				ability.useBattleCommand(monster, controller);
			}
		}
		else
		{
			try {
				ability.useBattleCommand(monster, target);
			} catch (NotAfflictedWithStatusException e) {
				e.printStackTrace();
			}
		}
		
		return result;
		//return result;
		//OffensiveAbility AIhighestDamage = pickHighestDamage(monster);
		//System.out.println("AI using ability: " + AIhighestDamage.toString());
		//Hero target = selectTarget(playerParty);
		//System.out.println(target.getClass());
		//AIhighestDamage.useBattleCommand(monster, target);
		// Return target and ability used 
		//result = new AiBattleReturnType(target,AIhighestDamage);
		
		//return result;
		
	}
	
	/**
	 * Picks a random status effect which the current actor has an item for.
	 * @param status A collection of all the status the current actor has.
	 * @return An item which can cure a status the current actor has.
	 */
	private Item setStatusCure(Collection<Status> status) {
		ArrayList<Item> matches = new ArrayList<>();
		Collection<Item> inventory = this.getInventory().values();
		for(Status currentStatus : status) {
			for(Item currentItem : inventory) {
				if (currentItem instanceof StatusItem && currentStatus.getName() == ((StatusItem) currentItem).getStatusAffected()) {
					matches.add(currentItem);
				}
			}
		}
		if (matches.size() == 0) { return null; }
		int selection = (int) Math.floor((Math.random() * (matches.size()-(1-1)) + 1) + (1-1));
		return matches.get(selection - 1);
	}

	/**
	 * Function used to start the AI turn, checks if the AI is crowd controlled, if it is will tick all current statuses on the acting hero and finish the AI's turn.
	 * If not will proceed to scan the player party to choose the best course of action.
	 * @param hero: current acting hero
	 * @param human: enemy party to scan
	 * AiBattleReturnType: What is this? A class specifically made so that we can return two types of values by setting them as fields in this class.
	 * This class stores the target and AI ability used to be published to the view.
	 * @throws NotAfflictedWithStatusException 
	 */
	public AiBattleReturnType aiTurn(Hero monster, Player human) {
		
		AiBattleReturnType target = new AiBattleReturnType(null,null);
		System.out.println("Enemy Turn!");
		boolean AIControlled = monster.updateStatuses();
		
		if(!AIControlled) {
			try	{ target = this.scan((Monster)monster, human); }
    		catch(MaximumStatException e) { e.printStackTrace(); }
		}
		
		return target;
	}
	
	/**
	 * AI's method to select the enemy target with the lowest health.
	 * @param party: enemy party to scan
	 * @return Hero: the target to attack 
	 */
	private Hero selectTarget(Collection<Hero> party)
	{
		Hero target = null;
		int lowestHealth = Integer.MAX_VALUE;
		int count = 0;
		for(Hero playerHero : party) {
			
			int currentHealth = playerHero.getHealth();
			if((count == 0) && (currentHealth > 0))
			{
				lowestHealth = currentHealth;
				target = playerHero;
			}
			else if((currentHealth > 0) && (currentHealth < lowestHealth))
			{
				lowestHealth = playerHero.getHealth();
				target=playerHero;
			}
			count ++;
		}
		return target;
	}
	
	/**
	 * AI's method to scan it's the enemy's party to determine their strongest available attack to use.
	 * @param party: enemy's party
	 * @return Enemy's strongest available attack party wide
	 */
	private OffensiveAbility pickHighestDamage(Collection<Hero> party)
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

	/**
	 * Picks current acting hero's highest damage attack.
	 * @param hero: Current acting hero.
	 * @return Current acting hero's highest damage attack.
	 */
	private OffensiveAbility pickHighestDamage(Hero hero)
	{
		// Pick Own best ability
		HashMap<String, Ability> abilities = hero.getAbilities();
		
		int availableAB = hero.getAbilityPoints();
		
		BaseAttack base = (BaseAttack) Ability.getAbility(hero,"BaseAttack");
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
	 * Pick strongest healing item if need.
	 * @return Strongest healing item.
	 */
	private Item pickHealingItem()
	{
		Item bestItem = null;
		TreeMap<String, Item> inv = this.getInventory();
		int largestHeal = 0;
		if(!inv.isEmpty())
		{
			for(Item item : inv.values())
			{
				// Make sure to check for healing items 
				if (item instanceof HealthItem)
				{
					int currentHeal = item.getEffectStrength();
					if(currentHeal > largestHeal)
					{
						bestItem = item;
					}
				}
			}
		}
		return bestItem;
	}
	
	/**
	 * Picks an item to remove a poison effect.
	 * @return Strongest healing item.
	 */
	private Item pickStatusItem(String status) {
		Item antidote = null;
		TreeMap<String, Item> inv = this.getInventory();
		if(!inv.isEmpty())
		{
			for(Item item : inv.values())
			{
				// Find an existing antidote item
				if (item instanceof StatusItem)
				{
					if(((StatusItem) item).getStatusAffected() == status)
					{
						antidote = item;
					}
				}
			}
		}
		System.out.println("Item: " + antidote.toString());
		return antidote;
	}

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
	
	public void setController(BattleController controller)
	{
		this.controller = controller;
	}
}
