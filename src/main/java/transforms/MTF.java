package transforms;

import java.util.ArrayList;
import java.util.List;

/*
    https://rosettacode.org/wiki/Move-to-front_algorithm#Java
 */
public class MTF{
    public static List<Integer> encode(List<Integer> msg, List<Integer> symTable){
        List<Integer> output = new ArrayList<>();
        List<Integer> s = new ArrayList<>(symTable);
        for(Integer a : msg){
            int c = a.intValue();
            int idx = getArrayIndex(s, c);
            output.add(idx);
            s.remove(idx);
            s.add(0, c);
        }
        return output;
    }

    public static List<Integer> decode(List<Integer> idxs, List<Integer> symTable){
        List<Integer> output = new ArrayList<>();
        List<Integer> s = new ArrayList<>(symTable);
        for(Integer idx : idxs){
            int c = s.get(idx.intValue());
            output.add(c);
            s.remove(idx.intValue());
            s.add(0, c);
        }
        return output;
    }

    public static int getArrayIndex(List<Integer> arr, int value) {
        int k=0, contador = 0;
        for(Integer number: arr){
            int n = number.intValue();
            if(n==value){
                k=contador;
                break;
            }
            contador++;
        }
        return k;
    }
}

