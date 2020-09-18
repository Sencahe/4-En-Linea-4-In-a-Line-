package Clases;

import java.util.Arrays;



public class BackBoard {

    public char[][] board;
    // Columnas con Filas -> [c][f]

    public int column_flag[] = new int[6];
    public int row_flag[] = new int[6];

    private int contX;
    private int contO;
    private int contZ;
    private int tokensNeeded;


    public BackBoard() {

    }

    public void setBoardDimension(int c, int f) {
        if (c > f) {
            board = new char[c][c];
        } else {
            board = new char[f][f];
        }
    }

    //----------------METODO PARA RECORRER LA MATRIZ----------------------------
    public char checkTokens(int columna, int fila) {
        print();
        char resultado;

        resetCounter();
        //ANALIZAR POR FILAS-------------------------
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                resultado = contador(board[j][i], j, i);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            resetCounter();
        }
        //ANALIZAR POR COLUMNAS-----------------------        
        for (int i = 0; i < columna; i++) {
            for (int j = 0; j < fila; j++) {
                resultado = contador(board[i][j], i, j);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            resetCounter();
        }
        //ANALIZAR POR DIAGONALES---------------------
        int auxColum = columna;
        int auxColumReverso = columna - 1;
        int auxFila = fila;
        int auxFilaReverso = fila;
        for (int i = 0; i < board.length; i++) {

            resetCounter();
            //diagonales por columnas -> mitad inferior del tablero  
            for (int j = 0; j < auxColum; j++) {
                resultado = contador(board[j + i][j], j + i, j);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            auxColum--;
            //diagonales por filas -> mitad superior del tablero 

            resetCounter();
            for (int j = 0; j < auxFila; j++) {
                resultado = contador(board[j][j + i], j, j + i);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            auxFila--;

            //------REVERSO------
            ////diagonales por columnas -> mitad inferior del tablero  
            resetCounter();
            int k = 0;
            for (int j = auxColumReverso; j >= 0; j--) {
                resultado = contador(board[j][k], j, k);
                if (resultado != ' ') {
                    return resultado;
                }
                k++;
            }
            auxColumReverso--;
            ////diagonales por filas -> mitad superior del tablero   
            resetCounter();
            int m = board.length - 1;
            for (int j = 0; j < auxFilaReverso; j++) {
                resultado = contador(board[m][j + i], m, j + i);
                if (resultado != ' ') {
                    return resultado;
                }
                m--;
            }
            auxFilaReverso--;
        }
        //no gano nadie
        return ' ';
    }

//----------------METODO PARA DETERMINAR GANADOR--------------------------------
    public char contador(char ficha, int col, int row) {
        switch (ficha) {
            case 'X':
                row_flag[contX] = row;
                column_flag[contX] = col;
                contX += 1;
                contO = 0;
                contZ = 0;
                break;
            case 'O':
                row_flag[contO] = row;
                column_flag[contO] = col;
                contO += 1;
                contX = 0;
                contZ = 0;
                break;
            case 'Z':
                row_flag[contZ] = row;
                column_flag[contZ] = col;
                contZ += 1;
                contO = 0;
                contX = 0;
                break;
            default:
                Arrays.fill(row_flag, -1);
                Arrays.fill(column_flag, -1);
                resetCounter();
                break;
        }
        // determinar ganador
        if (contX == tokensNeeded) {
            return 'X';
        } else if (contO == tokensNeeded) {
            return 'O';
        }else if (contZ == tokensNeeded) {           
            return 'Z';
        } else {
            return ' ';
        }

    }

    public void resetCounter() {
        contX = 0;
        contO = 0;
        contZ = 0;
    }

    //consola
    private void print() {
        int dim = board.length - 1;

        for (int i = dim; i > -1; i--) {
            System.out.println("");
            for (int j = 0; j < dim + 1; j++) {
                System.out.print(board[j][i] == '\u0000' ? "- " : board[j][i] + " ");
            }
        }
        System.out.println("");
        System.out.println("");
        System.out.println("-------------------------");
    }

    //-------------------GETTERS AND SETTERS------------------------------------
    public char[][] getBoard() {
        return board;
    }

    public void setTokensNeeded(int tokensNeeded) {
        this.tokensNeeded = tokensNeeded;
    }
    
}
