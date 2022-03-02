package utils;

import java.util.Arrays;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;

public class OtherFonctions {
	
	
	public static Image lecture(String path) {
		Image i = ImageLoader.exec(path);
		System.out.println("Largeur: " + i.getXDim() + " , Hauteur " + i.getYDim());
		System.out.println("Nombre de canaux: " + i.getBDim());
		return i;
	}
	
	public static void afficherImage(Image im) {
		
		boolean rgb = false;
		if(im.getBDim() == 3) rgb = true; 
		
		for(int y = 0; y < im.getYDim(); y++) {
			for(int x = 0; x < im.getXDim(); x++) {
				if(rgb) {
					System.out.print(im.getPixelXYBByte(x, y, 0) + ",");
					System.out.print(im.getPixelXYBByte(x, y, 1) + ",");
					System.out.print(im.getPixelXYBByte(x, y, 2) + "  ");
				}
				else {
					System.out.println(im.getPixelXYBByte(x, y, 0) + "  ");
				}
				
				if(x == im.getXDim() - 1) {
					System.out.println(System.lineSeparator());
				}
			}
		}
	}
	
	public static Image drapeauBretagne() {
		ByteImage img = new ByteImage(450, 300, 1, 1, 1);
		for(int y = 0; y < img.getYDim(); y++) {
			for(int x = 0; x < img.getXDim(); x++) {
				img.setPixelXYBByte(x, y, 0, 255);
			}
		}
		//haut en bas -> Y 0 à MAX, gauche à droite : X 0 à MAX
		// 225 - 25 , 225 + 25
		//VERTICAL
		int XMIL = img.getXDim() / 2, YMIL = img.getYDim() / 2;
		for(int y = 0; y < img.getYDim(); y++) {
			for(int x = XMIL; x < XMIL + 25; x++) {
				img.setPixelXYBByte(x, y, 0, 0);
			}
			for(int x = XMIL; x < XMIL - 25; x++) {
				img.setPixelXYBByte(x, y, 0, 0);
			}
		}
		//HORIZONTAL
		for (int x = 0; x < img.getXDim(); x++) {
			for(int y = YMIL; y < YMIL + 25; y++) {
				img.setPixelXYByte(x, y, 0);
			}
			for(int y = YMIL; y < YMIL - 25; y++) {
				img.setPixelXYByte(x, y, 0);
			}
		}
		
		return img;
	}
	
	public static int[] extrema(Image im) {
		int min = 125, max = 125;
		
		for(int x = 0; x < im.getXDim(); x++) {
			for(int y = 0; y < im.getYDim(); y++) {
				int val = im.getPixelXYBByte(x, y, 0);
				System.out.println(val);
				if(min < val) min = val;
				if (max > val) max = val;
			}
		}
		System.out.println("Minimum : " + min + ", Maximum: " + max);
		int[] tab = new int[2];
		tab[0] = min;
		tab[1] = max;
		return tab;
	}
	
	/**public static Image inversion(Image im) {
		ByteImage nouv = new ByteImage(im.getXDim(), im.getYDim(),);
	}
	
	**/
	
	
	
