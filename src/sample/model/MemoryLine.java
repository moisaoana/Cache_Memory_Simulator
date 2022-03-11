package sample.model;

import java.util.ArrayList;
import java.util.BitSet;

public class MemoryLine {
    private BitSet address;
    private ArrayList<Byte> data;
    private String printableAddress;
    private String printableData;
    public MemoryLine(int size, ArrayList<Byte> data, BitSet address, int addressSize){
        this.address=new BitSet(size);
        this.address=address;
        this.data=data;
        setPrintableData();
        setPrintableAddress(addressSize);
    }

    public BitSet getAddress() {
        return address;
    }

    public void setAddress(BitSet address) {
        this.address = address;
    }

    public ArrayList<Byte> getData() {
        return data;
    }

    public void setData(ArrayList<Byte> data) {
        this.data = data;
        setPrintableData();
    }

    public String getPrintableAddress() {
        return printableAddress;
    }

    public void setPrintableAddress(int addressSize) {
        this.printableAddress = bitSetToString(this.address,addressSize);
    }

    public String getPrintableData() {
        return printableData;
    }

    public void setPrintableData() {
        this.printableData = bytesToString(this.data);
    }
    public static String bytesToString(ArrayList<Byte> byteArray){
        StringBuilder finalString= new StringBuilder();
        for(Byte by: byteArray) {
            finalString.append(String.format("%8s", Integer.toBinaryString(by & 0xFF)).replace(' ', '0'));
        }
        return  finalString.toString();
    }
    public static String bitSetToString(BitSet bitSet, int size) {
        StringBuilder stringBuilder = new StringBuilder(size);
        for (int i =size - 1; i >= 0; i--)
            stringBuilder.append(bitSet.get(i) ? 1 : 0);
        return stringBuilder.toString();
    }
    public static ArrayList<Byte> fromStringToArrayListByte(String bits){
        StringBuilder eightBits=new StringBuilder();
        ArrayList<Byte> bytes=new ArrayList<>();
        for(int i=0;i<=bits.length();i++){
            if(eightBits.length()==8){
                int nr=Integer.parseInt(eightBits.toString(), 2);
                bytes.add((byte) nr);
                if(i!=bits.length()) {
                    eightBits = new StringBuilder();
                    eightBits.append(bits.charAt(i));
                }
            }else{
                eightBits.append(bits.charAt(i));
            }
        }
        return bytes;
    }
}
