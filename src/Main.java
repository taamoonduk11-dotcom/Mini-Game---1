import javax.swing.JFrame;
public class Main {
    public static void main(String[] args){
       JFrame window=new JFrame();

       window.setTitle("Sneak GAME - BİLAL");

       window.setSize(450,450);

       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       window.setVisible(true);

       window.setLocationRelativeTo(null);

       window.add(new GamePanel());

       window.setVisible(true);


    }
}