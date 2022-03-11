package sample.model;

import java.util.ArrayList;
import java.util.BitSet;

public class Cache {
    private int indexSize;
    private int blockOffsetSize;
    private int addressSize;
    private int nrOfBlocks;
    private int blockSize;
    private ArrayList<CacheLine>lines;
    public Cache(int indexSize, int blockOffsetSize, int addressSize){
        this.indexSize=indexSize;
        this.blockOffsetSize=blockOffsetSize;
        this.addressSize=addressSize;
        this.nrOfBlocks= (int) Math.pow(2,indexSize);
        this.blockSize= (int) Math.pow(2,blockOffsetSize);
        lines=new ArrayList<>();
    }

    public int getIndexSize() {
        return indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public int getBlockOffsetSize() {
        return blockOffsetSize;
    }

    public void setBlockOffsetSize(int blockOffsetSize) {
        this.blockOffsetSize = blockOffsetSize;
    }

    public int getAddressSize() {
        return addressSize;
    }

    public void setAddressSize(int addressSize) {
        this.addressSize = addressSize;
    }

    public int getNrOfBlocks() {
        return nrOfBlocks;
    }

    public void setNrOfBlocks(int nrOfBlocks) {
        this.nrOfBlocks = nrOfBlocks;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public ArrayList<CacheLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<CacheLine> lines) {
        this.lines = lines;
    }

    public void createCache(){
        for(int i=0;i<this.nrOfBlocks;i++){
            BitSet valid=new BitSet(1);
            valid=MainMemory.intToBitSet(0,1);
            CacheLine cacheLine=new CacheLine(MainMemory.intToBitSet(i,this.indexSize),this.indexSize,valid,blockOffsetSize,addressSize);
            lines.add(cacheLine);
        }
    }
}
