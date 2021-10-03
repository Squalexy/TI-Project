package codecs;

import compressionAlgorithms.Deflate;
import compressionAlgorithms.RLE;
import org.apache.commons.io.FileUtils;
import transforms.BWT;
import transforms.MTF;
import utils.Entropy;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BMR_deflate {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();

        System.out.println("*** Burrows-Wheeler + MTF + RLE + Deflate ***");
        String path = "bmr_deflate/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath1 = outputFilePath.concat("bmr.data");
        String outputPath2 = outputFilePath.concat("bmr_deflate.data");


        long startTime = utils.beginTimer();

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);
        ArrayList<Integer> alphabet = utils.createAlphabet();

        long initialLength = encodedList.size();
        System.out.println("Initial length: " + initialLength);


        // ------------------ BURROWS-WHEELER TRANSFORM ------------------ //
        int divisionLength = 1000; // 23659464/8= 2957433/100 =2957433
        final int divisionNumber = (int)Math.ceil((double)encodedList.size() / divisionLength) ;

        ArrayList<List<Integer>> parts = utils.separateFileInBlocks(divisionNumber, divisionLength, encodedList);

        BWT.mainBWTencoder(parts, outputFilePath.concat("burrows-wheelerIndexes.txt"));
        List<List<Integer>> mtfOutput = new ArrayList<>();
        for(List<Integer> bwtOutput: parts){
            // ------------------ MOVE-TO-FRONT ---------------------------- //
            mtfOutput.add(MTF.encode(bwtOutput, alphabet));
        }
        List<Integer> output = new ArrayList<>();
        for(List<Integer> list: mtfOutput){
            for(Integer number: list){
                output.add(number);
            }
        }

        // ----------------- RUN LENGTH ENCODING ------------------------ //
        List<Integer> rleOutput = RLE.encode(output);
        double initialEntropy = Entropy.main(encodedList);
        System.out.printf("Initial Shannon entropy: %.3f\n", initialEntropy);
        double finalEntropy = Entropy.main(output);
        System.out.printf("Final Shannon Entropy: %.3f\n", finalEntropy);

        byte[] decodedBytes = utils.closeFileEncoded(rleOutput, outputPath1, outputFilePath.concat("indexesWith2bytes.txt"));
        byte[] deflateBytes = Deflate.compressBytes(decodedBytes);
        FileUtils.writeByteArrayToFile(new File(outputPath2), deflateBytes);

        
        long finalLength = deflateBytes.length;
        System.out.println("Final length: " + finalLength);
        utils.calculateCompressionPercentage(initialLength, finalLength);

        utils.calculateRunningProgramTime(startTime);
    }

    public static void  decode(){
        //CODE
    }
}
