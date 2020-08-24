import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TicTacToeServer extends Application {

	BorderPane bp1, bp2;
	VBox vBox1;
	TextField tf1;
	ListView<String> listItems;
	Button btn1;
	HBox hBox1;
	Text txt1, txt2;

	Server serverConnection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Log In");

		this.txt1 = new Text("Input Port Number");
		this.txt2 = new Text("");
		this.tf1 = new TextField();
		this.btn1 = new Button("Log into Server");
		this.hBox1 = new HBox(10, tf1, btn1);
		this.hBox1.setAlignment(Pos.CENTER);
		this.vBox1 = new VBox(10, txt1, hBox1, txt2);
		this.vBox1.setAlignment(Pos.CENTER);

		this.bp1 = new BorderPane();
		this.bp1.setCenter(this.vBox1);
		this.bp1.setPadding(new Insets(10));

		this.listItems = new ListView<String>();
		// consists of only a log in screen and a listView
		this.btn1.setOnAction(e->{
					if(!tf1.getText().equals("5555")){
						tf1.clear();
						txt2.setText("Error: not valid port number");
					} else {
						this.bp2 = new BorderPane();
						bp2.setPadding(new Insets(10));
						bp2.setCenter(this.listItems);
						Scene serverScene = new Scene(bp2, 500,600);
						primaryStage.setScene(serverScene);
						primaryStage.setTitle("RPSLS Server");
						serverConnection = new Server(data -> {
							Platform.runLater(()->{
								listItems.getItems().add(data.toString());
							});
						});

						primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							@Override
							public void handle(WindowEvent event) {
								Platform.exit();
								System.exit(0);
							}
						});
					}
				}
		);

		Scene startScene = new Scene(bp1,400,100);
		primaryStage.setScene(startScene);
		primaryStage.show();
	}

}
