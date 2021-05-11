package tsp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.script.ScriptException;

import tsp.geneticAlgorithm.FitnessFunction;
import tsp.geneticAlgorithm.GeneticAlgorithm;
import tsp.geneticAlgorithm.Individual;
import tsp.geneticAlgorithm.TSPFunctions;
import tsp.geneticAlgorithm.TSPFunctions.TSPFitnessFunction;
import tsp.lib.Graph;
import tsp.lib.TSPParser;

public class TSPLauncher {

	public static void main(String[] args) throws ScriptException {
		// City graph construction
		Graph<String> cities = TSPParser.parseInstance("./resources/tspInstances/a280.tsp");

		// Parameters
		// int maxIterations = 60;
		int popSize = 100;
		double crossoverProbability = 0.8;
		double mutationProbability = 0.2;
		int maxTime = 1000 * 10;
		int reproduce = 1;
		int mutate = 1;
		tspAIModernGeneticAlgorithm(cities, popSize, crossoverProbability, mutationProbability, maxTime, reproduce,
				mutate);
	}

	private static void tspAIModernGeneticAlgorithm(Graph<String> cities, int populationSize,
			double crossoverProbability, double mutationProbability, int maxIterations, int reproduceOperator,
			int mutationOperator) {
		System.out.println("--- TSP AIModern GeneticAlgorithm ---");
		FitnessFunction<String> fitnessFunction = TSPFunctions.getFitnessFunction();
		((TSPFitnessFunction) fitnessFunction).setCities(cities);

		// Generate an initial population
		Set<Individual<String>> population = new HashSet<>();
		List<String> cityList = cities.getNodes();
		for (int i = 0; i < populationSize; i++)
			population.add(TSPFunctions.generateRandomIndividual(cityList));

		GeneticAlgorithm<String> ga = new GeneticAlgorithm<>(cityList.size() + 1, crossoverProbability,
				mutationProbability, maxIterations, reproduceOperator, mutationOperator);
		System.out.println("Starting evolution");
		Individual<String> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction);

		System.out.println("\nMax time unlimited, Best Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("City number = " + cities.getNodes().size());
		System.out.println("Fitness = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations = " + ga.getIterations());
		System.out.println("Took = " + ga.getTimeInMilliseconds() + "ms.");
	}

}
