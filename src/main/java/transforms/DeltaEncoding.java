package transforms;

import java.util.*;

/*
https://github.com/sebysr/HackerRank/blob/master/Booking/DeltaEncoding.java
 */
public class DeltaEncoding {

    public static List<Integer> encode(List<Integer> list) {
        List<Integer> deltaArray = new ArrayList<>();
        int i=0;

        deltaArray.add(0, list.get(0).intValue());
        for(i = 1; i < list.size(); i++) {
            deltaArray.add(i, list.get(i).intValue() - list.get(i-1).intValue());
        }

        return deltaArray;
    }

    public static List<Integer> decode(List<Integer> deltaArray){
        List<Integer> deltaCopy = new ArrayList<>(deltaArray);
        List<Integer> output = new ArrayList<>();
        int i = 0;

        output.add(deltaCopy.get(i).intValue());
        for(i = 1; i < deltaCopy.size(); i++){
            output.add(deltaCopy.get(i).intValue() + deltaCopy.get(i-1).intValue());
            deltaCopy.set(i, deltaCopy.get(i).intValue() + deltaCopy.get(i-1).intValue());
        }

        return output;
    }
}
