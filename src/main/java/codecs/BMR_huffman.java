package codecs;

import compressionAlgorithms.RLE;
import compressionAlgorithms.huffmanTree.Huffman;
import org.apache.commons.io.FileUtils;
import transforms.*;
import utils.Entropy;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BMR_huffman {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();

        System.out.println("*** Burrows-Wheeler + MTF + RLE + Huffman ***");

        String path = "bmr_huffman/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("bmr_huffman.data");

        long startTime = utils.beginTimer();

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);
        ArrayList<Integer> alphabet = utils.createAlphabet();


        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);


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

        // ----------------- HUFFMAN TREE ------------------------------- //
        List<Byte> huffmanOutput = Huffman.buildHuffmanTree(rleOutput, outputFilePath.concat("huffmanMap.obj"));
        byte[] decodedBytes = new byte[huffmanOutput.size()];

        int index = 0;
        for(Byte b: huffmanOutput){
            decodedBytes[index++] = b;
        }

        FileUtils.writeByteArrayToFile(new File(outputPath), decodedBytes);

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);

        utils.calculateCompressionPercentage(tamanhoInicial, tamanhoFinal);
        utils.calculateRunningProgramTime(startTime);
    }

    public static void decode(String inputFilePath, String outputFilePath) throws IOException{
        //CODE
    }
}
