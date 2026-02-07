import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel{
    public GamePanel(){
        this.setBackground(Color.black);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.white);
        g.fillRect(100,100,10,10);
    }


}