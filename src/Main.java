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
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Enter the board size");
        String boardSize = scanner1.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.split( "X")[0]));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.split( "X")[1]));

        /*creates the boards.*/
        int[][] playersGameBoard = creatingBoards(boardSize, RAW, COLUMN);
        int[][] playersGuessBoard = creatingBoards(boardSize, RAW, COLUMN);
        int[][] computersGameBoard = creatingBoards(boardSize, RAW, COLUMN);
        int[][] computersGuessBoard = creatingBoards(boardSize, RAW, COLUMN);

        /* The number of battleships and their size.*/
        System.out.println("Enter the battleships size");
        String battleshipSizes = scanner1.nextLine();
        /*Sets an array that store the number of battleships and there size.*/
        int[][] arrOfShipsAndSize = setArrOfShipsAndSize(battleshipSizes);

        /*Sets the Location and orientation of user's battleships.*/
        int[][] updatedPlayersGameBoard = placingTheShips(arrOfShipsAndSize, scanner1, RAW, COLUMN, playersGameBoard);
        int[][] updatedComputersGameBoard = placingTheShips(arrOfShipsAndSize, scanner1, RAW, COLUMN, computersGameBoard);

    }

    /**Creates the boards.
     * * We get the board size from the user,
     * * Create 4 boards (2 for the player and 2 for the computer, when there are 2 board types: game and guessing),
     *  * and initialize the boards according to the requirements.
     * @param boardSize Gets the size of the board.
     * @return Returns default board.
     */
    public static int[][] creatingBoards(String boardSize, int RAW, int COLUMN) {
        int[][] board = new int[RAW][COLUMN];
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
     * @param arrOfShipsAndSize Gets array that store the number of battleships and there size.
     * @param scanner1 Used to gets input from the user/
     * @param RAW Number of raws of each board.
     * @param COLUMN Number of column of each board.
     * @param board Gets a game board.
     * @return Returns the updated game board in accordance to the validity of the location of the battleships.
     */
    public static int[][] placingTheShips(int[][] arrOfShipsAndSize, Scanner scanner1, int RAW, int COLUMN, int[][] board){
        boolean massageFlag = true;
        boolean validPosition = true;
        boolean alreadyTaken = true;
        for (int[] ints : arrOfShipsAndSize) {
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
                            if (((y - 2 > 0) && (board[x][y - 2] == '–') || (y - 2 < 0))
                                    && (y + SizeOfShips >= COLUMN || (board[x][y + SizeOfShips] == '–'))){
                                /**checking the upside of the ship.*/
                                if ((x - 1 >= 0) && validPosition) {
                                    if (y - 1 >= 0){
                                        if (y + SizeOfShips < COLUMN) {
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
                                if ((x > y + SizeOfShips) && validPosition) {
                                    for (int k = y - 2; k < (y + SizeOfShips) && validPosition; k++) {
                                        if (board[x][k] == '–') {
                                            validPosition = false;

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



