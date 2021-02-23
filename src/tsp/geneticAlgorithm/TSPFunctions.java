package tsp.geneticAlgorithm;

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
				String source = individual.getRepresentation().get(i-1);
				String target = individual.getRepresentation().get(i);
				if (cities.existEdge(source, target))
					fitness = fitness + cities.getEdge(source, target);
				else //In case there is no path between two cities
					return 0; 
			}
			
			return 1/fitness;
		}
	}

}
