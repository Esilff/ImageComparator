package icomparator;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class SimilarityStructure {
	
	private TreeMap<Double, TreeSet<String>> structure;
	
	public SimilarityStructure() {
		structure = new TreeMap<Double,TreeSet<String>>();
	}
	
	public void put(Double value, String imageName) {
		if(structure.containsKey(value)) {
			structure.get(value).add(imageName);
		}
		else {
			TreeSet<String> names = new TreeSet<String>();
			names.add(imageName);
			structure.put(value, names);
		}
	}
	
	private double getMin() {
		return structure.firstKey();
	}
	
	private void removeMin() {
		structure.remove(getMin());
	}
	
	public TreeMap<Double, TreeSet<String>> getStructure(){
		return structure;
	}
	
	public ArrayList<String> mostSimilarImages() {
		ArrayList<String> images = new ArrayList<>(10);
		
		while(images.size() < 10) {
			for(int number = 0; number < structure.get(getMin()).size(); number++) {
				if(images.size() == 10) break;
				String name = structure.get(getMin()).first();
				System.out.println(structure.get(getMin()));
				images.add(name);
				structure.get(getMin()).remove(name);
				// peut-être stocker ça avant ???
			}
			removeMin();
			if(structure.isEmpty()) break;
		}
		return images;
	}
	
}
