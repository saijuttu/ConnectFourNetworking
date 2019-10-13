import java.io.*;
import java.net.*;

public class ServerMain {

    public static void main(String[] args)
    {
        try
        {
            ServerSocket serverSocket = new ServerSocket(8001);
            ConnectFourGame game = new ConnectFourGame(ConnectFourGame.WAITING_FOR_B);

            //red connect Code
            Socket RCTC = serverSocket.accept();
            ObjectOutputStream ros = new ObjectOutputStream(RCTC.getOutputStream());
            ObjectInputStream ris = new ObjectInputStream(RCTC.getInputStream());

            ros.writeObject(new Command_From_Server(Command_From_Server.CONNECTED_AS_RED,game));
            ros.reset();

            Thread t = new Thread(new Servers_Listener(ros,ris,game,'R'));
            t.start();
            System.out.println("Red has Connected");

            //black connect Code
            Socket BCTC = serverSocket.accept();
            ObjectOutputStream bos = new ObjectOutputStream(BCTC.getOutputStream());
            ObjectInputStream bis = new ObjectInputStream(BCTC.getInputStream());

            bos.writeObject(new Command_From_Server(Command_From_Server.CONNECTED_AS_BLACK,game));
            bos.reset();
            t = new Thread(new Servers_Listener(bos,bis,game,'B'));
            t.start();
            System.out.println("Black has Connected");

            game.setStatus(game.PLAYING);
            game.setTurn('R');

            Command_From_Server a = new Command_From_Server(Command_From_Server.UPDATE_GAME,game);
            ros.writeObject(a);
            ros.reset();
            bos.writeObject(a);
            bos.reset();
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
            e.printStackTrace();
        }
    }


}
