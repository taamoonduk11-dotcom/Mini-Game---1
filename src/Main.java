import javax.swing.JFrame;
public class Main {
    public static void main(String[] args){
       JFrame window=new JFrame();

       window.setTitle("Sneak GAME - BİLAL");

       window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       window.add(new GamePanel());

       window.setSize(600,600);

       window.setVisible(true);

       window.setLocationRelativeTo(null);

    }
}