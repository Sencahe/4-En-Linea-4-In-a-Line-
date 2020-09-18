/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Settings extends JFrame implements ActionListener {

    boolean changes;
    JButton buttonStart;
    JComboBox players, winTokens;
    JComboBox row;
    JComboBox column;
    JCheckBox checkBot;
    JTextField textRed, textBlue, textGreen;
    JLabel labelBlue, labelRed, labelGreen, labelInfo;
    String redName, blueName, greenName;
    int wining;
    int winingFlag;
    boolean restart;
    
    FrontBoard frontBoard;

    @SuppressWarnings("unchecked")
    public Settings() {
        this.checkBot = new JCheckBox("Bot");
        this.column = new JComboBox();
        this.row = new JComboBox();
        this.buttonStart = new JButton("Empezar");
        this.restart = false;
        this.changes = true;
        redName = "";
        greenName = "";
        blueName = "";

        setLayout(null);
        setTitle("4 en Linea");
        setBounds(0, 0, 300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ImageIcon icono = new ImageIcon("src/img/icono.png");
        setIconImage(icono.getImage());

        //cantidad de jugadores
        JLabel labelPlayers = new JLabel("Nro de Jugadores");
        labelPlayers.setBounds(40, 20, 90, 25);
        add(labelPlayers);
        players = new JComboBox();
        players.setBounds(50, 45, 70, 25);
        players.addItem(2);
        players.addItem(3);
        players.addActionListener(this);
        add(players);

        //fichas para ganar
        JLabel labelWinTokens = new JLabel("Fichas para ganar");
        labelWinTokens.setBounds(145, 20, 90, 25);
        add(labelWinTokens);
        winTokens = new JComboBox();
        winTokens.setBounds(150, 45, 70, 25);
        winTokens.addItem(3);
        winTokens.addItem(4);
        winTokens.addItem(5);
        winTokens.setSelectedIndex(1);
        add(winTokens);

        // Establecer columnas
        JLabel labelColumn = new JLabel("Columnas");
        labelColumn.setBounds(50, 80, 70, 25);
        labelColumn.setVisible(true);
        add(labelColumn);
        column.setBounds(50, 100, 70, 25);
        column.setVisible(true);
        for (int i = 0; i < 9; i++) {
            column.addItem(7 + i);
        }
        add(column);

        //Establecer filas
        JLabel labelFila = new JLabel("Filas");
        labelFila.setBounds(150, 80, 70, 25);
        labelFila.setVisible(true);
        add(labelFila);
        row.setBounds(150, 100, 70, 25);
        row.setVisible(true);
        for (int i = 0; i < 7; i++) {
            row.addItem(6 + i);
        }
        add(row);

        //jugar con bot
        checkBot.setBounds(125, 130, 200, 25);
        checkBot.setFocusPainted(false);
        checkBot.setOpaque(false);
        checkBot.addActionListener(this);
        add(checkBot);

        //TEXTFIELD
        labelBlue = new JLabel("Nombre Azul: ");
        labelBlue.setBounds(10, 160, 80, 22);
        add(labelBlue);
        textBlue = new JTextField();
        textBlue.setBounds(85, 160, 170, 22);
        add(textBlue);

        labelRed = new JLabel("Nombre Rojo: ");
        labelRed.setBounds(10, 190, 80, 22);
        add(labelRed);
        textRed = new JTextField("");
        textRed.setBounds(85, 190, 170, 22);
        add(textRed);

        labelGreen = new JLabel("Nombre Verde: ");
        labelGreen.setBounds(10, 220, 80, 22);
        labelGreen.setVisible(false);
        add(labelGreen);
        textGreen = new JTextField();
        textGreen.setBounds(85, 220, 170, 22);
        textGreen.setVisible(false);
        add(textGreen);

        // Boton para comenzar partida
        buttonStart.setBounds(85, 260, 90, 30);
        buttonStart.setVisible(true);
        buttonStart.addActionListener(this);
        add(buttonStart);

        labelInfo = new JLabel("<html><center>"
                + "Si durante la partida hace modificaciones al tablero, sepa"
                + " que cambiar la cantidad de jugadores o agregar un bot "
                + "reiniciara la puntuacion."
                + "</center></html>");
        labelInfo.setBounds(10, 280, 270, 100);
        add(labelInfo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonStart) {
            if (validation()) {
                int c =  (int)column.getSelectedItem();
                int f = (int) row.getSelectedItem();              

                frontBoard.setRedName(redName);
                frontBoard.setBlueName(blueName);
                frontBoard.setGreenName(greenName);
                
                if (changes) {
                    frontBoard.setPlayers((int) players.getSelectedItem());
                    frontBoard.firstMatch();
                    restart = false;
                    changes = false;
                }
                
                frontBoard.components(c, f, (int)winTokens.getSelectedItem(), restart, checkBot.isSelected());
                frontBoard.setVisible(true);    

                System.gc();
                dispose();

            } else {
                JOptionPane.showMessageDialog(null, "Los nombres de los jugadores son obligatorios y no pueden ser iguales entre si.");
            }

        }

        //-------------------------------
        if (e.getSource() == players) {
            if ((int) players.getSelectedItem() == 2) {
                checkBot.setVisible(true);
                checkBot.setSelected(false);
                textRed.setText("");
                textRed.setEnabled(true);
                textGreen.setVisible(false);
                labelGreen.setVisible(false);
                changes = true;
            } else {
                
                textGreen.setVisible(true);
                labelGreen.setVisible(true);
                String text = textRed.getText();
                if (text.equals("Bot")) {
                    textRed.setText("");
                    textRed.setEnabled(true);
                }
                text = textGreen.getText();
                if(text.equals("Bot")){
                    textGreen.setText("");
                    textGreen.setEnabled(true);
                }
                checkBot.setSelected(false);
                changes = true;
            }
        }
        if (e.getSource() == checkBot) {
            if (checkBot.isSelected()) {
                if ((int) players.getSelectedItem() == 2) {
                    textRed.setText("Bot");
                    textRed.setEnabled(false);
                    changes = true;
                } else {
                    textGreen.setText("Bot");
                    textGreen.setEnabled(false);
                    changes = true;
                }

            } else {
                if((int) players.getSelectedItem() == 2){
                    textRed.setText("");
                    textRed.setEnabled(true);
                    changes = true;
               
                } else {
                    textGreen.setText("");
                    textGreen.setEnabled(true);
                    changes = true;
                }

            }
        }
    }

    private boolean validation() {
        int numOfPlayers = (int) players.getSelectedItem();
        if (numOfPlayers == 2) {

            String red = textRed.getText();
            String blue = textBlue.getText();

            if (red.isEmpty() || blue.isEmpty()) {
                return false;
            } else {
                if (red.equals(blue)) {
                    return false;
                } else {
                    redName = red;
                    blueName = blue;
                    return true;
                }
            }

        } else {

            String red = textRed.getText();
            String blue = textBlue.getText();
            String green = textGreen.getText();

            if (red.isEmpty() || blue.isEmpty() || green.isEmpty()) {
                return false;
            } else {
                if (red.equals(blue) || red.equals(green) || blue.equals(green)) {
                    return false;
                } else {
                    redName = red;
                    blueName = blue;
                    greenName = green;
                    return true;
                }

            }

        }
    }

    //------------------------GETTERS Y SETTERS-------------------------------
    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    public void setFrontBoard(FrontBoard frontBoard) {
        this.frontBoard = frontBoard;
    }

    
}
