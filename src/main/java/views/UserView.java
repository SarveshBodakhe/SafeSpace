package views;

import dao.DataDAO;
import model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;

    UserView(String email)
    {
        this.email=email;
    }

    public void home(){
        do{
            System.out.println("Welcome " + this.email);
            System.out.println("Press 1 to show hidden files");
            System.out.println("Press 2 to hide a new file");
            System.out.println("Press 3 to unhide a file");
            System.out.println("Press 0 to exit");
            Scanner sc = new Scanner(System.in);
            int ch = Integer.parseInt(sc.nextLine());

            switch(ch){
                // In case 1 we want to see all hidden files...

                case 1 -> {
                    try{
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID    -    File Name");

                        for(Data file : files){
                            System.out.println(file.getId() + "    -    " + file.getFilename());
                        }
                    } catch(SQLException ex){
                        ex.printStackTrace();
                    }
                }

                // In case 2 we want to hide a new file...

                case 2 -> {
                    System.out.println("Enter the file path");
                    String path = sc.nextLine();
                    File f = new File(path);
                    Data file = new Data(0, f.getName(), path, this.email);
                    try{
                        DataDAO.hideFile(file);
                    } catch(SQLException ex)
                    {
                        ex.printStackTrace();
                    } catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                }

                // In case 3 we give option to choose the id and unhide that file and delete that row from the database table...

                case 3 -> {
                    List<Data> files = null;
                    try {
                        files = DataDAO.getAllFiles(this.email);

                    System.out.println("ID    -    File Name");

                    for(Data file : files){
                        System.out.println(file.getId() + "    -    " + file.getFilename());
                    }

                    System.out.println("Enter the id of file to unhide the file: ");

                    int id = Integer.parseInt(sc.nextLine());
                    boolean isValidID = false;
                    for(Data file : files)
                    {
                        if(file.getId() == id)
                        {
                            isValidID = true;
                            break;
                        }
                    }

                    // if id is valid then unhide the file... then delete
                    if(isValidID){
                        DataDAO.unHide(id);
                    }else {
                        System.out.println("Wrong ID!!!");
                    }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch(IOException ex)
                    {
                        ex.printStackTrace();
                    }

                }

                case 0 -> {
                    System.exit(0);
                }
            }

        } while(true);
    }
}
