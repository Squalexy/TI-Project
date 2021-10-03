import compressionAlgorithms.RLE;
import transforms.BWT;
import transforms.DeltaEncoding;
import transforms.MTF;
import compressionAlgorithms.huffmanTree.Huffman;
import utils.UsefulMethods;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EncodingAndDecodingTesting {
    private static String egg_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/egg.bmp";
    private static String egg_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/tests/egg/encoding/";
    private static String landscape_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/landscape.bmp";
    private static String landscape_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/tests/landscape/encoding/";
    private static String pattern_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/pattern.bmp";
    private static String pattern_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/tests/pattern/encoding/";
    private static String zebra_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/zebra.bmp";
    private static String zebra_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/tests/zebra/encoding/";

    /*
    ELIMINAR !!!
     */
    private static String lena_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/lena.bmp";
    private static String lena_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/tests/lena/encoding/";

    public static void main() throws IOException {
        BWT(egg_inputPath, egg_outputPath);
        MTF(egg_inputPath, egg_outputPath);
        RLE(egg_inputPath, egg_outputPath);
        huffman(egg_inputPath, egg_outputPath);
        delta(egg_inputPath, egg_outputPath);
    }

    private static void delta(String inputFilePath, String outputFilePath) throws IOException{
        UsefulMethods utils = new UsefulMethods();

        // ------------------- ENCODER ----------------------- //
        System.out.println("*** Delta - ENCODER ***");

        String path = "delta/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("delta.data");

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);

        List<Integer> deltaOutput = DeltaEncoding.encode(encodedList);
        System.out.println("Delta size: " + deltaOutput.size());

        byte[] decodedBytes = utils.closeFileEncoded(deltaOutput, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);
        utils.calculateCompressionPercentage(tamanhoInicial, tamanhoFinal);
        System.out.println();


        // --------------------- DECODER ----------------------- //
        System.out.println("*** Delta - DECODER ***");

        String path2 = "delta.bmp";
        String outputPath2 = outputFilePath.concat(path2);

        ArrayList<Integer> decodedList = utils.openEncodedFile(outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoInicial1 = decodedList.size();
        System.out.println("Initial length: " + tamanhoInicial1);

        List<Integer> deltaDecoded = DeltaEncoding.decode(decodedList);
        System.out.println("Huffman size: " + deltaDecoded.size());

        byte[] decodedBytes1 = utils.closeFileDecoded(deltaDecoded, outputPath2);

        long tamanhoFinal1 = decodedBytes1.length;
        System.out.println("Final length: " + tamanhoFinal1);
        utils.calculateCompressionPercentage(tamanhoInicial1, tamanhoFinal1);
    }


    private static void huffman(String inputFilePath, String outputFilePath) throws IOException{
        UsefulMethods utils = new UsefulMethods();

        // ------------------- ENCODER ----------------------- //
        System.out.println("*** Huffman - ENCODER ***");

        String path = "huffman/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("huffman.data");

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);

        List<Byte> huffmanOutput = Huffman.buildHuffmanTree(encodedList, "huffmanMap.obj");
        Byte[] decodedBytes = new Byte[huffmanOutput.size()];

        int index = 0;
        for(Byte b: huffmanOutput){
            decodedBytes[index++] = b;
        }

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);
        System.out.println();


        // --------------------- DECODER ----------------------- //
        System.out.println("*** Huffman - DECODER ***");

        String path2 = "huffman.bmp";
        String outputPath2 = outputFilePath.concat(path2);


        List<Integer> huffmanDecoded = Huffman.decode("huffmanMap.obj", huffmanOutput);

        System.out.println("Huffman size: " + huffmanDecoded.size());

        byte[] decodedBytes1 = utils.closeFileDecoded(huffmanDecoded, outputPath2);

        long tamanhoFinal1 = decodedBytes1.length;
        System.out.println("Final length: " + tamanhoFinal1);
    }


    private static void RLE(String inputFilePath, String outputFilePath) throws IOException{
        UsefulMethods utils = new UsefulMethods();

        // ------------------- ENCODER ----------------------- //
        System.out.println("*** compressionAlgorithms.RLE - ENCODER ***");

        String path = "rle/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("rle.data");

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);

        List<Integer> rleOutput = RLE.encode(encodedList);
        System.out.println("compressionAlgorithms.RLE size: " + rleOutput.size());

        byte[] decodedBytes = utils.closeFileEncoded(rleOutput, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);
        utils.calculateCompressionPercentage(tamanhoInicial, tamanhoFinal);
        System.out.println();


        // --------------------- DECODER ----------------------- //
        System.out.println("*** compressionAlgorithms.RLE - DECODER ***");

        String path2 = "rle.bmp";
        String outputPath2 = outputFilePath.concat(path2);

        ArrayList<Integer> decodedList = utils.openEncodedFile(outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoInicial1 = decodedList.size();
        System.out.println("Initial length: " + tamanhoInicial1);

        List<Integer> rleDecoded = RLE.decode(decodedList);
        System.out.println("compressionAlgorithms.RLE size: " + rleDecoded.size());

        byte[] decodedBytes1 = utils.closeFileDecoded(rleDecoded, outputPath2);

        long tamanhoFinal1 = decodedBytes1.length;
        System.out.println("Final length: " + tamanhoFinal1);
        utils.calculateCompressionPercentage(tamanhoInicial1, tamanhoFinal1);
    }

    private static void MTF(String inputFilePath, String outputFilePath) throws IOException{
        UsefulMethods utils = new UsefulMethods();

        // ------------------- ENCODER ----------------------- //
        System.out.println("*** MTF - ENCODER ***");

        String path = "mtf/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("mtf.data");

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);


        List<Integer> alfabeto = utils.createAlphabet();
        List<Integer> mtfOutput = MTF.encode(encodedList, alfabeto);

        byte[] decodedBytes = utils.closeFileEncoded(mtfOutput, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);
        utils.calculateCompressionPercentage(tamanhoInicial, tamanhoFinal);
        System.out.println();


        // --------------------- DECODER ----------------------- //
        System.out.println("*** MTF - DECODER ***");

        String path2 = "mtf.bmp";
        String outputPath2 = outputFilePath.concat(path2);

        ArrayList<Integer> decodedList = utils.openEncodedFile(outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoInicial1 = decodedList.size();
        System.out.println("Initial length: " + tamanhoInicial1);

        List<Integer> mtfdecoded = MTF.decode(decodedList, alfabeto);

        byte[] decodedBytes1 = utils.closeFileDecoded(mtfdecoded, outputPath2);

        long tamanhoFinal1 = decodedBytes1.length;
        System.out.println("Final length: " + tamanhoFinal1);
        utils.calculateCompressionPercentage(tamanhoInicial1, tamanhoFinal1);
    }

    private static void BWT(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();

        // ------------------- ENCODER ----------------------- //
        System.out.println("*** Burrows-Wheeler - ENCODER ***");

        String path = "bwt/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("burrows-wheeler.data");

        ArrayList<Integer> encodedList = utils.openFileAsIntegerArray(inputFilePath);

        long tamanhoInicial = encodedList.size();
        System.out.println("Initial length: " + tamanhoInicial);

        int divisionLength = 500; // 23659464/8= 2957433/100 =2957433
        final int divisionNumber = (int)Math.ceil((double)encodedList.size() / divisionLength) ;

        ArrayList<List<Integer>> parts = utils.separateFileInBlocks(divisionNumber, divisionLength, encodedList);
        BWT.mainBWTencoder(parts, outputFilePath.concat("burrows-wheelerIndexes.txt"));
        List<Integer> output = new ArrayList<>();
        for(List<Integer> list: parts){
            for(Integer number: list){
                output.add(number);
            }
        }

        byte[] decodedBytes = utils.closeFileEncoded(output, outputPath, outputFilePath.concat("indexesWith2bytes.txt"));

        long tamanhoFinal = decodedBytes.length;
        System.out.println("Final length: " + tamanhoFinal);
        utils.calculateCompressionPercentage(tamanhoInicial, tamanhoFinal);
        System.out.println();



        // --------------------- DECODER ----------------------- //
        System.out.println("*** Burrows-Wheeler - DECODER ***");

        String path2 = "burrows-wheeler.bmp";
        String outputPath2 = outputFilePath.concat(path2);


        ArrayList<Integer> decodedList = utils.openEncodedFile(outputPath, outputFilePath.concat("indexesWith2bytes.txt"));


        long tamanhoInicial1 = decodedList.size();
        System.out.println("Initial length: " + tamanhoInicial1);


        int divisionLength1 = 500; // 23659464/8= 2957433/100 =2957433
        final int divisionNumber1 = (int)Math.ceil((double)decodedList.size() / divisionLength1) ;

        ArrayList<List<Integer>> parts1 = utils.separateFileInBlocks(divisionNumber1, divisionLength1, decodedList);

        BWT.mainBWTdecoder(parts1, outputFilePath.concat("burrows-wheelerIndexes.txt"));
        List<Integer> output1 = new ArrayList<>();
        for(List<Integer> list: parts1){
            for(Integer number: list){
                output1.add(number);
            }
        }

        byte[] decodedBytes1 = utils.closeFileDecoded(output1, outputPath2);

        long tamanhoFinal1 = decodedBytes1.length;
        System.out.println("Final length: " + tamanhoFinal1);
        utils.calculateCompressionPercentage(tamanhoInicial1, tamanhoFinal1);
    }
}
