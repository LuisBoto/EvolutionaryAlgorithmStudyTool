package tsp.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import tsp.lib.Util;
import tsp.metricFramework.Algorithm;

public class GeneticAlgorithm<A> extends Algorithm<A> {

	protected String instanceName;
	protected int individualLength;
	protected double crossoverProbability;
	protected double mutationProbability;
	protected int maxTime;
	protected Random random;
	protected int reproduceOperator;
	protected int mutateOperator;

	public GeneticAlgorithm(String instanceName, int individualLength, double crossoverProbability,
			double mutationProbability, int maxTime, int reproduceOperator, int mutateOperator) {
		this(individualLength, crossoverProbability, mutationProbability, maxTime, reproduceOperator, mutateOperator,
				new Random());
		this.instanceName = instanceName;
	}

	public GeneticAlgorithm(int individualLength, double crossoverProbability, double mutationProbability, int maxTime,
			int reproduceOperator, int mutateOperator, Random random) {
		super(); // Calls createTrackers and thread
		this.individualLength = individualLength;
		this.crossoverProbability = crossoverProbability;
		this.mutationProbability = mutationProbability;
		this.maxTime = maxTime;
		this.random = random;
		this.mutateOperator = mutateOperator;
		this.reproduceOperator = reproduceOperator;

		assert (this.mutationProbability >= 0.0 && this.mutationProbability <= 1.0);
		assert (this.crossoverProbability >= 0.0 && this.crossoverProbability <= 1.0);
		assert (this.mutateOperator == 0 || this.mutateOperator == 1);
		assert (this.reproduceOperator == 0 || this.reproduceOperator == 1);
	}

	@Override
	protected void createTrackers() {
		this.addProgressTracker(new ProgressTracker(Algorithm.ITERATIONS));
		this.addProgressTracker(new ProgressTracker(Algorithm.TIME_IN_MILLISECONDS));
		this.addProgressTracker(new ProgressTracker("bestFitness"));
		this.addProgressTracker(new ProgressTracker("averageFitness"));
		this.addProgressTracker(new ProgressTracker("mutations"));
		this.addProgressTracker(new ProgressTracker("cruces"));
	}

	@Override
	protected boolean stopCondition() {
		return getTimeInMilliseconds() > this.maxTime;
	}

	@Override
	protected boolean saveCondition() {
		long module10s = getTimeInMilliseconds() % 10000;
		return (module10s > 9950 || module10s < 50); // Every 10s save metrics, with an error margin
	}

