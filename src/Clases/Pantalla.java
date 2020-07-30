package Clases;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Pantalla extends JFrame implements ActionListener {

    //**RECORDATORIO** Buscar la forma de hacer un metodo para todo lo que tenga que ver con turno % 2 para simplificar el codigo
    public static int columnas = 7;
    public static int filas = 6;
    public static int jugadorX = 0;
    public static int jugadorO = 0;
    public static int partida = 1;
    public static boolean IA;

    private int contFila[] = new int[columnas]; //Esta variable es un flag para las filas segun la columna a jugar
    private int turno;

    private JButton botones[] = new JButton[columnas];
    private JButton reiniciar = new JButton("Reiniciar");
    private JButton modificarTablero = new JButton("Modificar");

    private JLabel[] cruz;
    private JLabel[] circulo;
    private int cruzContador;
    private int circuloContador;
    private JLabel pantallaTurno;
    private JLabel pantallaContTurno;
    private JLabel pantallaJugadorX;
    private JLabel pantallaJugadorO;

    //-----------------CONSTRUCTOR----------------//
    public Pantalla() {
        // FRAME
        setLayout(null);
        setBounds(0, 0, columnas * 50 + 50, 175 + filas * 50);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setTitle("Tablero 4 en Linea");
        ImageIcon icono = new ImageIcon("src/img/icono.png");
        setIconImage(icono.getImage());

        if (partida % 2 == 0) {
            turno = 1;
            pantallaTurno = new JLabel("Turno jugador O");
        } else {
            turno = 2;
            pantallaTurno = new JLabel("Turno jugador X");
        }

        //Botones                
        int posicionBotonX = 21;
        for (int i = 0; i < columnas; i++) {
            botones[i] = new JButton("↓");
            botones[i].setVisible(true);
            botones[i].setBounds(posicionBotonX, 90, 40, 25);
            botones[i].addActionListener(this);
            add(botones[i]);
            posicionBotonX += 50;
        }

        reiniciar.setBounds(5, 5, 90, 30);
        reiniciar.setVisible(true);
        reiniciar.addActionListener(this);
        add(reiniciar);

        modificarTablero.setBounds(100, 5, 90, 30);
        modificarTablero.setVisible(true);
        modificarTablero.addActionListener(this);
        add(modificarTablero);

        // Labels
        pantallaTurno.setBounds(10, 35, 110, 30);
        add(pantallaTurno);

        pantallaContTurno = new JLabel("Turno Nro " + (turno - 1));
        pantallaContTurno.setBounds(10, 55, 110, 30);
        add(pantallaContTurno);

        pantallaJugadorX = new JLabel("Jugador X: " + jugadorX);
        pantallaJugadorX.setBounds(130, 35, 110, 30);
        add(pantallaJugadorX);

        pantallaJugadorO = new JLabel("Jugador O: " + jugadorO);
        pantallaJugadorO.setBounds(130, 55, 110, 30);
        add(pantallaJugadorO);
        
        //Fichas
        cruz = new JLabel[(columnas * filas) / 2];
        circulo = new JLabel[(columnas * filas) / 2];
        for (int i = 0; i < (columnas * filas) / 2; i++) {
            cruz[i] = new JLabel();
            circulo[i] = new JLabel();
            add(cruz[i]);
            add(circulo[i]);
        }
 
        
        //wallpaper        
        JLabel fondo = new JLabel();
        fondo.setBounds(0,0, this.getWidth(), this.getHeight());
        ImageIcon imagen = new ImageIcon("src/img/wallpaper.jpg");
        Icon wallpaper = new ImageIcon(imagen.getImage().getScaledInstance(fondo.getWidth(),
                fondo.getHeight(), Image.SCALE_DEFAULT));
        fondo.setIcon(wallpaper);
        add(fondo);
    }

    //---------- REINICIAR PANTALLA-----------//
    private void reiniciar() {
        Analizar.Tablero(columnas, filas); //Este metodo reinicia los valores de la variable Matriz en la clase Analizar
        Pantalla reiniciarTablero = new Pantalla();
        reiniciarTablero.setLocationRelativeTo(this);
        reiniciarTablero.setVisible(true);
        dispose();
    }

    //--------------TABLERO-----------------//
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.black);
        int pixel = 50;

        // Filas
        int posicionFilaX = 25;
        int posicionFilaY = 150;
        for (int i = 0; i < filas + 1; i++) {
            g.drawLine(posicionFilaX/*1*/, posicionFilaY/*2*/, posicionFilaX + (pixel * columnas)/*3*/, posicionFilaY/*4*/);
            posicionFilaY += pixel;
        }

        // Columnas
        int posicionColumnaX = 25;
        int posicionColumnaY = 150;
        for (int i = 0; i < columnas + 1; i++) {
            g.drawLine(posicionColumnaX/*1*/, posicionColumnaY/*2*/, posicionColumnaX/*3*/, posicionColumnaY + (pixel * filas))/*4*/;
            posicionColumnaX += pixel;
        }

    }

    //---------------FICHAS TABLERO----------------//
    public void fichas(int columna, int fila) {

        if (turno % 2 == 0) {
            cruz[cruzContador].setBounds(18 + columna * 50, 70 + (50 * filas - fila * 50), 48, 48);
            cruz[cruzContador].setIcon(new ImageIcon("src/Img/cruz.png"));
            cruzContador++;
            pantallaTurno.setText("Turno jugador O");
        } else {
            circulo[circuloContador].setBounds(18 + columna * 50, 70 + (50 * filas - fila * 50), 48, 48);
            circulo[circuloContador].setIcon(new ImageIcon("src/Img/circulo.png"));
            circuloContador++;
            pantallaTurno.setText("Turno jugador X");
        }

    }

    //--------------EVENTO BOTONES-------------------//
    @Override
    public void actionPerformed(ActionEvent e) {
        // -- Botones para colocar las fichas--
        for (int i = 0; i < columnas; i++) {
            if (e.getSource() == botones[i]) {
                if (contFila[i] < filas) {
                    // metodo que coloca la imagen de la ficha segun el turno del jugador
                    fichas(i, contFila[i]);
                    //Pasando a Matriz el valor de la ficha que coloco el jugador segun la posicion y el turno
                    if (turno % 2 == 0) {
                        Analizar.Matriz[i][contFila[i]] = 'x';
                    } else {
                        Analizar.Matriz[i][contFila[i]] = 'o';
                    }
                    // Analizando si ya hubo un ganador
                    Analizar resultado = new Analizar();
                    char ganador = resultado.analisis(columnas, filas);
                    if (ganador == 'x') {
                        ganador(ganador);
                    } else if (ganador == 'o') {
                        ganador(ganador);
                    }
                    //si no lo hubo, sigue el juego
                    contFila[i] ++;
                    turno ++;
                    if (partida % 2 == 0) {
                        pantallaContTurno.setText("Turno Nro " + (turno));
                    } else {
                        pantallaContTurno.setText("Turno Nro " + (turno - 1));
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Esta columna esta llena.");
                }
            }
        }
        // -- Boton para reiniciar el tablero --
        if (e.getSource() == reiniciar) {
            reiniciar();
        }
        // -- Boton para modificar el tablero --
        if (e.getSource() == modificarTablero) {
            Inicio modificar = new Inicio();
            modificar.setVisible(true);
            dispose();
        }
    }
    // METODO para reiniciar el tablero segun el ganador
    private void ganador(char ficha) {
        String turnoDe = "";
        char ganador = ficha;
        if (partida % 2 == 0) {
            turnoDe = "Ahora comienza jugador X";
        } else {
            turnoDe = "Ahora comienza jugador O";
        }
        JOptionPane.showMessageDialog(null, "       Gano jugador " + ganador + "\n" + turnoDe);
        if(ficha == 'x'){
         jugadorX++;   
        } else if (ficha == 'o'){
         jugadorO++;   
        }
        
        partida++;
        reiniciar();
    }
}
