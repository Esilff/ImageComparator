package appli;

import java.io.File;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import icomparator.ImageComparator;
import icomparator.RGBPrototype;

public class Application {
	
	public static void main(String[] agrs) {
		
	
		ImageComparator rgb = new RGBPrototype();
		rgb.setPathFolder("./database/motos");
		rgb.setImagePath("./database/motos/000.jpg");
		rgb.premierPrototype();
		
		//./../images/eiffel.jpg
	}
}
