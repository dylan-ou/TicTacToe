import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.image.Image;

import java.awt.*;

public class TicTacToeClient extends Application {

	Client clientConnection;

	// log in scene
	Text text1, text2, warningText;
	TextField portNum, ipAddr;
	Button logIn;
	HBox line1, line2, logInElements;
	VBox txtFields;
	BorderPane bp1;

	// title scene
	Text title, score, score1, score2, score3, yourScore;
	ListView topScores;
	int playerScore;
	Button easy, medium, expert;
	HBox difficultyBoxes;
	VBox scores;
	BorderPane bp2;

	// tictactoe board
	GridPane grid;
	Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;
	BorderPane bp3;
	Text gameText;

	// winLose scene
	HBox winOrLoseButtons;
	Button playAgain, quit;
	Text winOrLoseText;
	BorderPane bp4;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		// Log In Scene
		primaryStage.setTitle("Log In");

		text1 = new Text("Port Num");
		text2 = new Text("Ip Address");
		warningText = new Text("");
		portNum = new TextField();
		ipAddr = new TextField();
		logIn = new Button("Log In");

		line1 = new HBox(10, text1, portNum);
		line1.setAlignment(Pos.CENTER_RIGHT);
		line2 = new HBox(10, text2, ipAddr);
		line2.setAlignment(Pos.CENTER_RIGHT);
		txtFields = new VBox(10, line1, line2, warningText);
		txtFields.setAlignment(Pos.CENTER);
		logInElements = new HBox(10, txtFields, logIn);

		bp1 = new BorderPane();
		bp1.setCenter(logInElements);
		bp1.setPadding(new Insets(10));

		// Title Screen
		title = new Text("TIC TAC TOE");
		title.setFont(Font.font("Comic Sans MS", 20));
		playerScore = 0;
		score = new Text("Scores");
		score.setFont(Font.font("Comic Sans MS"));
		topScores = new ListView<String>();
		score1 = new Text("");
		score2 = new Text("");
		score3 = new Text("");
		easy = new Button("Easy");
		medium = new Button("Medium");
		expert = new Button("Expert");

		// Game Screen
		grid = new GridPane();
		grid.setGridLinesVisible(true);
		btn0 = new Button();
		btn0.setMinSize(100,100);
		btn1 = new Button();
		btn1.setMinSize(100,100);
		btn2 = new Button();
		btn2.setMinSize(100,100);
		btn3 = new Button();
		btn3.setMinSize(100,100);
		btn4 = new Button();
		btn4.setMinSize(100,100);
		btn5 = new Button();
		btn5.setMinSize(100,100);
		btn6 = new Button();
		btn6.setMinSize(100,100);
		btn7 = new Button();
		btn7.setMinSize(100,100);
		btn8 = new Button();
		btn8.setMinSize(100,100);

		gameText = new Text("Your Turn");

		// Win Or Lose Screen
		playAgain = new Button("Play Again");
		quit = new Button("Quit");

		playAgain.setOnAction(e->{
			playerScore = clientConnection.gameInfo.getPlayerPoints();
			primaryStage.setScene(createTitleScreen());
			gameText.setText("Your Turn");
			clientConnection.gameInfo.reset();
			clientConnection.send();
			topScores.getItems().clear();
			updateTopScores();
		});

		quit.setOnAction(e->{
			Platform.exit();
			System.exit(0);
		});

