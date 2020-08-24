import java.util.ArrayList;
import java.util.Random;

public class FindNextMove {
    AI_MinMax ai;
    String boardState;
    String difficulty;
    Random r = new Random();

    FindNextMove(String board, String difficulty){
        this.ai = new AI_MinMax(board);
        this.boardState = board;
        this.difficulty = difficulty;
    }

    public synchronized int returnNextMove(){
        ArrayList<Integer> movesToChoose = ai.getBestMoves();
        ArrayList<Integer> movesNotToChoose = new ArrayList<>();

        for(int i=0;i<9;i++){
            if(this.boardState.charAt(i) == 'b') {
                movesNotToChoose.add(i);
            }
        }

        if(difficulty.equals("Easy")){
            int rand = new Random().nextInt(movesNotToChoose.size());
            return movesNotToChoose.get(rand);
        } else if(difficulty.equals("Medium")){
            float chance = r.nextFloat();
            if(chance < 0.3){
                int rand = new Random().nextInt(movesNotToChoose.size());
                return movesNotToChoose.get(rand);
            } else {
                if(!movesToChoose.isEmpty()) {
                    return movesToChoose.get(0);
                } else {
                    int rand = new Random().nextInt(movesNotToChoose.size());
                    return movesNotToChoose.get(rand);
                }
            }
        } else {
            return movesToChoose.get(0);
        }
    }
}
