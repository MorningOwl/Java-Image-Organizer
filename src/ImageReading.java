import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import java.io.File;

public class ImageReading {

	Deque<ImageThreading> threadPool = new ArrayDeque<ImageThreading>();

	final static int MAXPIXELS = 255;
	final static int GREYPIXELS = 128;
	static List<ImageColorKeys> colorPalleteList;
	

	public static void main(String[] args0) {
		System.out.println("The number of processors: " + Runtime.getRuntime().availableProcessors());

		new ImageReading();
	}

	public ImageReading() {
		ImageItem imageItem1, imageItem2, imageItem3;
		this.setImageColorPalleteList();
		
		/*
		long startTime, endTime, duration;
		startTime = System.nanoTime();
		imageItem1 = readTheImageByThreading(new File("C:/Users/Edward/Pictures/Wordpress/DSC_0080.jpg"));
		endTime = System.nanoTime();
		duration = (endTime - startTime) / 1000000; // divide by 1000000 to
		                     						// get milliseconds.
		System.out.println("Thread work = " + duration);
		printPrettyInfo(imageItem1);
		*/
		
		ImageItem images[] = new ImageItem[3];
		imageItem1 = readTheImageByThreading(new File("C:/Users/Edward/Pictures/Wordpress/DSC_0080.jpg"));
		imageItem2 = readTheImageByThreading(new File("C:\\Users\\Edward\\Pictures\\Edited\\DSC_0356-1.jpg"));
		imageItem3 = readTheImageByThreading(new File("C:\\Users\\Edward\\Pictures\\Edited\\DSC_0476-1.jpg"));
		/*printPrettyInfo(imageItem1);
		System.out.println();
		printPrettyInfo(imageItem2);
		System.out.println();
		printPrettyInfo(imageItem3);
		System.out.println(images.length);
		*/
		
		images[0] = imageItem1;
		images[1] = imageItem2;
		images[2] = imageItem3;
		sortByColor(images, ImageColors.GREY);
		for(ImageItem ii: images){
			if(ii != null){
				printPrettyInfo(ii);
			}			
		}


		

	}

