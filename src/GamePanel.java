import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GamePanel extends JPanel implements java.awt.event.ActionListener{
    static final int UNIT_SIZE=25;
    int x[]=new int[600];
    int y[]=new int[600];
    int bodyParts=6;
    public static char direction='R';
    boolean running = false;
    javax.swing.Timer timer;
    public GamePanel(){
        this.setBackground(Color.black);
        this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        startGame();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        for(int i=0;i<bodyParts;i++){
            if(i==0){
                g.setColor(Color.green);
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }
            else{
                g.setColor(new Color(45,170,0));
                g.fillRect(x[i],y[i],UNIT_SIZE,UNIT_SIZE);
            }
        }
    }
    public void startGame(){
        running =true;

        for (int i=0;i<bodyParts;i++){
            x[i]=100 -(i*UNIT_SIZE);
            y[i]=100;
        }

        timer =new javax.swing.Timer(150,this);
        timer.start();
    }
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){
            case 'U' ->y[0]= y[0] - UNIT_SIZE;
                case 'D'->y[0]=y[0] + UNIT_SIZE;
                    case 'L'->x[0]=x[0]-UNIT_SIZE;
                        case 'R'->x[0]=x[0] + UNIT_SIZE;
        }
    }
    public void checkApple(){ }
    public void checkCollisions(){ }
}