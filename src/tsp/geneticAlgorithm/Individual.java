package tsp.geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual<A> {
	private List<A> representation = new ArrayList<>();
	private int descendants; // for debugging!

	public Individual(List<A> representation) {
		this.representation = Collections.unmodifiableList(representation);
	}

	public List<A> getRepresentation() {
		return representation;
	}

	public int length() {
		return representation.size();
	}

	/**
	 * Should be called by the genetic algorithm whenever the individual is
	 * selected to produce a descendant.
	 */
	public void incDescendants() {
		descendants++;
	}

	public int getDescendants() {
		return descendants;
	}

	@Override
	public String toString() {
		return representation.toString() + descendants;
	}
}