	public ImageItem readTheImageByThreading(File file) {
		try {
			
			BufferedImage image = ImageIO.read(file);
			int nThreads = Runtime.getRuntime().availableProcessors();
			int start = 0;
			int end;
			int width = image.getWidth();
			int height = image.getHeight();
			int interval = width / nThreads;
			int remainder = width % nThreads;
			int greyCounter = 0, whiteCounter = 0, redCounter = 0, greenCounter = 0, blueCounter = 0, yellowCounter = 0,
					magentaCounter = 0, cyanCounter = 0, blackCounter = 0;
			List<Future<?>> futures = new ArrayList<Future<?>>();
			ImageThreading thread;

			ExecutorService exec = Executors.newFixedThreadPool(nThreads);
			for (int i = 0; i < nThreads; i++) {
				end = start + interval;
				if (i == nThreads && remainder > 0) {
					end += remainder;
				}
				thread = new ImageThreading(image, colorPalleteList, start, end);
				threadPool.add(thread);
				Future<?> f = exec.submit(thread);
				futures.add(f);
				start = end;
			}

			for (Future<?> future : futures)
				try {
					future.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			boolean allDone = true;
			for (Future<?> future : futures) {
				allDone &= !future.isDone();
			}

			while (!threadPool.isEmpty()) {
				thread = threadPool.pop();
				greyCounter += thread.getGreyCounter();
				whiteCounter += thread.getWhiteCounter();
				redCounter += thread.getRedCounter();
				greenCounter += thread.getGreenCounter();
				blueCounter += thread.getBlueCounter();
				yellowCounter += thread.getYellowCounter();
				magentaCounter += thread.getMagentaCounter();

			}

			return new ImageItem(file.getPath(), file.getName(), height, width, greyCounter, whiteCounter, redCounter,
					greenCounter, blueCounter, yellowCounter, magentaCounter, cyanCounter, blackCounter);
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	public void setImageColorPalleteList() {
		colorPalleteList = new ArrayList<ImageColorKeys>();

		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(128, 128, 128, null), ImageColors.GREY));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 255, null), ImageColors.WHITE));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 0, null), ImageColors.YELLOW));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 0, 255, null), ImageColors.MAGENTA));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 0, 0, null), ImageColors.RED));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 255, 255, null), ImageColors.CYAN));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 255, 0, null), ImageColors.GREEN));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 0, 255, null), ImageColors.BLUE));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 0, 0, null), ImageColors.BLACK));
	}

	public void printPrettyInfo(ImageItem item) {
		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		defaultFormat.setMinimumFractionDigits(0);

		int width = item.getWidth();
		int height = item.getHeight();
		int greyCounter = item.getGreyCounter();
		int whiteCounter = item.getWhiteCounter();
		int redCounter = item.getRedCounter();
		int greenCounter = item.getGreenCounter();
		int blueCounter = item.getBlueCounter();
		int yellowCounter = item.getYellowCounter();
		int magentaCounter = item.getMagentaCounter();
		int cyanCounter = item.getCyanCounter();
		int blackCounter = item.getBlackCounter();

		System.out.println("Original image\nWidth: " + width + "\nHeight: " + height);
		System.out.println("Color reference ");
		System.out.println("Grey percentage: " + defaultFormat.format((double) greyCounter / (width * height)));
		System.out.println("White percentage: " + defaultFormat.format((double) whiteCounter / (width * height)));
		System.out.println("Red percentage: " + defaultFormat.format((double) redCounter / (width * height)));
		System.out.println("Green percentage: " + defaultFormat.format((double) greenCounter / (width * height)));
		System.out.println("Blue percentage: " + defaultFormat.format((double) blueCounter / (width * height)));
		System.out.println("Yellow percentage: " + defaultFormat.format((double) yellowCounter / (width * height)));
		System.out.println("Magenta percentage: " + defaultFormat.format((double) magentaCounter / (width * height)));
		System.out.println("Cyan percentage: " + defaultFormat.format((double) cyanCounter / (width * height)));
		System.out.println("Black percentage: " + defaultFormat.format((double) blackCounter / (width * height)));
	}

	public void printDetailedInfo(ImageItem item) {

		int width = item.getWidth();
		int height = item.getHeight();
		int greyCounter = item.getGreyCounter();
		int whiteCounter = item.getWhiteCounter();
		int redCounter = item.getRedCounter();
		int greenCounter = item.getGreenCounter();
		int blueCounter = item.getBlueCounter();
		int yellowCounter = item.getYellowCounter();
		int magentaCounter = item.getMagentaCounter();
		int cyanCounter = item.getCyanCounter();
		int blackCounter = item.getBlackCounter();

		System.out.println("Original image\nWidth: " + width + "\nHeight: " + height);
		System.out.println("Color reference ");
		System.out.println("Grey percentage: " + (double) greyCounter / (width * height));
		System.out.println("White percentage: " + (double) whiteCounter / (width * height));
		System.out.println("Red percentage: " + (double) redCounter / (width * height));
		System.out.println("Green percentage: " + (double) greenCounter / (width * height));
		System.out.println("Blue percentage: " + (double) blueCounter / (width * height));
		System.out.println("Yellow percentage: " + (double) yellowCounter / (width * height));
		System.out.println("Magenta percentage: " + (double) magentaCounter / (width * height));
		System.out.println("Cyan percentage: " + (double) cyanCounter / (width * height));
		System.out.println("Black percentage: " + (double) blackCounter / (width * height));
	}
	
	/**
	 * This is just a proof of concept, will be moving to a fitting class.
	 * @param images
	 * @param color
	 */
	public void sortByColor(ImageItem[] images, ImageColors color){
		boolean allSorted = false;
		boolean keepSorting = true;
		int timesSorted = 0;
		ImageItem tempItem;
		while(!allSorted){			
			for(int i = 0; i < images.length - 1; i++){

				if(images[i].compareColorWith(images[i+1], color) == -1){
					tempItem = images[i+1];
					images[i+1] = images[i];
					images[i] = tempItem;
					timesSorted++;
				}
			}
			
			if(timesSorted == 0){				
				allSorted = true;
			} else {
				timesSorted = 0;
			}
		}
	}

}
