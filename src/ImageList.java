
public class ImageList {
	
	private int currentIndex;
	private ImageItem[] images;
	private int maxSize;
	private ImageColors sortedByColor;
	
	public ImageList(int maxSize){
		this.maxSize = maxSize;
		currentIndex = 0;
		images = new ImageItem[3];
	}
	
	/**
	 * Gets the max size of this collection
	 * @return
	 */
	public int getMaxSize(){
		return maxSize;
	}
	
	/**
	 * Adds a new image item in to list
	 * @param item
	 * @return Returns true if item was added, false if not
	 */
	public boolean addImageItem(ImageItem item){
		if(currentIndex < maxSize){
			images[currentIndex] = item;
			return true;
		} else {
			return false;
		}		
	}
	
	/**
	 * Returns the color that is sorted by this. 
	 * @return Returns a ImageColor if it is currently sorted by a color, null if not.
	 */
	public ImageColors getSortedByColor(){
		return sortedByColor;
	}
		
	/**
	 * Sorts the list. This method is a publicly accessed method that
	 * starts the iterative sorting method merge().
	 */
	public void sort(ImageColors colorToSort){
		merge(images);		
		sortedByColor = colorToSort;
	}
	
	/**
	 * Performs an interative merge sorting of this list.
	 * Method is private to protect the need for the array to be accessed publicly.
	 */
	private void merge(ImageItem[] unsorted){
		if(unsorted == null){
			return;
		}
		
		if(unsorted.length > 1){
			int middle = unsorted.length/2;
			//Split left
			ImageItem[] leftArray = new ImageItem[middle];
			for(int i = 0; i < middle; i++){
				leftArray[i] = unsorted[i];
			}
			//Split right
			ImageItem[] rightArray = new ImageItem[unsorted.length - middle];
			for(int i = 0; i < middle; i++){
				rightArray[i - middle] = unsorted[i];
			}
			
			//Iterate until size-able
			merge(leftArray);
			merge(rightArray);
			
			int i = 0, j = 0, k = 0;
			//Start sorting and merging
            while(i < leftArray.length && j < rightArray.length)
            {
                if(leftArray[i].compareColorWith(rightArray[i], sortedByColor) == -1)
                {
                	unsorted[k] = leftArray[i];
                    i++;
                }
                else
                {
                	unsorted[k] = rightArray[j];
                    j++;
                }
                k++;
            }
            //Retrieve the rest
            while(i < leftArray.length)
            {
            	unsorted[k] = leftArray[i];
                i++;
                k++;
            }
            while(j < rightArray.length)
            {
            	unsorted[k] = rightArray[j];
                j++;
                k++;
            }
		}
		
	}
	
	
	
}
