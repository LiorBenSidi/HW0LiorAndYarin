import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Random rnd;

    public static void battleshipGame() {
        // TODO: Add your code here (and add more methods).
        Scanner scanner1 = new Scanner(System.in);
        System.out.println("Enter the board size");
        String boardSize = scanner1.nextLine();
        final int RAW = Integer.parseInt(String.valueOf(boardSize.charAt(0)));
        final int COLUME = Integer.parseInt(String.valueOf(boardSize.charAt(2)));
        int[][] playersGameBoard = new int[RAW+1][COLUME+1];
        int[][] computersGameBorad = new int[RAW+1][COLUME+1];
        int[][] playersGuessBorad = new int[RAW+1][COLUME+1];
        int[][] computersGuessBorad = new int[RAW+1][COLUME+1];
        for (int i = 0; i< RAW; i++){
            for (int j = 0; j < COLUME; j++){
                playersGameBoard[i][j] = '–';
                playersGuessBorad[i][j] = '–';
                computersGameBorad[i][j] = '–';
                computersGuessBorad[i][j] = '–';
            }
        }

        System.out.println("Enter the battleships size");
        String battleshipSizes = scanner1.nextLine();
        int numberOfBattelships = ((battleshipSizes.length()) / 4) + 1;
        int[][] arr = new int[numberOfBattelships][2];
        int j = 0;
        for (int i = 0; i <= (battleshipSizes.length() - 2); i += 4) {
            arr[j][0] = Integer.parseInt(String.valueOf(battleshipSizes.charAt(i)));
            arr[j][1] = Integer.parseInt(String.valueOf(battleshipSizes.charAt(i + 2)));
            j++;
        }
        boolean massageFlag = true;
        for (int i = 0; i < arr.length; i++) {
            int k = arr[i][0];
            do{
                if(massageFlag) {
                    System.out.println("Enter location and orientation for battleship of size " + arr[i][1]);
                }
                String xyo = scanner1.nextLine();
                int x = Integer.parseInt(String.valueOf(xyo.charAt(0)));
                int y = Integer.parseInt(String.valueOf(xyo.charAt(3)));
                int o = Integer.parseInt(String.valueOf(xyo.charAt(6)));
                if (x > COLUME || y > RAW){
                    System.err.println("Illegal tile, try again!");
                    massageFlag = false;
                } else if (o != 0 && o != 1) {
                    System.err.println("Illegal orientation, try again!");
                    massageFlag = false;
                } else {
                    massageFlag = true;
                    k--;
                }

            }while(k>0);
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



