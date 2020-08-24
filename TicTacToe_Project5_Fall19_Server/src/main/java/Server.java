import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.regex.PatternSyntaxException;

import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    private Consumer<Serializable> callback;

    Server(Consumer<Serializable> call){
        callback = call;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread{
        public void run() {
            try(ServerSocket mySocket = new ServerSocket(5555);){
                System.out.println("Server is waiting for a client!");
                callback.accept("Server is waiting for a client!");
                while(true) {
                    ClientThread c = new ClientThread(mySocket.accept(), count);
                    callback.accept("Client has connected to server: " + "Client #" + count);
                    clients.add(c);
                    c.start();
                    count++;
                }
            } //end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        } //end of while
    }

    class ClientThread extends Thread{
        Socket connection;
        int count;
        int points;
        ObjectInputStream in;
        ObjectOutputStream out;

        ArrayList <Integer> moveList;
        int newMove;

        GameInfo gameObj;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
            this.points = 0;
        }

        ClientThread getClient(int count){
            ClientThread result = clients.get(0);
            for(int i=0;i<clients.size();i++){
                if(clients.get(i).count == count){
                    result = clients.get(i);
                }
            }

            return result;
        }

        public synchronized void updateClients(GameInfo g) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.reset();
                    t.out.writeObject(g);
                }
                catch(Exception e) {}
            }
        }

        public synchronized ArrayList findTopThree(){
            ClientThread first = null;
            ClientThread second =  null;
            ClientThread third = null;
            int firstPoints = Integer.MIN_VALUE;
            int secondPoints = Integer.MIN_VALUE;
            int thirdPoints = Integer.MIN_VALUE;
            ArrayList<String> result = new ArrayList<>();
            for(int i=0; i < clients.size();i++) {
                if(clients.get(i).points > firstPoints){
                    thirdPoints = secondPoints;
                    secondPoints = firstPoints;
                    firstPoints = clients.get(i).points;
                    first = clients.get(i);
                } else if (clients.get(i).points > secondPoints){
                    thirdPoints = secondPoints;
                    secondPoints = clients.get(i).points;
                    second = clients.get(i);
                } else if (clients.get(i).points > thirdPoints){
                    thirdPoints = clients.get(i).points;
                    third = clients.get(i);
                }
            }

            if(first.count == count){
                result.add("You: " + first.points);
            } else {
                result.add("Player " + first.count + ": " + first.points);
            }

            if(second != null) {
                if (second.count == count) {
                    result.add("You: " + second.points);
                } else {
                    result.add("Player " + second.count + ": " + second.points);
                }
            }

            if(third != null) {
                if (third.count == count) {
                    result.add("You: " + third.points);
                } else {
                    result.add("Player " + third.count + ": " + third.points);
                }
            }

            return result;
        }

        public synchronized void run(){
            gameObj = new GameInfo();

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
                Platform.exit();
            }

            while(true){
                try {
                    GameInfo temp = (GameInfo) in.readObject();

                    if(temp.getDifficulty().equals("Easy")||temp.getDifficulty().equals("Medium")||temp.getDifficulty().equals("Expert")){
                        callback.accept("Player " + count + " started a game with " + temp.getDifficulty() + " difficulty");
                        gameObj.setDifficulty(temp.getDifficulty());
                    }
                    if(!gameObj.getBoardState().equals(temp.getBoardState())){
                        gameObj.setBoardState(temp.getBoardState());

                        if (gameObj.getBoardState().matches("OOO......|...OOO...|......OOO|" +
                                "O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..")) {
                            Thread.sleep(1000);
                            gameObj.setTextCommunication("Win");
                            this.points++;
                            gameObj.setPlayerPoints(gameObj.getPlayerPoints()+1);
                            gameObj.setTopThreePlayers(findTopThree());
                            callback.accept("Player " + count + " won a game!");
                            this.out.reset();
                            this.out.writeObject(gameObj);
                            gameObj.reset();
                        } else if (!gameObj.getBoardState().contains("b")) {
                            Thread.sleep(1000);
                            gameObj.setTextCommunication("Tie");
                            callback.accept("Player " + count + " tied with the computer");
                            gameObj.setTopThreePlayers(findTopThree());
                            this.out.reset();
                            this.out.writeObject(gameObj);
                            gameObj.reset();
                        } else {
                            Thread.sleep(1000);
                            FindNextMove nextMove = new FindNextMove(gameObj.getBoardState(), gameObj.getDifficulty());
                            newMove = nextMove.returnNextMove();
                            System.out.println(moveList);

                            String newState = gameObj.getBoardState().substring(0, newMove)
                                    + "X" + gameObj.getBoardState().substring(newMove + 1);

                            gameObj.setBoardState(newState);

                            if (gameObj.getBoardState().matches("XXX......|...XXX...|......XXX|" +
                                    "X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..")) {
                                Thread.sleep(1000);
                                gameObj.setTextCommunication("Lose");
                                callback.accept("Player " + count + " lost a game");
                                gameObj.setTopThreePlayers(findTopThree());
                                this.out.reset();
                                this.out.writeObject(gameObj);
                                gameObj.reset();
                            } else {
                                gameObj.setTopThreePlayers(findTopThree());
                                this.out.reset();
                                this.out.writeObject(gameObj);
                                Thread.sleep(500);
                            }

                        }

                    }

                } catch (Exception e) {
                    callback.accept("UH OH...Something wrong with the socket from client: " + count + "....closing down!");
                    clients.remove(this);
                    ArrayList<String> temp = new ArrayList<>();
                    for (int i = 0; i < clients.size(); i++) {
                        temp.add("Client " + clients.get(i).count);
                    }
                    e.printStackTrace();
                    break;
                }
            }

        }//end of run
    }//end of client thread
}

