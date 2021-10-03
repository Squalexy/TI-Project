package codecs;

import compressionAlgorithms.huffmanTree.Huffman;
import transforms.DeltaEncoding;
import utils.Entropy;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

import static utils.UsefulMethods.closeFileDecoded;


public class DeltaHuffman {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** Delta Encoding + Huffman ***");

        String path = "deltaHuffman/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("deltaHuffman.data");;

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


        // ----------------- HUFFMAN TREE ------------------------------- //
        List<Byte> huffmanOutput = Huffman.buildHuffmanTree(deltaOutput, outputFilePath.concat("huffmanMap.obj"));
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

    public static void decode(String inputFilePath) throws IOException{
        UsefulMethods utils = new UsefulMethods();

        String path = "deltaHuffman/";
        inputFilePath = inputFilePath.concat(path);
        inputFilePath = inputFilePath.replace(File.separator, "/");
        String outputPath = inputFilePath.concat("deltaHuffman.bmp");


        byte[] fileContent = FileUtils.readFileToByteArray(new File(inputFilePath.concat("deltaHuffman.data")));

        int index = 0;
        List<Byte> decodedBytes = new ArrayList<>();
        for(Byte b: fileContent){
            decodedBytes.add(b);
        }

        List<Integer> huffmanDecoded = Huffman.decode(inputFilePath.concat("huffmanMap.obj"), decodedBytes);

        List<Integer> deltaDecoded = DeltaEncoding.decode(huffmanDecoded);

        byte[] decodedBytes1 = closeFileDecoded(deltaDecoded, outputPath);
    }
}
