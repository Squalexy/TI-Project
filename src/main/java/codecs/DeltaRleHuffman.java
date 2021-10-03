package codecs;

import compressionAlgorithms.RLE;
import compressionAlgorithms.huffmanTree.Huffman;
import org.apache.commons.io.FileUtils;
import transforms.DeltaEncoding;
import utils.Entropy;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeltaRleHuffman {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** Delta Encoding + RLE + Huffman ***");

        String path = "deltaRleHuffman/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("deltaRleHuffman.data");;

        long startTime = utils.beginTimer();

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long initialSize = encodedList.size();
        System.out.println("Initial length: " + initialSize);

        // ------------------ DELTA ENCODING ------------------ //
        List<Integer> deltaOutput = DeltaEncoding.encode(encodedList);
        double initialEntropy = Entropy.main(encodedList);
        System.out.printf("Initial Shannon entropy: %.3f\n", initialEntropy);
        double finalEntropy = Entropy.main(deltaOutput);
        System.out.printf("Final Shannon Entropy: %.3f\n", finalEntropy);

        // --------------------- RLE ----------------------- //
        List<Integer> rleOutput = RLE.encode(deltaOutput);

        // ----------------- HUFFMAN TREE ------------------------------- //
        List<Byte> huffmanOutput = Huffman.buildHuffmanTree(rleOutput, outputFilePath.concat("BMR_huffmanMap.obj"));
        byte[] decodedBytes = new byte[huffmanOutput.size()];

        int index = 0;
        for(Byte b: huffmanOutput){
            decodedBytes[index++] = b;
        }
        FileUtils.writeByteArrayToFile(new File(outputPath), decodedBytes);

        long finalSize = decodedBytes.length;
        System.out.println("Final length: " + finalSize);
        utils.calculateCompressionPercentage(initialSize, finalSize);
        utils.calculateRunningProgramTime(startTime);

    }

    public static void decode(String inputFilePath, String outputFilePath) throws IOException{
        //CODE
    }
}
