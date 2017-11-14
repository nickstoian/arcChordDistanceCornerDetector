//Nicolas Stoian
//CS780 Image Processing
//Project 9.2 - Maximum Arc-Chord Distance Corner Detector

import java.io.PrintWriter;

public class Image {
	private int[][] imgArray;
	private int numRows;
	private int numCols;
	private int minVal;
	private int maxVal;

	public Image(){
		imgArray = null;
		numRows = 0;
		numCols = 0;
		minVal = 0;
		maxVal = 0;
	}

	public Image(ArcChordDistance imageArcChord, int numRows, int numCols, int minVal, int maxVal){
	    this.numRows = numCols; // These are reversed in the input file for a true (x,y) coordinate image representation, x = cols, y = rows.
	    this.numCols = numRows; // I have reversed them here to get proper output but would recommend fixing the input file.
	    this.minVal = minVal;
	    this.maxVal = maxVal;
	    imgArray = new int[this.numRows][this.numCols];
		for(int row = 0; row < this.numRows; row++){
	        for(int col = 0; col < this.numCols; col++){
	            imgArray[row][col] = 0;
	        }
	    }
	    for(int row = 0; row < this.numRows; row++){
	        for(int col = 0; col < this.numCols; col++){
	        	imgArray[row][col] = 0;
	        }
	    }
	    for(int i = 0; i < imageArcChord.numPts; i++){
	    	imgArray[imageArcChord.boundPtAry[i].y][imageArcChord.boundPtAry[i].x] = imageArcChord.boundPtAry[i].corner;
	    }
	}

	public void prettyPrint(PrintWriter outFile){
	    //for(int row = numRows - 1; row >= 0; row--){
	    for(int row = 0; row < numRows; row++){
	        for(int col = 0; col < numCols; col++){
	            if(imgArray[row][col] <= 0){
	                outFile.print(" " + " ");
	            }
	            else{
	                outFile.print(imgArray[row][col] + " ");
	            }
	        }
	        outFile.println();
	    }
	}

	public void printImage(PrintWriter outFile){
		outFile.println(numRows + " " + numCols + " " + minVal + " " + maxVal);
		for(int row = 0; row < numRows; row++){
	        for(int col = 0; col < numCols; col++){
	            outFile.print(imgArray[row][col] + " ");
	        }
	        outFile.println();
	    }
	}
}
