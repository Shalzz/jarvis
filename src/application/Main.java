package application;

import com.shalzz.bluetoothController.BluetoothServer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Jarvis.fxml"));

			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setTitle("HelloSwingNode Sample");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println("Server startup now");
		new BluetoothServer().start();
	}
}
