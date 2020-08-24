import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;

class TicTacToeTest {

	GameInfo gameInfo;
	Server theServer;

	@BeforeEach
	void setGameInfo(){
		Consumer<Serializable> call = new Consumer<Serializable>() {
			@Override
			public void accept(Serializable serializable) {
				System.out.println("Hello!");
			}
		};
		gameInfo = new GameInfo();
		theServer = new Server(call);
	}

	@Test
	void testServerClass() {
		assertEquals("Server", theServer.getClass().getName(), "Server construction is not correct");
	}

	@Test
	void testGameInfoClass() {
		assertEquals("GameInfo", gameInfo.getClass().getName(), "GameInfo construction is not correct");
	}

	@Test
	void topThreePlayers(){
		ArrayList<Integer> arr = new ArrayList();
		for(int i=0; i < 10; i++){
			arr.add(i);
		}
		int first, second, third;
		first = second = third = Integer.MIN_VALUE;
		for(int i=0; i < arr.size(); i++){
			if(arr.get(i) > first){
				third = second;
				second = first;
				first = arr.get(i);
			} else if (arr.get(i) > second) {
				third = second;
				second = arr.get(i);
			} else if (arr.get(i) > third) {
				third = arr.get(i);
			}
		}

		ArrayList<Integer> result = new ArrayList<>();
		result.add(first);
		result.add(second);
		result.add(third);

		gameInfo.setTopThreePlayers(result);
		ArrayList<Integer> answer = new ArrayList<>();
		answer.add(9);
		answer.add(8);
		answer.add(7);
		assertEquals(answer, gameInfo.getTopThreePlayers(), "Either set or get top three players doesn't work");
	}

}
