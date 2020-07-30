/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Inicio extends JFrame implements ActionListener {

    JButton arrancar = new JButton("Empezar");
    JComboBox fila = new JComboBox();
    JComboBox column = new JComboBox();

    public Inicio() {
        setLayout(null);
        setTitle("4 en Linea");
        setBounds(0, 0, 300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        ImageIcon icono = new ImageIcon("src/img/icono.png");
        setIconImage(icono.getImage());

        // Establecer columnas
        JLabel labelColumn = new JLabel("Columnas");
        labelColumn.setBounds(50, 30, 70, 25);
        labelColumn.setVisible(true);
        add(labelColumn);
        column.setBounds(50, 50, 70, 25);
        column.setVisible(true);
        for (int i = 0; i < 9; i++) {
            column.addItem(7 + i);
        }
        add(column);

        //Establecer filas
        JLabel labelFila = new JLabel("Filas");
        labelFila.setBounds(150, 30, 70, 25);
        labelFila.setVisible(true);
        add(labelFila);
        fila.setBounds(150, 50, 70, 25);
        fila.setVisible(true);
        for (int i = 0; i < 7; i++) {
            fila.addItem(6 + i);
        }
        add(fila);

        // Boton para comenzar partida
        arrancar.setBounds(85, 100, 90, 30);
        arrancar.setVisible(true);
        arrancar.addActionListener(this);
        add(arrancar);

        //wallpaper        
        JLabel fondo = new JLabel();
        fondo.setBounds(0, 0, this.getWidth(), this.getHeight());
        ImageIcon imagen = new ImageIcon("src/img/wallpaper.jpg");
        Icon wallpaper = new ImageIcon(imagen.getImage().getScaledInstance(fondo.getWidth(),
                fondo.getHeight(), Image.SCALE_DEFAULT));
        fondo.setIcon(wallpaper);
        add(fondo);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == arrancar) {
            int c = column.getSelectedIndex() + 7;
            int f = fila.getSelectedIndex() + 6;

            Analizar.Tablero(c, f);
            Pantalla.columnas = c;
            Pantalla.filas = f;

            Pantalla pantalla = new Pantalla();
            pantalla.setVisible(true);
            dispose();

        }
    }

    

}
