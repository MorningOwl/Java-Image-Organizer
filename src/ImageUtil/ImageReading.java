package ImageUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.FilenameFilter;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;

public class ImageReading {

	Deque<ImageThreading> threadPool = new ArrayDeque<ImageThreading>();

	static List<ImageColorKeys> colorPalleteList;

	public static void main(String[] args0) {
		System.out.println("The number of processors: "
				+ Runtime.getRuntime().availableProcessors());

		new ImageReading();
	}

	public ImageReading() {
		ImageItem imageItem1, imageItem2, imageItem3;
		this.setImageColorPalleteList();
		/*
		 * long startTime, endTime, duration; startTime = System.nanoTime();
		 * imageItem1 = readTheImageByThreading(new
		 * File("C:/Users/Edward/Pictures/Wordpress/DSC_0080.jpg")); endTime =
		 * System.nanoTime(); duration = (endTime - startTime) / 1000000; //
		 * divide by 1000000 to // get milliseconds.
		 * System.out.println("Thread work = " + duration);
		 * printPrettyInfo(imageItem1);
		 */

		ImageList list = readImageDirectory(new File("C:\\Users\\Edward\\Pictures\\Mahoko and Nao Visting Nagoya"));
		list.sort(ImageColors.GREY);
		System.out.println(list);
	}

	/**
	 * Reads a given directory and returns a list of ImageItems for each image
	 * file found in directory.
	 * 
	 * @param directory
	 *            A File type directory
	 * @return Returns an ImageList containing a list of ImageItems found on
	 *         each file in directory. Returns a null if not a directory
	 */
	public ImageList readImageDirectory(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.matches("([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");
				}
			});
			ImageList list = new ImageList(files.length);
			for(File f : files){
				ImageItem item = readImageFile(f);
				if(item != null){
					list.addImageItem(item);
				}
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * Reads an image given file location and returns an ImageItem
	 * 
	 * @param file
	 * @return Returns a valid ImageItem if found, null if not.
	 */
	public ImageItem readImageFile(File file) {
		if (!file.exists()) {
			return null;
		} else if (file.isDirectory()) {
			return null;
		} else if (!checkIfFileIsImage(file.getName())) {
			return null;
		} else {
			try {
				BufferedImage image = ImageIO.read(file);
				int nThreads = Runtime.getRuntime().availableProcessors();
				int start = 0;
				int end;
				int width = image.getWidth();
				int height = image.getHeight();
				int interval = width / nThreads;
				int remainder = width % nThreads;
				int greyCounter = 0, whiteCounter = 0, redCounter = 0, greenCounter = 0, blueCounter = 0, yellowCounter = 0, magentaCounter = 0, cyanCounter = 0, blackCounter = 0;
				List<Future<?>> futures = new ArrayList<Future<?>>();
				ImageThreading thread;

				ExecutorService exec = Executors.newFixedThreadPool(nThreads);
				for (int i = 0; i < nThreads; i++) {
					end = start + interval;
					if (i == nThreads && remainder > 0) {
						end += remainder;
					}
					thread = new ImageThreading(image, colorPalleteList, start,
							end);
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
				return new ImageItem(file.getPath(), file.getName(), height,
						width, greyCounter, whiteCounter, redCounter,
						greenCounter, blueCounter, yellowCounter,
						magentaCounter, cyanCounter, blackCounter);
			} catch (IOException e) {
				System.out.println(e);
				return null;
			}
		}
	}

	/**
	 * Checks if the file is an image type
	 * 
	 * @param fileName
	 *            Name of the file, including extension
	 * @return True if .jpg, false if not
	 */
	private boolean checkIfFileIsImage(String fileName) {
		if (fileName.endsWith(".jpg")) {
			return true;
		}
		return false;
	}

	private void setImageColorPalleteList() {
		colorPalleteList = new ArrayList<ImageColorKeys>();

		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(128, 128, 128,
				null), ImageColors.GREY));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 255,
				null), ImageColors.WHITE));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 255, 0,
				null), ImageColors.YELLOW));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(255, 0, 255,
				null), ImageColors.MAGENTA));
		colorPalleteList.add(new ImageColorKeys(
				Color.RGBtoHSB(255, 0, 0, null), ImageColors.RED));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 255, 255,
				null), ImageColors.CYAN));
		colorPalleteList.add(new ImageColorKeys(
				Color.RGBtoHSB(0, 255, 0, null), ImageColors.GREEN));
		colorPalleteList.add(new ImageColorKeys(
				Color.RGBtoHSB(0, 0, 255, null), ImageColors.BLUE));
		colorPalleteList.add(new ImageColorKeys(Color.RGBtoHSB(0, 0, 0, null),
				ImageColors.BLACK));
	}

}
