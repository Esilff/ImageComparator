package icomparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import fr.unistra.pelican.Image;

public class IndexingSystem {
	
	
	private File histogramDatabase = new File("./database/histograms.txt");
	
	
	public TreeMap<String, double[][]> getHistogramDatabase() {
		TreeMap<String, double[][]> input = new TreeMap<String, double[][]>();
		Scanner in;
		String line, fileName;
		String[] file;
		String delimSpace = " ";
		double[][] histo = new double[3][2];
		try {
			in = new Scanner(new FileInputStream(histogramDatabase));
			while(in.hasNextLine()) {
				line = in.nextLine();
				file = line.split(delimSpace);
				fileName = file[0];
				//createHistogram(file, histo);
				input.put(fileName, createHistogram(file));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	private double[][] createHistogram(String[] file){
		double[][] h = new double[3][2];
		int cpt = 1;
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < 2; val++) {
				h[canal][val] = Double.parseDouble(file[cpt++]);
			}
		}
		return h;
	}
	
	public void setHistogramDatabase(File folder) {
		StringBuilder sb = new StringBuilder();
		for(File file : folder.listFiles()) {
			if(!file.isDirectory()) {
				//filtre median applique dans la lecture
				ImageComparator rgb = new RGBPrototype();
				Image current = rgb.lecture(file.getPath());
				double[][] curHisto = rgb.histogram(current);
				curHisto = rgb.discretization(curHisto);
				curHisto = rgb.normalize(curHisto, current.getXDim() * current.getYDim());
				writeOnString(curHisto, file.getName(), sb);
			}
		}
		String data = sb.toString();
		writeOnFile(data);
	}
	
	private void writeOnString(double[][] histo, String fileName, StringBuilder sb) {
		sb.append(fileName + " ");
		for(int canal = 0; canal < 3; canal++) {
			sb.append(histo[canal][0]);
			sb.append(" ");
			sb.append(histo[canal][1]);
			if(canal == 2) break;
			sb.append(" ");
		}
		sb.append(System.lineSeparator());
	}
	
	private void writeOnFile(String data) {
		try {
			OutputStream out = new FileOutputStream(histogramDatabase.getPath());
			byte[] dataBytes = data.getBytes();
			try {
				out.write(dataBytes);
				System.out.println("Data has been written !");
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
