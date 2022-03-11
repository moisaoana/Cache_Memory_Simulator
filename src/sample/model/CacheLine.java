package sample.model;

import java.util.ArrayList;
import java.util.BitSet;

public class CacheLine {
    private int indexSize;
    private int blockOffSetSize;
    private int addressSize;
    private BitSet index;
    private BitSet valid;
    private BitSet tag;
    private ArrayList<ArrayList<Byte>>data;
    private String printableIndex;
    private String printableValid;
    private String printableTag;
    private String printableData;
    public CacheLine(BitSet index, int indexSize, BitSet valid,int blockOffSetSize, int addressSize){
        this.index=index;
        this.indexSize=indexSize;
        this.valid=valid;
        setPrintableIndex(this.index);
        setPrintableValid(this.valid);
        this.tag=null;
        this.blockOffSetSize=blockOffSetSize;
        this.addressSize=addressSize;
    }

    public BitSet getIndex() {
        return index;
    }

    public void setIndex(BitSet index) {
        this.index = index;
    }

    public BitSet getValid() {
        return valid;
    }

    public void setValid(BitSet valid) {
        this.valid = valid;
        setPrintableValid(valid);
    }

    public BitSet getTag() {
        return tag;
    }

    public void setTag(BitSet tag) {
        this.tag = tag;
        setPrintableTag(tag);
    }

    public ArrayList<ArrayList<Byte>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<Byte>> data) {
        this.data = data;
        setPrintableData(data);
    }

    public String getPrintableIndex() {
        return printableIndex;
    }

    public void setPrintableIndex(BitSet printableIndex) {
        this.printableIndex = MemoryLine.bitSetToString(printableIndex,indexSize);
    }

    public String getPrintableValid() {
        return printableValid;
    }

    public void setPrintableValid(BitSet printableValid) {
        this.printableValid = MemoryLine.bitSetToString(printableValid,1);
    }

    public String getPrintableTag() {
        return printableTag;
    }

    public void setPrintableTag(BitSet printableTag) {
        this.printableTag = MemoryLine.bitSetToString(printableTag,addressSize-indexSize-blockOffSetSize);
    }

    public String getPrintableData() {
        return printableData;
    }

    public void setPrintableData(ArrayList<ArrayList<Byte>>data) {
        StringBuilder print=new StringBuilder();
        int index=0;
        for(ArrayList<Byte> d: data){
            index++;
            print.append(MemoryLine.bytesToString(d));
            if(index!=data.size())
                print.append("   -   ");
        }
        this.printableData = print.toString();
    }
}
