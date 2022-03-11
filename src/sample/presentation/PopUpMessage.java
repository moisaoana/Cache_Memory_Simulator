package sample.presentation;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PopUpMessage extends Stage {
    public PopUpMessage(String string, int duration) {
        BorderPane borderPane=new BorderPane();
        borderPane.setStyle("-fx-background-color: darkseagreen");
        this.setWidth(400);
        this.setHeight(100);
        this.setTitle("Message");
        Text text = new Text(string);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        text.setFill(Color.WHITE);
        borderPane.setCenter(text);
        Scene scene = new Scene(borderPane);
        this.setScene(scene);
        this.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(duration));
        delay.setOnFinished( event -> this.close() );
        delay.play();
    }
}
