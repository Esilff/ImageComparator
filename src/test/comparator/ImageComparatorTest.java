package test.comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.unistra.pelican.Image;
import icomparator.IComparator;
import icomparator.RGBPrototype;

public class ImageComparatorTest {
	
	private IComparator cmp;
	@BeforeEach
	public void init() {
		cmp = new RGBPrototype();
	}
	
	@Test
	void test() {
		//Image im = cmp.lecture("./imagesTP/eiffel.jpg");
	}

}
