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
        System.out.println("Enter the board size");
        String boardSize = scanner.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));

        /*creates the boards.*/
        char[][] userGameBoard = creatingBoards(RAW, COLUMN);
        char[][] userGuessBoard = creatingBoards(RAW, COLUMN);
        char[][] pcGameBoard = creatingBoards(RAW, COLUMN);
        char[][] pcGuessBoard = creatingBoards(RAW, COLUMN);

        /* The number of battleships and their size.*/
        System.out.println("Enter the battleships sizes");
        String battleshipSizes = scanner.nextLine();
        /*Sets an array that store the number of battleships and there size.*/
        int[][] arrShipsSize = setArrOfShipsAndSize(battleshipSizes);

        /*Sets the Location and orientation of user's battleships,
          and gets the total number of ships.*/
        int numOfShipsUser = checkingAndPlacingTheShips(arrShipsSize, RAW,
                COLUMN, userGameBoard, true);


        /*Sets the Location and orientation of computer's battleships,
          and gets the total number of ships.*/
        int numOfShipsPc = checkingAndPlacingTheShips(arrShipsSize, RAW,
                COLUMN, pcGameBoard, false);

        // TODO: We need to create a loop for all the user's and computer's attacks, in order of attacks.
        /*Attacks*/
        while (numOfShipsUser > 0 && numOfShipsPc > 0){
            /*The user is attacking:*/
            numOfShipsPc = playerAttacks(userGuessBoard , pcGameBoard, RAW, COLUMN, true, numOfShipsPc);
            if (numOfShipsPc == 0){
                System.out.println("You won the game!");
                return;
            }
            /*The computer is attacking:*/
            numOfShipsUser = playerAttacks(pcGuessBoard, userGameBoard , RAW, COLUMN, false, numOfShipsUser);
            if (numOfShipsUser == 0){
                System.out.println("You lost ):");
                return;
            }
        }
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
     * @param RAW Gets umber of raws of each board.
     * @param COLUMN Gets Number of column of each board.
     * @param board Gets a game board.
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns the updated game board in accordance to the validity of the location of the battleships.
     */
    public static int checkingAndPlacingTheShips(int[][] arrShipsSize, int RAW,
                                                      int COLUMN, char[][] board, boolean isUserBoard){
        int x, y, o;
        int countShips = 0;
        boolean massageFlag = true;
        for (int[] ints : arrShipsSize) {
            int numOfShipsOfTheSameSize = ints[0];
            int SizeOfShips = ints[1];
            do {
                if (massageFlag) {
                    countShips++;
                    /*Prints the current game board each time.*/
                    if (isUserBoard){
                        printTheGameBoard(board, RAW, COLUMN);
                        System.out.println("Enter location and orientation for battleship of size " + SizeOfShips);
                    }
                }
                if(isUserBoard){
                    String xyo = scanner.nextLine();
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
                }else if (x >= RAW || y >= COLUMN || x < 0 || y < 0) {
                    if (isUserBoard){
                        System.err.println("Illegal tile, try again!");
                    }
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    if (o == 0){
                        /*Checking if valid position.*/
                        massageFlag = checkHorizontalShip(board, x, y, RAW, COLUMN, SizeOfShips, isUserBoard);
                    }
                    if (o == 1){
                        massageFlag = checkVerticalShip(board, x, y, RAW, COLUMN, SizeOfShips, isUserBoard);
                    }
                    if (massageFlag){
                        board = locatesTheShip(board, x, y, o, SizeOfShips);
                        numOfShipsOfTheSameSize--;
                    }
                }
            } while (numOfShipsOfTheSameSize > 0);
        }

        return countShips;
    }

    /**Prints the current game board each time.
     * Prints the row of numbers and the space before them.
     * Prints the board without the number row.
     * @param board Gets the game board before and after the input.
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     */
    public static void printTheGameBoard(char[][] board, int RAW, int COLUMN){
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
        System.out.println();
    }

    /**Checks if the location of horizontal ship is valid.
     * Checks the left and the right sides of the ship.
     * Checks the upside of the ship.
     * Checks the underside of the ship.
     * @param board Gets the game board after each input.
     * @param x Gets the horizontal axis coordinate
     * @param y Gets the vertical axis coordinate
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     * @param SizeShips Gets the size of each battleship
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns boolean value that tells if we can locate the battleship.
     */
    // TODO: Check the validity of the corners of the frame.
    public static boolean checkHorizontalShip(char[][] board, int x, int y, int RAW,
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
        /*Checks the left and the right sides of the ship. */
        if (!((y - 1 < 0 || (board[x][y - 1] == '–'))
                && (y + SizeShips >= COLUMN || (board[x][y + SizeShips] == '–')))) {
            if (isUserBoard){
                System.err.println("Adjacent battleship detected, try again!");
            }
            return false;
        }
        /*Checks the upside of the ship. */
        if (x - 1 >= 0) {
            if (y - 1 >= 0) {
                if (y + SizeShips < COLUMN) {
                    for (int k = y - 1; k <= (y + SizeShips); k++) {
                        if (board[x - 1][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else {
                    for (int k = y - 1; k < (y + SizeShips); k++){
                        if (board[x - 1][k] != '–'){
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            } else if (y + SizeShips < COLUMN) {
                for (int k = y; (k <= (y + SizeShips)); k++) {
                    if (board[x - 1][k] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }else {
                for (int k = y; (k < (y + SizeShips)); k++) {
                    if (board[x - 1][k] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }
        }
        /*Checks the underside of the ship.*/
        if (x + 1 < RAW) {
            if (y > 0) {
                if (y + SizeShips < COLUMN){
                    for (int k = y - 1; k <= (y + SizeShips); k++) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else{
                    for (int k = y - 1; k < (y + SizeShips); k++) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            }else if (y + SizeShips >= COLUMN){
                for (int k = y; k < (y + SizeShips); k++) {
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

    /**Checks if the location of vertical ship is valid.
     * Checks the upside and the underside sides of the ship.
     * Checks the left of the ship.
     * Checks the rightSide of the ship.
     * @param board Gets the game board after each input.
     * @param x Gets the horizontal axis coordinate
     * @param y Gets the vertical axis coordinate
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     * @param SizeShips Gets the size of each battleship
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns boolean value that tells if we can locate the battleship.
     */
    // TODO: Check the validity of the corners of the frame.
    public static boolean checkVerticalShip(char[][] board, int x, int y, int RAW,
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
        /*Checks the upside and the underside sides of the ship. */
        if (!((x - 1 < 0 || (board[x - 1][y] == '–'))
                && (x + SizeShips >= RAW || (board[x + SizeShips][y] == '–')))) {
            if (isUserBoard){
                System.err.println("Adjacent battleship detected, try again!");
            }
            return false;
        }
        /*Checks the left of the ship. */
        if (y - 1 >= 0) {
            if (x - 1 >= 0) {
                if (x + SizeShips < RAW) {
                    for (int k = x - 1; (k <= (x + SizeShips)); k++) {
                        if (board[k][y - 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else {
                    for (int k = x - 1; k < (x + SizeShips); k++) {
                        if (board[k][y - 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            } else if (x + SizeShips < RAW){
                for (int k = x; (k <= (x + SizeShips)); k++) {
                    if (board[k][y - 1] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                            return false;
                    }
                }
            } else {
                for (int k = x; k < (x + SizeShips); k++) {
                    if (board[k][y - 1] != '–') {
                        if (isUserBoard){
                            System.err.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }
        }
        /*Checks the rightSide of the ship. */
        if (y + 1 < RAW) {
            if (x - 1 >= 0) {
                if (x + SizeShips < RAW){
                    for (int k = x - 1; k <= (x + SizeShips); k++) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }else{
                    for (int k = x - 1; k < (x + SizeShips); k++) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            } else if(x + SizeShips < RAW) {
                for (int k = x ; k <= (x + SizeShips); k++) {
                    if (x + 1 < RAW) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            }else{
                for (int k = x ; k < (x + SizeShips); k++) {
                    if (x + 1 < RAW) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.err.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            }
        }else{
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

    /**The user or the computers attacks.
     * Prints the current guessing board each time.
     * Checks if tile is valid.
     * Checks if tile already attacked.
     * Checks if player misses or hits.
     * @param guessingBoard The player's guessing board.
     * @param gameBoard The player's game board.
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     * @param isUserAttack Checks if were updating the user or computer game board.
     */
    public static int playerAttacks(char[][] guessingBoard, char[][] gameBoard, int RAW,
                                     int COLUMN, boolean isUserAttack, int numOfShips){
        int x, y;
        boolean firstTry = true;
        boolean isValidAttack = true;
            do {
                /* Prints the current guessing board each time.*/
                if (firstTry && isUserAttack){
                    printTheGameBoard(gameBoard, RAW, COLUMN);
                    printTheGuessingBoard(guessingBoard, RAW, COLUMN);
                    System.out.println("Enter a tile to attack");
                    firstTry = false;
                }
                isValidAttack = true;
                if(isUserAttack){
                    String xy = scanner.nextLine();
                    String[] temp3 = xy.split(", ");
                    x = Integer.parseInt(String.valueOf(temp3[0]));
                    y = Integer.parseInt(String.valueOf(temp3[1]));
                } else {
                    x = rnd.nextInt(RAW);
                    y = rnd.nextInt(COLUMN);
                }
                /*Checks if tile is valid.*/
                if (x >= RAW || y >= COLUMN || x < 0 || y < 0) {
                    if (isUserAttack){
                        System.err.println("Illegal tile, try again!");
                    }
                    isValidAttack = false;
                } else {
                    /*Checks if tile already attacked.*/
                    for (int i = 0; i < RAW; i++){
                        for (int j = 0; (j < COLUMN) && isValidAttack; j++){
                            if (guessingBoard[i][j] != '–'){
                                if (isUserAttack){
                                    System.err.println("Tile already attacked, try again!");
                                }
                                isValidAttack = false;
                            }
                        }
                    }
                    /*Checks if player misses or hits.*/
                    if (isValidAttack){
                        if (!isUserAttack){
                            System.out.println("The computer attacked (" + x + ", " + y + ")");
                        }
                        if (gameBoard[x][y] != '#'){
                            guessingBoard[x][y] = 'X';
                            System.out.println("That is a miss!");
                        } else {
                            guessingBoard[x][y] = 'V';
                            System.out.println("That is a hit!");
                            gameBoard[x][y] = 'X';
                            /*Checks if player's battleship has been drowned*/
                            boolean isDrowned = checkIfDrowned(guessingBoard, gameBoard, x, y);
                            if (isDrowned){
                                numOfShips--;
                                if (isUserAttack){
                                    System.out.println("The computer's battleship has been drowned, " +
                                            numOfShips + " more battleships to go!");
                                } else {
                                    System.out.println("Your battleship has been drowned, you have left " +
                                            numOfShips + " more battleships!");
                                }
                            }
                        }
                    }
                }
            } while (!isValidAttack);

        return numOfShips;
    }
    /**Prints the current guessing board each time.
     * Prints the row of numbers and the space before them.
     * Prints the board without the number row.
     * @param board Gets the game board before and after the input.
     * @param RAW Gets the number of raws of each board.
     * @param COLUMN Gets the number of columns of each board.
     */
    public static void printTheGuessingBoard(char[][] board, int RAW, int COLUMN){
        System.out.println("Your current guessing board:");

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
        System.out.println();
    }

    public static boolean checkIfDrowned(char[][] guessingBoard, char[][] gameBoard, int x, int y) {
        // TODO: Check if player's battleship has been drowned

        boolean isTherePartOfShip = true;
        /*Checks horizontal.*/
        if (y != guessingBoard[0].length - 1){
            for (int i = y + 1; (i < guessingBoard[0].length) && isTherePartOfShip; i++){
                if (gameBoard[x][i] == '#'){
                    return false;
                } else {
                    isTherePartOfShip = false;
                }
            }
        }
        if (y != 0){
            for (int i = y - 1; i >= 0; i--){
                if (gameBoard[x][i] == '#'){
                    return false;
                }
            }
        }
        if (x != guessingBoard.length){
            for (int i = x + 1; (i < guessingBoard.length) && isTherePartOfShip; i++){
                if (gameBoard[i][y] == '#'){
                    return false;
                } else {
                    isTherePartOfShip = false;
                }
            }
        }
        if (x != guessingBoard.length -1){
            for (int i = x - 1; i >= 0; i--){
                if (gameBoard[i][y] == '#'){
                    return false;
                }
            }
        }
        return true;
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