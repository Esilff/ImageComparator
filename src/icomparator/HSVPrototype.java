package icomparator;

import java.util.Arrays;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;

public class HSVPrototype extends RGBPrototype {
	
	private int M;
	private int m;
	private double H, S, V;
	
	/**
	 * Sets up the minimum and the maximum values of an image
	 * @param im: the image to analyze
	 */
	private void extrema(Image im) {
		m = 255; M = 0;
		for(int x = 0; x < im.getXDim(); x++) {
			for(int y = 0; y < im.getYDim(); y++) {
				int val = im.getPixelXYBByte(x, y, 0);
				System.out.println(val);
				if(val < m) m = val;
				if (val  > M) M = val;
			}
		}
	}
	
	private void setSpaceHSV(Image im) {
		extrema(im);
		V = M/255;
		if(M>0) S = 1-m/M;
		else S = 0;
		
	}
	
	/**
	 * Applies the median filter in a HSV image
	 * @param img: the HSV image
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
	private static int[][] getNeighValue(Image img, int x, int y) {
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

}
