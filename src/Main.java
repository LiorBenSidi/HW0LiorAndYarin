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
        String boardSize = scanner1.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.charAt(0)));
        final int COLUMN = Integer.parseInt(String.valueOf(boardSize.charAt(2)));
        int[][] playersGameBoard = new int[RAW+1][COLUMN+1];
        int[][] playersGuessBoard = new int[RAW+1][COLUMN+1];
        int[][] computersGameBoard = new int[RAW+1][COLUMN+1];
        int[][] computersGuessBoard = new int[RAW+1][COLUMN+1];
        for (int i = 1; i < RAW + 1; i++){
            for (int j = 1; j < COLUMN + 1; j++){
                playersGameBoard[i][j] = '–';
                playersGuessBoard[i][j] = '–';
                computersGameBoard[i][j] = '–';
                computersGuessBoard[i][j] = '–';
            }
        }
          // TODO: Inserting an empty space at position 0,0 of each board.
//        String emptySpace = " ";
//        playersGameBoard[0][0] = Integer.parseInt(String.valueOf(emptySpace.charAt(0)));
//        playersGuessBoard[0][0] = Integer.parseInt(String.valueOf(emptySpace.charAt(0)));
//        computersGameBoard[0][0] = Integer.parseInt(String.valueOf(emptySpace.charAt(0)));
//        computersGuessBoard[0][0] = Integer.parseInt(String.valueOf(emptySpace.charAt(0)));
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

        /** The number of battleships and their size.*/
        /**<p>*/System.out.println("Enter the battleships size");
        String battleshipSizes = scanner1.nextLine();
        int numberOfBattleships = ((battleshipSizes.length()) / 4) + 1;
        int[][] arr = new int[numberOfBattleships][2];
        int j = 0;
        for (int i = 0; i <= (battleshipSizes.length() - 2); i += 4) {
            arr[j][0] = Integer.parseInt(String.valueOf(battleshipSizes.charAt(i)));
            arr[j][1] = Integer.parseInt(String.valueOf(battleshipSizes.charAt(i + 2)));
            j++;
        }

        /**Location of user submarines.*/
        /**<p>*/boolean massageFlag = true;
        for (int[] ints : arr) {
            int numOfShipsOfTheSameSize = ints[0];
            int SizeOfShips = ints[1];
            do {
                if (massageFlag) {
                    System.out.println("Enter location and orientation for battleship of size " + SizeOfShips);
                }
                String xyo = scanner1.nextLine();
                int x = Integer.parseInt(String.valueOf(xyo.charAt(0)));
                int y = Integer.parseInt(String.valueOf(xyo.charAt(3)));
                int o = Integer.parseInt(String.valueOf(xyo.charAt(6)));
                if (x > COLUMN || y > RAW) {
                    System.err.println("Illegal tile, try again!");
                    massageFlag = false;
                } else if (o != 0 && o != 1) {
                    System.err.println("Illegal orientation, try again!");
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    if (o == 0){
                        for (int i = y + 1; i < (y + SizeOfShips + 1); i++){
                            playersGameBoard[x + 1][i] = '#';
                        }
                    }
                    if (o == 1){
                        for (int i = x + 1; i < (x + SizeOfShips + 1); i++){
                            playersGameBoard[i][y] = '#';
                        }
                    }
                    numOfShipsOfTheSameSize--;
                    // TODO: Enforce the integrity of battleship's location through conditions.
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



