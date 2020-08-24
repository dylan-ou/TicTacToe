import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;
    private Consumer<Serializable> callback;
    GameInfo gameInfo = new GameInfo();

    Client(Consumer<Serializable> call){
        callback = call;
    }

    public void setCallback(Consumer<Serializable> callback) {
        this.callback = callback;
    }

    public void run() {
        try {
            socketClient= new Socket("127.0.0.1",5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception e) {}

        while(true) {
            try {
                GameInfo temp = (GameInfo)in.readObject(); // reads information
                gameInfo.setBoardState(temp.getBoardState());
                gameInfo.setTextCommunication(temp.getTextCommunication());
                gameInfo.setPlayerPoints(temp.getPlayerPoints());
                gameInfo.setTopThreePlayers(temp.getTopThreePlayers());
                callback.accept(temp.getTopThreePlayers());
            }
            catch(Exception e) {
            }
        }
    }

    public void send() { // sends to server
        try {
            out.reset();
            out.writeObject(gameInfo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Platform.exit();
            System.exit(0);
        }
    }


}