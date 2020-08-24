import java.io.Serializable;
import java.util.ArrayList;

public class GameInfo implements Serializable {
    private int playerPoints;
    private String textCommunication;
    private String difficulty;
    private String boardState;
    private ArrayList topThreePlayers;

    GameInfo(){
        playerPoints = 0;
        textCommunication = "";
        difficulty = "";
        boardState = "bbbbbbbbb";
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public void setPlayerPoints(int playerPoints) {
        this.playerPoints = playerPoints;
    }

    public String getTextCommunication() {
        return textCommunication;
    }

    public void setTextCommunication(String textCommunication) {
        this.textCommunication = textCommunication;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }

    public ArrayList getTopThreePlayers() {
        return topThreePlayers;
    }

    public void setTopThreePlayers(ArrayList topThreePlayers) {
        this.topThreePlayers = topThreePlayers;
    }

    public void reset(){
        textCommunication = "";
        difficulty = "";
        boardState = "bbbbbbbbb";
    }
}