	public static Image versNiveauDeGris(Image i) {
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
	
	
	public static Image binarisation(Image i) throws Exception {
		if(i.getBDim() != 1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		int seuil = 140;
		ByteImage binar = new ByteImage(i.getXDim(), i.getYDim(), 1, 1, 1);
		for(int x= 0; x < binar.getXDim(); x++) {
			for(int y=0; y < binar.getYDim(); y++) {
				int v = i.getPixelXYBByte(x, y, 0);
				if(v <= seuil) binar.setPixelXYBByte(x, y, 0, 0);
				else binar.setPixelXYBByte(x, y, 0, 255);
			}
		}
		return binar;
	}
	
	public static Image etirementContraste(Image i) throws Exception {
		if(i.getBDim() != 1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		ByteImage cont = new ByteImage(i.getBDim(), i.getYDim(),1,1,1);
		int[] tab = extrema(i);
		int gmin = tab[0], gmax = tab[1];
		for(int x = 0; x < cont.getXDim(); x++) {
			for(int y = 0; y < cont.getYDim(); y++) {
				int v = i.getPixelXYBByte(x, y, 0);
				cont.setPixelXYBByte(x, y, 0, 255 * (v-gmin) / (gmax - gmin));
			}
		}
		return cont;
	}
	
	public static double[] histogramme(Image i) throws Exception{
		if(i.getBDim() != 1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		
		double[] histo = new double[256];
		for(int val= 0; val< histo.length; val++) {
			histo[val] = 0;
		}
		for(int x = 0; x < i.getXDim(); x++) {
			for(int y = 0; y < i.getYDim(); y++) {
				int v = i.getPixelXYBByte(x, y, 0);
				histo[v] += 1;
			}
		}
		return histo;
	}
	
	public static void egalisationHisto(Image i) throws Exception{	
		double[] histo = histogramme(i);
		double[] cumul = new double[256];
		
		for(int v= 0; v < cumul.length; v++) {
			for(int cum = 0; cum <= v; cum++) {
				cumul[v] += histo[cum];
			}
				// 3-> 3 ->2e -> 3 + 5
		}
		
		
	}
	
	//faire une fonction générique pour les différents filres
	public static int[] getPixelsVoisins(Image in, int i, int j){
		int cpt = 0;
		int[] values = new int[9];
		for(int x = -1; x<= 1; x++) {
			for(int y= -1; y<= 1; y++) {
				System.out.println("X = " + x + "Y = " + y + "I = "+ x + " Y =" +y );
				values[cpt++] = in.getPixelXYBByte(i+x, i+y, 0);
				System.out.println("CPT: " + cpt);
			}
		}
		return values;
	}
	
	public static Image filtreMoyenneur(Image in) throws Exception{
		if(in.getBDim() !=1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		ByteImage out = new ByteImage(in.getXDim(), in.getYDim(),1,1,1);
		
		for(int x = 1; x < out.getXDim() - 2; x++) {
			for(int y=1; y < out.getYDim() - 2 ; y++) {
				int[] voisins = getPixelsVoisins(in, x, y);
				int moy= 0;
				for(int pos = 0; pos < voisins.length ; pos++) {
					moy += voisins[pos];
				}
				moy = moy/9;
				out.setPixelXYBByte(x, y, 0, moy);
			}
		}
		return out;
		
	}
	
	public static Image filtreMoyenneurRGB(Image in) throws Exception{
		if(in.getBDim() !=3) {
			throw new Exception("Cette image n'est pas en couleur");
		}
		ByteImage out = new ByteImage(in.getXDim(), in.getYDim(),1,1,3);
		for(int x = 1; x < out.getXDim() - 2; x++) {
			for(int y=1; y < out.getYDim() - 2 ; y++) {
				for(int canal = 0; canal < 3; canal++) {
					int[] voisins = getPixelsVoisins(in, x, y);
					int moy= 0;
					for(int pos = 0; pos < voisins.length ; pos++) {
						moy += voisins[pos];
					}
					moy = moy/9;
					out.setPixelXYBByte(x, y, canal, moy);
				}
				
			}
		}
		return out;
		
	}
	
	public static Image filtreMedian(Image in) throws Exception{
		if(in.getBDim() !=1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		ByteImage out = new ByteImage(in.getXDim(), in.getYDim(),1,1,1);
		for(int x = 1; x < out.getXDim() - 2; x++) {
			for(int y=1; y < out.getYDim() - 2 ; y++) {
				int[] voisins = getPixelsVoisins(in, x, y);
				Arrays.sort(voisins);
				out.setPixelXYBByte(x, y, 0, voisins[4]);
			}
		}
		return out;

	}
	
	public static Image filtreMedianRGB(Image in) throws Exception{
		if(in.getBDim() !=3) {
			throw new Exception("Cette image n'est pas en couleur");
		}
		ByteImage out = new ByteImage(in.getXDim(), in.getYDim(),1,1,3);
		for(int x = 1; x < out.getXDim() - 2; x++) {
			for(int y=1; y < out.getYDim() - 2 ; y++) {
				for(int canal  = 0; canal < 3; canal++) {
					int[] voisins = getPixelsVoisins(in, x, y);
					Arrays.sort(voisins);
					out.setPixelXYBByte(x, y, canal, voisins[4]);
				}
			}
		}
		return out;

	}
	
	public static Image regularisation(Image in) throws Exception{
		if(in.getBDim() !=1) {
			throw new Exception("Cette image n'est pas en niveaux de gris");
		}
		ByteImage out = new ByteImage(in.getXDim(), in.getYDim(),1,1,3);
		for(int x = 1; x < out.getXDim() - 2; x++) {
			for(int y=1; y < out.getYDim() - 2 ; y++) {
				int sum = 0;
				int[] voisins = getPixelsVoisins(in, x, y);
				sum += (4 * voisins[0] + (-4) * voisins[8]);
				out.setPixelXYBByte(x, y, 0, sum);
			}
		}
		return out;
	}
	
}
