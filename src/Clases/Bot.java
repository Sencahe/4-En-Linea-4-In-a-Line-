package Clases;

import java.util.Random;

public class Bot {

    FrontBoard frontBoard;
    BackBoard backBoard;
    Random random;

    int rowHeight[];
    char board[][];

    public Bot(FrontBoard frontBoard, BackBoard backBoard) {
        this.frontBoard = frontBoard;
        this.backBoard = backBoard;

        random = new Random();

    }

    public void play() {
        int columns = frontBoard.getColumnas();
        rowHeight = frontBoard.getRowHeight();
        board = backBoard.getBoard();

        frontBoard.insertToken(random.nextInt(columns));

    }
}