	public Individual<A> geneticAlgorithm(Collection<Individual<A>> initPopulation, FitnessFunction<A> fitnessFn) {
		// Initial values
		metrics.setValue("mutations", 0);
		metrics.setValue("crossovers", 0);
		Individual<A> bestIndividual = null;

		// Create a local copy of the population to work with
		List<Individual<A>> population = new ArrayList<>(initPopulation);
		validatePopulation(population);

		updateMetrics(population, 0);
		this.startTime = System.currentTimeMillis();
		this.calculateFitness(initPopulation, fitnessFn); // Must be called so fitness values are available
		bestIndividual = retrieveBestIndividual(initPopulation);
		int itCount = 0;

		do {
			updateMetrics(population, ++itCount);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			metrics.setValue(Algorithm.ITERATIONS, itCount);
			metrics.setValue("bestFitness", bestIndividual.getFitness());
			metrics.setValue("averageFitness", averageFitness(population));
			printStatus();
			metricsDumpCheck();

			population = nextGeneration(population, bestIndividual);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			this.calculateFitness(population, fitnessFn);
			metrics.setValue(Algorithm.TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
			bestIndividual = retrieveBestIndividual(population);
		} while (!this.stopCondition());

		metricsDumpCheck();
		return bestIndividual;
	}

	private void printStatus() {
		// Monitor average and best fitness, time, iteration etc.
		System.out.println("\nTime: " + getTimeInMilliseconds() + " Gen: " + getIterations() + " Best f: "
				+ metrics.getValue("bestFitness") + " Average f:" + metrics.getValue("averageFitness"));
	}

	public Individual<A> retrieveBestIndividual(Collection<Individual<A>> population) {
		Individual<A> bestIndividual = null;
		double bestSoFarFValue = 0;

		for (Individual<A> individual : population) {
			double fValue = individual.getFitness();
			if (fValue > bestSoFarFValue) {
				bestIndividual = individual;
				bestSoFarFValue = fValue;
			}
		}

		return bestIndividual;
	}

	protected void calculateFitness(Collection<Individual<A>> population, FitnessFunction<A> fitnessFn) {
		for (Individual<A> individual : population)
			individual.setFitness(fitnessFn.apply(individual));
	}

	protected List<Individual<A>> nextGeneration(List<Individual<A>> population, Individual<A> bestBefore) {
		List<Individual<A>> newPopulation = new ArrayList<>(population.size());
		for (int i = 0; i < population.size() - 1; i++) { // -1 for elitism
			Individual<A> x = randomSelection(population);
			Individual<A> y = randomSelection(population);
			Individual<A> child = x;
			if (random.nextDouble() <= crossoverProbability)
				child = this.reproduceOperator == 0 ? this.reproduce(x, y) : this.reproduce2(x, y);

			if (random.nextDouble() <= mutationProbability) {
				child = this.mutateOperator == 0 ? this.mutate(child) : this.mutate2(child);
			}
			newPopulation.add(child);
		}
		newPopulation.add(bestBefore); // Adding elite individual not iterated on loop
		return newPopulation;
	}

	protected Individual<A> randomSelection(List<Individual<A>> population) {
		// Default result is last individual to avoid problems with rounding errors
		Individual<A> selected = population.get(population.size() - 1);

		// Determine all of the fitness values
		double[] fValues = new double[population.size()];
		double minFitness = population.get(0).getFitness();
		for (int i = 0; i < population.size(); i++) {
			fValues[i] = population.get(i).getFitness();
			if (minFitness > fValues[i])
				minFitness = fValues[i];
		}

		// Fitness scalation: Every individual is sustracted lowest fitness
		for (int i = 0; i < population.size(); i++) {
			fValues[i] -= minFitness;
		}
		fValues = Util.normalize(fValues);

		double prob = random.nextDouble();
		double totalSoFar = 0.0;
		for (int i = 0; i < fValues.length; i++) {
			totalSoFar += fValues[i];
			if (prob <= totalSoFar) {
				selected = population.get(i);
				break;
			}
		}

		selected.incDescendants();
		return selected;
	}

	protected Individual<A> reproduce(Individual<A> x, Individual<A> y) {
		// OX type cross operator
		int workingIndividualLength = individualLength - 1;

		List<A> childRepresentation = new ArrayList<A>(x.getRepresentation());
		int p1 = randomOffset(workingIndividualLength);
		int p2 = randomOffset(workingIndividualLength);
		List<A> inheritedFromFirstParent = new ArrayList<A>();

		// Inheriting from first parent
		int i = p1;
		while (i != p2) {
			inheritedFromFirstParent.add(x.getRepresentation().get(i));
			i++;
			if (i == workingIndividualLength)
				i = 0;
		}

		// Inheriting from second parent
		int secondParentInheritsAt = p2;
		for (i = 0; i < workingIndividualLength; i++) {
			if (!inheritedFromFirstParent.contains(y.getRepresentation().get(i))) {
				childRepresentation.set(secondParentInheritsAt, y.getRepresentation().get(i));
				secondParentInheritsAt++;
				if (secondParentInheritsAt == workingIndividualLength)
					secondParentInheritsAt = 0;
			}
		}

		// Last city must be initial one
		childRepresentation.set(individualLength - 1, childRepresentation.get(0));
		metrics.incrementIntValue("cruces");
		return new Individual<A>(childRepresentation);
	}

	protected Individual<A> reproduce2(Individual<A> x, Individual<A> y) {
		// Halfs cross operator
		int workingIndividualLength = individualLength - 1;
		List<A> childRepresentation = new ArrayList<A>(x.getRepresentation().size());
		int counter = 0;
		int randSize = this.random.nextInt(workingIndividualLength);

		// Adding random amount of cities from first parent, on same order
		for (int i = 0; i < randSize; i++) {
			childRepresentation.add(counter, x.getRepresentation().get(i));
			counter++;
		}

		// Inheriting the rest from second parent, on inverse relative order
		for (int i = workingIndividualLength - 1; i >= 0; i--) {
			if (!childRepresentation.contains(y.getRepresentation().get(i))) {
				childRepresentation.add(counter, y.getRepresentation().get(i));
				counter++;
			}
		}

		// Last city must be initial one
		childRepresentation.add(individualLength - 1, childRepresentation.get(0));
		metrics.incrementIntValue("cruces");
		return new Individual<A>(childRepresentation);
	}

	protected double averageFitness(List<Individual<A>> population) {
		double totalFitness = 0.0;
		for (int i = 0; i < population.size(); i++) {
			totalFitness += population.get(i).getFitness();
		}
		return totalFitness / population.size();
	}

	protected Individual<A> mutate(Individual<A> child) {
		// Select two random cities that are not the initial one and exchange them
		List<A> mutatedRepresentation = new ArrayList<A>(child.getRepresentation());
		int mutateOffsetPos1 = randomOffset(individualLength - 1);
		int mutateOffsetPos2 = randomOffset(individualLength - 1);
		A mutateOffsetValue1 = mutatedRepresentation.get(mutateOffsetPos1);
		A mutateOffsetValue2 = mutatedRepresentation.get(mutateOffsetPos2);

		mutatedRepresentation.set(mutateOffsetPos1, mutateOffsetValue2);
		mutatedRepresentation.set(mutateOffsetPos2, mutateOffsetValue1);

		// Last city must be initial one
		mutatedRepresentation.set(individualLength - 1, mutatedRepresentation.get(0));
		metrics.incrementIntValue("mutations");
		return new Individual<A>(mutatedRepresentation);
	}

	protected Individual<A> mutate2(Individual<A> child) {
		// Reverse individual
		int workingIndividualLength = individualLength - 1;
		List<A> mutatedRepresentation = new ArrayList<A>();

		for (int i = workingIndividualLength - 1; i >= 0; i--) {
			mutatedRepresentation.add(child.getRepresentation().get(i));
		}

		// Last city must be initial one
		mutatedRepresentation.add(individualLength - 1, mutatedRepresentation.get(0));
		metrics.incrementIntValue("mutations");
		return new Individual<A>(mutatedRepresentation);
	}

	protected int randomOffset(int length) {
		return random.nextInt(length);
	}

	protected void validatePopulation(Collection<Individual<A>> population) {
		if (population.size() < 1) {
			throw new IllegalArgumentException("Must start with at least a population of size 1");
		}
		// String lengths are assumed to be of fixed size,
		// therefore ensure initial populations lengths correspond to this
		for (Individual<A> individual : population) {
			if (individual.length() != this.individualLength) {
				throw new IllegalArgumentException("Individual [" + individual
						+ "] in population is not the required length of " + this.individualLength);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected String getExecutionFilename() {
		return "GATSP_" + this.instanceName + "_" + reproduceOperator + "_" + mutateOperator + "_" + POPULATION_SIZE
				+ "_" + crossoverProbability + "_" + mutationProbability + "_" + maxTime + "_execution_"
				+ new Date().toGMTString().replace(':', '_').replace(" ", "_") + random.nextInt(10000) + ".csv";
	}

}