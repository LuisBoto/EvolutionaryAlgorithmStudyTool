package tsp.lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPParser {

	private static List<Point> points;

	public static Graph<String> parseInstance(String fileURL) {
		points = new ArrayList<Point>();
		try {
			FileReader fr = new FileReader(fileURL);
			BufferedReader bf = new BufferedReader(fr);
			String line = "";
			String[] parts;
			boolean readingCoords = false;
			while ((line = bf.readLine()) != null) {
				if (line.equals("EOF"))
					break;

				if (!line.equals("NODE_COORD_SECTION") && !readingCoords)
					continue;

				if (line.equals("NODE_COORD_SECTION")) {
					readingCoords = true;
					continue;
				}
				parts = line.split(" ");
				Point point = new Point(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
				points.add(point);
			}
			bf.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createGraph();
	}

	private static Graph<String> createGraph() {
		Graph<String> instance = new Graph<String>(points.size());
		for (Point point : points)
			instance.addNode(point.getName());
		for (Point point : points) {
			for (Point point2 : points) {
				instance.addEdge(point.getName(), point2.getName(), point.getDistance(point2));
			}
		}
		return instance;

	}

	private static class Point {

		private String name;
		private int x;
		private int y;

		Point(String name, int x, int y) {
			this.name = name;
			this.x = x;
			this.y = y;
		}

		String getName() {
			return this.name;
		}

		int getX() {
			return this.x;
		}

		int getY() {
			return this.y;
		}

		int getDistance(Point destination) {
			return Math.abs(this.x - destination.getX()) + Math.abs(this.y - destination.getY());
		}
	}

}
