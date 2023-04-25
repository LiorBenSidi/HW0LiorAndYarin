import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).

        /*Gets the size of the board.*/
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the board size");
        String boardSize = s.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));


        /*creates the boards.*/
        char[][] playersGameBoard = creatingBoards(RAW, COLUMN);
        char[][] playersGuessBoard = creatingBoards(RAW, COLUMN);
        char[][] pcGameBoard = creatingBoards(RAW, COLUMN);
        char[][] computersGuessBoard = creatingBoards(RAW, COLUMN);

        /* The number of battleships and their size.*/
        System.out.println("Enter the battleships size");
        String battleshipSizes = s.nextLine();
        /*Sets an array that store the number of battleships and there size.*/
        int[][] arrShipsSize = setArrOfShipsAndSize(battleshipSizes);

        /*Sets the Location and orientation of user's battleships.*/
        boolean isUserBoard = true;
        char[][] updatedPlayersGameBoard = checkingAndPlacingTheShips(arrShipsSize, s, RAW,
                COLUMN, playersGameBoard, isUserBoard);
        isUserBoard = false;
        char[][] updatedComputersGameBoard = checkingAndPlacingTheShips(arrShipsSize, s, RAW,
                COLUMN, pcGameBoard, isUserBoard);

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
     * @param arrShipsSize Gets array that store the number of battleships and there size.
     * @param s Used to gets input from the user/
     * @param RAW Gets umber of raws of each board.
     * @param COLUMN Gets Number of column of each board.
     * @param board Gets a game board.
     * @return Returns the updated game board in accordance to the validity of the location of the battleships.
     */
    public static char[][] checkingAndPlacingTheShips(int[][] arrShipsSize, Scanner s, int RAW,
                                                      int COLUMN, char[][] board, boolean isUserBoard){
        int x, y, o;
        boolean massageFlag = true;
        for (int[] ints : arrShipsSize) {
            int numOfShipsOfTheSameSize = ints[0];
            int SizeOfShips = ints[1];
            do {
                if (massageFlag) {
                    /*Prints the current game board each time.*/
                    printTheBoard(board, RAW, COLUMN);
                    System.out.println("Enter location and orientation for battleship of size " + SizeOfShips);
                }
                if(isUserBoard){
                    String xyo = s.nextLine();
                    String[] temp3 = xyo.split(", ");
                    x = Integer.parseInt(String.valueOf(temp3[0]));
                    y = Integer.parseInt(String.valueOf(temp3[1]));
                    o = Integer.parseInt(String.valueOf(temp3[2]));
                } else {
                    x = rnd.nextInt(RAW);
                    y = rnd.nextInt(COLUMN);
                    o = rnd.nextInt(2);
                }
                /*Checks if tile is valid*/
                if (o != 0 && o != 1) {
                    if (isUserBoard){
                        System.err.println("Illegal orientation, try again!");
                    }
                    massageFlag = false;
                }else if (x > COLUMN || y > RAW) {
                    if (isUserBoard){
                        System.err.println("Illegal tile, try again!");
                    }
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    if (o == 0){
                        /*Checking if valid position.*/
                        massageFlag = checkHorizontalShip(board, x, y, o, RAW, COLUMN, SizeOfShips, isUserBoard);
                    }
                    if (o == 1){
                        massageFlag = checkVerticalShip(board, x, y, o, RAW, COLUMN, SizeOfShips, isUserBoard);
                    }
                    if (massageFlag){
                        board = locatesTheShip(board, x, y, o, SizeOfShips);
                        numOfShipsOfTheSameSize--;
                    }
                }
            } while (numOfShipsOfTheSameSize > 0);

        }
        printTheBoard(board, RAW, COLUMN);
        return board;
    }

    /**Prints the current game board each time.
     * Prints the row of numbers and the space before them.
     * Prints the board without the number row.
     * @param board Gets the game board before and after the input.
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     */
    public static void printTheBoard(char[][] board, int RAW, int COLUMN){
        System.out.println("Your current game board:");

        /*Prints the row of numbers and the space before them.*/
        String raw = String.valueOf(RAW);
        int rawLength = raw.length();
        for (int i = 0; i < rawLength; i++){
            System.out.print(" ");
        }
        for (int i = 0; i < COLUMN; i++){
            System.out.print(" " + i);
        }
        System.out.println();

        /*Prints the board without the number row.*/
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

    public static boolean checkHorizontalShip(char[][] board, int x, int y, int o, int RAW,
                                              int COLUMN, int SizeShips, boolean isUserBoard) {
        if (y + SizeShips > COLUMN) {
            if (isUserBoard){
                System.err.println("Battleship exceeds the boundaries of the board, try again!");
            }
            return false;
        }
        for (int i = y; i < y + SizeShips; i++) {
            if (board[x][i] == '#') {
                if (isUserBoard){
                    System.err.println("Battleship overlaps another battleship, try again!");
                }
                return false;
            }
        }
        /*checking the left and the right sides of the ship. */
        if (!((y - 1 < 0 || (board[x][y - 1] == '–'))
                && (y + SizeShips >= COLUMN || (board[x][y + SizeShips] == '–')))) {
            if (isUserBoard){
                System.err.println("Adjacent battleship detected, try again!");
            }
            return false;
        }
        /*checking the upside of the ship. */
        if (x - 1 >= 0) {
            if (y - 1 >= 0) {
                if (y + SizeShips <= COLUMN) {
                    for (int k = y - 1; (k <= (y + SizeShips)); k++) {
                        if (board[x - 1][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else {
                    if (isUserBoard){
                        System.err.println("Adjacent battleship detected, try again!");
                    }
                    return false;
                }
            } else {
                for (int k = y; (k <= (y + SizeShips)); k++) {
                    if (board[x - 1][k] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }
        }
        /*checking the underside of the ship.*/
        if (x + 1 < RAW) {
            if (y > 0) {
                for (int k = y - 1; k <= (y + SizeShips); k++) {
                    if (board[x + 1][k] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }else {
                for (int k = y; k <= (y + SizeShips); k++) {
                    if (x + 1 < RAW) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }else{
                        if (board[x][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            }
        }else {
            return true;
        }
        return true;
    }

    public static boolean checkVerticalShip(char[][] board, int x, int y, int o, int RAW,
                                            int COLUMN, int SizeShips, boolean isUserBoard) {
        if (x + SizeShips > RAW) {
            if (isUserBoard){
                System.err.println("Battleship exceeds the boundaries of the board, try again!");
            }
            return false;
        }
        for (int i = x; i < x + SizeShips; i++) {
            if (board[i][y] == '#') {
                if (isUserBoard){
                    System.err.println("Battleship overlaps another battleship, try again!");
                }
                return false;
            }
        }
        /*checking the upside and the underside sides of the ship. */
        if (!((x - 1 < 0 || (board[x - 1][y] == '–'))
                && (x + SizeShips >= RAW || (board[x + SizeShips][y] == '–')))) {
            if (isUserBoard){
                System.err.println("Adjacent battleship detected, try again!");
            }
            return false;
        }
        /*checking the left of the ship. */
        if (y - 1 >= 0) {
            if (x - 1 >= 0) {
                if (x + SizeShips <= RAW) {
                    for (int k = x - 1; (k <= (x + SizeShips)); k++) {
                        if (board[k][y - 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else {
                    if (isUserBoard){
                        System.err.println("Adjacent battleship detected, try again!");
                    }
                    return false;
                }
            } else {
                for (int k = x; (k < (x + SizeShips)); k++) {
                    if (board[k][y - 1] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }
        }
        /*checking the rightSide of the ship.*/
        if (y + 1 < RAW) {
            if (x - 1 >= 0) {
                for (int k = x - 1; k <= (x + SizeShips); k++) {
                    if (board[k][y + 1] != '–') {
                        System.err.println("Adjacent battleship detected, try again!");
                        return false;
                    }
                }
            } else {
                for (int k = x ; k <= (x + SizeShips); k++) {
                    if (x + 1 < RAW) {
                        if (board[k][y + 1] != '–') {
                            System.err.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            }
        }else {
            return true;
        }
        return true;
    }


    /**Inserts the battleship coordinators into the game board
     * Check the orientation
     * @param board Gets the game board before and after the input.
     * @param x Gets the horizontal axis coordinate
     * @param y Gets the vertical axis coordinate
     * @param o Gets the orientation
     * @param SizeShips Gets the size of each battleship
     * @return Returns the game board.
     */
    public static char[][] locatesTheShip(char[][] board, int x, int y, int o, int SizeShips) {
        if (o == 0) {
            for (int i = y; i < (y + SizeShips); i++) {
                board[x][i] = '#';
            }
        } else {
            for (int i = x; i < (x + SizeShips); i++) {
                board[i][y] = '#';
            }

        }
        return board;
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