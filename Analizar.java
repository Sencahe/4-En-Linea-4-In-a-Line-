package Clases;

public class Analizar {

    public static char Matriz[][];
    private int contX;
    private int contO;
    private int ganador = 4;

    public static void Tablero(int c, int f) {
        if (c > f) {
            Matriz = new char[c][c];
        } else {
            Matriz = new char[f][f];
        }
    }

    //----------------METODO PARA RECORRER LA MATRIZ----------------------------
    public char analisis(int columna, int fila) {
        contX = 0;
        contO = 0;
        char resultado;
        //ANALIZAR POR FILAS-------------------------
        for (int i = 0; i < fila; i++) {
            contX = 0;
            contO = 0;
            for (int j = 0; j < columna; j++) {
                resultado = contador(Matriz[j][i]);
                if (resultado != ' ') {
                    return resultado;
                }
            }
        }
        //ANALIZAR POR COLUMNAS-----------------------        
        for (int i = 0; i < columna; i++) {
            contX = 0;
            contO = 0;
            for (int j = 0; j < fila; j++) {
                resultado = contador(Matriz[i][j]);
                if (resultado != ' ') {
                    return resultado;
                }
            }
        }
        //ANALIZAR POR DIAGONALES---------------------
        int auxColum = columna;
        int auxColumReverso = columna - 1;       
        int auxFila = fila;
        int auxFilaReverso = fila;
        for (int i = 0; i < Matriz.length; i++) {
            contX = 0;
            contO = 0;
            //diagonales por columnas -> mitad inferior del tablero  
            for (int j = 0; j < auxColum; j++) {
                resultado = contador(Matriz[j + i][j]);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            auxColum--;
            //diagonales por filas -> mitad superior del tablero 
            contX = 0;
            contO = 0;
            for (int j = 0; j < auxFila; j++) {
                resultado = contador(Matriz[j][j + i]);
                if (resultado != ' ') {
                    return resultado;
                }
            }
            auxFila--;

            //------REVERSO------
            ////diagonales por columnas -> mitad inferior del tablero  
            contX = 0;
            contO = 0;
            int k = 0;
            for (int j = auxColumReverso; j >= 0; j--) {
                resultado = contador(Matriz[j][k]);
                if (resultado != ' ') {
                    return resultado;
                }
                k++;
            }
            auxColumReverso--;
            ////diagonales por filas -> mitad superior del tablero   
            contX = 0;
            contO = 0;
            int m = Matriz.length - 1;
            for (int j = 0; j < auxFilaReverso; j++) {
                resultado = contador(Matriz[m][j + i]);
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
    public char contador(char ficha) {
        switch (ficha) {
            case 'x':
                contX += 1;
                contO = 0;
                break;
            case 'o':
                contO += 1;
                contX = 0;
                break;
            default:
                contX = 0;
                contO = 0;
                break;
        }
        // determinar ganador
        if (contX == ganador) {
            return 'x';
        } else if (contO == ganador) {
            return 'o';
        } else {
            return ' ';
        }

    }
}
