package tsp.geneticAIModern;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import tsp.geneticAIModern.TSPFunctions.TSPFitnessFunction;
import tsp.utils.Graph;

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
		cities.addEdge("A", "D", 20.0);
		cities.addEdge("B", "C", 35.0);
		cities.addEdge("B", "D", 25.0);
		cities.addEdge("C", "D", 30.0);
		cities.addEdge("E", "A", 100.0);
		cities.addEdge("E", "D", 1.0);

		// Parameters
		int popSize = 150;
		double mutationProbability = 0.20; 
		int numberOfGenerations = 500;
		double matingProbability = 1;
		
		tspAIModernGeneticAlgorithm(cities, popSize, mutationProbability, numberOfGenerations, matingProbability);
	}

	private static void tspAIModernGeneticAlgorithm(Graph<String> cities, int populationSize, double mutationProbability, int numberGenerations,
			double matingProbability) {
		System.out.println("--- TSP AIModern GeneticAlgorithm ---\n");

		FitnessFunction<String> fitnessFunction = TSPFunctions.getFitnessFunction();
		((TSPFitnessFunction) fitnessFunction).setCities(cities);

		// Generate an initial population
		Set<Individual<String>> population = new HashSet<>();
		List<String> cityList = cities.getNodes();
		for (int i = 0; i < populationSize; i++)
			population.add(TSPFunctions.generateRandomIndividual(cityList));
		
		Collection<String> alphabet = cityList;
		GeneticAlgorithm<String> ga = new GeneticAlgorithm<>(cityList.size()+1, alphabet, mutationProbability);
		Individual<String> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction);
		
		System.out.println("\nMax time unlimited, Best Individual:\n" + bestIndividual.getRepresentation());
		System.out.println("City number      = " + cities.getNodes().size());
		System.out.println("# Different Paths = " + (new BigDecimal(cities.getNodes().size())).pow(cities.getNodes().size())); //TODO: Fix this later
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations       = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}

}
