
package Clases;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;


public class MyPanel extends JPanel implements MouseListener{
    
    private int index;
    Color hover;
    Color notHover;
    FrontBoard pantalla;
    
    public MyPanel(FrontBoard pantalla, int index){
        this.index = index;
        this.pantalla = pantalla;
        this.notHover = this.getBackground();
        this.hover = new Color(0,0,200,70);  
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        pantalla.insertToken(index);
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {        
        setBackground(hover);
        pantalla.repaint();
        

    }

    @Override
    public void mouseExited(MouseEvent e) {       
        setBackground(notHover);
        pantalla.repaint();
    }
    
}
