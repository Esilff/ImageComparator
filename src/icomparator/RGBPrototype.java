package icomparator;

import utils.HistogramTools;
import utils.NoiseTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class RGBPrototype implements ImageComparator {
	/*The data structure that stores the similarity measures*/
	private SimilarityStructure similarities = new SimilarityStructure();
	
	/*The folder path*/
	private String pathFolder;
	
	/*The image path*/
	private String imagePath;
	
	public void setPathFolder(String path) {
		pathFolder = path;
	}
	
	public void setImagePath(String path) {
		imagePath = path;
	}
	
	/**
	 * Launches the image comparator
	 * @param path : the path to the request image
	 */
	public void premierPrototype() {
		Image im = lecture(imagePath);
		double[][] histo = histogram(im);
		histo = discretization(histo);
		// faire une binarisation !!!!!!!
		histo = normalize(histo, im.getXDim() * im.getYDim());
		//similarImages(histo);
		File folder = new File(pathFolder);
		//IndexingSystem.setHistogramDatabase(folder);
		similarImages(histo);
	}
	
	
	/**
	 * Reads an Image through his file path
	 * @param path : the image file path
	 * @return The image red
	 */
	public Image lecture(String path) {
		Image im = ImageLoader.exec(path);
		//Viewer2D.exec(im);
		im = NoiseTools.addNoise(im, 0.05);
		//Viewer2D.exec(im);
		Image med = medianFilter(im);
		med.setColor(true);
		//Viewer2D.exec(med);
		return med;
	}
	
	/**
	 * Applies the median filter in a RBG image
	 * @param img: the RGB image
	 * @return the new edited image
	 */
	protected Image medianFilter(Image img) {
        ByteImage image = new ByteImage(img.getXDim(), img.getYDim(),1,1,3);
        for (int i = 1; i < img.getXDim() - 2; i++) {
            for (int j = 1; j < img.getYDim() - 2; j++) {
                int[][] tab = getNeighValue(img,i,j);
                for (int k = 0; k < 3; k++) {
                    Arrays.sort(tab[k]);
                    image.setPixelXYBByte(i,j,k, tab[k][tab[k].length/2]);
                }
            }
        }
        return image;
    }
	
	/**
	 * Gets a RGB pixel's neibourgh values
	 * @param img: the image
	 * @param x: the image's witdh
	 * @param y: the image's length
	 * @return the pixel's neibourgh values into an array
	 */
	private int[][] getNeighValue(Image img, int x, int y) {
        int[][] tab = new int[3][9];
        int count = 0;
       for (int i = - 1; i <= 1; i++) {
           for (int j = -1; j <= 1; j++) {
               for (int k = 0; k < 3; k++) {
                   tab[k][count] = img.getPixelXYBByte(x+i,y+j,k);
               }
                count++;
           }
       }
       return tab;
   }
	
	/**
	 * Fill a histogram values to zero
	 * @param histo: the histogram to initialize
	 * @return the histogram initialized
	 */
	protected double[][] initializeHistogram(double[][] histo){
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < histo[canal].length; val++) {
				histo[canal][val] = 0;
			}
		}
		return histo;
	}
	
	/**
	 * Gets the histograms of a RGB image
	 * @param i: the image
	 * @return the histogram into an array
	 */
	public double[][] histogram(Image i){
		double[][] histo = new double[3][256];
		histo = initializeHistogram(histo);
		for(int x = 0; x < i.getXDim(); x++) {
			for(int y = 0; y < i.getYDim(); y++) {
				for(int canal = 0; canal < 3; canal++) {
					int v = i.getPixelXYBByte(x, y, canal);
					histo[canal][v] += 1;
				}
			}
		}
		return histo;
	}
	
	/**
	 * Discretize an histogram
	 * @param histo : the histogram that will be discretized
	 * @return the histogram discretized
	 */
	public double[][] discretization(double[][] histo){
		double[][] discret = new double[3][2];
		discret = initializeHistogram(discret);
		
		int cpt  = 0, sum = 0, nb = 0;
		for(int canal = 0; canal < 3; canal++) {
			nb = 0;
			for(int val= 0; val< histo[canal].length; val++) {
				sum+= histo[canal][val];
				cpt++;
				if(cpt %128 == 0) {
					discret[canal][nb++] = sum;
					cpt = 0;
					sum = 0;
				}
			}
		}
		return discret;
	}
	
	/**
	 * Normalize a discret histogram
	 * @param disc: the discret histogram
	 * @param nbPixels: the number of pixels of the request image
	 * @return the normalized histogram into an array
	 */
	public double[][] normalize(double[][] disc, int nbPixels){
		double[][] norm = new double[3][2];
		norm = initializeHistogram(norm);
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < disc[canal].length; val++) {
				norm[canal][val] = disc[canal][val]/nbPixels;
			}
		}
		return norm;
	}
	
	private void egalisationRBG(double[][] histoRGB, Image gray) {
		double[][] egal = new double[3][256];
		double[][] sum = new double[3][256];
		int nbPixels = gray.getXDim() * gray.getYDim();
		int length = gray.getYDim(), width = gray.getXDim();
		egal = initializeHistogram(egal);
		sum = initializeHistogram(sum);
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < egal[canal].length; val++) {
				for(int end = 0; end <= val; end++) {
					sum[canal][val]+= histoRGB[canal][end];
				}
			}
		}
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < length; y++) {
				
			}
		}
	}
	
	
	private Image toGrayscale(Image i) {
		ByteImage gris = new ByteImage(i.getXDim(), i.getYDim(), 1, 1, 1);
		for (int x = 0; x < gris.getXDim(); x++) {
			for (int y = 0; y < gris.getYDim(); y++) {
				int v = 0;
				for (int canal = 0; canal < 3; canal++) {
					v+= i.getPixelXYBByte(x, y, canal);
				}
				gris.setPixelXYBByte(x, y, 0, v/3);
			}
		}
		return gris;
	}
	
	/**
	 * 
	 * @param request
	 * @param reqPath
	 */
	private void similarImages(double[][] request) {
		TreeMap<String, double[][]> images = new IndexingSystem().getHistogramDatabase();
		double[][] t = images.get("001.jpg");
		
		
		Set<String> files = images.keySet();
		Iterator<String> it = files.iterator();
		while(it.hasNext()) {
			String file = it.next();
			double[][] test = images.get(file);
			
			double distance = similarityMeasure(request, images.get(file));
			similarities.put(distance, file);
		}
		ArrayList<String> names = similarities.mostSimilarImages();
	}
	
	/**
	 * 
	 * @param h1
	 * @param h2
	 * @return
	 */
	private double similarityMeasure(double[][] h1, double[][] h2) {
		double[] result = new double[3];
		double[][] values = new double[3][2];
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < h1[canal].length; val++) {
				values[canal][val] = Math.pow(h1[canal][val] - h2[canal][val], 2);
			}
		}
		
		for(int canal = 0; canal < 3; canal++) {
			result[canal] = Math.sqrt(values[canal][0] + values[canal][1]);
		}
		return result[0] + result[1] + result[2];
	}

}
