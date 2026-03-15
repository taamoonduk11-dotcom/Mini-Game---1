import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class VeriTabaniTest {
        public static void main(String[] args) {
            String url= "jdbc:sqlite:C:";
            Connection baglanti =null;

            try{
                baglanti= DriverManager.getConnection(url);
                System.out.println("Snake Game Veritabanına Bağlandık!");

                String oyuncuAdi= JOptionPane.showInputDialog("Yılan Oyununa Hoş Geldiniz...");

                if(oyuncuAdi !=null && !oyuncuAdi.trim().isEmpty()){
                    oyuncuKaydet(baglanti,oyuncuAdi);

                    getRekor(baglanti, oyuncuAdi);
                }
            }
            catch(SQLException e){
                System.out.println("Hata Aldık! "+e.getMessage());
            }
            finally{
                try{
                    if(baglanti!=null) {
                        baglanti.close();
                    }
                }catch(SQLException ex){
                    System.out.println(ex.getMessage());
                }
            }
        }
        public static void oyuncuKaydet(Connection baglanti,String isim){
          String sql="INSERT OR IGNORE INTO oyuncular(kullanici_adi) VALUES(?)";
          try(PreparedStatement pstmt=baglanti.prepareStatement(sql)){
              pstmt.setString(1,isim);
              pstmt.executeUpdate();
              System.out.println(isim+" için kayıt kontrolü yapıldı.");
          }catch(SQLException e){
              System.out.println("Kayıt hatası: "+ e.getMessage());
          }

        }
        public static void getRekor(Connection baglanti,String isim){
            String sql= "SELECT en_yuksek_skor FROM oyuncular WHERE kullanici_adi = ?";
            try(PreparedStatement pstmt= baglanti.prepareStatement(sql)){
                pstmt.setString(1,isim);
                ResultSet rs= pstmt.executeQuery();
                if(rs.next()){
                    int rekor = rs.getInt("en_yuksek_skor");
                    System.out.println("Hoş geldin "+ isim + "! Mevcut Rekorun: "+rekor);

                }
            }catch(SQLException e){
                System.out.println("Veri çekme hatası: "+ e.getMessage());
            }
        }
}

