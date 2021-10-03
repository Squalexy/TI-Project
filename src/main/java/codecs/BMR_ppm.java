package codecs;

import compressionAlgorithms.RLE;
import compressionAlgorithms.arithmeticAndPPM.PpmCompress;
import compressionAlgorithms.arithmeticAndPPM.PpmDecompress;
import org.apache.commons.io.FileUtils;
import transforms.BWT;
import transforms.MTF;
import utils.Entropy;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BMR_ppm {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** Burrows-Wheeler + MTF + RLE + PPM ***");

        String path = "BMR_ppm/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath1 = outputFilePath.concat("BMR.data");
        String outputPath2 = outputFilePath.concat("BMR_ppm.data");


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

        byte[] encodedBytes = utils.closeFileEncoded(rleOutput, outputPath1, outputFilePath.concat("indexesWith2bytes.txt"));

        PpmCompress.main(outputPath1, outputPath2);
        byte[] decodedBytes = FileUtils.readFileToByteArray(new File(outputPath2));

        long finalLength = decodedBytes.length;
        System.out.println("Final length: " + finalLength);
        utils.calculateCompressionPercentage(initialLength, finalLength);
        utils.calculateRunningProgramTime(startTime);
    }

    public static void  decode(String inputFilePath) throws IOException {
        System.out.println("----------- DECODER ------------");
        UsefulMethods utils = new UsefulMethods();

        String path = "BMR_ppm/";
        inputFilePath = inputFilePath.concat(path);
        String outputFilePath = inputFilePath.concat("BMR_ppm.bmp");

        // --------------------- PPM DECODER ----------------------- //
        String inputPath = inputFilePath.concat("BMR_ppm.data");
        String ppmOutputPath = inputFilePath.concat("ppmDecoded.data");
        PpmDecompress.main(inputPath, ppmOutputPath);


        System.out.println(inputFilePath.concat("indexesWith2bytes.txt"));
        ArrayList<Integer> decodedList = utils.openEncodedFile(inputFilePath.concat("ppmDecoded.data"), inputFilePath.concat("indexesWith2bytes.txt"));
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

        BWT.mainBWTdecoder((ArrayList<List<Integer>>) output, inputFilePath.concat("burrows-wheelerIndexes.txt"));
        List<Integer> output1 = new ArrayList<>();
        for(List<Integer> list: output){
            for(Integer number: list){
                output1.add(number);
            }
        }

        byte[] decodedBytes1 = utils.closeFileDecoded(output1, outputFilePath);



    }
}
