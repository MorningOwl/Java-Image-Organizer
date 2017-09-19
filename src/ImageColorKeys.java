
public class ImageColorKeys {
	private float H, S, G;
	private float[] HSG;
	private ImageColors color;
	
	public ImageColorKeys(float h, float s, float g, ImageColors color) {
		super();
		H = h;
		S = s;
		G = g;		
		this.color = color;
		
		HSG = new float[3];
		HSG[0] = H;
		HSG[1] = S;
		HSG[2] = G;
	}
	
	public ImageColorKeys(float[] HSG, ImageColors color){
		if(HSG.length == 3){
			this.H = HSG[0];
			this.S = HSG[1];
			this.G = HSG[2];
			
			this.HSG = new float[3];
			this.HSG[0] = H;
			this.HSG[1] = S;
			this.HSG[2] = G;
			
			this.color = color;
		} else {
			this.H = 0;
			this.S = 0;
			this.G = 0;
			
			HSG = new float[3];
			this.HSG[0] = H;
			this.HSG[1] = S;
			this.HSG[2] = G;
			
			this.color = ImageColors.BLACK;
			
		}
	}

	public float getH() {
		return H;
	}

	public float getS() {
		return S;
	}

	public float getG() {
		return G;
	}

	public float[] getHSG() {
		return HSG;
	}

	public ImageColors getColor() {
		return color;
	}

	public void setH(float h) {
		H = h;
	}

	public void setS(float s) {
		S = s;
	}

	public void setG(float g) {
		G = g;
	}

	public void setHSG(float[] HSG) {
		if(HSG.length == 3){
			this.HSG[0] = HSG[0];
			this.HSG[1] = HSG[1];
			this.HSG[2] = HSG[2];
		}
	}

	public void setColor(ImageColors color) {
		this.color = color;
	}
}
