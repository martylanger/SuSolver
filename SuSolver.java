/** mlanger: This is where the magic happens
*/

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

public class SuSolver{

	static boolean boxAlive = true;
	static boolean numAlive = true;
	static int blanks = 0;
	public static void main(String[] args) {

		Box[] boxes = Board.getBoard("hard2");

		//I manually recorded the puzzles in the Board.java file from
		//websudoku.com,
		//This program finishes the "easy" and "medium" boards, but not the
		//"hard" or "evil" ones.  The switch appears to be
		//working, so changing "medium" to "easy," "hard," or "evil," should
		//work to see how it handles those.
		//I had

		while(boxAlive==true){
			boxCheck(boxes);
			if(amIDone(boxes)) return;
			numCheck(boxes);
			System.out.println("End of the loop ");
		}

		// Box[] boxes = readyBoard();
		// Scanner in = new Scanner(System.in);
		// System.out.println("Which puzzle do you want to me to try?");
		// System.out.println("Do you feel like winning? (yes/no)");
		// String yn = in.nextLine();
		// if (yn.charAt(0) == 'y') {
		// 	Box[] boxes = Board.getBoard("easy");
		// 	anyToFix(boxes);
		// }
		// else {
		// 	System.out.println("Do you feel like entering a new puzzle? (y/n)");
		// 	String nuevo = in.nextLine();
		// 	if (nuevo.charAt(0) == 'y') {
		// 	System.out.println("Are you sure?\n\tIt's rather tedious, although I do have two"+
		// 			" different ways for you to do the tedius deed, and I'm always in"+
		// 			" favor of getting a new puzzle, not to mention tedium. In fact, tedius"+
		// 			" tasks are a particular specialty of mine. y/n");in:spam
		// 	}
		// 	else {
		// 		System.out.println("Are you sure?\n\t I do have two different ways for you to enter a  puzzle"+
		// 			" although, admittedly, both are rather tedius. I've always preferred tedius tasks, personally. y/n" );
		// 	}
		// 	String sunRise = in.nextLine();
		// 	if ( sunRise.charAt(0)=='y') readyBoard();
		// 	else
		// 	System.out.println("Are you on fire right now? (y/n)");
		// 	String giveUp = in.nextLine();
		// 	if (giveUp.charAt(0) == 'y')
		// 		{
		// 		System.out.println("You know what? I'M gonna choose, and I'M gonna win.");
		// 		Box[] boxes = Board.getBoard("easy");
		// 		anyToFix(boxes);
		// 	}
		// 	else {
		// 		System.out.println("I'm actually feeling rather sinister at the moment, and, if you don't mind, I'm going to go ahead and lose.");
		// 		Box[] boxes = Board.getBoard("medium");
		// 		anyToFix(boxes);
		// 	}
		// }
	 // }

	}

	public static Box[] readyBoard() {
		final double X = -10.0;
		//Start off by making the 9x9 board.
		//Even though there are only 81 Boxes in the puzzle,
		//including the extra junk/filler boxes (the else below) makes
		//the index in the array and the address (row*10+column)
		//for each puzzle-Box match.

		//In my subsequent attempts to improve on this program, I mostly
		//switched over to 81-Box arrays. It turns out that %10 and /10 just
		//aren't that different from /9 and %9.

		Box[] boxes = new Box[81]; // make the array

		for(int i = 0; i <81; i++) { // fill 'er up

				boxes[i] = new Box(i);
		}

		//Inputting the given boxes
		Scanner in = new Scanner(System.in);
		System.out.println("Please refer to a box by its row and column");
		System.out.println("in the form of a 2-digit number, between 11");
		System.out.println("and 99, where the tens' digit is the row");
		System.out.println("number and the ones' digit is the column number.\n");
		System.out.println("Enter the location of the first number given.");
		int rc = in.nextInt(); //(rc for row/column)
		int ricky = 9*((rc/10)-1) + (rc%10)-1;
		Box.setProduct(boxes, ricky, X);
		boardNow(boxes);

		System.out.println("What number is it?");
		int digit = in.nextInt();
		Box.setProduct(boxes, ricky, Box.DIVISOR[digit]);
		boardNow(boxes); //done the first one

		boolean ready1 = false; //getting the rest of them
		while(ready1 == false) {
			System.out.println("Where is the next number given?");
			System.out.println("(If there are no numbers left, enter 100)");
			rc = in.nextInt();
			if (rc!=100) {
				ricky = 9*((rc/10)-1) + (rc%10)-1;
				Box.setProduct(boxes,ricky, X);
				boardNow(boxes);
				System.out.println("What number is it?");
				digit = in.nextInt();
				Box.setProduct(boxes, ricky, Box.DIVISOR[digit]);
				boardNow(boxes);
			}
			else {
				ready1 = true;
			}
		} //the loop is closed, the puzzle is ready to solve
		return boxes;
	}


