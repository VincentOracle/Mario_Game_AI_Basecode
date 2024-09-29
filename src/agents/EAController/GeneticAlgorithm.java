package agents.EAController;

import engine.core.MarioForwardModel;
import engine.core.MarioGame;
import engine.core.MarioResult;

import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm {
    private static final int POPULATION_SIZE = 50;
    private static final int CHROMOSOME_LENGTH = 1800; // Number of actions in the sequence
    private static final double MUTATION_RATE = 0.01;
    private static final int MAX_GENERATIONS = 100;

    private MarioGame game;
    private String level;

    public GeneticAlgorithm(MarioGame game, String level) {
        this.game = game;
        this.level = level;
    }

    public boolean[] evolve() {
        Population population = new Population(POPULATION_SIZE, CHROMOSOME_LENGTH);
        population.initialize();

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            evaluatePopulation(population);
            population = population.nextGeneration();
        }

        return population.getBestChromosome().getActions();
    }

    private void evaluatePopulation(Population population) {
        for (Chromosome chromosome : population.chromosomes) {
            MarioResult result = game.runGame(new GeneticAgent(chromosome.getActions()) {
                @Override
                public void train(MarioForwardModel model) {

                }
            }, level, 60, 0, true, 30, 3.5f);
            chromosome.setFitness(result.getCompletionPercentage());
        }
    }

    class Population {
        private Chromosome[] chromosomes;

        public Population(int size, int chromosomeLength) {
            chromosomes = new Chromosome[size];
            for (int i = 0; i < size; i++) {
                chromosomes[i] = new Chromosome(chromosomeLength);
            }
        }

        public void initialize() {
            for (Chromosome chromosome : chromosomes) {
                chromosome.initialize();
            }
        }

        public Chromosome getBestChromosome() {
            Chromosome best = chromosomes[0];
            for (Chromosome chromosome : chromosomes) {
                if (chromosome.getFitness() > best.getFitness()) {
                    best = chromosome;
                }
            }
            return best;
        }

        public Population nextGeneration() {
            Population newPopulation = new Population(chromosomes.length, chromosomes[0].getLength());
            Arrays.sort(chromosomes, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));

            // Elitism - carry the best chromosome to the next generation
            newPopulation.chromosomes[0] = chromosomes[0];

            for (int i = 1; i < chromosomes.length; i++) {
                Chromosome parent1 = selectParent();
                Chromosome parent2 = selectParent();
                Chromosome offspring = parent1.crossover(parent2);
                offspring.mutate();
                newPopulation.chromosomes[i] = offspring;
            }

            return newPopulation;
        }

        private Chromosome selectParent() {
            double totalFitness = 0;
            for (Chromosome chromosome : chromosomes) {
                totalFitness += chromosome.getFitness();
            }

            double randomValue = new Random().nextDouble() * totalFitness;
            double cumulativeFitness = 0;
            for (Chromosome chromosome : chromosomes) {
                cumulativeFitness += chromosome.getFitness();
                if (cumulativeFitness >= randomValue) {
                    return chromosome;
                }
            }
            return chromosomes[0]; // Shouldn't reach here
        }
    }

    class Chromosome {
        private boolean[] actions;
        private double fitness;

        public Chromosome(int length) {
            actions = new boolean[length];
        }

        public void initialize() {
            Random random = new Random();
            for (int i = 0; i < actions.length; i++) {
                actions[i] = random.nextBoolean();
            }
        }

        public boolean[] getActions() {
            return actions;
        }

        public int getLength() {
            return actions.length;
        }

        public double getFitness() {
            return fitness;
        }

        public void setFitness(double fitness) {
            this.fitness = fitness;
        }

        public Chromosome crossover(Chromosome other) {
            Chromosome offspring = new Chromosome(actions.length);
            Random random = new Random();
            for (int i = 0; i < actions.length; i++) {
                if (random.nextBoolean()) {
                    offspring.actions[i] = this.actions[i];
                } else {
                    offspring.actions[i] = other.actions[i];
                }
            }
            return offspring;
        }

        public void mutate() {
            Random random = new Random();
            for (int i = 0; i < actions.length; i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    actions[i] = !actions[i];
                }
            }
        }
    }
}
