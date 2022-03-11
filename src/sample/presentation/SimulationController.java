package sample.presentation;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.model.*;
import sample.start.Main;

import java.util.ArrayList;
import java.util.BitSet;


public class SimulationController  {
    private Main main;
    private Scene startScene;
    private MainMemory mainMemory;
    private Cache cache;
    private ObservableList<MemoryLine> memoryLineObservableList=FXCollections.observableArrayList();
    private ObservableList<CacheLine> cacheLineObservableList=FXCollections.observableArrayList();
    public void setMain(Main main){
        this.main = main;
    }
    public void setStartScene(Scene scene){
        this.startScene = scene;
    }
    public void setMainMemory(MainMemory mainMemory){
        this.mainMemory=mainMemory;
    }
    public void setCache(Cache cache){
        this.cache=cache;
    }

    @FXML
    private Label titleLabel;

    @FXML
    private Button doneButton;

    @FXML
    private TableView<MemoryLine> mainMemoryTableView;

    @FXML
    private TableColumn<MemoryLine, String> dataColumn;

    @FXML
    private TableColumn<MemoryLine, String> addressColumn;

    @FXML
    private Label mainMemoryLabel;

    @FXML
    private Label cacheMemoryLabel;

    @FXML
    private Button retrieveDataButton;

    @FXML
    private Button changeDataButton;

    @FXML
    private TextField inputAddressTextField;

    @FXML
    private TextField inputDataTextField;

    @FXML
    private Label inputAddressLabel;

    @FXML
    private Label inputDataLabel;

    @FXML
    private Label emptyLabel;

    @FXML
    private TableView<CacheLine> cacheTableView;

    @FXML
    private TableColumn<CacheLine, String> indexColumn;

    @FXML
    private TableColumn<CacheLine, String> validColumn;

    @FXML
    private TableColumn<CacheLine, String> tagColumn;

    @FXML
    private TableColumn<CacheLine, String> dataCacheColumn;

    @FXML
    private Label statisticsLabel;

    @FXML
    private Label hitRateLabel;

    @FXML
    private Label missRateLabel;

    @FXML
    private TextField hitRateTextField;

    @FXML
    private TextField missRateTextField;

    @FXML
    private Label incorrectAddressLabel;

    @FXML
    private Label incorrectDataLabel;

    private BitSet inputAddress;
    private int totalNrOfRequests=0;
    private int nrOfHits=0;
    private float hitRate=100;
    private float missRate=0;

    public int getTotalNrOfRequests() {
        return totalNrOfRequests;
    }

    public void setTotalNrOfRequests(int totalNrOfRequests) {
        this.totalNrOfRequests = totalNrOfRequests;
    }

    public int getNrOfHits() {
        return nrOfHits;
    }

    public void setNrOfHits(int nrOfHits) {
        this.nrOfHits = nrOfHits;
    }

    @FXML
    void clickDone(ActionEvent event) {
        main.setScene(startScene);
        memoryLineObservableList.clear();
        cacheLineObservableList.clear();
        inputAddressTextField.clear();
        inputDataTextField.clear();
        emptyLabel.setVisible(false);
        incorrectAddressLabel.setVisible(false);
        incorrectDataLabel.setVisible(false);
        hitRateTextField.clear();
        missRateTextField.clear();
    }

