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
        int n = Integer.parseInt(String.valueOf(boardSize.split("X")[0]));
        int m = Integer.parseInt(String.valueOf(boardSize.split("X")[1]));

        /*Gets the number of battleships and their size.*/
        System.out.println("Enter the battleships sizes");
        String strShipsNumAndSizes = scanner.nextLine();

        /*Sets a two-dimensional array that store the number of battleships and there size.*/
        int[][] arrShipsNumAndSizes = shipsNumAndSizes(strShipsNumAndSizes);

        /*creates the boards.*/
        char[][] userGameBoard = creatingBoards(n, m);
        char[][] userGuessBoard = creatingBoards(n, m);
        char[][] computerGameBoard = creatingBoards(n, m);
        char[][] computerGuessBoard = creatingBoards(n, m);

        /*gets the total number of ships of each player.*/
        int numOfShips = 0;
        for(int[] ints : arrShipsNumAndSizes) {
            numOfShips += ints[0];
        }
        int userNumOfShips = numOfShips;
        int computerNumOfShips = numOfShips;

        /*Sets the Location and orientation of user's battleships.*/
        userPlaceShipsAccordingToValidPosition(n, m, arrShipsNumAndSizes, userGameBoard);

        /*Sets the Location and orientation of computer's battleships,*/
        computerPlaceShipsAccordingToValidPosition(n, m, arrShipsNumAndSizes, computerGameBoard);

        /*Attacks:*/
        while(userNumOfShips > 0 && computerNumOfShips > 0) {

            /*The user is attacking:*/
            computerNumOfShips = userAttacks(n, m, userGuessBoard, computerGameBoard, computerNumOfShips);
            if(computerNumOfShips == 0) {
                System.out.println("You won the game!");
                return;
            }
            /*The computer is attacking:*/
            userNumOfShips = computerAttacks(n, m, computerGuessBoard, userGameBoard, userNumOfShips);
            if(userNumOfShips == 0) {
                System.out.println("You lost ):");
                return;
            }
        }
    }

    /**Sets a two-dimensional array that store the number of battleships and there size.
     * @param strShipsNumAndSizes Gets the number and size of the battleships.
     * @return Returns array that store the number of battleships and there size.
     */
    public static int[][] shipsNumAndSizes(String strShipsNumAndSizes) {
        String[] splitStrShipsNumAndSizes = strShipsNumAndSizes.split(" ");
        int[][] twoDimArr = new int[splitStrShipsNumAndSizes.length][2];
        int i = 0;
        for(String strSplit : splitStrShipsNumAndSizes) {
            twoDimArr[i][0] = Integer.parseInt(String.valueOf(strSplit.split("X")[0]));
            twoDimArr[i][1] = Integer.parseInt(String.valueOf(strSplit.split("X")[1]));
            i++;
        }
        return twoDimArr;
    }

    /**Creates a game/guess board.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @return Returns a default game/guess board.
     */
    public static char[][] creatingBoards(int n, int m) {
        char[][] board = new char[n][m];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                board[i][j] = '\u2013';
            }
        }
        return board;
    }

    /**Prints a game/guess board each time.
     * @param board Gets the game/guess board before and after the input.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     */
    public static void printTheBoard(char[][] board, int n, int m) {
        /*first, prints the first line of board that is a row of numbers and space before each obe them.*/
        for(int i = 0; i < String.valueOf(n).length(); i++) {
            System.out.print(" ");
        }
        for(int i = 0; i < m; i++) {
            System.out.print(" " + i);
        }
        System.out.println();

        /*Second ,prints the board without the number row.*/
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j == 0) {
                    for(int k = 0; k < String.valueOf(n).length() - String.valueOf(i).length(); k++) {
                        System.out.print(" ");
                    }
                    System.out.print(i);
                }
                System.out.print(" " + board[i][j]);
                if(j + 1 == m) {
                    System.out.println();
                }
            }
        }
        System.out.println();
    }

    /**Places the battleships on the user's game board according to the validity of a requested position.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param arrShipsNumAndSizes Gets a two-dimensional array of all the battleships the player have and their sizes.
     * @param userGameBoard Gets the user's game board.
     */
    public static void userPlaceShipsAccordingToValidPosition(int n, int m, int[][] arrShipsNumAndSizes,
                                                              char[][] userGameBoard) {
        int x, y, o;
        boolean isValidPosition = true; /*Gets 'true' if the requested position is valid, else - false.*/
        for(int[] arrShipsNumAndSize : arrShipsNumAndSizes) {
            int numOfShips = arrShipsNumAndSize[0];
            int sizeOfShips = arrShipsNumAndSize[1];
            do {
                if(isValidPosition) {
                    /*Prints the current user's game board each time.*/
                    System.out.println("Your current game board:");
                    printTheBoard(userGameBoard, n, m);
                    System.out.println("Enter location and orientation for battleship of size " + sizeOfShips);
                }
                /*Gets location and orientation*/
                String xyo = scanner.nextLine();
                String[] splitStr = xyo.split(", ");
                x = Integer.parseInt(String.valueOf(splitStr[0]));
                y = Integer.parseInt(String.valueOf(splitStr[1]));
                o = Integer.parseInt(String.valueOf(splitStr[2]));

                /*Checks if tile is valid*/
                isValidPosition = userIsValidLocationAndOrientation(x, y, o, n, m, userGameBoard, sizeOfShips);
                if(isValidPosition) {
                    /*Locates the battleship on user's game board.*/
                    locatesTheShip(x, y, o, userGameBoard, sizeOfShips);
                    numOfShips--;
                }
            } while(numOfShips > 0);
        }
        /*Prints the user's game board after we locate all of his battleships.*/
        System.out.println("Your current game board:");
        printTheBoard(userGameBoard, n, m);
    }

    /**Checks if the tile and orientation we got from the user are valid.
     * @param x Gets the x-point rates of the user's battleship position.
     * @param y Gets the y-point rates of the user's battleship position.
     * @param o Gets 0 for horizontal orientation or 1 for vertical orientation.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param userGameBoard Gets the user's game board.
     * @param sizeOfShips Gets the size of the battleship that the user wants to place.
     * @return Returns 'true' if the location and orientation are valid, else, return 'false'.
     */
    public static boolean userIsValidLocationAndOrientation(int x, int y, int o, int n, int m,
                                                            char[][] userGameBoard, int sizeOfShips) {
        if(o != 0 && o != 1) {
            System.out.println("Illegal orientation, try again!");
            return false;
        }
        if(x >= n || y >= m || x < 0 || y < 0) {
            System.out.println("Illegal tile, try again!");
            return false;
        }
        if(o == 0) {
            if (!userCheckHorizontalShip(x, y, n, m, userGameBoard, sizeOfShips)) {
                return false;
            }
        } else if (!userCheckVerticalShip(x, y, n, m, userGameBoard, sizeOfShips)) {
            return false;
        }
        return true;
    }

    /**Checks if the user gives valid horizontal tile, checks if we can place the battleship there.
     * @param x Gets the x-point rates of the user's battleship position.
     * @param y Gets the y-point rates of the user's battleship position.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param userGameBoard Gets the user's gameBoard.
     * @param sizeOfShips Gets the size of the battleship that the user wants to place.
     * @return Returns 'True' if the user can place the battleship, else, return 'false'.
     */
    public static boolean userCheckHorizontalShip(int x, int y, int n, int m,
                                                  char[][] userGameBoard, int sizeOfShips) {
        /*Checks if the user's battleship exceeds the boundaries of the board.*/
        if(y + sizeOfShips > m) {
            System.out.println("Battleship exceeds the boundaries of the board, try again!");
            return false;
        }
        /*Checks if the user's battleship overlaps another user's battleship.*/
        for(int i = y; i < y + sizeOfShips; i++) {
            if(userGameBoard[x][i] == '#') {
                System.out.println("Battleship overlaps another battleship, try again!");
                return false;
            }
        }
        /*Checks the left and the right sides of the user's battleship. */
        if(!((y - 1 < 0 || (userGameBoard[x][y - 1] == '\u2013'))
                && (y + sizeOfShips >= m || (userGameBoard[x][y + sizeOfShips] == '\u2013')))) {
            System.out.println("Adjacent battleship detected, try again!");
            return false;
        }
        /*Checks the upside of the user's battleship. */
        if(x - 1 >= 0) {
            if(y - 1 >= 0) {
                if(y + sizeOfShips < m) {
                    for(int k = y - 1; k <= (y + sizeOfShips); k++) {
                        if(userGameBoard[x - 1][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                } else {
                    for(int k = y - 1; k < (y + sizeOfShips); k++) {
                        if(userGameBoard[x - 1][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            } else if(y + sizeOfShips < m) {
                for(int k = y; (k <= (y + sizeOfShips)); k++) {
                    if(userGameBoard[x - 1][k] != '\u2013') {
                        System.out.println("Adjacent battleship detected, try again!");
                        return false;
                    }
                }
            } else {
                for(int k = y; (k < (y + sizeOfShips)); k++) {
                    if(userGameBoard[x - 1][k] != '\u2013') {
                        System.out.println("Adjacent battleship detected, try again!");
                        return false;
                    }
                }
            }
        }
        /*Checks the underside of the user's battleship.*/
        if(x + 1 < n) {
            if(y > 0) {
                if(y + sizeOfShips < m) {
                    for(int k = y - 1; k <= (y + sizeOfShips); k++) {
                        if(userGameBoard[x + 1][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                } else {
                    for(int k = y - 1; k < (y + sizeOfShips); k++) {
                        if(userGameBoard[x + 1][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            } else if(y + sizeOfShips < m) {
                for(int k = y; k < (y + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if(userGameBoard[x + 1][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    } else {
                        if(userGameBoard[x][k] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**Checks if the user gives valid vertical tile, checks if we can place the battleship there.
     * @param x Gets the x-point rates of the user's battleship position.
     * @param y Gets the y-point rates of the user's battleship position.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param userGameBoard Gets the user's gameBoard.
     * @param sizeOfShips Gets the size of the battleship that the user wants to place.
     * @return Returns 'True' if we can place the battleship, else, return 'false'.
     */
    public static boolean userCheckVerticalShip(int x, int y, int n, int m,
                                                char[][] userGameBoard, int sizeOfShips) {
        /*Checks if the user's battleship exceeds the boundaries of the board.*/
        if(x + sizeOfShips > n) {
            System.out.println("Battleship exceeds the boundaries of the board, try again!");
            return false;
        }
        /*Checks if the user's battleship overlaps another user's battleship.*/
        for(int i = x; i < x + sizeOfShips; i++) {
            if(userGameBoard[i][y] == '#') {
                System.out.println("Battleship overlaps another battleship, try again!");
                return false;
            }
        }
        /*Checks the upside and the underside sides of the user's battleship. */
        if(!((x - 1 < 0 || (userGameBoard[x - 1][y] == '\u2013'))
                && (x + sizeOfShips >= n || (userGameBoard[x + sizeOfShips][y] == '\u2013')))) {
            System.out.println("Adjacent battleship detected, try again!");
            return false;
        }
        /*Checks the left of the user's battleship. */
        if(y - 1 >= 0) {
            if(x - 1 >= 0) {
                if(x + sizeOfShips < n) {
                    for(int k = x - 1; (k <= (x + sizeOfShips)); k++) {
                        if(userGameBoard[k][y - 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                } else {
                    for(int k = x - 1; k < (x + sizeOfShips); k++) {
                        if(userGameBoard[k][y - 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            } else if(x + sizeOfShips < n) {
                for(int k = x; (k <= (x + sizeOfShips)); k++) {
                    if(userGameBoard[k][y - 1] != '\u2013') {
                        System.out.println("Adjacent battleship detected, try again!");
                        return false;
                    }
                }
            } else {
                for(int k = x; k < (x + sizeOfShips); k++) {
                    if(userGameBoard[k][y - 1] != '\u2013') {
                        System.out.println("Adjacent battleship detected, try again!");
                        return false;
                    }
                }
            }
        }
        /*Checks the rightSide of the user's battleship. */
        if(y + 1 < m) {
            if(x - 1 >= 0) {
                if(x + sizeOfShips < n) {
                    for(int k = x - 1; k <= (x + sizeOfShips); k++) {
                        if(userGameBoard[k][y + 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                } else {
                    for(int k = x - 1; k < (x + sizeOfShips); k++) {
                        if(userGameBoard[k][y + 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            } else if(x + sizeOfShips < n) {
                for(int k = x; k <= (x + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if(userGameBoard[k][y + 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            } else {
                for(int k = x; k < (x + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if(userGameBoard[k][y + 1] != '\u2013') {
                            System.out.println("Adjacent battleship detected, try again!");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**Place the battleships on the computer's game board according to the validity of a requested position.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param arrShipsNumAndSizes Gets a two-dimensional array of all the battleships the player have and their sizes.
     * @param computerGameBoard Gets the computer's game board.
     */
    public static void computerPlaceShipsAccordingToValidPosition(int n, int m, int[][] arrShipsNumAndSizes,
                                                                  char[][] computerGameBoard) {
        int x, y, o;
        boolean isValidPosition; /*Gets 'true' if the requested position is valid, else - false.*/
        for(int[] ints : arrShipsNumAndSizes) {
            int numOfShips = ints[0];
            int sizeOfShips = ints[1];
            do {
                /*Gets location and orientation*/
                x = rnd.nextInt(n);
                y = rnd.nextInt(m);
                o = rnd.nextInt(2);

                /*Checks if tile is valid*/
                isValidPosition = computerIsValidLocationAndOrientation(x, y, o, n, m, computerGameBoard, sizeOfShips);
                if(isValidPosition) {
                    /*Locates the battleship on computer's game board.*/
                    locatesTheShip(x, y, o, computerGameBoard, sizeOfShips);
                    numOfShips--;
                }
            } while(numOfShips > 0);
        }
    }

    /**Checks if the tile and orientation we got from the computer are valid.
     * @param x Gets the x-point rates of the computer's battleship position.
     * @param y Gets the y-point rates of the computer's battleship position.
     * @param o Gets 0 for horizontal orientation or 1 for vertical orientation.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param computerGameBoard Gets the computer's game board.
     * @param sizeOfShips Gets the size of the battleship that the computer wants to place.
     * @return Returns 'true' if the computer can place the battleship, else, return 'false'.
     */
    public static boolean computerIsValidLocationAndOrientation(int x, int y, int o, int n, int m,
                                                                char[][] computerGameBoard, int sizeOfShips) {
        if(o != 0 && o != 1) {
            return false;
        }
        else if(x >= n || y >= m || x < 0 || y < 0) {
            return false;
        }
        if(o == 0) {
            if (!computerCheckHorizontalShip(x, y, n, m, computerGameBoard, sizeOfShips)) {
                return false;
            }
        } else if (!computerCheckVerticalShip(x, y, n, m, computerGameBoard, sizeOfShips)) {
            return false;
        }
        return true;
    }

    /** If the computer gives valid horizontal tile, checks if we can place the battleship there.
     * @param x Gets the x-point rates of the computer's submarine position.
     * @param y Gets the y-point rates of the computer's submarine position
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param computerGameBoard Gets the computer's game board.
     * @param sizeOfShips Gets the size of the battleship that the computer wants to place.
     * @return Returns 'true' if the computer can place the battleship, else, return 'false'.
     */
    public static boolean computerCheckHorizontalShip(int x, int y, int n, int m,
                                                      char[][] computerGameBoard, int sizeOfShips) {
        /*Checks if the computer's battleship exceeds the boundaries of the board.*/
        if(y + sizeOfShips > m) {
            return false;
        }

        /*Checks if the computer's battleship overlaps another computer's battleship.*/
        for(int i = y; i < y + sizeOfShips; i++) {
            if (computerGameBoard[x][i] == '#') {
                return false;
            }
        }
        /*Checks the left and the right sides of the computer's battleship. */
        if(!((y - 1 < 0 || (computerGameBoard[x][y - 1] == '\u2013'))
                && (y + sizeOfShips >= m || (computerGameBoard[x][y + sizeOfShips] == '\u2013')))) {
            return false;
        }
        /*Checks the upside of the computer's battleship. */
        if(x - 1 >= 0) {
            if(y - 1 >= 0) {
                if(y + sizeOfShips < m) {
                    for(int k = y - 1; k <= (y + sizeOfShips); k++) {
                        if(computerGameBoard[x - 1][k] != '\u2013') {
                            return false;
                        }
                    }
                } else {
                    for(int k = y - 1; k < (y + sizeOfShips); k++) {
                        if(computerGameBoard[x - 1][k] != '\u2013') {
                            return false;
                        }
                    }
                }
            } else if(y + sizeOfShips < m) {
                for(int k = y; (k <= (y + sizeOfShips)); k++) {
                    if(computerGameBoard[x - 1][k] != '\u2013') {
                        return false;
                    }
                }
            } else {
                for(int k = y; (k < (y + sizeOfShips)); k++) {
                    if(computerGameBoard[x - 1][k] != '\u2013') {
                        return false;
                    }
                }
            }
        }
        /*Checks the underside of the computer's battleship.*/
        if(x + 1 < n) {
            if(y > 0) {
                if(y + sizeOfShips < m) {
                    for(int k = y - 1; k <= (y + sizeOfShips); k++) {
                        if(computerGameBoard[x + 1][k] != '\u2013') {
                            return false;
                        }
                    }
                } else {
                    for(int k = y - 1; k < (y + sizeOfShips); k++) {
                        if(computerGameBoard[x + 1][k] != '\u2013') {
                            return false;
                        }
                    }
                }
            } else if(y + sizeOfShips < m) {
                for(int k = y; k < (y + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if(computerGameBoard[x + 1][k] != '\u2013') {
                            return false;
                        }
                    } else {
                        if(computerGameBoard[x][k] != '\u2013') {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /** If the computer gives valid vertical tile, checks if we can place the battleship there.
     * @param x Gets the x-point rates of the computer's battleship position.
     * @param y Gets the y-point rates of the computer's battleship position.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param computerGameBoard Gets the computer's game board.
     * @param sizeOfShips Gets the size of the battleship that the computer wants to place.
     * @return Returns True if the computer can place the battleship, else, return false.
     */
    public static boolean computerCheckVerticalShip(int x, int y, int n, int m,
                                                    char[][] computerGameBoard, int sizeOfShips) {
        /*Checks if the computer's battleship exceeds the boundaries of the board.*/
        if(x + sizeOfShips > n) {
            return false;
        }

        /*Checks if the computer's battleship overlaps another computer's battleship.*/
        for(int i = x; i < x + sizeOfShips; i++) {
            if(computerGameBoard[i][y] == '#') {
                return false;
            }
        }
        /*Checks the upside and the underside sides of the computer's battleship. */
        if(!((x - 1 < 0 || (computerGameBoard[x - 1][y] == '\u2013'))
                && (x + sizeOfShips >= n || (computerGameBoard[x + sizeOfShips][y] == '\u2013')))) {
            return false;
        }

        /*Checks the left of the computer's battleship. */
        if(y - 1 >= 0) {
            if(x - 1 >= 0) {
                if(x + sizeOfShips < n) {
                    for(int k = x - 1; (k <= (x + sizeOfShips)); k++) {
                        if(computerGameBoard[k][y - 1] != '\u2013') {
                            return false;
                        }
                    }
                } else {
                    for(int k = x - 1; k < (x + sizeOfShips); k++) {
                        if(computerGameBoard[k][y - 1] != '\u2013') {
                            return false;
                        }
                    }
                }
            } else if(x + sizeOfShips < n) {
                for(int k = x; (k <= (x + sizeOfShips)); k++) {
                    if(computerGameBoard[k][y - 1] != '\u2013') {
                        return false;
                    }
                }
            } else {
                for(int k = x; k < (x + sizeOfShips); k++) {
                    if(computerGameBoard[k][y - 1] != '\u2013') {
                        return false;
                    }
                }
            }
        }
        /*Checks the rightSide of the computer's battleship. */
        if(y + 1 < m) {
            if(x - 1 >= 0) {
                if(x + sizeOfShips < n) {
                    for(int k = x - 1; k <= (x + sizeOfShips); k++) {
                        if(computerGameBoard[k][y + 1] != '\u2013') {
                            return false;
                        }
                    }
                } else {
                    for(int k = x - 1; k < (x + sizeOfShips); k++) {
                        if(computerGameBoard[k][y + 1] != '\u2013') {
                            return false;
                        }
                    }
                }
            } else if(x + sizeOfShips < n) {
                for(int k = x; k <= (x + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if (computerGameBoard[k][y + 1] != '\u2013') {
                            return false;
                        }
                    }
                }
            } else {
                for(int k = x; k < (x + sizeOfShips); k++) {
                    if(x + 1 < n) {
                        if(computerGameBoard[k][y + 1] != '\u2013') {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**Locates a ship on a game board
     * @param x Gets the x-point rates of the player's battleship position.
     * @param y Gets the y-point rates of the player's battleship position.
     * @param o Gets 0 for horizontal orientation or 1 for vertical orientation.
     * @param GameBoard Gets the player's game board.
     * @param sizeOfShips Gets the size of the battleship that the player wants to place.
     */
    public static void locatesTheShip(int x, int y, int o, char[][] GameBoard, int sizeOfShips) {
        if(o == 0) {
            for(int i = y; i < (y + sizeOfShips); i++) {
                GameBoard[x][i] = '#';
            }
        } else {
            for(int i = x; i < (x + sizeOfShips); i++) {
                GameBoard[i][y] = '#';
            }
        }
    }

    /** Gets the tile the user wants to attack the computer's field.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's column.
     * @param userGuessBoard Gets the user's guess board.
     * @param computerGameBoard Gets the computer's game board.
     * @param computerNumOfShips Gets the number of the computer's battleships that have not drowned.
     * @return Returns The number of computer's battleships that have not drowned after user's attack.
     */
    public static int userAttacks(int n, int m, char[][] userGuessBoard,
                                  char[][] computerGameBoard, int computerNumOfShips) {
        int x, y;
        boolean firstTry = true; /*The first time that the user enter an attack in is turn.*/
        boolean isValidAttack; /*Gets 'true' if the attack is valid, else - false.*/
        do {
            /* Prints the current user's guess board each time.*/
            if(firstTry) {
                System.out.println("Your current guessing board:");
                printTheBoard(userGuessBoard, n, m);
                System.out.println("Enter a tile to attack");
                firstTry = false;
            }

            /*Gets a tile to attack from the user.*/
            String xy = scanner.nextLine();
            String[] splitStr = xy.split(", ");
            x = Integer.parseInt(String.valueOf(splitStr[0]));
            y = Integer.parseInt(String.valueOf(splitStr[1]));

            /*Checks if the user's attack is valid.*/
            isValidAttack = userIsValidAttack(x, y, n, m, userGuessBoard);

            /*Checks if the user misses or hits.*/
            if(isValidAttack) {
                if(computerGameBoard[x][y] != '#') {
                    userGuessBoard[x][y] = 'X';
                    System.out.println("That is a miss!");
                } else {
                    userGuessBoard[x][y] = 'V';
                    computerGameBoard[x][y] = 'X';
                    System.out.println("That is a hit!");

                    /*Checks if computer's battleship has been drowned*/
                    boolean isDrowned = checkIfDrowned(x, y, n, m, userGuessBoard, computerGameBoard);
                    if(isDrowned) {
                        computerNumOfShips--;
                        System.out.println("The computer's battleship has been drowned, " +
                                computerNumOfShips + " more battleships to go!");
                    }
                }
            }
        }
        while(!isValidAttack);
        return computerNumOfShips;
    }

    /**Checks if the user's attack is valid.
     * @param x Gets the x point rates of the user's attack.
     * @param y Gets the y point rates of the user's attack.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param userGuessBoard Gets the user's guess board.
     * @return True if user's attack is valid, else, false.
     */
    public static boolean userIsValidAttack(int x, int y, int n, int m, char[][] userGuessBoard) {
        if(x >= n || y >= m || x < 0 || y < 0) { /*Checks if illegal tile.*/
            System.out.println("Illegal tile, try again!");
            return false;
        } else if(userGuessBoard[x][y] != '\u2013') { /*Checks if tile already attacked.*/
                System.out.println("Tile already attacked, try again!");
                return false;
        }
        return true;
    }

    /** Gets the tile the computer wants to attack the user's field.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param computerGuessBoard Gets the computer's guess board.
     * @param userGameBoard Gets the user's game board.
     * @param userNumOfShips Gets the number of the user's battleships that have not drowned.
     * @return Returns The number of the user's battleships that have not drowned after computer's attack.
     */
    public static int computerAttacks(int n, int m, char[][] computerGuessBoard,
                                      char[][] userGameBoard, int userNumOfShips) {
        int x, y;
        boolean isValidAttack; /*Gets 'true' if the attack is valid, else - false.*/
        do {
            /*Gets a tile to attack from the computer.*/
            x = rnd.nextInt(n) ;
            y = rnd.nextInt(m) ;

            /*Checks if the computer's attack is valid.*/
            isValidAttack = computerIsValidAttack(x, y, n, m, computerGuessBoard);

            /*Checks if the computer misses or hits.*/
            if(isValidAttack) {
                System.out.println("The computer attacked (" + x + ", " + y + ")");
                if(userGameBoard[x][y] != '#') {
                    computerGuessBoard[x][y] = 'X';
                    System.out.println("That is a miss!");
                } else {
                    computerGuessBoard[x][y] = 'V';
                    userGameBoard[x][y] = 'X';
                    System.out.println("That is a hit!");

                    /*Checks if user's battleship has been drowned.*/
                    boolean isDrowned = checkIfDrowned(x, y, n, m, computerGuessBoard, userGameBoard);
                    if(isDrowned) {
                        userNumOfShips--;
                        System.out.println("Your battleship has been drowned, you have left " +
                                userNumOfShips + " more battleships!");
                    }
                }
            }
        }
        while(!isValidAttack);

        /*Prints the user's game board after a valid attack of the computer.*/
        System.out.println("Your current game board:");
        printTheBoard(userGameBoard, n, m);
        return userNumOfShips;
    }

    /**Checks if the computer's attack is valid.
     * @param x Gets the x point rates of the computer's attack.
     * @param y Gets the y point rates of the computer's attack.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param computerGuessBoard Gets the computer's guess board.
     * @return Returns 'True' if computer's attack is valid, else, return 'false'.
     */
    public static boolean computerIsValidAttack(int x, int y, int n, int m, char[][] computerGuessBoard) {
        if(x >= n || y >= m || x < 0 || y < 0) { /*Checks if illegal tile.*/
            return false;
        } else if(computerGuessBoard[x][y] != '\u2013') { /*Checks if tile already attacked.*/
            return false;
        }
        return true;
    }

    /**Checks if the last attack sank a battleship.
     * @param x Gets the x point rates of the last attack.
     * @param y Gets the y point rates of the last attack.
     * @param n Gets the number of board's rows.
     * @param m Gets the number of board's columns.
     * @param guessBoard Gets the user's/computer's guess board.
     * @param gameBoard Gets the user's/computer's game board.
     * @return Returns 'True' if the last attack has drowned a battleship, else, return 'false'.
     */
    public static boolean checkIfDrowned(int x, int y, int n, int m, char[][] guessBoard,
                                         char[][] gameBoard) {
        boolean isTherePartOfShip = true; /*Gets 'true' if there is a part of a ship, else - false.*/

        /*Checks horizontal axis:*/
        /*Checks the right side.*/
        if(y != m - 1){
            for(int i = y + 1; (i < m) && isTherePartOfShip; i++){
                if(gameBoard[x][i] != '\u2013') {
                    if(guessBoard[x][i] != 'V'){
                        return false;
                    }
                } else {
                    isTherePartOfShip = false;
                }
            }
        }
        /*Checks the left side.*/
        isTherePartOfShip = true;
        if(y != 0){
            for(int i = y - 1; (i >= 0) && isTherePartOfShip; i--){
                if(gameBoard[x][i] != '\u2013'){
                    if(guessBoard[x][i] != 'V'){
                        return false;
                    }
                } else {
                    isTherePartOfShip = false;
                }
            }
        }

        /*Checks vertical axis:*/
        /*Checks the under side.*/
        isTherePartOfShip = true;
        if(x != n - 1){
            for(int i = x + 1; (i < n) && isTherePartOfShip; i++){
                if(gameBoard[i][y] != '\u2013'){
                    if(guessBoard[i][y] != 'V'){
                        return false;
                    }
                } else {
                    isTherePartOfShip = false;
                }
            }
        }
        /*Checks the upside.*/
        isTherePartOfShip = true;
        if(x != 0){
            for(int i = x - 1; (i >= 0) && isTherePartOfShip; i--){
                if(gameBoard[i][y] != '\u2013'){
                    if(guessBoard[i][y] != 'V'){
                        return false;
                    }
                } else {
                    isTherePartOfShip = false;
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