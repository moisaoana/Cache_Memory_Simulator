
package sample.start;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.presentation.SimulationController;
import sample.presentation.StartPageController;

import java.io.File;
import java.net.URL;

public class Main extends Application {
    Stage window;
    public SimulationController simulationController;
    @Override
    public void start(Stage primaryStage) throws Exception{
        URL urlStart=new File("src/sample/presentation/StartPageView.fxml").toURI().toURL();
        URL urlResults=new File("src/sample/presentation/SimulationView.fxml").toURI().toURL();
        window=primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(urlStart);
        Parent rootStart=loader.load();
        StartPageController startPageController = loader.getController();

        loader = new FXMLLoader();
        loader.setLocation(urlResults);
        Parent rootSim=loader.load();
        simulationController = loader.getController();

        Scene startScene = new Scene(rootStart, 610, 530);
        Scene simScene = new Scene(rootSim, 900, 600);

        startPageController.setMain(this);
        startPageController.setSimScene(simScene);

        simulationController.setMain(this);
        simulationController.setStartScene(startScene);


        window.setScene(startScene);
        window.setTitle("CACHE SIMULATOR");
        window.show();
    }
    public void setScene(Scene scene){
        window.setScene(scene);
    }
    public SimulationController getSimController(){
        return this.simulationController;
    }
    public static void main(String[] args) {
        launch(args);
    }
}

