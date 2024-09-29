package engine.core;

import engine.helper.MarioActions;

/**
 * A simple AI agent that can clear a level by moving to the right and jumping over obstacles.
 */
public class SimpleAI implements MarioAgent {
    private boolean[] actions;

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        actions = new boolean[MarioActions.numberOfActions()];
    }

    @Override
    public void train(MarioForwardModel model) {
        // No training required for this simple AI
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        // Always move to the right
        actions[MarioActions.RIGHT.getValue()] = true;

        // Check if there's an obstacle in front
        if (isObstacleInFront(model)) {
            // Jump if there's an obstacle
            actions[MarioActions.JUMP.getValue()] = true;
        } else {
            // Otherwise, don't jump
            actions[MarioActions.JUMP.getValue()] = false;
        }

        return actions;
    }

    @Override
    public String getAgentName() {
        return "SimpleAI";
    }

    /**
     * Checks if there's an obstacle in front of Mario.
     *
     * @param model the current state of the game.
     * @return true if there's an obstacle in front, false otherwise.
     */
    private boolean isObstacleInFront(MarioForwardModel model) {
        int marioX = model.getMarioScreenTileX();
        int marioY = model.getMarioScreenTileY();

        // Check tiles in front of Mario
        for (int x = marioX + 1; x <= marioX + 2; x++) {
            for (int y = marioY - 1; y <= marioY + 1; y++) {
                if (model.getBlockValue(x, y) != 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
