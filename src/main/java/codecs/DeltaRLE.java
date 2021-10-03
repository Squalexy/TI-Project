package codecs;

import utils.Entropy;
import utils.UsefulMethods;
import transforms.*;
import compressionAlgorithms.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeltaRLE {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** Delta Encoding + RLE ***");

        String path = "deltaRLE/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("deltaRLE.data");;

        long startTime = utils.beginTimer();

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long initialSize = encodedList.size();
        System.out.println("Initial length: " + initialSize);

        // ------------------ DELTA ENCODING ------------------ //
        List<Integer> deltaOutput = DeltaEncoding.encode(encodedList);

        // --------------------- RLE ----------------------- //
        List<Integer> rleOutput = RLE.encode(deltaOutput);
        double initialEntropy = Entropy.main(encodedList);
        System.out.printf("Initial Shannon entropy: %.3f\n", initialEntropy);
        double finalEntropy = Entropy.main(deltaOutput);
        System.out.printf("Final Shannon Entropy: %.3f\n", finalEntropy);

        byte[] decodedBytes = utils.closeFileEncoded(rleOutput, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long finalSize = decodedBytes.length;
        System.out.println("Final length: " + finalSize);
        utils.calculateCompressionPercentage(initialSize, finalSize);
        utils.calculateRunningProgramTime(startTime);
    }

    public static void decode(String inputFilePath, String outputFilePath) throws IOException{
        //CODE
    }
}
