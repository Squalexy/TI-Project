package compressionAlgorithms;

import java.util.ArrayList;
import java.util.List;

/*
https://rosettacode.org/wiki/Run-length_encoding
 */
public class RLE {
    public static ArrayList<Integer> encode(List<Integer> source) {
        ArrayList<Integer> dest = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            int runLength = 1;
            while (i+1 < source.size() && source.get(i).intValue() == source.get(i+1).intValue()) {
                runLength++;
                i++;
            }
            dest.add(runLength);
            dest.add(source.get(i));
        }
        return dest;
    }

    public static List<Integer> decode(List<Integer> source) {
        List<Integer> dest = new ArrayList<>();

        int number;
        for(int i = 0; i < source.size(); i = i + 2){
            number = source.get(i);
            while(number-- != 0){
                dest.add(source.get(i+1));
            }
        }

        return dest;
    }

}