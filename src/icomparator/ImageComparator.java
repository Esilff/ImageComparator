package icomparator;

import fr.unistra.pelican.Image;

public interface ImageComparator {
	
	public void setPathFolder(String path);
	
	public void setImagePath(String path);
	
	//Will be erased
	public void premierPrototype();
	
	public Image lecture(String path);
	public double[][] histogram(Image i);
	public double[][] discretization(double[][] histo);
	public double[][] normalize(double[][] disc, int nbPixels);
}