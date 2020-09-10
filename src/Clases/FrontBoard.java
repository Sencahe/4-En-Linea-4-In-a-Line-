package Clases;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrontBoard extends JFrame implements ActionListener {

    //Constantes
    private static String RED_PLAYER;
    private static final char RED_CHAR = 'O';
    private static String BLUE_PLAYER;
    private static final char BLUE_CHAR = 'X';
    private static String GREEN_PLAYER;
    private static final char GREEN_CHAR = 'Z';

    private static int winingNeed;
    private static int players;

    public final char BOT_CHAR;
    public final String BOT_PLAYER;

    private static final Icon RED_CIRCLE = new ImageIcon("src/Img/red.png");
    private static final Icon BLUE_CIRCLE = new ImageIcon("src/Img/blue.png");
    private static final Icon GREEN_CIRCLE = new ImageIcon("src/Img/green.png");
    private static final Icon CROSS_ICON = new ImageIcon("src/Img/cruz.png");
    private static final Icon BOARD_SQUARE = new ImageIcon("src/Img/board.png");

    //**RECORDATORIO** Buscar la forma de hacer un metodo para todo lo que tenga que ver con turno % 2 para simplificar el codigo
    public static int bluePlayer = 0;
    public static int redPlayer = 0;
    public static int greenPlayer = 0;
    public static int match = 1;
    public static String whoseTurn;
    public static String firstTurn;

    // dimension del tablero
    private int columnas;
    private int filas;

    //Match Flags---------------------------------
    private int tokenCounter; //Flag para el numero de fichas utilizadas
    private int[] rowHeight; //Esta array es un flag para la altura de la ficha la columna a jugar   
    private int turno;

    //Componentes
    private MyPanel panels[];
    private JPanel header;
    public JButton reiniciar;
    public JButton modificarTablero;

    private JLabel[] token;
    private JLabel[][] board;
    private JLabel[] cross;

    private JLabel pantallaTurno;
    private JLabel pantallaContTurno;
    private JLabel pantallaJugadorX;
    private JLabel pantallaJugadorO;
    private JLabel pantallaJugadorZ;

    //OBJETOS PROPIOS
    BackBoard backBoard;
    Inicio inicio;
    Bot bot;

    //Cursor
    private Cursor cursor;

    //-----------------CONSTRUCTOR----------------//
    public FrontBoard(Inicio inicio, BackBoard backBoard, int columnas, int filas, int win, boolean restart) {
        this.inicio = inicio;
        this.backBoard = backBoard;
        this.backBoard.setBoardDimension(columnas, filas);
        this.columnas = columnas;
        this.filas = filas;
        this.winingNeed = win;
        this.turno = 0;

        this.cursor = new Cursor(Cursor.HAND_CURSOR);
        this.rowHeight = new int[columnas];
        this.bot = null;

        if (players == 2) {
            this.BOT_CHAR = RED_CHAR;
            this.BOT_PLAYER = RED_PLAYER;
        } else {
            this.BOT_CHAR = GREEN_CHAR;
            this.BOT_PLAYER = GREEN_PLAYER;
        }

        // FRAME
        setLayout(null);
        setBounds(0, 0, columnas * 50 + 50, 185 + filas * 50);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tablero 4 en Linea");
        this.getContentPane().setBackground(Color.white);
        ImageIcon icono = new ImageIcon("src/img/icono.png");
        setIconImage(icono.getImage());

        //Winner icons
        cross = new JLabel[winingNeed];
        for (int i = 0; i < cross.length; i++) {
            cross[i] = new JLabel();
            cross[i].setIcon(CROSS_ICON);
            add(cross[i]);
        }

        //Fichas
        token = new JLabel[(columnas * filas)];

        for (int i = 0; i < (columnas * filas); i++) {
            token[i] = new JLabel();
            token[i].setCursor(cursor);
            add(token[i]);
        }

        //board
        paintBoard();

        //panels
        panels = new MyPanel[columnas];
        int posicionPanelX = 17;
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new MyPanel(this, i);
            panels[i].setBounds(posicionPanelX, 130, 50, filas * 50);
            posicionPanelX += 50;
            add(panels[i]);
        }

        //botones
        reiniciar = new JButton("Reiniciar");
        reiniciar.setBounds(17, 5, 90, 30);
        reiniciar.setVisible(true);
        reiniciar.addActionListener(this);
        add(reiniciar);

        modificarTablero = new JButton("Modificar");
        modificarTablero.setBounds(117, 5, 90, 30);
        modificarTablero.setVisible(true);
        modificarTablero.addActionListener(this);
        add(modificarTablero);

        // Labels     
        pantallaContTurno = new JLabel();
        pantallaContTurno.setBounds(17, 30, 180, 30);
        pantallaContTurno.setFont(new Font("Arial", 1, 12));
        pantallaContTurno.setForeground(Color.black);
        add(pantallaContTurno);

        pantallaTurno = new JLabel();
        pantallaTurno.setBounds(17, 80, 300, 30);
        pantallaTurno.setFont(new Font("Arial", 1, 16));
        add(pantallaTurno);

        pantallaJugadorX = new JLabel(BLUE_PLAYER + ": " + bluePlayer);
        pantallaJugadorX.setBounds(215, 0, 180, 30);
        pantallaJugadorX.setFont(new Font("Arial", 1, 14));
        pantallaJugadorX.setForeground(Color.blue);
        add(pantallaJugadorX);

        pantallaJugadorO = new JLabel(RED_PLAYER + ": " + redPlayer);
        pantallaJugadorO.setBounds(215, 20, 180, 30);
        pantallaJugadorO.setFont(new Font("Arial", 1, 14));
        pantallaJugadorO.setForeground(Color.red);
        add(pantallaJugadorO);

        pantallaJugadorZ = new JLabel(GREEN_PLAYER + ": " + greenPlayer);
        pantallaJugadorZ.setBounds(215, 40, 180, 30);
        pantallaJugadorZ.setVisible(players == 3);
        pantallaJugadorZ.setFont(new Font("Arial", 1, 14));
        pantallaJugadorZ.setForeground(Color.green);
        add(pantallaJugadorZ);

        if (restart) {
            turno++;
            whoseTurn = firstTurn;
            System.out.println("entro aca");
            
            if (whoseTurn.equals(BLUE_PLAYER)) {      
                pantallaTurno.setForeground(Color.blue);
            } else if (whoseTurn.equals(RED_PLAYER)) {
                pantallaTurno.setForeground(Color.red);
            } else if (whoseTurn.equals(GREEN_PLAYER)) {
                pantallaTurno.setForeground(Color.green);
            }
            pantallaTurno.setText("Turno de " + whoseTurn);
            pantallaContTurno.setText("Turno Nro " + turno);
        } else {
            nextTurn();
        }

        //header
        header = new JPanel();
        header.setBounds(17, 0, columnas * 50, 115);
        add(header);

    }

    //--------------TABLERO-----------------//
    public void paintBoard() {
        int pixel = 50;
        board = new JLabel[columnas][filas];

        int initX = 17;
        int initY = 80 + filas * 50;
        int y = initY;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new JLabel();
                board[i][j].setIcon(BOARD_SQUARE);
                board[i][j].setBounds(initX, y, 50, 50);
                board[i][j].setCursor(cursor);
                add(board[i][j]);
                y -= 50;
            }
            initX += 50;
            y = initY;
        }
    }

    //--------------EVENTO BOTONES-------------------//
    @Override
    public void actionPerformed(ActionEvent e) {

        // -- Boton para reiniciar el tablero --
        if (e.getSource() == reiniciar) {
            reiniciar(true);
        }
        // -- Boton para modificar el tablero --
        if (e.getSource() == modificarTablero) {
            inicio.setRestart(true);
            inicio.setVisible(true);
            dispose();
        }
    }

    //---------------FICHAS TABLERO----------------//
    public void obtainToken(int columna, int fila) {

        token[tokenCounter].setBounds(17 + columna * 50, 80 + (50 * filas - fila * 50), 50, 50);
        if (whoseTurn.equals(BLUE_PLAYER)) {

            token[tokenCounter].setIcon(BLUE_CIRCLE);
            backBoard.board[columna][rowHeight[columna]] = BLUE_CHAR;

        } else if (whoseTurn.equals(RED_PLAYER)) {

            token[tokenCounter].setIcon(RED_CIRCLE);
            backBoard.board[columna][rowHeight[columna]] = RED_CHAR;

        } else if (whoseTurn.equals(GREEN_PLAYER)) {
            token[tokenCounter].setIcon(GREEN_CIRCLE);
            backBoard.board[columna][rowHeight[columna]] = GREEN_CHAR;
        }

        tokenCounter++;

    }

    //Metodo que es llamado desde el los MouseListener de la clase MyPanel que extiende de JPanel.
    //Cada panel representa el fondo de las columnas en el tablero
    public boolean insertToken(int column) {
        if (rowHeight[column] < filas) {
            // metodo que coloca la imagen de la ficha segun el turno del jugador
            //tmb en la matriz del backboard
            obtainToken(column, rowHeight[column]);

            // Analizando si ya hubo un ganador
            char ganador = backBoard.checkTokens(columnas, filas);
            if (ganador == BLUE_CHAR) {
                ganador(ganador);
                return false;
            } else if (ganador == RED_CHAR) {
                ganador(ganador);
                return false;
            } else if (ganador == GREEN_CHAR) {
                ganador(ganador);
                return false;
            } else {
                //si no lo hubo, sigue el juego
                rowHeight[column]++;
                nextTurn();

                if (bot != null) {
                    if (whoseTurn.equals(BOT_PLAYER)) {
                        bot.play();
                    }
                }
                return false;
            }
        } else {
            if (bot != null && whoseTurn.equals(BOT_PLAYER)) {
                //no hace nada, sigue intentado hasta que inserte en una columna no llena
            } else {
                JOptionPane.showMessageDialog(null, "Esta columna esta llena.");
            }
            return true;
        }
    }

    //----------------------------TURNOS----------------------------------------
    private void nextTurn() {
        if (players == 2) {
            twoPlayers();
        } else {
            threePlayers();
        }

        pantallaTurno.setText("Turno de " + whoseTurn);
        pantallaContTurno.setText("Turno Nro " + ++turno);
    }

    private void twoPlayers() {
        if (turno == 0) {
            //establece el turno dependiendo si recien comienza la partida
            if (firstTurn.equals(BLUE_PLAYER)) {
                firstTurn = RED_PLAYER;
                whoseTurn = RED_PLAYER;
                pantallaTurno.setForeground(Color.red);

            } else if (firstTurn.equals(RED_PLAYER)) {
                firstTurn = BLUE_PLAYER;
                whoseTurn = BLUE_PLAYER;
                pantallaTurno.setForeground(Color.blue);
            }
        } else {
            //estable el turno despues de de haber una jugada
            if (whoseTurn.equals(BLUE_PLAYER)) {
                whoseTurn = RED_PLAYER;
                pantallaTurno.setForeground(Color.red);
            } else if (whoseTurn.equals(RED_PLAYER)) {
                whoseTurn = BLUE_PLAYER;
                pantallaTurno.setForeground(Color.blue);
            }

        }
    }

    private void threePlayers() {
        if (turno == 0) {
            //establece el turno dependiendo si recien comienza la partida
            if (firstTurn.equals(BLUE_PLAYER)) {
                firstTurn = RED_PLAYER;
                whoseTurn = RED_PLAYER;
                pantallaTurno.setForeground(Color.red);
            } else if (firstTurn.equals(RED_PLAYER)) {
                firstTurn = GREEN_PLAYER;
                whoseTurn = GREEN_PLAYER;
                pantallaTurno.setForeground(Color.green);
            } else if (firstTurn.equals(GREEN_PLAYER)) {
                firstTurn = BLUE_PLAYER;
                whoseTurn = BLUE_PLAYER;
                pantallaTurno.setForeground(Color.blue);

            }
        } else {
            //estable el turno despues de de haber una jugada
            if (whoseTurn.equals(BLUE_PLAYER)) {
                whoseTurn = RED_PLAYER;
                pantallaTurno.setForeground(Color.red);
            } else if (whoseTurn.equals(RED_PLAYER)) {
                whoseTurn = GREEN_PLAYER;
                pantallaTurno.setForeground(Color.green);
            } else if (whoseTurn.equals(GREEN_PLAYER)) {
                whoseTurn = BLUE_PLAYER;
                pantallaTurno.setForeground(Color.blue);
            }

        }
        System.out.println(firstTurn);
        System.out.println(whoseTurn);
    }

    //------------------------------------------------------------------------
    // METODO para reiniciar el tablero segun el ganador
    private void ganador(char ficha) {
        String turnoDe = "";
        String winner = "";
        if (ficha == RED_CHAR) {
            winner = RED_PLAYER;
        } else if (ficha == BLUE_CHAR) {
            winner = BLUE_PLAYER;
        } else if (ficha == GREEN_CHAR) {
            winner = GREEN_PLAYER;
        }

        turnoDe = nextPlayerAfterWin();

        for (int i = 0; i < winingNeed; i++) {
            Rectangle bounds = board[BackBoard.column_flag[i]][BackBoard.row_flag[i]].getBounds();
            bounds.setLocation((int) bounds.getX() + 1, (int) bounds.getY());
            cross[i].setBounds(bounds);
        }

        JOptionPane.showMessageDialog(header, "Gano jugador " + winner + "\n" + turnoDe);
        if (ficha == BLUE_CHAR) {
            bluePlayer++;
        } else if (ficha == RED_CHAR) {
            redPlayer++;
        } else if (ficha == GREEN_CHAR) {
            greenPlayer++;
        }

        match++;
        reiniciar(false);
    }

    private String nextPlayerAfterWin() {
        String turn = "";
        if (players == 2) {
            if (firstTurn.equals(BLUE_PLAYER)) {
                turn = "Ahora comienza " + RED_PLAYER;
            } else if (firstTurn.equals(RED_PLAYER)) {
                turn = "Ahora comienza " + BLUE_PLAYER;
            }
        } else {
            if (firstTurn.equals(BLUE_PLAYER)) {
                turn = "Ahora comienza  " + RED_PLAYER;
            } else if (firstTurn.equals(RED_PLAYER)) {
                turn = "Ahora comienza  " + GREEN_PLAYER;
            } else if (firstTurn.equals(GREEN_PLAYER)) {
                turn = "Ahora comienza " + BLUE_PLAYER;
            }
        }

        return turn;
    }

    //---------- REINICIAR PANTALLA-----------//
    private void reiniciar(boolean restart) {
        FrontBoard front = new FrontBoard(inicio, backBoard, columnas, filas, winingNeed, restart);
        if (this.bot != null) {
            Bot bot = new Bot(front, backBoard);
            front.setBot(bot);
        }
        front.setLocationRelativeTo(this);
        front.setVisible(true);
        front = null;
        System.gc();
        dispose();

    }

    //-------------------GETTERS Y SETTERS----------------------------------
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }

    public static String getWhoseTurn() {
        return whoseTurn;
    }

    public int[] getRowHeight() {
        return rowHeight;
    }

    public static String getRED_PLAYER() {
        return RED_PLAYER;
    }

    public static void setRED_PLAYER(String RED_PLAYER) {
        FrontBoard.RED_PLAYER = RED_PLAYER;
    }

    public static String getBLUE_PLAYER() {
        return BLUE_PLAYER;
    }

    public static void setBLUE_PLAYER(String BLUE_PLAYER) {
        FrontBoard.BLUE_PLAYER = BLUE_PLAYER;
    }

    public static String getGREEN_PLAYER() {
        return GREEN_PLAYER;
    }

    public static void setGREEN_PLAYER(String GREEN_PLAYER) {
        FrontBoard.GREEN_PLAYER = GREEN_PLAYER;
    }

    public static void setPlayers(int players) {
        FrontBoard.players = players;
    }


    
    
    
    public static void firstMatch() {
        if (players == 2) {
            whoseTurn = RED_PLAYER;
            firstTurn = RED_PLAYER;
            bluePlayer = 0;
            redPlayer = 0;
            greenPlayer = 0;         
        } else {
            whoseTurn = GREEN_PLAYER;
            firstTurn = GREEN_PLAYER;
            bluePlayer = 0;
            redPlayer = 0;
            greenPlayer = 0;
        }
    }

}
