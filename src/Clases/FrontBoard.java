package Clases;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrontBoard extends JFrame implements ActionListener {

    //Constantes
    private String redName;
    private static final char RED_CHAR = 'O';
    private String blueName;
    private final char BLUE_CHAR = 'X';
    private String greenName;
    private final char GREEN_CHAR = 'Z';

    public char BOT_CHAR;
    public String botName;

    private int winingNeed;
    private int players;
    private boolean botOn;

    private static final Icon RED_CIRCLE = new ImageIcon("src/Img/red.png");
    private static final Icon BLUE_CIRCLE = new ImageIcon("src/Img/blue.png");
    private static final Icon GREEN_CIRCLE = new ImageIcon("src/Img/green.png");
    private static final Icon CROSS_ICON = new ImageIcon("src/Img/cruz.png");
    private static final Icon BOARD_SQUARE = new ImageIcon("src/Img/board.png");

    public int bluePlayer = 0;
    public int redPlayer = 0;
    public int greenPlayer = 0;
    public int match = 1;
    public String whoseTurn;
    public String firstTurn;

    // dimension del tablero
    private int columnas;
    private int filas;

    //Match Flags---------------------------------
    private int tokenCounter; //Flag para el numero de fichas utilizadas
    private int[] rowHeight; //Esta array es un flag para la altura de la ficha la columna a jugar   
    private int turnCounter;

    //Componentes
    private MyPanel panels[];
    private JPanel header;
    public JButton buttonReset;
    public JButton buttonModify;

    private JLabel[] token;
    private JLabel[][] board;
    private JLabel[] cross;

    private JLabel screenTurn;
    private JLabel screenTurnCounter;
    private JLabel screenBluePlayer;
    private JLabel screenRedPlayer;
    private JLabel screenGreenPlayer;

    //OBJETOS PROPIOS
    BackBoard backBoard;
    Settings settings;
    Bot bot;

    //Cursor
    private Cursor cursor;

    //-----------------CONSTRUCTOR----------------//
    public FrontBoard() {
        this.cursor = new Cursor(Cursor.HAND_CURSOR);
        // FRAME
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tablero 4 en Linea");
        this.getContentPane().setBackground(Color.white);
        ImageIcon icono = new ImageIcon("src/img/icono.png");
        setIconImage(icono.getImage());
        setLocationRelativeTo(null);
    }

    public void components(int columnas, int filas, int winTokens, boolean restart, boolean botOn) {
        //restart
        //if true -> resets the board within the current match
        //if false -> resets the board and inits a new match with diferent first turn player

        getContentPane().removeAll();
        repaint();

        backBoard.setBoardDimension(columnas, filas);
        backBoard.setTokensNeeded(winTokens);

        setBounds(0, 0, columnas * 50 + 50, 185 + filas * 50);
        setLocationRelativeTo(null);

        this.columnas = columnas;
        this.filas = filas;
        this.winingNeed = winTokens;
        this.botOn = botOn;
        this.turnCounter = 0;
        this.rowHeight = new int[columnas];

        if (players == 2 && botOn) {
            BOT_CHAR = RED_CHAR;
            botName = redName;
        } else if (botOn) {
            BOT_CHAR = GREEN_CHAR;
            botName = greenName;
        }

        //Winner icons
        cross = new JLabel[winingNeed];
        for (int i = 0; i < cross.length; i++) {
            cross[i] = new JLabel();
            cross[i].setIcon(CROSS_ICON);
            add(cross[i]);
        }

        //Fichas
        tokenCounter = 0;
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
        buttonReset = new JButton("Reiniciar");
        buttonReset.setBounds(17, 5, 90, 30);
        buttonReset.setVisible(true);
        buttonReset.addActionListener(this);
        add(buttonReset);

        buttonModify = new JButton("Modificar");
        buttonModify.setBounds(117, 5, 90, 30);
        buttonModify.setVisible(true);
        buttonModify.addActionListener(this);
        add(buttonModify);

        // Labels     
        screenTurnCounter = new JLabel();
        screenTurnCounter.setBounds(17, 30, 180, 30);
        screenTurnCounter.setFont(new Font("Arial", 1, 12));
        screenTurnCounter.setForeground(Color.black);
        add(screenTurnCounter);
        JLabel screenMatchCounter = new JLabel("Partida Nro: " + match);
        screenMatchCounter.setBounds(17, 50, 180, 30);
        screenMatchCounter.setFont(new Font("Arial", 1, 12));
        screenMatchCounter.setForeground(Color.black);
        add(screenMatchCounter);

        screenTurn = new JLabel();
        screenTurn.setBounds(17, 80, 300, 30);
        screenTurn.setFont(new Font("Arial", 1, 16));
        add(screenTurn);

        screenBluePlayer = new JLabel(blueName + ": " + bluePlayer);
        screenBluePlayer.setBounds(215, 0, 180, 30);
        screenBluePlayer.setFont(new Font("Arial", 1, 14));
        screenBluePlayer.setForeground(Color.blue);
        add(screenBluePlayer);

        screenRedPlayer = new JLabel(redName + ": " + redPlayer);
        screenRedPlayer.setBounds(215, 20, 180, 30);
        screenRedPlayer.setFont(new Font("Arial", 1, 14));
        screenRedPlayer.setForeground(Color.red);
        add(screenRedPlayer);

        screenGreenPlayer = new JLabel(greenName + ": " + greenPlayer);
        screenGreenPlayer.setBounds(215, 40, 180, 30);
        screenGreenPlayer.setVisible(players == 3);
        screenGreenPlayer.setFont(new Font("Arial", 1, 14));
        screenGreenPlayer.setForeground(Color.green);
        add(screenGreenPlayer);

        //si restart es falso, hubieron cambios para reiniciar la puntuacion
        if (restart) {
            turnCounter++;
            whoseTurn = firstTurn;

            if (whoseTurn.equals(blueName)) {
                screenTurn.setForeground(Color.blue);
            } else if (whoseTurn.equals(redName)) {
                screenTurn.setForeground(Color.red);
            } else if (whoseTurn.equals(greenName)) {
                screenTurn.setForeground(Color.green);
            }
            screenTurn.setText("Turno de " + whoseTurn);
            screenTurnCounter.setText("Turno Nro " + turnCounter);
        } else {
            nextTurn();
        }

        //header
        header = new JPanel();
        header.setBounds(17, 0, columnas * 50, 115);
        add(header);

        if (whoseTurn.equals(botName) && botOn) {
            bot.play();
        }
    }

    //--------------TABLERO-----------------//
    public void paintBoard() {
        int pixel = 50;
        board = new JLabel[columnas][filas];

        int initX = 17;
        int initY = 80 + filas * pixel;
        int y = initY;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new JLabel();
                board[i][j].setIcon(BOARD_SQUARE);
                board[i][j].setBounds(initX, y, pixel, pixel);
                board[i][j].setCursor(cursor);
                add(board[i][j]);
                y -= pixel;
            }
            initX += pixel;
            y = initY;
        }
    }

    //--------------EVENTO BOTONES-------------------//
    @Override
    public void actionPerformed(ActionEvent e) {

        // -- Boton para reiniciar el tablero --
        if (e.getSource() == buttonReset) {
            restartBoard(true);
        }
        // -- Boton para modificar el tablero --
        if (e.getSource() == buttonModify) {
            settings.setRestart(true);
            settings.setVisible(true);
            dispose();
        }
    }

    //---------------FICHAS TABLERO----------------//
    public void obtainToken(int columna, int fila) {

        token[tokenCounter].setBounds(17 + columna * 50, 80 + (50 * filas - fila * 50), 50, 50);
        if (whoseTurn.equals(blueName)) {

            token[tokenCounter].setIcon(BLUE_CIRCLE);
            backBoard.board[columna][rowHeight[columna]] = BLUE_CHAR;

        } else if (whoseTurn.equals(redName)) {

            token[tokenCounter].setIcon(RED_CIRCLE);
            backBoard.board[columna][rowHeight[columna]] = RED_CHAR;

        } else if (whoseTurn.equals(greenName)) {
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
            //tambien en la matriz del backboard
            obtainToken(column, rowHeight[column]);

            // Analizando si ya hubo un ganador
            char ganador = backBoard.checkTokens(columnas, filas);
            if (ganador == BLUE_CHAR) {
                winner(ganador);
                return false;
            } else if (ganador == RED_CHAR) {
                winner(ganador);
                return false;
            } else if (ganador == GREEN_CHAR) {
                winner(ganador);
                return false;
            } else {
                //si no lo hubo, sigue el juego
                rowHeight[column]++;
                nextTurn();

                if (botOn) {
                    if (whoseTurn.equals(botName)) {
                        bot.play();
                    }
                }
                return false;
            }
        } else {
            //En caso de que el bot inserte en una columna llena
            if (botOn && whoseTurn.equals(botName)) {
                bot.play();
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

        screenTurn.setText("Turno de " + whoseTurn);
        screenTurnCounter.setText("Turno Nro " + ++turnCounter);
    }

    private void twoPlayers() {
        if (turnCounter == 0) {
            //establece el turno dependiendo si recien comienza la partida
            if (firstTurn.equals(blueName)) {
                firstTurn = redName;
                whoseTurn = redName;
                screenTurn.setForeground(Color.red);

            } else if (firstTurn.equals(redName)) {
                firstTurn = blueName;
                whoseTurn = blueName;
                screenTurn.setForeground(Color.blue);
            }
        } else {
            //estable el turno despues de de haber una jugada
            if (whoseTurn.equals(blueName)) {  
                whoseTurn = redName;
                screenTurn.setForeground(Color.red);
            } else if (whoseTurn.equals(redName)) {
                whoseTurn = blueName;
                screenTurn.setForeground(Color.blue);
            }

        }
    }

    private void threePlayers() {
        if (turnCounter == 0) {
            //establece el turno dependiendo si recien comienza la partida
            if (firstTurn.equals(blueName)) {
                firstTurn = redName;
                whoseTurn = redName;
                screenTurn.setForeground(Color.red);
            } else if (firstTurn.equals(redName)) {
                firstTurn = greenName;
                whoseTurn = greenName;
                screenTurn.setForeground(Color.green);
            } else if (firstTurn.equals(greenName)) {
                firstTurn = blueName;
                whoseTurn = blueName;
                screenTurn.setForeground(Color.blue);

            }
        } else {
            //estable el turno despues de de haber una jugada
            if (whoseTurn.equals(blueName)) {
                whoseTurn = redName;
                screenTurn.setForeground(Color.red);
            } else if (whoseTurn.equals(redName)) {
                whoseTurn = greenName;
                screenTurn.setForeground(Color.green);
            } else if (whoseTurn.equals(greenName)) {
                whoseTurn = blueName;
                screenTurn.setForeground(Color.blue);
            }

        }
    }

    //------------------------------------------------------------------------
    // METODO para reiniciar el tablero segun el ganador
    private void winner(char ficha) {
        String turnoDe = "";
        String winner = "";
        if (ficha == RED_CHAR) {
            winner = redName;
        } else if (ficha == BLUE_CHAR) {
            winner = blueName;
        } else if (ficha == GREEN_CHAR) {
            winner = greenName;
        }

        turnoDe = nextPlayerAfterWin();

        //colocando las cruces en las fichas que determinaron al ganador
        for (int i = 0; i < winingNeed; i++) {
            Rectangle bounds = board[backBoard.column_flag[i]][backBoard.row_flag[i]].getBounds();
            bounds.setLocation((int) bounds.getX() + 1, (int) bounds.getY());
            cross[i].setBounds(bounds);
        }

        JOptionPane.showMessageDialog(header, "Gano jugador " + winner + "\n" + turnoDe);

        //aumentando la puntuacion
        if (ficha == BLUE_CHAR) {
            bluePlayer++;
        } else if (ficha == RED_CHAR) {
            redPlayer++;
        } else if (ficha == GREEN_CHAR) {
            greenPlayer++;
        }

        match++;
        restartBoard(false);
    }

    private String nextPlayerAfterWin() {
        String turn = "";
        if (players == 2) {
            if (firstTurn.equals(blueName)) {
                turn = "Ahora comienza " + redName;
            } else if (firstTurn.equals(redName)) {
                turn = "Ahora comienza " + blueName;
            }
        } else {
            if (firstTurn.equals(blueName)) {
                turn = "Ahora comienza  " + redName;
            } else if (firstTurn.equals(redName)) {
                turn = "Ahora comienza  " + greenName;
            } else if (firstTurn.equals(greenName)) {
                turn = "Ahora comienza " + blueName;
            }
        }

        return turn;
    }

    //---------- REINICIAR PANTALLA-----------//
    private void restartBoard(boolean restart) {
        components(columnas, filas, winingNeed, restart, botOn);
        System.gc();
    }

    public void firstMatch() {
        if (players == 2) {
            whoseTurn = redName;
            firstTurn = redName;
            bluePlayer = 0;
            redPlayer = 0;
            greenPlayer = 0;
            match = 1;
        } else {
            whoseTurn = greenName;
            firstTurn = greenName;
            bluePlayer = 0;
            redPlayer = 0;
            greenPlayer = 0;
            match = 1;
        }
    }

    //-------------------GETTERS Y SETTERS----------------------------------
    public void setBot(Bot bot) {
        this.bot = bot;
    }

    public void setBackBoard(BackBoard backBoard) {
        this.backBoard = backBoard;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }

    public String getWhoseTurn() {
        return whoseTurn;
    }

    public int[] getRowHeight() {
        return rowHeight;
    }

    public String getRedName() {
        return redName;
    }

    public void setRedName(String redName) {
        this.redName = redName;
    }

    public String getBlueName() {
        return blueName;
    }

    public void setBlueName(String blueName) {
        this.blueName = blueName;
    }

    public String getGreenName() {
        return greenName;
    }

    public void setGreenName(String greenName) {
        this.greenName = greenName;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

}