    public void populateMainMemoryTable() {
        memoryLineObservableList.addAll(mainMemory.getLines());
        dataColumn.setCellValueFactory((new PropertyValueFactory<>("printableData")));
        addressColumn.setCellValueFactory((new PropertyValueFactory<>("printableAddress")));
        mainMemoryTableView.setItems(memoryLineObservableList);
        hitRateTextField.setText(String.format("%.2f",hitRate)+ " %");
        missRateTextField.setText(String.format("%.2f", missRate)+ " %");
    }
    public void populateCacheMemoryTable(){
        cacheLineObservableList.addAll(cache.getLines());
        indexColumn.setCellValueFactory((new PropertyValueFactory<>("printableIndex")));
        validColumn.setCellValueFactory((new PropertyValueFactory<>("printableValid")));
        tagColumn.setCellValueFactory((new PropertyValueFactory<>("printableTag")));
        dataCacheColumn.setCellValueFactory((new PropertyValueFactory<>("printableData")));
        cacheTableView.setItems(cacheLineObservableList);
    }
    public void refreshTable()
    {
        cacheLineObservableList.clear();
        cacheLineObservableList.addAll(cache.getLines());
        cacheTableView.setItems(cacheLineObservableList);
        cacheTableView.refresh();

        memoryLineObservableList.clear();
        memoryLineObservableList.addAll(mainMemory.getLines());
        mainMemoryTableView.setItems(memoryLineObservableList);
        mainMemoryTableView.refresh();
    }

    @FXML
    void clickChangeData(ActionEvent event) {
        incorrectAddressLabel.setVisible(false);
        emptyLabel.setVisible(false);
        incorrectDataLabel.setVisible(false);
        if(inputDataTextField.getText().isEmpty()){
            emptyLabel.setVisible(true);
        }else {
            if(inputDataTextField.getText().length()!=mainMemory.getDataSizeInBytes()*8){
                incorrectDataLabel.setVisible(true);
            }else {
                String data = inputDataTextField.getText();
                ArrayList<Byte> bytes = MemoryLine.fromStringToArrayListByte(data);
                writeOrRead(true, bytes);
            }
        }
    }
    public void sendDataToUI(String string){
        inputDataTextField.setText(string);
    }

    @FXML
    void clickRetrieveData(ActionEvent event) {
        ArrayList<Byte>nullList=new ArrayList<>();
        emptyLabel.setVisible(false);
        incorrectAddressLabel.setVisible(false);
        incorrectDataLabel.setVisible(false);
        writeOrRead(false,nullList);
    }
    public void writeOrRead(Boolean write, ArrayList<Byte>newData){
        MemoryLine requestedMemoryLine = null;
        MMU mmu=null;
        ArrayList<MemoryLine> memoryLines=new ArrayList<>();
        if(inputAddressTextField.getText().isEmpty()){
            emptyLabel.setVisible(true);
        }else {
            if (inputAddressTextField.getText().length() != mainMemory.getAddressSize()) {
                incorrectAddressLabel.setVisible(true);
            } else
                {
                    try {
                        emptyLabel.setVisible(false);
                        String addr = inputAddressTextField.getText();
                        inputAddress = MainMemory.intToBitSet(Integer.parseInt(addr, 2), mainMemory.getAddressSize());
                        BitSet inputAddressStart = inputAddress.get(cache.getBlockOffsetSize(), cache.getAddressSize());
                        for (MemoryLine memoryLine : mainMemory.getLines()) {
                            BitSet memoryLineStart = memoryLine.getAddress().get(cache.getBlockOffsetSize(), cache.getAddressSize());
                            if (memoryLineStart.equals(inputAddressStart)) {
                                memoryLines.add(memoryLine);
                            }
                            if (memoryLine.getAddress().equals(inputAddress)) {
                                requestedMemoryLine = memoryLine;
                            }
                        }
                        mmu = new MMU(memoryLines, cache, requestedMemoryLine, mainMemory);
                        mmu.setMain(main);
                        mmu.manageAddress(write, newData);

                        totalNrOfRequests++;
                        hitRate = ((float) nrOfHits / totalNrOfRequests) * 100;
                        missRate = 100 - hitRate;
                        hitRateTextField.setText(String.format("%.2f",hitRate)+ " %");
                        missRateTextField.setText(String.format("%.2f",missRate)+ " %");
                    }catch(NumberFormatException e){
                        incorrectAddressLabel.setVisible(true);
                    }
                }
        }
    }
}
