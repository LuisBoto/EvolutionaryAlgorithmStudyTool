package tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tsp.graph.Graph;

public class TSPFunctions {
	
	public static Individual<String> generateRandomIndividual(List<String> cities) {
		List<String> individualRepresentation = new ArrayList<>();		
		for (int i = 0; i < cities.size(); i++) 
			individualRepresentation.add(cities.get(i));		
		Collections.shuffle(individualRepresentation);
		return new Individual<>(individualRepresentation);
	}
	
	public static TSPFitnessFunction getFitnessFunction() {
		return new TSPFitnessFunction();
	}
	
	public static class TSPFitnessFunction implements FitnessFunction<String> {

		public double apply(Graph<String> cities, Individual<String> individual) {
			double fitness = 0;
			
			for (int i=1; i<individual.getRepresentation().size(); i++) {
				fitness += cities.getEdge(individual.getRepresentation().get(i-1), individual.getRepresentation().get(i));
			}

			return fitness;
		}
	}

}
