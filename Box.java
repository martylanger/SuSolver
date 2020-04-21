/** mlanger: A program to solve sudoku puzzles
*/

import java.util.Scanner;
import java.util.Arrays;
import java.util.Objects;

public class Box{

//The sudoku board is square-shaped and divided into a 3x3-square grid.
//Each smaller square is further subdivided into 9 boxes in a 3x3 grid of its own.
//Together, these boxes form a grid 9 rows tall and 9 columns wide, 81 boxes in all.
//The puzzle is solved by filling in the boxes such that each row, column,
//and 3x3-box square has all the digits 1-9.

//FIELDS

	//For convenience and flexibility,
	//*Boxes* can be identified by their *row* and *column* as separate fields...
	private int row;
	private int column;

	//...or by a single 2-digit integer with the row number in the tens' place
	//and the column number in the ones' place, here called the *address*.
	private int address;
	
	//Additionally, each Box belongs to one of the 3x3-Box *squares*.
	private int square;

	//This solver starts by assuming that any digit, 1-9, might turn out to be
	//the correct one for each box.
	//It proceeds by a process of elimination, making note whenever it
	//determines that a digit could not possibly be the solution for a given box. 
	//Each box has its remaining possibilities represented by its *sum*.
	private double product;

	private Box[] bobster;
	

//CONSTANTS
	
	// Debated between binary sums and prime products, the primes look simpler
	
	//Every box starts with its sum set to 511, the decimal form of the nine-digit
	//binary number, 111111111.
	//final short ANY = 2^(k^2)-1;

	final double  ANY = 223092870; // i.e. 2*3*5*7*11*13*17*19*23
	
	protected final static double[] DIVISOR = {2,3,5,7,11,13,17,19,23};

//CONSTRUCTORS
	
	public Box(Box [] bobby){
		bobster = bobby;

	}


	//Default - no arguments
	public Box(){
		row = 10;
		column = 10;
		address = 81;
		square = 10;
		product = 0;
	}

	//Given row and column
	public Box(int a, int p){
		if (p%10==0){
			row = a/9;
			column = a%9;
			address = a;
			square = inSquare();
			product = ANY;
		}
		// else if (c<=9){
		// 	row = r;
		// 	column = c;
		// 	address = 10*r+c;
		// 	square = inSquare();
		// 	product = ANY;
		// }
		else {
			row = a/9;
			column = a%9;
			address = a;
			square = inSquare();
			product = DIVISOR [p%9];
		}
	}

	//Given address
	public Box(int a){
		row = a/9;
		column = a%9;
		address = a;
		square = inSquare();
		product = ANY;
	}

	//Given row, column, and product
	public Box(int r, int c, long p){
		row = r;
		column = c;
		address = 9*r+c;
		square = inSquare();
		product = p;
	}

	//Given address and product
	public Box(int a, long p){
		row = a/9;
		column = a%9;
		address = a;
		square = inSquare();
		product = p;
	}


//METHODS

	//inSquare determines which square the box is in
	public int inSquare(){
		return 3*(address/27) + (address%9)/3;
	}
	

	// toString dictates how the box is printed
	// Note: This format is for presentation in the puzzle, not for general printing
	// Note: If the solution to a box bob is k, if bob has been solved, the product 
	// will be the kth prime. The fixer will multiply that by -1 when it processes it.
	public String toString(){
		if(product == -10) {
			return "X|"; //X marks the selected box in boardNow
		}	
		else if (product < 0){
			double pos = -product;
			return (Arrays.binarySearch(DIVISOR, pos)+1) + "|";
		}
		else if (Arrays.binarySearch(DIVISOR, product)>0){
			return (Arrays.binarySearch(DIVISOR, product)+1) + "|";
		}
		else
			return "_|";
	}


	//txt provides an alternative to toString, to call for ordinary printing
	public static String text(Box b){
		return "Box("+b.row+","+b.column+") "+b.product;
	}

	// getRow
	public static int getRow(Box b) {
		return b.row;
	}

	// getColumn
	public static int getColumn(Box b) {
		return b.column;
	}

	// getAddress
	public static int getAddress(Box b) {
		return b.address;
	}

	//get Square
	public static int getSquare(Box b) {
		return b.square;
	}

	//getProduct
	public static double getProduct(Box b) {
		return b.product;
	}

	// setAddress
	public  	 void openBox(Box[] baby, int a) {
		baby[a].row = a/10;
		baby[a].column = a%10;
		baby[a].address = a;
		baby[a].square = inSquare();
		baby[a].product = ANY;
	}

	//setProduct
	public static void setProduct(Box[] baby, int b, double p) {
		baby[b].product = p;
	}

	public static void setProduct(Box bob, double p){
		bob.product = p;
	}

	// //setBox
	// public  void setBox(Box [] baby, int a){
	// 		// row = baby[a].row;
	// 		// column = baby[a].column;
	// 		// address = baby[a].address;
	// 		// square = baby[a].square;
	// 		// product = baby[a].product;
	// 	Box = baby[a];
	// }
}