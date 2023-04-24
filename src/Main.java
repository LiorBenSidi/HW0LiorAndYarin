import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).

        /*Gets the size of the board.*/
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Enter the board size");
        String boardSize = scanner1.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));

        /*creates the boards.*/
        char[][] playersGameBoard = creatingBoards(RAW, COLUMN);
        char[][] playersGuessBoard = creatingBoards(RAW, COLUMN);
        char[][] computersGameBoard = creatingBoards(RAW, COLUMN);
        char[][] computersGuessBoard = creatingBoards(RAW, COLUMN);

        /* The number of battleships and their size.*/
        System.out.println("Enter the battleships size");
        String battleshipSizes = scanner1.nextLine();
        /*Sets an array that store the number of battleships and there size.*/
        int[][] arrOfShipsAndSize = setArrOfShipsAndSize(battleshipSizes);

        /*Sets the Location and orientation of user's battleships.*/
        char[][] updatedPlayersGameBoard = placingTheShips(arrOfShipsAndSize, scanner1, RAW, COLUMN, playersGameBoard);
        char[][] updatedComputersGameBoard = placingTheShips(arrOfShipsAndSize, scanner1, RAW, COLUMN, computersGameBoard);

    }

    /** Creates the boards.
     * We get the board size from the user,
     * Create 4 boards (2 for the player and 2 for the computer, when there are 2 board types: game and guessing),
     * and initialize the boards according to the requirements.
     * @param RAW Get the number of raws of each board.
     * @param COLUMN Get the number of columns of each board.
     * @return Returns default board.
     */
    public static char[][] creatingBoards(int RAW, int COLUMN) {
        char[][] board = new char[RAW][COLUMN];
        for (int i = 0; i < RAW; i++){
            for (int j = 0; j < COLUMN; j++){
                board[i][j] = '–';
            }
        }
        return board;
    }

    /**Sets an array that store the number of battleships and there size.
     * @param battleshipSizes Gets the number and size of the battleships.
     * @return Returns array that store the number of battleships and there size.
     */
    // TODO: to sort the array according to the size from small to big.
    public static int[][] setArrOfShipsAndSize(String battleshipSizes){
        String[] splitBattleshipSizesBySpace = battleshipSizes.split(" ");
        int[][] arrOfShipsAndSize = new int[splitBattleshipSizesBySpace.length][2];
        int j = 0;
        for (String s : splitBattleshipSizesBySpace) {
            arrOfShipsAndSize[j][0] = Integer.parseInt(String.valueOf(s.split("X")[0]));
            arrOfShipsAndSize[j][1] = Integer.parseInt(String.valueOf(s.split("X")[1]));
            j++;
        }
        return arrOfShipsAndSize;
    }

    /**Sets the Location and orientation of user's battleships.
     * @param arrOfShipsAndSize Gets array that store the number of battleships and there size.
     * @param scanner1 Used to gets input from the user/
     * @param RAW Number of raws of each board.
     * @param COLUMN Number of column of each board.
     * @param board Gets a game board.
     * @return Returns the updated game board in accordance to the validity of the location of the battleships.
     */
    public static char[][] placingTheShips(int[][] arrOfShipsAndSize, Scanner scanner1, int RAW, int COLUMN, char[][] board){
        boolean massageFlag = true;
        boolean validPosition = true;
        boolean alreadyTaken = true;
        for (int[] ints : arrOfShipsAndSize) {
            int numOfShipsOfTheSameSize = ints[0];
            int SizeOfShips = ints[1];
            do {
                if (massageFlag) {
                    printTheBoard(board, RAW, COLUMN);
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
                            if ((y - 1 < 0 || (board[x][y - 1] == '–'))
                                    && (y + SizeOfShips >= COLUMN || (board[x][y + SizeOfShips] == '–'))){
                                /**checking the upside of the ship.*/
                                if ((x - 1 >= 0) && validPosition) {
                                    if (y - 1 >= 0){
                                        if (y + SizeOfShips <= COLUMN) {
                                            for (int k = y - 1; (k < (y + SizeOfShips) && validPosition); k++) {
                                                if (board[x - 1][k] != '–') {
                                                    validPosition = false;

                                                }
                                            }
                                        } else {
                                            validPosition = false;
                                        }
                                    } else {
                                        for (int k = y; (k < (y + SizeOfShips) && validPosition); k++) {
                                            if (board[x - 1][k] != '–') {
                                                validPosition = false;

                                            }
                                        }
                                    }
                                }
                                /**checking the underside of the ship.*/
                                if ((x + 1 < RAW) && validPosition) {
                                    if (y > 0){
                                        for (int k = y - 1; k < (y + SizeOfShips) && validPosition; k++) {
                                            if (board[x + 1][k] != '–') {
                                                validPosition = false;
                                            }
                                        }
                                    }else{
                                        for (int k = y ; k < (y + SizeOfShips) && validPosition; k++) {
                                            if (x + 1 < RAW){
                                                if (board[x + 1][k] != '–') {
                                                    validPosition = false;
                                                }
                                            }else{
                                                if (board[x][k] != '–') {
                                                    validPosition = false;
                                                }
                                            }

                                        }
                                    }
                                }
                            } else {
                                validPosition = false;
                            }
                            if (validPosition){
                                for (int i = y; i < (y + SizeOfShips); i++) {
                                    if (board[x][i] == '–') {
                                        board[x][i] = '#';
                                        alreadyTaken = true;
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
                                board[i][y] = '#';
                            }
                        }
                    }
                    if (massageFlag){
                        numOfShipsOfTheSameSize--;
                    }
                }
            } while (numOfShipsOfTheSameSize > 0);

        }
        return board;
    }
    public static void printTheBoard(char[][] board, int RAW, int COLUMN){
        System.out.println("Your current game board:");
        String raw = String.valueOf(RAW);
        int rawLength = raw.length();
        for (int i = 0; i < rawLength; i++){
            System.out.print(" ");
        }
        for (int i = 0; i < COLUMN; i++){
            System.out.print(" " + i);
        }
        System.out.println();
        /*print the board without the number line.*/
        for (int i = 0; i < RAW; i++){
            String lenOfI = String.valueOf(i);
            int temp1 = lenOfI.length();
            for (int j = 0; j < COLUMN; j++){
                if ( j == 0 ){
                        for (int k = 0; k < rawLength - temp1; k++){
                            System.out.print(" ");
                        }
                    System.out.print(i);
                }
                System.out.print(" " + board[i][j]);
                if (j + 1 == COLUMN){
                    System.out.println();
                }
            }
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



