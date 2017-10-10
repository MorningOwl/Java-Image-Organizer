package ImageUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

public class ImageReadingThreading implements Runnable {

	private Thread t;
	private int greyCounter, whiteCounter, redCounter, greenCounter, blueCounter, yellowCounter, magentaCounter,
			cyanCounter, blackCounter;

	BufferedImage image;
	Set<ImageColorKeys> colorReferences;

	int start;
	int end;
	String threadName;

	ImageReadingThreading(BufferedImage image, Set<ImageColorKeys> colorReferences, int start, int end) {
		this.image = image;
		this.colorReferences = colorReferences;
		this.start = start;
		this.end = end;
	}

	@Override
	public void run() {
		doWork();
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	private void doWork() {
		int red, blue, green;
		float[] c1 = new float[3];
		int h = image.getHeight();
		int pixel;
		ImageColors colorReference;

		for (int x = start; x < end; x++) {
			for (int y = 0; y < h; y++) {
				/*
				 * How it works: 11111111 00000000 11111111 00000000 alpha red
				 * green blue
				 * 
				 * First, the >> is a bitwise operation that shifts the bits in
				 * a given position. For example, alpha shifts the bits 24 steps
				 * resulting in 00000000 00000000 00000000 11111111. Next the &
				 * bitwise operator is a result of the following. Initially Java
				 * assigns the value to a 32-bit int (ff ff ff fe). Next the
				 * 0xff converts the int to a (00 00 00 ff) value meaning it
				 * masks all but the lowest 8 bits of number and take its value,
				 * 11111111 (255).
				 */
				pixel = image.getRGB(x, y);
				red = (pixel >> 16) & 0xff;
				green = (pixel >> 8) & 0xff;
				blue = (pixel) & 0xff;
				Color.RGBtoHSB(red, green, blue, c1);

				colorReference = getColorReference(c1);

				switch (colorReference) {
				case GREY:
					this.greyCounter++;
					break;
				case WHITE:
					this.whiteCounter++;
					break;
				case RED:
					this.redCounter++;
					break;
				case GREEN:
					this.greenCounter++;
					break;
				case BLUE:
					this.blueCounter++;
					break;
				case YELLOW:
					this.yellowCounter++;
					break;
				case MAGENTA:
					this.magentaCounter++;
					break;
				case CYAN:
					this.cyanCounter++;
					break;
				default:
					this.blackCounter++;
					break;
				}
			}
		}
	}

	private ImageColors getColorReference(float[] c1) {
		/*
		 * //https://en.wikipedia.org/wiki/Color_difference
		 */
		double currentLeastDistance = 255;
		ImageColors currentColor = null;
		double deltaH, deltaS, deltaG;
		double distance;

		for (ImageColorKeys c2 : colorReferences) {
			deltaH = c1[0] - c2.getH();
			deltaS = c1[1] - c2.getS();
			deltaG = c1[2] - c2.getG();

			distance = Math.sqrt(Math.pow(deltaH, 2) + Math.pow(deltaS, 2) + Math.pow(deltaG, 2));

			if (distance < currentLeastDistance) {
				currentColor = c2.getColor();
				currentLeastDistance = distance;
			}
		}
		return currentColor;
	}
	
	public int getGreyCounter() {
		return greyCounter;
	}

	public int getWhiteCounter() {
		return whiteCounter;
	}

	public int getRedCounter() {
		return redCounter;
	}

	public int getGreenCounter() {
		return greenCounter;
	}

	public int getBlueCounter() {
		return blueCounter;
	}

	public int getYellowCounter() {
		return yellowCounter;
	}

	public int getMagentaCounter() {
		return magentaCounter;
	}

	public int getCyanCounter() {
		return cyanCounter;
	}

	public int getBlackCounter() {
		return blackCounter;
	}
}
