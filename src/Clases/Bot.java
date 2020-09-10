
package Clases;

import java.util.Random;

public class Bot {
       
    FrontBoard frontBoard;
    BackBoard backBoard;
    Random random;
    
    int rowHeight[];
    char board[][];
    int columns;
    
    public Bot(FrontBoard frontBoard,BackBoard backBoard){
        this.frontBoard = frontBoard;
        this.backBoard = backBoard;
        
        columns = frontBoard.getColumnas();
        random = new Random();

        
        String whoseTurn = frontBoard.getWhoseTurn();
        if(whoseTurn.equals(frontBoard.BOT_PLAYER)){
            play();
        }
    }
    
    public void play(){
        rowHeight = frontBoard.getRowHeight();
        board = backBoard.getBoard();
        
        boolean fullColumn = true;
        while(fullColumn){
            fullColumn = frontBoard.insertToken(random.nextInt(columns));
        }
        
    }
}
