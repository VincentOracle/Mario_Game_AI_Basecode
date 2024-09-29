package agents.EAController;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class GeneticAgent implements MarioAgent {
    private boolean[] actions;
    private int currentActionIndex;

    public GeneticAgent(boolean[] actions) {
        this.actions = actions;
        this.currentActionIndex = 0;
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        // No initialization needed for this agent
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        if (currentActionIndex < actions.length) {
            boolean[] currentActions = new boolean[MarioActions.numberOfActions()];
            currentActions[MarioActions.RIGHT.getValue()] = actions[currentActionIndex];
            currentActions[MarioActions.JUMP.getValue()] = actions[currentActionIndex];
            //currentActions[MarioActions.SPEED.getValue()] = actions[currentActionIndex];
            currentActionIndex++;
            return currentActions;
        }
        return new boolean[MarioActions.numberOfActions()]; // No action if we've exhausted the sequence
    }

    @Override
    public String getAgentName() {
        return "GeneticAgent";
    }

    @Override
    public void train(MarioForwardModel model){

    }
}
