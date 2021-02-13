package tsp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tsp.graph.Graph;

public class TSPLauncher {

	public static void main(String[] args) {
		//City graph construction
		Graph<String> cities = new Graph<String>(5);
		cities.addNode("A");
		cities.addNode("B");
		cities.addNode("C");
		cities.addNode("D");
		cities.addNode("E");
		cities.addEdge("A", "B", 10.0);
		cities.addEdge("A", "C", 15.0);
		cities.addEdge("A", "D", 16.0);
		cities.addEdge("A", "E", 9.0);
		cities.addEdge("B", "C", 30.0);
		cities.addEdge("B", "D", 4.0);
		cities.addEdge("B", "E", 12.50);
		cities.addEdge("C", "D", 7.80);
		cities.addEdge("C", "E", 8.0);
		cities.addEdge("D", "E", 14.0);
		
		// Parameters
		int popSize = 50;
		double mutationProbability = 0.15; 
		int numberOfGenerations = 200;
		double matingProbability = 1;
		
		tspGeneticAlgorithm(cities, popSize, mutationProbability, numberOfGenerations, matingProbability);
	}

	private static void tspGeneticAlgorithm(Graph<String> cities, int populationSize, double mutationProbability, int numberGenerations,
			double matingProbability) {
		System.out.println("\n--- TSP GeneticAlgorithm ---");

		FitnessFunction<String> fitnessFunction = TSPFunctions.getFitnessFunction();

		// Generate an initial population
		Set<Individual<String>> population = new HashSet<>();
		List<String> cityList = Arrays.asList(cities.getNodes());
		for (int i = 0; i < populationSize; i++)
			population.add(TSPFunctions.generateRandomIndividual(cityList));

		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>(boardSize,
				NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize), mutationProbability);

		/*
		 * // Run for a set amount of time Individual<Integer> bestIndividual =
		 * ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);
		 * System.out.println("Max time 1 second, Best Individual:\n" +
		 * NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		 * System.out.println("Board Size      = " + boardSize);
		 * System.out.println("# Board Layouts = " + (new
		 * BigDecimal(boardSize)).pow(boardSize));
		 * System.out.println("Fitness         = " +
		 * fitnessFunction.apply(bestIndividual));
		 * System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		 * System.out.println("Population Size = " + ga.getPopulationSize());
		 * System.out.println("Iterations      = " + ga.getIterations());
		 * System.out.println("Took            = " + ga.getTimeInMilliseconds() +
		 * "ms.");
		 * 
		 * // Run till goal is achieved bestIndividual = ga.geneticAlgorithm(population,
		 * fitnessFunction, goalTest, 0L); System.out.println("");
		 * System.out.println("Max time unlimited, Best Individual:\n" +
		 * NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		 * System.out.println("Board Size      = " + boardSize);
		 * System.out.println("# Board Layouts = " + (new
		 * BigDecimal(boardSize)).pow(boardSize));
		 * System.out.println("Fitness         = " +
		 * fitnessFunction.apply(bestIndividual));
		 * System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		 * System.out.println("Population Size = " + ga.getPopulationSize());
		 * System.out.println("Itertions       = " + ga.getIterations());
		 * System.out.println("Took            = " + ga.getTimeInMilliseconds() +
		 * "ms.");
		 */

		// Run till a number of generations
		Individual<String> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, numberGenerations);
		System.out.println("");
		System.out.println("Max time unlimited, Best Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("City number      = " + cities.getNodes().length);
		System.out.println("# Different Paths = " + (new BigDecimal(10)).pow(5)); //TODO: Fix this later
		System.out.println("Fitness         = " + fitnessFunction.apply(cities, bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Itertions       = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}

}
