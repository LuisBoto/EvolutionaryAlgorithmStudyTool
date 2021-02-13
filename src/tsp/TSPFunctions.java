package tsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tsp.utils.Graph;

public class TSPFunctions {
	
	public static Individual<String> generateRandomIndividual(List<String> cities) {
		List<String> individualRepresentation = new ArrayList<>();		
		for (int i = 0; i < cities.size(); i++) 
			individualRepresentation.add(cities.get(i));		
		Collections.shuffle(individualRepresentation);
		//End city must be same as initial one
		individualRepresentation.add(individualRepresentation.get(0));
		return new Individual<>(individualRepresentation);
	}
	
	public static TSPFitnessFunction getFitnessFunction() {
		return new TSPFitnessFunction();
	}
	
	public static class TSPFitnessFunction implements FitnessFunction<String> {

		private Graph<String> cities;
		
		public void setCities(Graph<String> cities) {
			this.cities = cities;
		}
		
		public double apply(Individual<String> individual) {
			double fitness = 0;
			int size = individual.getRepresentation().size();
			
			for (int i=1; i<size; i++) {
				fitness = fitness + cities.getEdge(individual.getRepresentation().get(i-1), individual.getRepresentation().get(i));
			}
			
			return fitness;
		}
	}

}
