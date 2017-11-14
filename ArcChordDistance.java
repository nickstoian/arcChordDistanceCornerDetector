//Nicolas Stoian
//CS780 Image Processing
//Project 9.2 - Maximum Arc-Chord Distance Corner Detector

//This program needs 5 command line arguments
//args[0] "input1" for text file representing the boundary points of an object in an image
//args[1] "input2" for kChord, the length of of the arc-chord, in the arc-chord distance computation
//args[2] "output1" for the result of the maximum arc-chord detector, as a text file.
//args[3] "output2" for pretty print (displaying) the result of args[2] as in an image.
//args[4] "output3" for all debugging output

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ArcChordDistance {
	protected BoundaryPt[] boundPtAry;
	private double[] chordAry;
	private int kChord;
	protected int numPts;
	private int p1;
	private int p2;

	public static void main(String args[]){
		try{
			Scanner inFile = new Scanner(new FileReader(args[0]));
			int numRows = inFile.nextInt();
			int numCols = inFile.nextInt();
			int minVal = inFile.nextInt();
			int maxVal = inFile.nextInt();
			int label = inFile.nextInt();
			int numPts = inFile.nextInt();
			int kChord = Integer.parseInt(args[1]);
			PrintWriter outFile3 = new PrintWriter(new FileWriter(args[4]));
			ArcChordDistance imageArcChord = new ArcChordDistance(inFile, numPts, kChord, outFile3);
			outFile3.println();
			outFile3.println();
			imageArcChord.printBoundPtAry(outFile3);
			imageArcChord.computeLocalMaxima();
			imageArcChord.isCorner();
			outFile3.close();
			PrintWriter outFile1 = new PrintWriter(new FileWriter(args[2]));
			imageArcChord.printTextFile(outFile1, numRows, numCols, minVal, maxVal, label);
			outFile1.close();
			PrintWriter outFile2 = new PrintWriter(new FileWriter(args[3]));
			Image image = new Image(imageArcChord, numRows, numCols, minVal, maxVal);
			image.prettyPrint(outFile2);
			outFile2.close();
		}
		catch(NoSuchElementException e){
			System.err.println("Error in input file format, check the input file and try again.");
            return;
		}
		catch(FileNotFoundException e){
			System.err.println("File not found exception, check arguements and try again.");
            return;
		}
		catch(IOException e){
			System.err.println("IO exception, check arguements and try again.");
            return;
		}
	}

	public ArcChordDistance(){
		boundPtAry = null;
		chordAry = null;
		kChord = 0;
		numPts = 0;
		p1 = 0;
		p2 = 0;
	}

	public ArcChordDistance(Scanner inFile, int numPts, int kChord, PrintWriter outFile) throws IOException{
		this.kChord = kChord;
		this.numPts = numPts;
		p1 = kChord;
		p2 = 0;
		int index = 0;
		boundPtAry = new BoundaryPt[numPts];
		chordAry = new double[kChord];
		for(int i = 0; i < kChord; i++){
			chordAry[i] = 0.0;
	    }
		int x;
	    int y;
	    while(inFile.hasNext()){
	    	x = inFile.nextInt();
	    	y = inFile.nextInt();
	    	loadData(x, y, index);
	        index++;
	    }
	    if (index != numPts){
	        throw new IOException();
	    }
	    do{
	    	index = 0;
		    int currPt = p2 + 1;
		    while(index < kChord){
		    	double distance = computeDistance(currPt);
			    chordAry[index] = distance;
			    index++;
			    currPt = (currPt + 1) % numPts;
		    }
		    printChordAry(outFile);
		    outFile.println();
		    int maxIndex = findMaxDist();
		    int whichIndex = (p2 + maxIndex + 1) % numPts;
		    if(chordAry[maxIndex] != 0.0){
		    	boundPtAry[whichIndex].maxCount++;
		    }
		    if(boundPtAry[whichIndex].maxDistance < chordAry[maxIndex]){
		    	boundPtAry[whichIndex].maxDistance = chordAry[maxIndex];
		    }
		    for(int i = p2 + 1; i <= p2 + kChord; i++){
		    	outFile.format("%-5s%-5s%-5s%-5s%-17s", i % numPts, boundPtAry[i % numPts].x, boundPtAry[i % numPts].y,
		    				   boundPtAry[i % numPts].maxCount, boundPtAry[i % numPts].maxDistance);
				outFile.println();
		    }
		    outFile.println();
		    p1 = (p1 + 1) % numPts;
		    p2 = (p2 + 1) % numPts;
	    }
	    while(p1 != (kChord / 2));
	}

	public void loadData(int x, int y, int index){
	    boundPtAry[index] = new BoundaryPt(x, y);
	}

	public double computeDistance(int currPt){
		double a = boundPtAry[p1].y - boundPtAry[p2].y;
		double b = boundPtAry[p2].x - boundPtAry[p1].x;
		double c = (boundPtAry[p1].x * boundPtAry[p2].y) - (boundPtAry[p2].x * boundPtAry[p1].y);
		return Math.abs((a * boundPtAry[currPt].x) + (b * boundPtAry[currPt].y) + c) / Math.sqrt((a * a) + (b * b));
	}

	public void printChordAry(PrintWriter outFile){
		for(int i = 0; i < kChord; i++){
			outFile.format("%.6f", chordAry[i]);
			outFile.print(" ");
		}
		outFile.println();
	}

	public int findMaxDist(){
		double maxDist = 0.0;
		int maxDistIndex = 0;
		for(int i = 0; i < kChord; i++){
			if(chordAry[i] > maxDist){
				maxDist = chordAry[i];
				maxDistIndex = i;
			}
		}
		return maxDistIndex;
	}

	public void printBoundPtAry(PrintWriter outFile){
		outFile.format("%-7s%-4s%-4s%-10s%-17s", "Index", "X", "Y", "maxCount", "maxDistance");
		outFile.println();
		for(int i = 0; i < numPts; i++){
			outFile.format("%-7s%-4s%-4s%-10s%-17s", i, boundPtAry[i].x, boundPtAry[i].y, boundPtAry[i].maxCount, boundPtAry[i].maxDistance);
			outFile.println();
		}
	}

	public void printTextFile(PrintWriter outFile, int numRows, int numCols, int minVal, int maxVal, int label){
	    outFile.println(numRows + " " + numCols + " " + minVal + " " + maxVal);
	    outFile.println(label);
	    outFile.println(numPts);
	    for(int i = 0; i < numPts; i++){
	    	outFile.println(boundPtAry[i].x + " " + boundPtAry[i].y + " " + boundPtAry[i].corner);
	    }
	}

	public void computeLocalMaxima(){
	    for(int i = 0; i < numPts; i++){
	        if(boundPtAry[i].maxCount >= boundPtAry[((i - 2) % numPts + numPts) % numPts].maxCount &&
	           boundPtAry[i].maxCount >= boundPtAry[((i - 1) % numPts + numPts) % numPts].maxCount &&
	           boundPtAry[i].maxCount >= boundPtAry[(i + 1) % numPts].maxCount &&
	           boundPtAry[i].maxCount >= boundPtAry[(i + 2) % numPts].maxCount &&
	           boundPtAry[i].maxCount != 0){
	            boundPtAry[i].localMax = 1;
	        }
	    }
	}

	public void isCorner(){
	    for(int i = 0; i < numPts; i++){
	        if(boundPtAry[i].localMax == 1){
	            if(boundPtAry[((i - 1) % numPts + numPts) % numPts].localMax == 1 &&
	               boundPtAry[(i + 1) % numPts].localMax == 1){
	                boundPtAry[i].corner = 1;
	            }
	            else{
	                boundPtAry[i].corner = 9;
	            }
	        }
	        else{
	            boundPtAry[i].corner = 1;
	        }
	    }
	}
}
