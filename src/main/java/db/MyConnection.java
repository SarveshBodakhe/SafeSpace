package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
     public static Connection connection=null;
     public static Connection getConnection(){
         try{
             Class.forName("com.mysql.cj.jdbc.Driver");
             connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaproject?useSSL=false","root","Ekshula#2002");
         } catch(ClassNotFoundException ex)
         {
             ex.printStackTrace();
         } catch(SQLException ex)
         {
             ex.printStackTrace();
         }

         System.out.println("Conncetion is done!!!");
         return connection;
     }

     public static void closeConnection(){
         if(connection!=null)
         {
             try{
                 connection.close();
             } catch(SQLException ex)
             {
                 ex.printStackTrace();
             }
         }
     }
}
