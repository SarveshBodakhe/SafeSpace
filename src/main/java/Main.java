import views.Welcome;

public class Main {
    //Entry point of our application...
    public static void main(String[] args) {
        Welcome w = new Welcome();
        // use a do while loop...
        do{
            w.welcomeScreen();
        }while(true);
    }
}
