import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.sql.*;
import javax.swing.JOptionPane;


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
    Connection baglanti;
    String aktifOyuncu;


    public static char direction='R';
    boolean running = false;
    Timer timer;
    public GamePanel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.white);
        this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        try{
            String url = "jdbc:sqlite:C:\\Users\\Bilal\\Desktop\\SneakGame.db\\Sneak_Game.db";
            baglanti = DriverManager.getConnection(url);
            aktifOyuncu = JOptionPane.showInputDialog("Sneak Game' e Hoş Geldin! Adın ne?");
            if(aktifOyuncu != null && !aktifOyuncu.trim().isEmpty()){
                oyuncuKaydet(baglanti,aktifOyuncu);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

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

            timer.stop();

            skorGuncelle(baglanti, aktifOyuncu, bodyParts - 6);
            String liderler = liderlikTablosuGetir(baglanti);
            JOptionPane.showMessageDialog(null,liderler,"Zirvedekiler",JOptionPane.INFORMATION_MESSAGE);
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
    public void oyuncuKaydet(Connection baglanti,String isim){
        String sql="INSERT OR IGNORE INTO oyuncular(kullanici_adi) VALUES (?)";
        try(PreparedStatement pstmt = baglanti.prepareStatement(sql)){
            pstmt.setString(1,isim);
            pstmt.executeUpdate();
            System.out.println("Sisteme giriş yapıldı: "+ isim);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void skorGuncelle(Connection baglanti, String isim, int yeniSkor){
        String sql="UPDATE oyuncular SET en_yuksek_skor = ? WHERE kullanici_adi = ? AND ? > en_yuksek_skor";
        try(PreparedStatement pstmt= baglanti.prepareStatement(sql)){
            pstmt.setInt(1,yeniSkor);
            pstmt.setString(2,isim);
            pstmt.setInt(3,yeniSkor);
            pstmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public String liderlikTablosuGetir(Connection baglanti){
        StringBuilder tablo = new StringBuilder("--- Liderlik Tablosu --- \n");
        String sql= "SELECT kullanici_adi, en_yuksek_skor FROM oyuncular ORDER BY en_yuksek_skor DESC LIMIT 5";
        try(Statement stmt= baglanti.createStatement();
            ResultSet rs= stmt.executeQuery(sql)){
            int sira = 1;
            while(rs.next()){
                tablo.append(sira).append(". ").append(rs.getString("kullanici_adi")).append(" : ").append(rs.getInt("en_yuksek_skor")).append("\n");
                sira++;

            }
        }catch(SQLException e){
            e.printStackTrace();

        }
        return tablo.toString();

    }
}