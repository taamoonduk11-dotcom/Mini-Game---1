import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT ->{ if(GamePanel.direction !='R') GamePanel.direction='L';}
            case KeyEvent.VK_RIGHT -> {if(GamePanel.direction !='L') GamePanel.direction='R';}
            case KeyEvent.VK_UP -> {if(GamePanel.direction !='D') GamePanel.direction='U';}
            case KeyEvent.VK_DOWN -> {if(GamePanel.direction !='U') GamePanel.direction='D';}

        }
    }
}
