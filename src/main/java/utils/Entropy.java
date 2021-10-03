package utils;

import java.lang.Math;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/*
    https://rosettacode.org/wiki/Entropy#Java
 */

public class Entropy {
    @SuppressWarnings("boxing")
    public static double getShannonEntropy(List<Integer> s) {
        int n = 0;
        Map<Integer, Integer> occ = new HashMap<>();

        for (int c_ = 0; c_ < s.size(); ++c_) {
            Integer cx = s.get(c_);
            if (occ.containsKey(cx)) {
                occ.put(cx, occ.get(cx) + 1);
            } else {
                occ.put(cx, 1);
            }
            ++n;
        }

        double e = 0.0;
        for (Map.Entry<Integer, Integer> entry : occ.entrySet()) {
            Integer cx = entry.getKey();
            double p = (double) entry.getValue() / n;
            e += p * log2(p);
        }
        return -e;
    }

    private static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }
    public static double main(List<Integer> args) {
        return Entropy.getShannonEntropy(args);
    }

}