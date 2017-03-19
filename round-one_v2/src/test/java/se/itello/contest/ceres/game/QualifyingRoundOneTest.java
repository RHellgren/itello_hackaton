package se.itello.contest.ceres.game;

import org.junit.Before;
import org.junit.Test;
import se.itello.contest.ceres.api.Position;
import se.itello.contest.ceres.api.TestMode;
import se.itello.contest.ceres.teamx.FullThrottleBrain;

import static org.junit.Assert.assertTrue;


/**
 * Your mission is to pick up contracts. A contract is picked up when the ship collides with it.
 * The contracts' positions are revealed in the game state. Positions always indicate the center of the object.
 * The ship's state reveals speed (pixels / seconds) and direction and rotation (0-359 degrees, where 0 is right).
 * Try to get all the contracts in each level using the least number of steps.
 *
 * @see se.itello.contest.ceres.api.GameState for what type of data is being sent to the brain.
 * @see se.itello.contest.ceres.api.GameConstants for some clues on how the game operates.
 */
public class QualifyingRoundOneTest {
    private final TestMode testMode = TestMode.Gui;
    private GamePlayer gamePlayer;

    @Before
    public void setUp() throws Exception {
        // Default run. Use your brain here!
        gamePlayer = new GamePlayer(new FullThrottleBrain());

        // Test to run your jar.
        // This must run for the solution to be valid. Watch out for security exceptions!
        //gamePlayer = new GamePlayer(<PATH_TO_JAR>, <FULL_CLASS_NAME_OF_BRAIN>);
    }

    /**
     * Full speed ahead!
     */
    @Test
    public void level1() throws Exception {
        Position startPosition = new Position(100, 50);
        double universeWidth = 200;
        double universeHeight = 700;

        play(universeWidth, universeHeight, startPosition,
            100, 100,
            100, 200,
            100, 300,
            100, 400,
            100, 500,
            100, 600);
    }

    /**
     * Set a planned course.
     */
    @Test
    public void level2() throws Exception {
        Position startPosition = new Position(50, 70);
        double universeWidth = 600;
        double universeHeight = 768;

        play(universeWidth, universeHeight, startPosition,
            100, 100,
            170, 200,
            240, 300,
            310, 400,
            380, 500,
            450, 600,
            520, 700);
    }

    /**
     * Introducing a little bit of turning.
     */
    @Test
    public void level3() throws Exception {
        Position startPosition = new Position(50, 50);
        double universeWidth = 600;
        double universeHeight = 600;

        play(universeWidth, universeHeight, startPosition,
            50, 100,
            75, 150,
            100, 200,
            125, 250,
            150, 300,
            200, 350,
            250, 375,
            300, 400,
            350, 420,
            400, 440,
            475, 450,
            550, 460);
    }

    /**
     * Turning one direction and then another.
     */
    @Test
    public void level4() throws Exception {
        Position startPosition = new Position(400, 28);
        double universeWidth = 800;
        double universeHeight = 768;


        play(universeWidth, universeHeight, startPosition,
            400, 700,
            350, 620,
            300, 540,
            250, 460,
            200, 380,
            250, 300,
            300, 220,
            350, 140,
            400, 60);
    }

    /**
     * Keep on turning.
     */
    @Test
    public void level5() throws Exception {
        Position startPosition = new Position(384, 80);
        double universeWidth = 768;
        double universeHeight = 768;

        double circleRadius = 300;
        int numberOfContracts = 10;

        double[] contractLocations = new double[numberOfContracts * 2];
        for (int i = 0; i < numberOfContracts * 2; i += 2) {
            contractLocations[i] = universeWidth / 2 + circleRadius * Math.cos((2 * Math.PI) * i / (numberOfContracts * 2));
            contractLocations[i + 1] = universeHeight / 2 + circleRadius * Math.sin((2 * Math.PI) * i / (numberOfContracts * 2));
        }

        play(universeWidth, universeHeight, startPosition, contractLocations);
    }

    private void play(double universeWidth, double universeHeight, Position startPosition, double... contractLocations) throws Exception {
        gamePlayer.play(testMode, universeWidth, universeHeight, startPosition, contractLocations);
        assertTrue(gamePlayer.isLevelCompleted());
    }
}