		this.logIn.setOnAction(e->{
			if(!(portNum.getText().equals("5555") && ipAddr.getText().equals("127.0.0.1"))){
				portNum.clear();
				ipAddr.clear();
				warningText.setTextAlignment(TextAlignment.RIGHT);
				warningText.setText("Error: not valid port number or IP address");
			} else {
				clientConnection = new Client(data->{
					Platform.runLater(()->{
						if(clientConnection.gameInfo.getTextCommunication().matches("Win|Lose|Tie")){
							primaryStage.setScene(winOrLoseScreen(clientConnection.gameInfo.getTextCommunication()));
						}
						
						updateTopScores();
						String[] splitString = clientConnection.gameInfo.getBoardState().split("");
						for (int i = 0; i < 9; i++) {
							if (splitString[i].equals("X")) {
								if (i == 0) {
									grid.getChildren().remove(btn0);
									grid.add(xImage(), 0, 0);
								} else if (i == 1) {
									grid.getChildren().remove(btn1);
									grid.add(xImage(), 1, 0);
								} else if (i == 2) {
									grid.getChildren().remove(btn2);
									grid.add(xImage(), 2, 0);
								} else if (i == 3) {
									grid.getChildren().remove(btn3);
									grid.add(xImage(), 0, 1);
								} else if (i == 4) {
									grid.getChildren().remove(btn4);
									grid.add(xImage(), 1, 1);
								} else if (i == 5) {
									grid.getChildren().remove(btn5);
									grid.add(xImage(), 2, 1);
								} else if (i == 6) {
									grid.getChildren().remove(btn6);
									grid.add(xImage(), 0, 2);
								} else if (i == 7) {
									grid.getChildren().remove(btn7);
									grid.add(xImage(), 1, 2);
								} else if (i == 8) {
									grid.getChildren().remove(btn8);
									grid.add(xImage(), 2, 2);
								}
								buttonsOn();
								gameText.setText("Your turn");
							}
						}
					});
				});

				clientConnection.start();

				primaryStage.setTitle("Tic Tac Toe");
				primaryStage.setScene(createTitleScreen());

				easy.setOnAction(event->{
					clientConnection.gameInfo.setDifficulty("Easy");
					clientConnection.send();
					primaryStage.setScene(createGameScene());
				});

				medium.setOnAction(event->{
					clientConnection.gameInfo.setDifficulty("Medium");
					clientConnection.send();
					primaryStage.setScene(createGameScene());
				});

				expert.setOnAction(event->{
					clientConnection.gameInfo.setDifficulty("Expert");
					clientConnection.send();
					primaryStage.setScene(createGameScene());
				});

				btn0.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState("O" + clientConnection.gameInfo.getBoardState().substring(1));
					clientConnection.send();
					grid.getChildren().remove(btn0);
					grid.add(oImage(), 0, 0);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn1.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,1) +
							"O" + clientConnection.gameInfo.getBoardState().substring(2));
					clientConnection.send();
					grid.getChildren().remove(btn1);
					grid.add(oImage(), 1, 0);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn2.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,2) +
							"O" + clientConnection.gameInfo.getBoardState().substring(3));
					clientConnection.send();
					grid.getChildren().remove(btn2);
					grid.add(oImage(), 2, 0);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn3.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,3) +
							"O" + clientConnection.gameInfo.getBoardState().substring(4));
					clientConnection.send();
					grid.getChildren().remove(btn3);
					grid.add(oImage(), 0, 1);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn4.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,4) +
							"O" + clientConnection.gameInfo.getBoardState().substring(5));
					clientConnection.send();
					grid.getChildren().remove(btn4);
					grid.add(oImage(), 1, 1);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn5.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,5) +
							"O" + clientConnection.gameInfo.getBoardState().substring(6));
					clientConnection.send();
					grid.getChildren().remove(btn5);
					grid.add(oImage(), 2, 1);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn6.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,6) +
							"O" + clientConnection.gameInfo.getBoardState().substring(7));
					clientConnection.send();
					grid.getChildren().remove(btn6);
					grid.add(oImage(), 0, 2);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn7.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,7) +
							"O" + clientConnection.gameInfo.getBoardState().substring(8));
					clientConnection.send();
					grid.getChildren().remove(btn7);
					grid.add(oImage(), 1, 2);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});

				btn8.setOnAction(event->{
					buttonsOff();
					clientConnection.gameInfo.setDifficulty("");
					clientConnection.gameInfo.setBoardState(clientConnection.gameInfo.getBoardState().substring(0,8) +
							"O");
					clientConnection.send();
					grid.getChildren().remove(btn8);
					grid.add(oImage(), 2, 2);
					if(!clientConnection.gameInfo.getBoardState().matches("OOO......|...OOO...|......OOO|" +
							"O..O..O..|.O..O..O.|..O..O..O|" + "O...O...O|..O.O.O..") &&
							!clientConnection.gameInfo.getBoardState().matches("XXX......|...XXX...|......XXX|" +
									"X..X..X..|.X..X..X.|..X..X..X|" + "X...X...X|..X.X.X..") &&
							clientConnection.gameInfo.getBoardState().contains("b")) {
						gameText.setText("Computer's turn");
					} else {
						gameText.setText("");
					}
				});


			}
		});

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

		Scene scene = new Scene(bp1,300,100);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Scene createTitleScreen(){
		yourScore = new Text("\nYour score: " + playerScore);
		scores = new VBox(10, score, topScores, yourScore);
		scores.setAlignment(Pos.CENTER);
		difficultyBoxes = new HBox(10, easy, medium, expert);
		difficultyBoxes.setAlignment(Pos.CENTER);
		bp2 = new BorderPane();
		bp2.setPadding(new Insets(10));
		bp2.setTop(title);
		bp2.setAlignment(title, Pos.CENTER);
		bp2.setCenter(scores);
		bp2.setAlignment(scores, Pos.CENTER);
		bp2.setBottom(difficultyBoxes);

		return new Scene(bp2, 300, 300);
	}

	public Scene createGameScene(){
		grid.setMinSize(300, 300);
		grid.getChildren().clear();
		grid.add(btn0, 0, 0);
		grid.add(btn1, 1, 0);
		grid.add(btn2, 2, 0);
		grid.add(btn3, 0, 1);
		grid.add(btn4, 1, 1);
		grid.add(btn5, 2, 1);
		grid.add(btn6, 0, 2);
		grid.add(btn7, 1, 2);
		grid.add(btn8, 2, 2);
		grid.setAlignment(Pos.CENTER);

		bp3 = new BorderPane();
		bp3.setTop(gameText);
		bp3.setAlignment(gameText, Pos.CENTER);
		bp3.setBottom(grid);

		return new Scene(bp3, 300, 300);
	}

	public Scene winOrLoseScreen(String results){
		winOrLoseText = new Text(results);
		winOrLoseButtons = new HBox(10, playAgain, quit);
		playAgain.setAlignment(Pos.CENTER);
		quit.setAlignment(Pos.CENTER);
		winOrLoseButtons.setAlignment(Pos.CENTER);
		bp4 = new BorderPane();
		bp4.setCenter(winOrLoseText);
		bp4.setAlignment(winOrLoseText, Pos.CENTER);
		bp4.setBottom(winOrLoseButtons);
		bp4.setPadding(new Insets(10));

		return new Scene(bp4, 300, 300);
	}

	public ImageView oImage(){
		Image image = new Image("cyrillic_small_letter_o_u043E_icon_128x128.png");
		ImageView oChoice = new ImageView(image);
		oChoice.setFitWidth(100);
		oChoice.setFitHeight(100);

		return oChoice;
	}

	public ImageView xImage(){
		Image image = new Image("latin_capital_letter_x_u0058_icon_128x128.png");
		ImageView xChoice = new ImageView(image);
		xChoice.setFitWidth(100);
		xChoice.setFitHeight(100);

		return xChoice;
	}

	public void buttonsOff(){
		btn0.setDisable(true);
		btn1.setDisable(true);
		btn2.setDisable(true);
		btn3.setDisable(true);
		btn4.setDisable(true);
		btn5.setDisable(true);
		btn6.setDisable(true);
		btn7.setDisable(true);
		btn8.setDisable(true);
	}

	public void buttonsOn(){
		btn0.setDisable(false);
		btn1.setDisable(false);
		btn2.setDisable(false);
		btn3.setDisable(false);
		btn4.setDisable(false);
		btn5.setDisable(false);
		btn6.setDisable(false);
		btn7.setDisable(false);
		btn8.setDisable(false);
	}

	public void updateTopScores(){
		topScores.getItems().clear();
		if(clientConnection.gameInfo.getTopThreePlayers() != null){
			if(clientConnection.gameInfo.getTopThreePlayers().size() == 1){
				score1.setText("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
				topScores.getItems().add("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
			} else if (clientConnection.gameInfo.getTopThreePlayers().size() == 2){
				score1.setText("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
				score2.setText("#2: " + clientConnection.gameInfo.getTopThreePlayers().get(1));
				topScores.getItems().add("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
				topScores.getItems().add("#2: " + clientConnection.gameInfo.getTopThreePlayers().get(1));
			} else {
				score1.setText("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
				score2.setText("#2: " + clientConnection.gameInfo.getTopThreePlayers().get(1));
				score3.setText("#2: " + clientConnection.gameInfo.getTopThreePlayers().get(2));
				topScores.getItems().add("#1: " + clientConnection.gameInfo.getTopThreePlayers().get(0));
				topScores.getItems().add("#2: " + clientConnection.gameInfo.getTopThreePlayers().get(1));
				topScores.getItems().add("#3: " + clientConnection.gameInfo.getTopThreePlayers().get(2));

			}
		}
	}

}
