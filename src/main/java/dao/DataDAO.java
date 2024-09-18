package dao;


// DataDAO shows hidden files, unhide hidden files, hide a new file...

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {
    // get all hidden files...
    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from data where email = ? ");
        ps.setString(1,email);
        ResultSet rs = ps.executeQuery();
        List<Data> files = new ArrayList<>();

        while(rs.next()){
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);
            files.add(new Data(id, name, path));
        }

        return files;
    }

    // we want to hide a file....

    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "insert into data(name, path, email, bin_data) values(?, ?, ?, ?)");
        ps.setString(1,file.getFilename());
        ps.setString(2,file.getPath());
        ps.setString(3,file.getEmail());

        // we copied the data of text file, saved the data in the database and delete the file...
        // when we unhide the file, we create the new file... The data we saved in the form of character stream , we took that data and write in the newly created file...

        File f = new File(file.getPath());
        FileReader fr = new FileReader(f);
        ps.setCharacterStream(4, fr, f.length());
        int ans = ps.executeUpdate();
        fr.close();
        f.delete();
        return ans;
    }

    // unhide a hidden file...
    public static void unHide(int id) throws SQLException, IOException{
        Connection connection = MyConnection.getConnection();

        // here we retrive the data on the basis of id, we read the data and write into new a file on the same path...

        PreparedStatement ps = connection.prepareStatement("select path, bin_data from data where id = ?");
        ps.setInt(1,id);
        ResultSet rs= ps.executeQuery();
        rs.next(); // move the pointer to next and print current data related to that id...

        String path = rs.getString("path");
        Clob c = rs.getClob("bin_data");

        Reader r = c.getCharacterStream();
        FileWriter fw = new FileWriter(path); // the path that we have, we will create the new file for that same path...

        int i; // In file handling, when we reading from file we get int or aschii not a char, so thats why we take int variable here...

        while((i = r.read()) != -1){
            // if we dont use r.read() here then alternate characters will print...
            fw.write((char) i); //type casting into char...
        }
        fw.close();

        // we have to delete that row from table after completing above operation...

        ps = connection.prepareStatement("delete from data where id = ?");
        ps.setInt(1,id);
        ps.executeUpdate();
        System.out.println("Successfully Unhidden the file!!!");

    }

}