	//MORE METHODS

	// Find any boxes that have been solved but haven't fixed the family
	public static void boxCheck(Box[] bobby){
		Scanner in = new Scanner(System.in);
		boolean goAroundAgain;
		int  loops = 1;
		do {
			goAroundAgain = false;
			for( Box bob : bobby ) {
				if(Arrays.binarySearch(Box.DIVISOR, Box.getProduct(bob))>=0){
					fix(bobby, bob);
					loops--;
					goAroundAgain = true;
					if(amIDone(bobby)) return;
					System.out.println(Box.text(bob));
					if(loops <= 0){
						System.out.println("how many rounds?");
						loops = in.nextInt();
					}
				}
			}
		} while(goAroundAgain == true);
		boxAlive = false;
		System.out.println("exiting the while loop");
	}


	//The fix method is the machine

	 // Box Alice's solution has just been determined.
	 // (The answer is k and her product is now a prime number - the kth prime.)
	public static void fix(Box[] bobby, Box alice){
		int i = -1;
		for(Box bob : bobby){

			 //alice.isFamily(boxes[bob])
				// If Alice is in the same row, column, or square as
				// Box Bobby, then they cannot have the same solution.

			 // boxes[bob].product % alice.product == 0
				// If Bobby's product is a multiple of Alice's (prime) product,
				// this indicates that Alice's solution was hitherto considered
				// a possible solution for Bobby.
			 i++; if(amIDone(bobby)) return;
			if(isFamily(bob, alice) && Box.getProduct(bob) % Box.getProduct(alice) == 0){
		/*	 // The fixer will divide Bobby's product by Alice's, thus removing Alice's
			 // solution from the list of possible solutions for Bobby.
		/**/	Box.setProduct(bobby, i, Box.getProduct(bob) / Box.getProduct(alice));
			System.out.println(Box.getProduct(bob));
			}
		}

		 // Set the product to negative to show the fix has been done

		Box.setProduct(bobby, Box.getAddress(alice), -Box.getProduct(alice));
		boardNow(bobby);
		numAlive = true;
		boxAlive = true;
	}

	// isFamily
	 // Are Box b and Box c related? (i.e. same row, column, or square)
	 // (and make sure they aren't the same box)
	public static boolean isFamily(Box bob, Box alice){

		boolean rMate = Box.getRow(bob)==Box.getRow(alice);
		boolean cMate = Box.getColumn(bob)==Box.getColumn(alice);
		boolean sMate = Box.getSquare(bob)==Box.getSquare(alice);

		return (rMate || cMate || sMate) && !(rMate && cMate);
	 }

	// Method boardNow prints the board in its current state
	public static void boardNow(Box[] bobby){

		//Make a dumb little for loop to print the top line
		System.out.print("\t  ");
		for(int i = 1; i<=8; i++) {
			System.out.print(" "+i);
		}
		System.out.println(" 9");

		//Put together each line then print it
		int bRow = 1;
		String bLine = bRow+" |";
		for(int index = 0; index<81; index++) {
			bLine = bLine + bobby[index];
			if(index%9==8) { // 1  |+boxes[11]+boxes[12]....
					//after all nine columns, print the line and start
				System.out.println("\t"+bLine); //building the next one.
				bRow++;
				bLine = bRow+" |";
			}
		}
	}

