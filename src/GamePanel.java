import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements java.awt.event.ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE=25;
    int x[]=new int[600];
    int y[]=new int[600];
    int appleX;
    int appleY;
    int bodyParts=6;
    int blinkCounter=0;

    public static char direction='R';
    boolean running = false;
    Timer timer;
    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.white);
        this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        startGame();
    }

    public void startGame(){
        newApple();
        running =true;

        for (int i=0;i<bodyParts;i++){
            x[i]=100 -(i*UNIT_SIZE);
            y[i]=100;
        }

        timer = new Timer(100, e ->{
            if(running){
                move();
                checkApple();
                checkCollisions();
            }
            else{
                blinkCounter++;
            }
            repaint();
        });
        timer.start();
    }
    public void newApple(){
        appleX= new java.util.Random().nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY= new java.util.Random().nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        System.out.println("Yeni elma burada: " + appleX + "," + appleY);
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        g.setColor(new Color(30, 30, 30));
        for(int i=0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }

        if(running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);


            for(int i=0; i<bodyParts; i++) {
                if(i==0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            gameOver(g);
        }

        g.setColor(new Color(104, 31, 31));
        g.setFont(new Font("Arial", Font.BOLD, 25));
        g.drawString("Skor: " + (bodyParts - 6), 15, 30);
    }
    public void gameOver(Graphics g){
        if(blinkCounter%2==0) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 70));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        }
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

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
        bodyParts++;
        newApple();
    } }
    public void checkCollisions(){
        for(int i=bodyParts;i>0;i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running =false;
            }
        }
        if(x[0] < 0) running = false;
        if(x[0] >=SCREEN_WIDTH) running =false;
        if(y[0] < 0) running =false;
        if(y[0] >=SCREEN_HEIGHT) running =false;

        if(!running){

            System.out.println("Game Over: please try again");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
}