package sample.model;

import sample.presentation.PopUpMessage;
import sample.presentation.SimulationController;
import sample.start.Main;

import java.util.ArrayList;
import java.util.BitSet;

public class MMU {
    private ArrayList<MemoryLine> memoryLines;
    private MemoryLine requestedMemoryLine;
    private Cache cache;
    private Main main;
    private MainMemory mainMemory;
    public void setMain(Main main){
        this.main = main;
    }

    public MMU(ArrayList<MemoryLine> memoryLines, Cache cache, MemoryLine requestedMemoryLine, MainMemory mainMemory) {
        this.memoryLines = memoryLines;
        this.cache = cache;
        this.requestedMemoryLine=requestedMemoryLine;
        this.mainMemory=mainMemory;
    }

    public void manageAddress(Boolean write, ArrayList<Byte>newData) {
        SimulationController simulationController=main.getSimController();
        ArrayList<Byte> dataForSelectedBlock=new ArrayList<>();
        ArrayList<ArrayList<Byte>> allData=new ArrayList<>();
        for(MemoryLine line: memoryLines){
            allData.add(line.getData());
        }
        MemoryLine memoryLine1=requestedMemoryLine;
        BitSet address = memoryLine1.getAddress();
        int n = cache.getBlockOffsetSize();
        int k = cache.getIndexSize();
        int tagSize = cache.getAddressSize() - n - k;
        BitSet blockOffset = address.get(0, n);
        BitSet index = address.get(n, n + k);
        BitSet tag = address.get(n + k, cache.getAddressSize());
        for (CacheLine cacheLine : cache.getLines()) {
            if (cacheLine.getIndex().equals(index)) {
                //some cache lines are still empty
                if (!cacheLine.getValid().get(0) || cacheLine.getTag() == null){
                     if(write){
                         new PopUpMessage("MISS- try again",1);
                      }else{
                         new PopUpMessage("MISS",1);
                     }

                    BitSet one=new BitSet(1);
                    one.set(0);
                    cacheLine.setValid(one);
                    cacheLine.setTag(tag);
                    cacheLine.setData(allData);
                    ArrayList<ArrayList<Byte>> dataRetrieved=cacheLine.getData();
                    dataForSelectedBlock=dataRetrieved.get(MainMemory.bitSetToInt(blockOffset));

                } else {
                    //HIT
                    if(cacheLine.getTag().equals(tag)) {
                        int hits=simulationController.getNrOfHits();
                        simulationController.setNrOfHits(hits+1);
                        new PopUpMessage("HIT",1);
                        ArrayList<ArrayList<Byte>> dataRetrieved=cacheLine.getData();
                        dataForSelectedBlock=dataRetrieved.get(MainMemory.bitSetToInt(blockOffset));
                        if(write){
                            for(MemoryLine memoryLine: mainMemory.getLines()){
                                if(memoryLine.getAddress().equals(address)){
                                    memoryLine.setData(newData);
                                }
                            }
                            for(int i=0;i<dataRetrieved.size();i++){
                                if(i==MainMemory.bitSetToInt(blockOffset)){
                                    dataRetrieved.set(i,newData);
                                }
                            }
                            cacheLine.setData(dataRetrieved);
                        }

                    }else{
                        //MISS
                         if (write){
                             new PopUpMessage("MISS-try again",1);
                         }else {
                             new PopUpMessage("MISS",1);
                         }
                        cacheLine.setTag(tag);
                        cacheLine.setData(allData);
                        ArrayList<ArrayList<Byte>> dataRetrieved=cacheLine.getData();
                        dataForSelectedBlock=dataRetrieved.get(MainMemory.bitSetToInt(blockOffset));
                    }
                }
            }
        }
        simulationController.setCache(cache);
        simulationController.setMainMemory(mainMemory);
        simulationController.refreshTable();
        if(!write)
            simulationController.sendDataToUI(MemoryLine.bytesToString(dataForSelectedBlock));
        else
            simulationController.sendDataToUI("");
    }
}
