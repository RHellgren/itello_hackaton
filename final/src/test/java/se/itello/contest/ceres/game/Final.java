package se.itello.contest.ceres.game;

import org.junit.Test;
import se.itello.contest.ceres.api.Brain;
import se.itello.contest.ceres.teamx.FullThrottleBrain;
import se.itello.contest.ceres.teamx.HobbitBrain;

public class Final {

    private static final boolean BUFFER = true;

    private final Brain brain = new HobbitBrain();

    @Test
    public void run() throws Exception {
        new GamePlayer(brain).playMultiplayerGame(BUFFER);

        // Test to run your jar.
        // This must run for the solution to be valid. Watch out for security exceptions!
        //new GamePlayer(<PATH_TO_JAR>, <FULL_CLASS_NAME_OF_BRAIN>).playMultiplayerGame(BUFFER);
    }
}
