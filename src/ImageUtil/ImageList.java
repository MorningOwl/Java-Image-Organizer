package ImageUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class ImageList{
	private ImageColors sortedByColor;
	ImageListComparator comparator;
	List<ImageItem> images;
	
	public ImageList(){
		images = new ArrayList<ImageItem>();
		comparator = new ImageListComparator();
		Configuration.getInstance();
	}
	
	public void add(ImageItem item){
		if(!images.contains(item)){
			images.add(item);
		}		
	}
	
	public void remove(ImageItem item){
		images.remove(item);
	}
	
	public void sort(ImageColors color){
		sortedByColor = color;
		Collections.sort(images, comparator);
	}
	
	public void clear(){
		images.clear();
	}
		
	class ImageListComparator implements Comparator<ImageItem>{
		@Override
		public int compare(ImageItem item1, ImageItem item2) {
			double val1, val2;
			int totalPixel1 = item1.getWidth() * item1.getHeight(), totalPixel2 = item2.getWidth() * item2.getHeight();
			
			switch (sortedByColor) {
			case GREY:
				val1 = item1.getGreyCounter();
				val2 = item2.getGreyCounter();
				break;
			case WHITE:
				val1 = item1.getWhiteCounter();
				val2 = item2.getWhiteCounter();
				break;
			case RED:
				val1 = item1.getRedCounter();
				val2 = item2.getRedCounter();
				break;
			case GREEN:
				val1 = item1.getGreenCounter();
				val2 = item2.getGreenCounter();
				break;
			case BLUE:
				val1 = item1.getBlueCounter();
				val2 = item2.getBlueCounter();
				break;
			case YELLOW:
				val1 = item1.getYellowCounter();
				val2 = item2.getYellowCounter();
				break;
			case MAGENTA:
				val1 = item1.getMagentaCounter();
				val2 = item2.getMagentaCounter();
				break;
			case CYAN:
				val1 = item1.getCyanCounter();
				val2 = item2.getCyanCounter();
				break;
			default:
				val1 = item1.getBlackCounter();
				val2 = item2.getBlackCounter();
				break;
			}	
			val1 /= totalPixel1;
			val2 /= totalPixel2;
			
			if(val1 < val2)
				return 1;
			else if(val1 == val2)
				return 0;
			else 
				return -1;	
		}		
	}	

	public String toString(){
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		
		for(ImageItem item : images){
			builder.append("[" + counter + "]" + " " + item+ "\n");
			counter++;
		}
		
		return builder.toString();
	}
}
