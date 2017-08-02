package JUnit_Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import BattleCommands.BaseAttack;
import BattleCommands.DefensiveAbility;
import BattleCommands.OffensiveAbility;
import Heros.SkeletonBoss;
import PartyContainers.AI;
import PartyContainers.AiBattleReturnType;
import PartyContainers.HumanPlayer;
import RPG_Exceptions.BattleModelException;
import Statuses.CrowdControlStatus;
import Statuses.Status;

public class JUnit_Tests_AI {

	/**
	 * Get AI to scan the player.
	 * Makes sure aiTurn is returning a command for the computer controlled Hero.
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_Turn() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		AiBattleReturnType result = ai.aiTurn(hero, human);
		assertTrue(result.getCmd() != null);
	}
	
	/**
	 * Test AI heal
	 * Ensures that item checking is happening and items are being used
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_Heal() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		hero.setHealth(2);
		ai.aiTurn(hero, human);
		int expected = 4;
		int actual = hero.getHealth();
		assertEquals(actual, expected);
		// The AI will heal 75% of the time (Based on Defender's heal chance).
	}
	
	/**
	 * Test AI attacks
	 * Makes sure that the random value returns the correct ability type
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_Attack() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		AiBattleReturnType result = hero.selectCommand(human.getParty().values(), 1.0); // Offensive Ability
		boolean resultTrue = result.getCmd() instanceof OffensiveAbility;
		assertTrue(resultTrue);
		// The AI should select first an OffensiveAbility.
	}
	
	/**
	 * Test AI defend
	 * Makes sure that the random value returns the correct ability type
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_Defend() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		AiBattleReturnType result = hero.selectCommand(human.getParty().values(), 0.21); // Defensive Ability
		boolean resultTrue = result.getCmd() instanceof DefensiveAbility;
		assertTrue(resultTrue);
		// The AI should select a DefensiveAbility.
	}
	
	/**
	 * Test AI baseAttack
	 * Makes sure that the Monster will use its base attack when it has no ability points.
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_baseAttack() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		hero.setAbilityPoints(0);
		AiBattleReturnType result = hero.selectCommand(human.getParty().values(), 0.51); // Would pick Crowd Control if it had AP.
		boolean resultTrue = result.getCmd() instanceof BaseAttack;
		assertTrue(resultTrue);
		// The AI should select a DefensiveAbility.
	}
	
	/**
	 * Test AI while under crowd control
	 * Makes sure that the Monster won't act while under a crowd control effect
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_crowdControl() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		Status stun = new CrowdControlStatus(null, 2, 2, 2);
		stun.addStatus(hero);
		AiBattleReturnType result = ai.aiTurn(hero, human);
		assertTrue(result != null);
		// The AI should not take any action
	}
	
	/**
	 * Test AI dead
	 * Makes sure that the Monster won't act while it is dead (Health = 0)
	 * @throws BattleModelException 
	 */
	@Test
	public void testAI_dead() throws BattleModelException {
		HumanPlayer human = new HumanPlayer();
		AI ai = new AI();
		SkeletonBoss hero = new SkeletonBoss(AI.CONTROLLER);
		hero = (SkeletonBoss) ai.getCharacter(hero.getClass().getName());
		hero.setHealth(0);
		AiBattleReturnType result = ai.aiTurn(hero, human);
		assertTrue(result != null);
		// The AI should not take any action
	}
	
}
