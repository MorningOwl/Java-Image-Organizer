import java.io.File;

public class ImageItem {
	String imageLocation, fileName;
	private int greyCounter, whiteCounter, redCounter, greenCounter, blueCounter, yellowCounter, magentaCounter, cyanCounter,
	blackCounter;
	private int height, width;


	public ImageItem(String imageLocation, String fileName, int height, int width, int greyCounter, int whiteCounter, int redCounter,
			int greenCounter, int blueCounter, int yellowCounter, int magentaCounter, int cyanCounter, int blackCounter) {
		super();
		this.imageLocation = imageLocation;
		this.fileName = fileName;
		this.greyCounter = greyCounter;
		this.whiteCounter = whiteCounter;
		this.redCounter = redCounter;
		this.greenCounter = greenCounter;
		this.blueCounter = blueCounter;
		this.yellowCounter = yellowCounter;
		this.magentaCounter = magentaCounter;
		this.cyanCounter = cyanCounter;
		this.blackCounter = blackCounter;
		this.height = height;
		this.width = width;
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
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}

	public void setGreyCounter(int greyCounter) {
		this.greyCounter = greyCounter;
	}

	public void setWhiteCounter(int whiteCounter) {
		this.whiteCounter = whiteCounter;
	}

	public void setRedCounter(int redCounter) {
		this.redCounter = redCounter;
	}

	public void setGreenCounter(int greenCounter) {
		this.greenCounter = greenCounter;
	}

	public void setBlueCounter(int blueCounter) {
		this.blueCounter = blueCounter;
	}

	public void setYellowCounter(int yellowCounter) {
		this.yellowCounter = yellowCounter;
	}

	public void setMagentaCounter(int magentaCounter) {
		this.magentaCounter = magentaCounter;
	}

	public void setCyanCounter(int cyanCounter) {
		this.cyanCounter = cyanCounter;
	}

	public void setBlackCounter(int blackCounter) {
		this.blackCounter = blackCounter;
	}
	
	public int compareColorWith(ImageItem item, ImageColors color){
		int val1, val2;
		
		switch (color) {
		case GREY:
			val1 = this.greyCounter;
			val2 = item.greyCounter;
			break;
		case WHITE:
			val1 = this.whiteCounter;
			val2 = item.whiteCounter;
			break;
		case RED:
			val1 = this.redCounter;
			val2 = item.redCounter;
			break;
		case GREEN:
			val1 = this.greenCounter;
			val2 = item.greenCounter;
			break;
		case BLUE:
			val1 = this.blueCounter;
			val2 = item.blueCounter;
			break;
		case YELLOW:
			val1 = this.yellowCounter;
			val2 = item.yellowCounter;
			break;
		case MAGENTA:
			val1 = this.magentaCounter;
			val2 = item.magentaCounter;
			break;
		case CYAN:
			val1 = this.cyanCounter;
			val2 = item.cyanCounter;
			break;
		default:
			val1 = this.blackCounter;
			val2 = item.blackCounter;
			break;
		}
		
		if(val1 < val2)
			return -1;
		else if(val1 == val2)
			return 0;
		else 
			return 1;		
	}
}
