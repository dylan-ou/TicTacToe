import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Serializable;
import java.util.function.Consumer;

class TicTacToeTest {

	Client theClient;
	GameInfo gameInfo;

	// these test cases don't run for some reason

	@BeforeEach
	void setUp(){
		Consumer<Serializable> call = new Consumer<Serializable>() {
			@Override
			public void accept(Serializable serializable) {
				System.out.println("Hello!");
			}
		};
		theClient = new Client(call);
		gameInfo = new GameInfo();
	}

	@Test
	void testClientClass() {
		assertEquals("Client", theClient.getClass().getName(), "Client construction is not correct");
	}

	@Test
	void playerPoints(){
		gameInfo.setPlayerPoints(7);
		assertEquals(7, gameInfo.getPlayerPoints(), "Either set or get player points doesn't work");
	}

	@Test
	void textCommunication(){
		gameInfo.setTextCommunication("Hello!");
		assertEquals("Hello!", gameInfo.getTextCommunication(), "Either set or get text communication doesn't work");
	}

	@Test
	void difficulty(){
		gameInfo.setDifficulty("Hard");
		assertEquals("Hard", gameInfo.getDifficulty(), "Either set or get difficulty doesn't work");
	}

	@Test
	void boardState(){
		gameInfo.setBoardState("bbbbbbbbb");
		assertEquals("bbbbbbbbb", gameInfo.getBoardState(), "Either set or get board state doesn't work");
	}
}
