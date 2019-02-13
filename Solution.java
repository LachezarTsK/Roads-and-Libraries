import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;

public class Solution {
	/**
	 * Map Key: A city that has connections to other cities.
	 * 
	 * Map Value: One or more cities connected to the key city.
	 */
	static Map<Integer, List<Integer>> cities;

	/**
	 * Creates a bidirectional road between two cities.
	 */
	private static void inputHashMap(Integer from, Integer to) {
		if (!cities.containsKey(from)) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(to);
			cities.put(from, list);
		} else {
			cities.get(from).add(to);
		}

		if (!cities.containsKey(to)) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(from);
			cities.put(to, list);
		} else {
			cities.get(to).add(from);
		}
	}

	/**
	 * Calculates the minimum cost for a closed network.
	 */
	private static long getMinCostPerClosedNetwork(long citiesInClosedNetwork, long costLibrary, long costRoad) {
		long minCost = 0;
		if (costLibrary <= costRoad) {
			minCost = costLibrary * citiesInClosedNetwork;
		} else {
			minCost = (citiesInClosedNetwork - 1) * costRoad + costLibrary;
		}
		return minCost;
	}

	/**
	 * Calculates the minimum cost for all cities.
	 */
	private static long roadsAndLibraries(int numberOfCities, int costLibrary, int costRoad) {
		long totalMinCost = 0;
		long totalCitiesInClosedNetworks = 0;
		Set<Integer> visited_allClosedNetworks = new HashSet<Integer>();
		for (int city : cities.keySet()) {
			if (!visited_allClosedNetworks.contains(city)) {
				long citiesInCurrentClosedNetwork = findClosedNetworks(city, visited_allClosedNetworks);
				totalCitiesInClosedNetworks += citiesInCurrentClosedNetwork;
				totalMinCost += getMinCostPerClosedNetwork(citiesInCurrentClosedNetwork, costLibrary, costRoad);
			}
		}
		/**
		 * Adds the cost, if any, for libraries for cities that have no roads.
		 */
		totalMinCost += (numberOfCities - totalCitiesInClosedNetworks) * costLibrary;
		return totalMinCost;
	}

	/**
	 * Finds a group of two or more cities connected with roads.
	 * 
	 * Applied method: Breadth First Search.
	 * 
	 * @return The number of cities in current closed network.
	 */
	private static long findClosedNetworks(Integer start, Set<Integer> visited_allClosedNetworks) {

		List<Integer> queue = new LinkedList<Integer>();
		queue.add(start);
		Set<Integer> visited_currentClosedNetwork = new HashSet<Integer>();
		long citiesInCurrentClosedNetwork = 0;

		while (!queue.isEmpty()) {

			int current = queue.remove(0);
			if (!visited_currentClosedNetwork.contains(current)) {
				visited_currentClosedNetwork.add(current);
				visited_allClosedNetworks.add(current);
				citiesInCurrentClosedNetwork++;

				for (int i = 0; i < cities.get(current).size(); i++) {
					int city = cities.get(current).get(i);
					if (!visited_currentClosedNetwork.contains(city)) {
						queue.add(city);
					}
				}
			}
		}
		return citiesInCurrentClosedNetwork;
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		int numberOfQueries = scanner.nextInt();

		for (int i = 0; i < numberOfQueries; i++) {

			int numberOfCities = scanner.nextInt();
			int numberOfRoads = scanner.nextInt();
			int costLibrary = scanner.nextInt();
			int costRoad = scanner.nextInt();

			cities = new HashMap<Integer, List<Integer>>();

			for (int j = 0; j < numberOfRoads; j++) {
				int from = scanner.nextInt();
				int to = scanner.nextInt();
				inputHashMap(from, to);
			}

			long result = roadsAndLibraries(numberOfCities, costLibrary, costRoad);
			System.out.println(result);
		}
		scanner.close();
	}
}
