package sample.model;
;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;

public class MainMemory {
    private int addressSize;
    private int size;
    private int dataSizeInBytes;
    private ArrayList<MemoryLine>lines;
    
    public MainMemory(int power, int dataSizeInBytes){
        this.addressSize=power;
        this.size= (int) Math.pow(2,power);
        this.dataSizeInBytes=dataSizeInBytes;
        lines=new ArrayList<>(size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<MemoryLine> getLines() {
        return lines;
    }

    public void setLines(ArrayList<MemoryLine> lines) {
        this.lines = lines;
    }

    public int getDataSizeInBytes() {
        return dataSizeInBytes;
    }

    public void setDataSizeInBytes(int dataSizeInBytes) {
        this.dataSizeInBytes = dataSizeInBytes;
    }

    public int getAddressSize() {
        return addressSize;
    }

    public void setAddressSize(int addressSize) {
        this.addressSize = addressSize;
    }

    public static BitSet intToBitSet(int number, int size) {
        BitSet bitSet = new BitSet(size);
        int i = 0;
        while (number != 0) {
            if (number % 2 != 0) {
                bitSet.set(i);
            }
            ++i;
            number = number >>> 1;
        }
        return bitSet;
    }

    public static int bitSetToInt(BitSet bitSet) {
        int number = 0;
        for (int i = 0; i < bitSet.length(); i++) {
            if (bitSet.get(i)) {
                number |= (1 << i);
            }
        }
        return number;
    }

    public void fillMemory(){

        for (int i=0;i<this.size;i++) {
            ArrayList<Byte> newArray=new ArrayList<>();
            byte[] b = new byte[this.dataSizeInBytes];
            new Random().nextBytes(b);
            for(int j=0;j<this.dataSizeInBytes;j++){
                newArray.add(b[j]);
            }
            BitSet bitSet=new BitSet(size);
            bitSet=intToBitSet(i,addressSize);
            lines.add(new MemoryLine(size,newArray,bitSet,this.addressSize));
        }
    }
}
