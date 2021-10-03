package codecs;

import transforms.*;
import compressionAlgorithms.*;
import utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BMR {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** Burrows-Wheeler + MTF + RLE ***");

        String path = "bmr/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("bmr.data");


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

        byte[] decodedBytes = utils.closeFileEncoded(rleOutput, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long finalLength = decodedBytes.length;
        System.out.println("Final length: " + finalLength);
        utils.calculateCompressionPercentage(initialLength, finalLength);

        utils.calculateRunningProgramTime(startTime);
    }

    public static void  decode(String outputFilePath) throws IOException {
        System.out.println("----------- DECODER ------------");
        UsefulMethods utils = new UsefulMethods();

        String path = "bmr/";
        outputFilePath = outputFilePath.concat(path);
        outputFilePath = outputFilePath.replace(File.separator, "/");

        String outputPath = outputFilePath.concat("bmr.bmp");

        ArrayList<Integer> decodedList = utils.openEncodedFile(outputFilePath.concat("bmr.data"), outputFilePath.concat("indexesWith2bytes.txt"));
        ArrayList<Integer> alphabet = utils.createAlphabet();


        // ----------------- RUN LENGTH  ------------------------ //
        List<Integer> rleOutput = RLE.decode(decodedList);

        // ---------------------  BURROWS WHEELER DECODER ----------------------- //
        int divisionLength1 = 1000; // 23659464/8= 2957433/100 =2957433
        final int divisionNumber1 = (int)Math.ceil((double)rleOutput.size() / divisionLength1) ;

        ArrayList<List<Integer>> parts = utils.separateFileInBlocks(divisionNumber1, divisionLength1, (ArrayList<Integer>) rleOutput);


        List<List<Integer>> output = new ArrayList<>();
        for(List<Integer> list: parts){
            // ------------------ MOVE-TO-FRONT ---------------------------- //
            output.add(MTF.decode(list, alphabet));
        }

        BWT.mainBWTdecoder((ArrayList<List<Integer>>) output, outputFilePath.concat("burrows-wheelerIndexes.txt"));
        List<Integer> output1 = new ArrayList<>();
        for(List<Integer> list: output){
            for(Integer number: list){
                output1.add(number);
            }
        }

        byte[] decodedBytes1 = utils.closeFileDecoded(output1, outputPath);
    }
}
