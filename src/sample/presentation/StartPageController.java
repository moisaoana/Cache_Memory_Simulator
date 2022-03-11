package sample.presentation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import sample.model.Cache;
import sample.model.MainMemory;
import sample.start.Main;

public class StartPageController {
    ObservableList<String> mapList;
    private Main main;
    private Scene simulationScene;
    private int addressSize,blockSizePower,nrOfBlocksPower,dataSizeInBytes;
    public void setMain(Main main){
        this.main = main;
        init();
    }
    public void setSimScene(Scene scene){
        this.simulationScene = scene;
    }

    @FXML
    private Label titleLabel;

    @FXML
    private Label configLabel;

    @FXML
    private Button simButton;

    @FXML
    private Button resetButton;

    @FXML
    private Label mapLabel;

    @FXML
    private ComboBox<String> mapComboBox;

    @FXML
    private Label addressSizeLabel;

    @FXML
    private Label nrOfBlocksLabel;

    @FXML
    private Label blockSizeLabel;

    @FXML
    private TextField addressSizeTextField;

    @FXML
    private TextField nrOfBlocksTextField;

    @FXML
    private TextField blockSizeTextField;

    @FXML
    private Label emptyLabel;

    @FXML
    private Label dataSizeLabel;

    @FXML
    private TextField dataSizeTextField;

    @FXML
    private Label incorrectLabel;

    @FXML
    void clickReset(ActionEvent event) {
        clearAll();
    }

    @FXML
    void clickSim(ActionEvent event) {
        if(addressSizeTextField.getText().isEmpty() || nrOfBlocksTextField.getText().isEmpty() || blockSizeTextField.getText().isEmpty() || dataSizeTextField.getText().isEmpty()){
            incorrectLabel.setVisible(false);
            emptyLabel.setVisible(true);
        }else{
            incorrectLabel.setVisible(false);
            emptyLabel.setVisible(false);
            if(Integer.parseInt(blockSizeTextField.getText())+Integer.parseInt(nrOfBlocksTextField.getText())>=Integer.parseInt(addressSizeTextField.getText()))
            {
                incorrectLabel.setVisible(true);
                new PopUpMessage("index size + block offset size + tag size = address size",10);
            }else {
                try {
                    incorrectLabel.setVisible(false);
                    emptyLabel.setVisible(false);
                    collectUserInfo();
                    createMainMemory();
                    createCache();
                    clearAll();
                    main.setScene(simulationScene);
                } catch (NumberFormatException numberFormatException) {
                    incorrectLabel.setVisible(true);
                }
            }
        }
    }

    @FXML
    void init(){
        mapList= FXCollections.observableArrayList("Direct Mapping");
        mapComboBox.setValue("Direct Mapping");
        mapComboBox.setItems(mapList);
    }

    private void clearAll(){
        addressSizeTextField.clear();
        nrOfBlocksTextField.clear();
        blockSizeTextField.clear();
        emptyLabel.setVisible(false);
        dataSizeTextField.clear();
        incorrectLabel.setVisible(false);
    }

    private void collectUserInfo(){
            addressSize = Integer.parseInt(addressSizeTextField.getText());
            nrOfBlocksPower = Integer.parseInt(nrOfBlocksTextField.getText());
            blockSizePower = Integer.parseInt(blockSizeTextField.getText());
            dataSizeInBytes = Integer.parseInt(dataSizeTextField.getText());
    }

    private void createMainMemory(){
        MainMemory mainMemory=new MainMemory(addressSize,dataSizeInBytes);
        mainMemory.fillMemory();
        SimulationController simulationController=main.getSimController();
        simulationController.setMainMemory(mainMemory);
        simulationController.populateMainMemoryTable();
    }
    private void createCache(){
        Cache cache=new Cache(nrOfBlocksPower,blockSizePower,addressSize);
        cache.createCache();
        SimulationController simulationController=main.getSimController();
        simulationController.setCache(cache);
        simulationController.populateCacheMemoryTable();
    }
}