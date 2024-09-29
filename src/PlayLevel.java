import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import agents.EAController.GeneticAgent;
import agents.EAController.GeneticAlgorithm;
import engine.core.*;

public class PlayLevel {
    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 2000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    public static void main(String[] args) {
        MarioGame game = new MarioGame();
        // Run the genetic algorithm to evolve the best action sequence
        GeneticAlgorithm ga = new GeneticAlgorithm(game, getLevel("./levels/SuperMarioBros/mario-1-1.txt"));
        boolean[] bestActions = ga.evolve();

        // Run the game with the best actions found by the genetic algorithm
        GeneticAgent geneticAgent = new GeneticAgent(bestActions) {
            @Override
            public void train(MarioForwardModel model) {

            }
        };
        MarioResult result = game.runGame(geneticAgent, getLevel("./levels/SuperMarioBros/mario-1-1.txt"), 60, 0, true, 30, 3.5f);

        printResults(result);
    }

}
