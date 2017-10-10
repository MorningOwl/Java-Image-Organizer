package ImageUtil;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public final class Configuration{
	
	private static Configuration instance;
	private static Set<ImageColorKeys> colorReferences;
	
	private Configuration(){
		colorReferences = new HashSet<ImageColorKeys>();
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(128, 128, 128, null), ImageColors.GREY));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 255, null), ImageColors.WHITE));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 0, null), ImageColors.YELLOW));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(255, 0, 255, null), ImageColors.MAGENTA));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(255, 0, 0, null), ImageColors.RED));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(0, 255, 255, null), ImageColors.CYAN));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(0, 255, 0, null), ImageColors.GREEN));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(0, 0, 255, null), ImageColors.BLUE));
		colorReferences.add(new ImageColorKeys(Color.RGBtoHSB(0, 0, 0, null), ImageColors.BLACK));
	}
	
	/**
	 * Gets the instance of this Configuration class.
	 * @return Configuration instance
	 */
	public static Configuration getInstance(){
		if (instance == null){		
			synchronized(Configuration.class){
				if (instance == null){
					instance = new Configuration();
				}				
			}			
		}
		return instance;
	}
	
	/**
	 * Gets a set of color references.
	 * @return
	 */
	public static Set<ImageColorKeys> getColorReferences(){
		return colorReferences;
	}
}
