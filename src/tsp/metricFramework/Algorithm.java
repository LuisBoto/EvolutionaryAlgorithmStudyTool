package tsp.metricFramework;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.util.Timer;
import java.util.TimerTask;

import tsp.geneticAlgorithm.Individual;

public abstract class Algorithm<A> {

	protected static final String POPULATION_SIZE = "populationSize";
	protected static final String ITERATIONS = "iterations";
	protected static final String TIME_IN_MILLISECONDS = "timeInMSec";

	protected static MetricStorage metrics = new MetricStorage(); // Must be static so multiple threads can use it
	private static List<ProgressTracker> progressTrackers = new ArrayList<>();
	protected String fileUrl;
	protected long startTime; // Must be initialized on impl when execution starts
	protected static Timer dumpTimer;
	private boolean timerRunning = false;
	private static boolean alreadySaved = false; // To avoid Timer firing multiple times on same Save Trigger

	public Algorithm() {
		this.createTrackers();
		dumpTimer = new Timer();
	}

	public void metricsDumpCheck() {
		// Method to be called from Impl that manages:
		// Timer that checks saving condition
		// File dump on execution finish
		if (!timerRunning)
			startTimer();
		if (stopCondition()) {
			dumpTimer.cancel();
			this.flushToFile();
		}
	}

	private void timerCheck() {
		if (saveCondition()) {
			if (!alreadySaved) {
				alreadySaved = true;
				this.notifyProgressTrackers();
			}
		} else {
			alreadySaved = false;
		}
	}

	private void startTimer() {
		dumpTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				timerCheck();
			}
		}, 0L, 25L);
		timerRunning = true;
	}

	// Default metric save condition to be overriden
	protected boolean saveCondition() {
		return getIterations() % 5 == 0;
	}

	// Default stop condition to be overriden
	protected boolean stopCondition() {
		if (getTimeInMilliseconds() > 30000) { // 30s default max time
			return true;
		}
		return false;
	}

	// Write metrics to csv file
	private void flushToFile() {
		if (this.fileUrl == null)
			this.fileUrl = "./" + getExecutionFilename();
		try {
			FileWriter fw = new FileWriter(this.fileUrl);
			BufferedWriter bf = new BufferedWriter(fw);
			int metricSize = 0;
			for (ProgressTracker tracer : progressTrackers) {
				if (metrics.getMetricValues(tracer.getName()) != null) {
					metricSize = metrics.getMetricValues(tracer.getName()).size();
					bf.write(tracer.getName().concat(";"));
				}
			}
			bf.write("\n");
			// System.out.println(progressTrackers.size());
			for (int i = 0; i < metricSize; i++) {
				for (ProgressTracker tracer : progressTrackers) {
					// System.out.println(tracer.getName() + " " + i + " " +
					// metrics.getMetricValues(tracer.getName()));
					bf.write(metrics.getMetricValues(tracer.getName()).get(i).concat(";"));
				}
				bf.write("\n");
			}
			bf.flush();
			bf.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MetricStorage getMetrics() {
		return metrics;
	}

	public int getPopulationSize() {
		return metrics.getInt(POPULATION_SIZE);
	}

	public int getIterations() {
		return metrics.getInt(ITERATIONS);
	}

	public long getTimeInMilliseconds() {
		return System.currentTimeMillis() - startTime; // Accurate time
		// return metrics.getLong(TIME_IN_MILLISECONDS);
	}

	protected void updateMetrics(Collection<Individual<A>> population, int itCount) {
		metrics.setValue(POPULATION_SIZE, population.size());
		metrics.setValue(ITERATIONS, itCount);
		metrics.setValue(TIME_IN_MILLISECONDS, this.getTimeInMilliseconds());
	}

	protected abstract String getExecutionFilename();

	// Method where metric trackers should be created,
	// called from Algorithm() constructor by default
	protected abstract void createTrackers();

	/** Progress trackers can be used to display progress information. */
	public void addProgressTracker(ProgressTracker pTracker) {
		progressTrackers.add(pTracker);
	}

	protected void notifyProgressTrackers() {
		for (ProgressTracker tracer : progressTrackers)
			tracer.saveProgress();
	}

	public static class ProgressTracker {

		private String name;

		public ProgressTracker(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		void saveProgress() {
			if (!metrics.existsMetrics(this.name))
				metrics.createMetric(name);
			metrics.saveMetric(name);
		}
	}

}
