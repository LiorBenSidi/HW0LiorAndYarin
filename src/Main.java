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
        int raw = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        int column = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));

        /*creates the boards.*/
        char[][] userGameBoard = creatingBoards(raw, column);
        char[][] userGuessBoard = creatingBoards(raw, column);
        char[][] pcGameBoard = creatingBoards(raw, column);
        char[][] pcGuessBoard = creatingBoards(raw, column);

        /* The number of battleships and their size.*/
        System.out.println("Enter the battleships sizes");
        String battleshipSizes = scanner.nextLine();
        /*Sets an array that store the number of battleships and there size.*/
        int[][] arrShipsSize = setArrOfShipsAndSize(battleshipSizes);

        /*Sets the Location and orientation of user's battleships,
          and gets the total number of ships of each player.*/
        int numOfShips= checkingAndPlacingTheShips(arrShipsSize, userGameBoard, true);

        /*Sets the Location and orientation of computer's battleships,*/
        checkingAndPlacingTheShips(arrShipsSize, pcGameBoard, false);

        /*Attacks*/
        int numOfShipsUser = numOfShips;
        int numOfShipsPc = numOfShips;
        while (numOfShipsUser > 0 && numOfShipsPc > 0){

            /*The user is attacking:*/
            numOfShipsPc = playerAttacks(userGuessBoard , pcGameBoard, true, numOfShipsPc);
            if (numOfShipsPc == 0){
                System.out.println("You won the game!");
                return;
            }

            /*The computer is attacking:*/
            numOfShipsUser = playerAttacks(pcGuessBoard, userGameBoard, false, numOfShipsUser);
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
     * @param raw Get the number of raws of each board.
     * @param column Get the number of columns of each board.
     * @return Returns default board.
     */
    public static char[][] creatingBoards(int raw, int column) {
        char[][] board = new char[raw][column];
        for (int i = 0; i < raw; i++){
            for (int j = 0; j < column; j++){
                board[i][j] = '–';
            }
        } return board;
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
        } return arrOfShipsAndSize;
    }

    /**Sets the Location and orientation of user's battleships.
     * @param arrShipsSize Gets array that store the number of battleships and there size.
     * @param board Gets a game board.
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns the updated game board in accordance to the validity of the location of the battleships.
     */
    public static int checkingAndPlacingTheShips(int[][] arrShipsSize, char[][] board, boolean isUserBoard){
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
                        printTheGameBoard(board);
                        System.out.println("Enter location and orientation for battleship of size " + SizeOfShips);
                    }
                }if (isUserBoard){
                    String xyo = scanner.nextLine();
                    String[] temp3 = xyo.split(", ");
                    x = Integer.parseInt(String.valueOf(temp3[0]));
                    y = Integer.parseInt(String.valueOf(temp3[1]));
                    o = Integer.parseInt(String.valueOf(temp3[2]));
                } else {
                    x = rnd.nextInt(board.length);
                    y = rnd.nextInt(board[0].length);
                    o = rnd.nextInt(2);
                }
                /*Checks if tile is valid*/
                if (o != 0 && o != 1) {
                    if (isUserBoard){System.out.println("Illegal orientation, try again!");}
                    massageFlag = false;
                }else if (x >= board.length || y >= board[0].length || x < 0 || y < 0){
                    if (isUserBoard){System.out.println("Illegal tile, try again!");}
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    if (o == 0){
                        /*Checking if valid position.*/
                        massageFlag = checkHorizontalShip(board, x, y, SizeOfShips, isUserBoard);
                    } if (o == 1) {massageFlag = checkVerticalShip(board, x, y, SizeOfShips, isUserBoard);}
                    if (massageFlag){
                        locatesTheShip(board, x, y, o, SizeOfShips);
                        numOfShipsOfTheSameSize--;
                    }
                }
            } while (numOfShipsOfTheSameSize > 0);
        } if(isUserBoard){printTheGameBoard(board);}
        return countShips;
    }

    /**Prints the current game board each time.
     * Prints the row of numbers and the space before them.
     * Prints the board without the number row.
     * @param board Gets the game board before and after the input.
     */
    public static void printTheGameBoard(char[][] board){
        System.out.println("Your current game board:");

        /*Prints the row of numbers and the space before them.*/
        for (int i = 0; i < String.valueOf(board.length).length(); i++){
            System.out.print(" ");
        } for (int i = 0; i < board[0].length; i++){System.out.print(" " + i);}
        System.out.println();

        /*Prints the board without the number row.*/
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[0].length; j++){
                if ( j == 0 ){
                        for (int k = 0; k < String.valueOf(board.length).length() - String.valueOf(i).length(); k++){
                            System.out.print(" ");
                        } System.out.print(i);
                } System.out.print(" " + board[i][j]);
                if (j + 1 == board[0].length){System.out.println();}
            }
        } System.out.println();
    }

    /**Checks if the location of horizontal ship is valid.
     * Checks the left and the right sides of the ship.
     * Checks the upside of the ship.
     * Checks the underside of the ship.
     * @param board Gets the game board after each input.
     * @param x Gets the horizontal axis coordinate
     * @param y Gets the vertical axis coordinate
     * @param SizeShips Gets the size of each battleship
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns boolean value that tells if we can locate the battleship.
     */
    public static boolean checkHorizontalShip(char[][] board, int x, int y, int SizeShips, boolean isUserBoard) {
        if (y + SizeShips > board[0].length) {
            if (isUserBoard){System.out.println("Battleship exceeds the boundaries of the board, try again!");}
            return false;
        } for (int i = y; i < y + SizeShips; i++) {
            if (board[x][i] == '#') {
                if (isUserBoard){System.out.println("Battleship overlaps another battleship, try again!");}
                return false;
            }
        }
        /*Checks the left and the right sides of the ship. */
        if (!((y - 1 < 0 || (board[x][y - 1] == '–'))
                && (y + SizeShips >= board[0].length || (board[x][y + SizeShips] == '–')))) {
            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
            return false;
        }
        /*Checks the upside of the ship. */
        if (x - 1 >= 0) {
            if (y - 1 >= 0) {
                if (y + SizeShips < board[0].length) {
                    for (int k = y - 1; k <= (y + SizeShips); k++) {
                        if (board[x - 1][k] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            } return false;
                        }
                    }
                } else {
                    for (int k = y - 1; k < (y + SizeShips); k++){
                        if (board[x - 1][k] != '–'){
                            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                            return false;
                        }
                    }
                }
            } else if (y + SizeShips < board[0].length) {
                for (int k = y; (k <= (y + SizeShips)); k++) {
                    if (board[x - 1][k] != '–') {
                        if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                        return false;
                    }
                }
            }else {
                for (int k = y; (k < (y + SizeShips)); k++) {
                    if (board[x - 1][k] != '–') {
                        if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                        return false;
                    }
                }
            }
        }
        /*Checks the underside of the ship.*/
        if (x + 1 < board.length) {
            if (y > 0) {
                if (y + SizeShips < board[0].length){
                    for (int k = y - 1; k <= (y + SizeShips); k++) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                            return false;
                        }
                    }
                } else{
                    for (int k = y - 1; k < (y + SizeShips); k++) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                            return false;
                        }
                    }
                }
            }else if (y + SizeShips >= board[0].length){
                for (int k = y; k < (y + SizeShips); k++) {
                    if (x + 1 < board.length) {
                        if (board[x + 1][k] != '–') {
                            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                            return false;
                        }
                    }else{
                        if (board[x][k] != '–') {
                            if (isUserBoard){System.out.println("Adjacent battleship detected, try again!");}
                            return false;
                        }
                    }
                }
            }
        } return true;
    }

    /**Checks if the location of vertical ship is valid.
     * Checks the upside and the underside sides of the ship.
     * Checks the left of the ship.
     * Checks the rightSide of the ship.
     * @param board Gets the game board after each input.
     * @param x Gets the horizontal axis coordinate
     * @param y Gets the vertical axis coordinate
     * @param SizeShips Gets the size of each battleship
     * @param isUserBoard Checks if were updating the user or computer game board.
     * @return Returns boolean value that tells if we can locate the battleship.
     */
    public static boolean checkVerticalShip(char[][] board, int x, int y, int SizeShips, boolean isUserBoard) {
        if (x + SizeShips > board.length) {
            if (isUserBoard){
                System.out.println("Battleship exceeds the boundaries of the board, try again!");
            }
            return false;
        }
        for (int i = x; i < x + SizeShips; i++) {
            if (board[i][y] == '#') {
                if (isUserBoard){
                    System.out.println("Battleship overlaps another battleship, try again!");
                }
                return false;
            }
        }
        /*Checks the upside and the underside sides of the ship. */
        if (!((x - 1 < 0 || (board[x - 1][y] == '–'))
                && (x + SizeShips >= board.length || (board[x + SizeShips][y] == '–')))) {
            if (isUserBoard){
                System.out.println("Adjacent battleship detected, try again!");
            }
            return false;
        }
        /*Checks the left of the ship. */
        if (y - 1 >= 0) {
            if (x - 1 >= 0) {
                if (x + SizeShips < board.length) {
                    for (int k = x - 1; (k <= (x + SizeShips)); k++) {
                        if (board[k][y - 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                } else {
                    for (int k = x - 1; k < (x + SizeShips); k++) {
                        if (board[k][y - 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            } else if (x + SizeShips < board.length){
                for (int k = x; (k <= (x + SizeShips)); k++) {
                    if (board[k][y - 1] != '–') {
                        if (isUserBoard){
                            System.out.println("Adjacent battleship detected, try again!");
                        }
                            return false;
                    }
                }
            } else {
                for (int k = x; k < (x + SizeShips); k++) {
                    if (board[k][y - 1] != '–') {
                        if (isUserBoard){
                            System.out.println("Adjacent battleship detected, try again!");
                        }
                        return false;
                    }
                }
            }
        }
        /*Checks the rightSide of the ship. */
        if (y + 1 < board[0].length) {
            if (x - 1 >= 0) {
                if (x + SizeShips < board.length){
                    for (int k = x - 1; k <= (x + SizeShips); k++) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }else{
                    for (int k = x - 1; k < (x + SizeShips); k++) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            } else if(x + SizeShips < board.length) {
                for (int k = x ; k <= (x + SizeShips); k++) {
                    if (x + 1 < board.length) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
                            }
                            return false;
                        }
                    }
                }
            }else{
                for (int k = x ; k < (x + SizeShips); k++) {
                    if (x + 1 < board.length) {
                        if (board[k][y + 1] != '–') {
                            if (isUserBoard){
                                System.out.println("Adjacent battleship detected, try again!");
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
     * @param x Gets the horizontal axis coordinate.
     * @param y Gets the vertical axis coordinate.
     * @param o Gets the orientation
     * @param SizeShips Gets the size of each battleship
     */
    public static void locatesTheShip(char[][] board, int x, int y, int o, int SizeShips) {
        if (o == 0) {for (int i = y; i < (y + SizeShips); i++) {board[x][i] = '#';}
        } else {for (int i = x; i < (x + SizeShips); i++) {board[i][y] = '#';}}
    }

    /**The user or the computers attacks.
     * Prints the current guessing board each time.
     * Checks if tile is valid.
     * Checks if tile already attacked.
     * Checks if player misses or hits.
     * @param guessingBoard The player's guessing board.
     * @param gameBoard The player's game board.
     * @param isUserAttack Checks if were updating the user or computer game board.
     */
    public static int playerAttacks(char[][] guessingBoard, char[][] gameBoard, boolean isUserAttack, int numOfShips){
        int x, y;
        boolean firstTry = true;
        boolean isValidAttack;
            do {
                /* Prints the current guessing board each time.*/
                if (firstTry){
                    if (isUserAttack){
                        printTheGuessingBoard(guessingBoard);
                        System.out.println("Enter a tile to attack");
                    }firstTry = false;
                }isValidAttack = true;
                if(isUserAttack){
                    String xy = scanner.nextLine();
                    String[] temp3 = xy.split(", ");
                    x = Integer.parseInt(String.valueOf(temp3[0]));
                    y = Integer.parseInt(String.valueOf(temp3[1]));
                } else {
                    x = rnd.nextInt(gameBoard.length);
                    y = rnd.nextInt(gameBoard[0].length);
                }
                /*Checks if tile is valid.*/
                if (x >= gameBoard.length || y >= gameBoard[0].length || x < 0 || y < 0) {
                    if (isUserAttack){System.out.println("Illegal tile, try again!");}
                    isValidAttack = false;
                } else {
                    /*Checks if tile already attacked.*/
                            if (guessingBoard[x][y] != '–'){
                                if (isUserAttack){System.out.println("Tile already attacked, try again!");}
                                isValidAttack = false;
                            }
                    /*Checks if player misses or hits.*/
                    if (isValidAttack){
                        if (!isUserAttack){System.out.println("The computer attacked (" + x + ", " + y + ")");}
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
            if(!isUserAttack){
                printTheGameBoard(gameBoard);
            } return numOfShips;
    }
    /**Prints the current guessing board each time.
     * Prints the row of numbers and the space before them.
     * Prints the board without the number row.
     * @param board Gets the game board before and after the input.
     */
    public static void printTheGuessingBoard(char[][] board){
        System.out.println("Your current guessing board:");

        /*Prints the row of numbers and the space before them.*/
        String strRaw = String.valueOf(board.length);
        int rawLength = strRaw.length();
        for (int i = 0; i < rawLength; i++){System.out.print(" ");}
        for (int i = 0; i < board[0].length; i++){System.out.print(" " + i);}
        System.out.println();

        /*Prints the board without the number row.*/
        for (int i = 0; i < board.length; i++){
            String strI = String.valueOf(i);
            int iLength = strI.length();
            for (int j = 0; j < board[0].length; j++){
                if ( j == 0 ){
                    for (int k = 0; k < rawLength - iLength; k++){System.out.print(" ");}
                    System.out.print(i);
                }
                System.out.print(" " + board[i][j]);
                if (j + 1 == board[0].length){System.out.println();}
            }
        } System.out.println();
    }

    /**Checks if the battleship that the tile we hits belong to have been drowned.
     * Checks horizontal axis.
     * Checks vertical axis.
     * @param guessingBoard The player's guessing board.
     * @param gameBoard The player's game board.
     * @param x Gets the horizontal axis coordinate.
     * @param y Gets the vertical axis coordinate.
     * @return Returns "true" if the battleship have been drowned, else "false".
     */
    public static boolean checkIfDrowned(char[][] guessingBoard, char[][] gameBoard, int x, int y) {
        boolean isTherePartOfShip = true;
        /*Checks horizontal axis.*/
        if (y != guessingBoard[0].length - 1){
            for (int i = y + 1; (i < guessingBoard[0].length) && isTherePartOfShip; i++){
                if (gameBoard[x][i] != '–') {
                    if (guessingBoard[x][i] != 'V'){return false;}
                } else {isTherePartOfShip = false;}
            }
        } isTherePartOfShip = true;
        if (y != 0){
            for (int i = y - 1; (i >= 0) && isTherePartOfShip; i--){
                if (gameBoard[x][i] != '–'){
                    if (guessingBoard[x][i] != 'V'){return false;}
                }else{isTherePartOfShip = false;}
            }
        }
        /*Checks vertical axis.*/
        isTherePartOfShip = true;
        if (x != guessingBoard.length - 1){
            for (int i = x + 1; (i < guessingBoard.length) && isTherePartOfShip; i++){
                if (gameBoard[i][y] != '–'){
                    if (guessingBoard[i][y] != 'V'){return false;}
                } else {isTherePartOfShip = false;}
            }
        }
        isTherePartOfShip = true;
        if (x != 0){
            for (int i = x - 1; (i >= 0) && isTherePartOfShip; i--){
                if (gameBoard[i][y] != '–'){
                    if (guessingBoard[i][y] != 'V'){return false;}
                }else{isTherePartOfShip = false;}
            }
        } return true;
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