	// Am I done?
	public static boolean amIDone(Box[] bobby){
		blanks = 0; //blanks are unsolved boxes
		for ( Box bob : bobby ) {
			if(Box.getProduct(bob)>0){
				if( !absPrime( Box.getProduct(bob) ))
					blanks++;
			}
		}
		if(blanks==0) {
			boardNow(bobby);
			System.out.println("Congratulations somebody!");
		}
		// // else {
		// // 	System.out.println("not there yet.");
		// }
		return blanks==0;
	}

	public static boolean numCheck(Box[] bobby){
		Scanner in = new Scanner(System.in);
		int  loops = 1;


		Box [][] row = new Box[9][9];	//	For comparing values from multiple elements across multiple dimensions,
		Box [][] col = new Box[9][9];		//	a merely searchable index would've been a very blunt tool.
		Box [][] sqr = new Box[9][9];		//	The fields array should let me run through everything with less clutter.
		Box [][][] fields = {row, col, sqr};	//	row, col, and sqr are all arrays of 9 puzzle groups plus a junk group,
							//	and all of the boxes these arrays refer to are defined already in
		for (int z = 0; z<3; z++){			//  <==	the boxes array (which bobby stands in for).
			for(int x=0;x<9; x++){
				for(int y=0;y<9;y++){
					fields [z][x][y] = bobby[9*x+y];
				}		//	As it stands, this is not correct because 9*x+y doesn't handle the squares correctly
			}			// 	(who knows whether it will work or not, though).
		}

		 /*		inSquare
		 for (int z = 0; z<2; z++)
			{for(int x=0;x<10; x++){
				for(int y=0;y<10;y++){
					fields [z][x][y] = bobby[10*x+y];
				}
			}
		 }
							else if( z==2 ) fields[]
		 3*((row - 1) / 3) + 1 + (column -1) / 3
		 xy is in square ^
*/
		boolean numAlive = false;
		int solo;
		boolean solved;
		double bubba;
		int index = 0;
		Box alice = new Box();
		do{
			numAlive = false;
			for (int digit=0; digit<=8; digit++) {//	For each digit
				for ( Box[][] field : fields) {	//	For each of the 3 exclusionary fields
					index = -1;
					for ( Box [] group : field) {	//	For each of the 9 groups (9 rows, 9 columns, 9 squares)
						solo = 0;
						solved = false;
						for ( Box bob : group) {		//	For each box in the group
							bubba = Box.getProduct(bob);
							index++;
							if(amIDone(bobby) )return true;
							if(bubba==-Box.DIVISOR[digit]) {
								solved = true; 		//	if it's a negative prime, move on to the next group
							}
							if(bubba==0 || solved==true || index<0) {
								continue; 		//	if it's junk, skip it
							}
							else if(bubba==Box.DIVISOR[digit]) {
								fix(bobby, bob);  	 //	if it's a positive prime, fix it (same as boxCheck)
								loops--;
								if(loops <= 0){
									System.out.println("how many rounds?");
									loops = in.nextInt();
									solved = true;
								}
								continue;
							}
							else if(bubba%Box.DIVISOR[digit]==0) {
								solo++;  		//	If product is a proper multiple of D[digit]
								alice = bobby[index];  //	Then count it
							}				//	and note which it was.
							boardNow(bobby);
						}
						if(solo == 1) {	// 	If there's only one hit in the group, then that's the answer for the box
							Box.setProduct ( bobby , Box.getAddress(alice) , Box.DIVISOR[digit] );
							fix(bobby, alice);
						}
					}
				}
			}
		} while(numAlive);
		return false;
	}

	public static boolean isPrime(int n){
		int d;
		for(d=2; (n%d) !=0; d++);
			return d==n;

		// for (int div= 2; n%div != 0; div++)
		//	return div==n ;
	}

	public static boolean absPrime(double n){
		double d; n=Math.abs(n);
		for(d=2; (n%d) !=0; d++);
			return d==n;
	}

}
