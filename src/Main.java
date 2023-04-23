import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).
        /** creating the boards.
         * We get the board size from the user,
         * Create 4 boards (2 for the player and 2 for the computer, when there are 2 board types: game and guessing),
         * and initialize the boards according to the requirements. */
        /**<p>*/Scanner scanner1 = new Scanner(System.in);
        System.out.println("Enter the board size");
        // TODO: לשים לב לקלט של מספר לא חד-ספרתי, להשתמש בsplit
        String boardSize = scanner1.nextLine();
//        final int RAW = Integer.parseInt(String.valueOf(boardSize.charAt(0)));
//        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.charAt(2)));
//        String[] temp = boardSize.split( "X");
        final int RAW = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));

        int[][] playersGameBoard = new int[RAW][COLUMN];
        int[][] playersGuessBoard = new int[RAW][COLUMN];
        int[][] computersGameBoard = new int[RAW][COLUMN];
        int[][] computersGuessBoard = new int[RAW][COLUMN];
        for (int i = 0; i < RAW; i++){
            for (int j = 0; j < COLUMN; j++){
                playersGameBoard[i][j] = '–';
                playersGuessBoard[i][j] = '–';
                computersGameBoard[i][j] = '–';
                computersGuessBoard[i][j] = '–';
            }
        }
        /*
        playersGameBoard[0][0] = ' ';
        playersGuessBoard[0][0] = ' ';
        computersGameBoard[0][0] = ' ';
        computersGuessBoard[0][0] = ' ';
        for (int i = 1; i < COLUMN + 1; i++){
            playersGameBoard[0][i] = i - 1;
            playersGuessBoard[0][i] = i - 1;
            computersGameBoard[0][i] = i - 1;
            computersGuessBoard[0][i] = i - 1;
        }
        for (int i = 1; i < RAW + 1; i++){
            playersGameBoard[i][0] = i - 1;
            playersGuessBoard[i][0] = i - 1;
            computersGameBoard[i][0] = i - 1;
            computersGuessBoard[i][0] = i - 1;
        }
        */

        /** The number of battleships and their size.*/
        /**<p>*/System.out.println("Enter the battleships size");
        String battleshipSizes = scanner1.nextLine();
//        int numberOfBattleships = ((battleshipSizes.length()) / 4) + 1;
        String[] temp2 = battleshipSizes.split(" ");
        int[][] arr = new int[temp2.length][2];
        int j = 0;
        for (int i = 0; i < temp2.length; i++) {
            // TODO: לשים לב לקלט של מספר לא חד-ספרתי
            arr[j][0] = Integer.parseInt(String.valueOf(temp2[i].split("X")[0]));
            arr[j][1] = Integer.parseInt(String.valueOf(temp2[i].split("X")[1]));
            j++;
        }

        /**Location of user submarines.*/
        /**<p>*/boolean massageFlag = true;
        boolean validPosition = true;
        boolean alreadyTaken = true;
        for (int[] ints : arr) {
            int numOfShipsOfTheSameSize = ints[0];
            int SizeOfShips = ints[1];
            do {
                if (massageFlag) {
                    System.out.println("Enter location and orientation for battleship of size " + SizeOfShips);
                }
                String xyo = scanner1.nextLine();
                String[] temp3 = xyo.split(", ");
                int x = Integer.parseInt(String.valueOf(temp3[0]));
                int y = Integer.parseInt(String.valueOf(temp3[1]));
                int o = Integer.parseInt(String.valueOf(temp3[2]));
                if (x > COLUMN || y > RAW) {
                    System.err.println("Illegal tile, try again!");
                    massageFlag = false;
                } else if (o != 0 && o != 1) {
                    System.err.println("Illegal orientation, try again!");
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    if (o == 0){
                        validPosition = true;
                        if (y + SizeOfShips > COLUMN){
                            massageFlag = false;
                            System.err.println("Battleship exceeds the boundaries of the board, try again!");
                        }else {
                            /**checking the left and the right sides of the ship.*/
                            // TODO: separate the following condition.
                            if (((y - 2 > 0) && (playersGameBoard[x][y - 2] == '–') || (y - 2 < 0))
                                    && (y + SizeOfShips >= COLUMN || (playersGameBoard[x][y + SizeOfShips] == '–'))){
                                /**checking the upside of the ship.*/
                                if ((x - 1 >= 0) && validPosition) {
                                    if (y - 1 >= 0){
                                        if (y + SizeOfShips < COLUMN) {
                                            for (int k = y - 1; (k < (y + SizeOfShips) && validPosition); k++) {
                                                if (playersGameBoard[x - 1][k] != '–') {
                                                    validPosition = false;

                                                }
                                            }
                                        } else {
                                            validPosition = false;
                                        }
                                    } else {
                                        for (int k = y; (k < (y + SizeOfShips) && validPosition); k++) {
                                            if (playersGameBoard[x - 1][k] != '–') {
                                                validPosition = false;

                                            }
                                        }
                                    }
                                }
                                /**checking the underside of the ship.*/
                                if ((x > y + SizeOfShips) && validPosition) {
                                    for (int k = y - 2; k < (y + SizeOfShips) && validPosition; k++) {
                                        if (playersGameBoard[x][k] == '–') {
                                            validPosition = false;

                                        }
                                    }

                                }
                            } else {
                                validPosition = false;
                            }
                            if (validPosition){
                                for (int i = y; i < (y + SizeOfShips); i++) {
                                    if (playersGameBoard[x][i] == '–') {
                                        playersGameBoard[x][i] = '#';
                                    }else {
                                        alreadyTaken = false;
                                        break;
                                    }
                                }
                                if (!alreadyTaken){
                                    System.err.println("Battleship overlaps another battleship, try again!");
                                    massageFlag = false;
                                }
                            }else {
                                System.err.println("Adjacent battleship detected, try again!");
                                massageFlag = false;
                            }
                        }
                    }
                    if (o == 1){
                        if (x + SizeOfShips - 1 > RAW){
                            massageFlag = false;
                            System.err.println("Battleship exceeds the boundaries of the board, try again!");
                        }else {
                            for (int i = x; i < (x + SizeOfShips); i++){
                                playersGameBoard[i][y] = '#';
                            }
                        }
                    }
                    if (massageFlag){
                        numOfShipsOfTheSameSize--;
                    }
                }
            } while (numOfShipsOfTheSameSize > 0);

        }
    }



    public static void main(String[] args) throws IOException {
        String path = args[0];
        scanner = new Scanner(new File(path));
        int numberOfGames = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Total of " + numberOfGames + " games.");

        for (int i = 1; i <= numberOfGames; i++) {
            scanner.nextLine();
            int seed = scanner.nextInt();
            rnd = new Random(seed);
            scanner.nextLine();
            System.out.println("Game number " + i + " starts.");
            battleshipGame();
            System.out.println("Game number " + i + " is over.");
            System.out.println("------------------------------------------------------------");
        }
        System.out.println("All games are over.");
    }
}



