package TestPackage;

import java.io.File;

import ImageUtil.ImageColors;
import ImageUtil.ImageItem;
import ImageUtil.ImageList;
import ImageUtil.ImageReadingSession;

public class MainTest {
	
	public static void main(String arg[]){
		ImageReadingSession reader = new ImageReadingSession();
		reader.readDirectory(new File(""));
		ImageList imageList = reader.getImageList();
		imageList.sort(ImageColors.GREY);
		System.out.println(imageList);
		
	}	
}
