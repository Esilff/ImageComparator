package partie1;

import utils.HistogramTools;
import utils.NoiseTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class ImageComparator {
	
	private static TreeMap<Double, String> dataStructure =
			new TreeMap<Double, String>();
	
	
	public static void premierPrototype(String path) {
		Image im = lecture(path);
		System.out.println(path);
		double[][] histo = histogramRGB(im);
		histo = discretization(histo);
		
		// faire une binarisation !!!!!!!
		histo = normalize(histo, im.getXDim() * im.getYDim());
		//Z:\\IMAGE\\images\\000.jpg
		
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < histo[canal].length; val++) {
				System.out.println(histo[canal][val]);
			}
		}
		
		System.out.println("2nd");
		
		//./database/motos/000.jpg
		
		Image i = lecture("./imagesTP/eiffel.jpg");
		double[][] test = histogramRGB(i);
		test = discretization(test);
		
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < histo[canal].length; val++) {
				System.out.println(histo[canal][val]);
			}
		}
		
		System.out.println(similarityMeasure(histo, test));
	}
	
	
	private static Image lecture(String path) {
		Image im = ImageLoader.exec(path);
		//Viewer2D.exec(im);
		im = NoiseTools.addNoise(im, 0.05);
		//Viewer2D.exec(im);
		Image med = medianFilterRGB(im);
		med.setColor(true);
		//Viewer2D.exec(med);
		return med;
	}
	

	private static Image medianFilterRGB(Image img) {
        ByteImage image = new ByteImage(img.getXDim(), img.getYDim(),1,1,3);
        for (int i = 1; i < img.getXDim() - 2; i++) {
            for (int j = 1; j < img.getYDim() - 2; j++) {
                int[][] tab = getNeighValueRGB(img,i,j);
                for (int k = 0; k < 3; k++) {
                    Arrays.sort(tab[k]);
                    image.setPixelXYBByte(i,j,k, tab[k][tab[k].length/2]);

                }
            }
        }
        return image;
    }

	
	private static int[][] getNeighValueRGB(Image img, int x, int y) {
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
	
	private static double[][] histogramRGB(Image i){
		double[][] histo = new double[3][256];
		for(int val= 0; val< histo.length; val++) {
			for(int canal = 0; canal < 3; canal++) {
				histo[canal][val] = 0;
			}
		}
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
	
	
	// pour chaque canal -> moy des 25 prem
	//256->29
	private static double[][] discretization(double[][] histo){
		double[][] discret = new double[3][2];
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < discret[canal].length; val++) {
				discret[canal][val] = 0;
			}
		}
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
	
	private void test(double histo[][]) {
		for(int canal=0; canal < 3; canal++) {
			for(int val=0; val < histo[canal].length; val++) {
				
			}
		}
	}
	
	private static double[][] normalize(double[][] disc, int nbPixels){
		double[][] norm = new double[3][2];
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < norm[canal].length; val++) {
				norm[canal][val] = 0;
			}
		}
		for(int canal = 0; canal < 3; canal++) {
			for(int val = 0; val < disc[canal].length; val++) {
				norm[canal][val] = disc[canal][val]/nbPixels;
			}
		}
		
		return norm;
	}
	
	
	private static void similarImages(double[][] request, String reqPath) {
		//parcourir avec une boucle le dossier stockant la base d'images
		String path = "./database/motos/00";
		for(int i = 0; i <= 283; i++) {
			path += i + ".jpg";
			if(reqPath.equals(path)) continue;
			//filtre median applique dans la lecture
			Image current = lecture(path);
			double[][] curHisto = histogramRGB(current);
			curHisto = discretization(curHisto);
			curHisto = normalize(curHisto, current.getXDim() * current.getYDim());
			double distance = similarityMeasure(request, curHisto);
			
		}
	}
	
	private static double similarityMeasure(double[][] h1, double[][] h2) {
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
