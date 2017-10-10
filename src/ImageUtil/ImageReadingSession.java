package ImageUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;
import java.io.File;

public class ImageReadingSession {

	private Deque<ImageReadingThreading> threadPool;
	private Set<ImageColorKeys> colorReferences;
	private ImageList imageList;
	
	
	public ImageReadingSession() {
		Configuration.getInstance();
		colorReferences = Configuration.getColorReferences();
		threadPool = new ArrayDeque<ImageReadingThreading>();
		imageList = new ImageList();
	}
	
	public ImageList getImageList(){
		return imageList;
	}
	
	public void readDirectory(File directory){
		if(directory.exists() && directory.isDirectory()){
			File[] files = directory.listFiles();
			for(int i = 0; i < files.length; i++){
				if(files[i].isDirectory()){
					readDirectory(files[i]);
				} else {
					ImageItem item = readFile(files[i]);
					if(item != null){
						imageList.add(item);
					}
				}
			}
		}
	}
	
	/**
	 * Reads an image and returns an ImageItem object.
	 * @param file
	 * @return Returns an ImageItem if the file exists if not null.
	 */
	private ImageItem readFile(File file) {
		String fileExt = "";
		String fileName = file.getAbsolutePath();
		int extIndex = fileName.lastIndexOf('.');
		if (extIndex > 0) {
			fileExt = fileName.substring(extIndex+1);
		}
		
		if(!file.exists() || !(fileExt.equals("jpg")||fileExt.equals("jpeg"))){
			return null;
		}
		
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
			ImageReadingThreading thread;

			ExecutorService exec = Executors.newFixedThreadPool(nThreads);
			for (int i = 0; i < nThreads; i++) {
				end = start + interval;
				if (i == nThreads && remainder > 0) {
					end += remainder;
				}
				thread = new ImageReadingThreading(image, colorReferences, start, end);
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
	
}
