import org.apache.commons.codec.DecoderException;

import codecs.*;
import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    /*
    ELIMINAR !!!
     */
    private static String lena_inputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/original/lena.bmp";
    private static String lena_outputPath = "C:/Users/ASUS/Documents/DEI/2o Ano/TI/Pratico/TP2/data/output3.0/lena/";


    public static void main(String[] args) throws IOException, DecoderException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Choose which codec to encode (1,...,10):\n" +
                    "1) BWT -> MTF -> RLE\n" +
                    "2) BWT -> MTF -> RLE -> Deflate\n" +
                    "3) BWT -> MTF -> RLE -> Huffman\n" +
                    "4) BWT -> MTF -> RLE -> PPM\n" +
                    "5) Delta -> Huffman\n" +
                    "6) Delta -> RLE\n" +
                    "7) Delta -> RLE -> Huffman\n" +
                    "8) CALIC\n" +
                    "9) PPM\n" +
                    "10) Deflate\n" +
                    "Press 0 to Exit\n" +
                    "Choice: ");

            String choice = sc.nextLine();
            System.out.println();
            switch (choice) {
                case "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" -> {
                    runEncoder(choice);
                    break;
                }
                default -> System.out.println("Wrong option. Pick a valid number.");
            }

            if (choice.equals('0')) {
                break;
            }
        }


        while (true) {
            System.out.print("Choose which codec to decode (1,...,10):\n" +
                    "1) BWT -> MTF -> RLE\n" +
                    "2) BWT -> MTF -> RLE -> PPM\n" +
                    "3) Delta -> Huffman\n" +
                    "4) PPM\n" +
                    "Press 0 to Exit\n" +
                    "Choice: ");

            String choice = sc.nextLine();
            System.out.println();
            switch (choice) {
                case "1", "2", "3", "4" -> {
                    runDecoder(choice);
                    break;
                }
                case "0" -> {
                    return;
                }
                default -> System.out.println("Wrong option. Pick a valid number.");
            }
        }
    }

    public static void runEncoder(String choice) throws IOException {
        /*
        Path currentPath = Paths.get("");
        String egg_inputPath =  currentPath.toAbsolutePath().toString() + "/data/original/lena.bmp";
        String egg_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/lena/";
        codec(egg_inputPath, egg_outputPath, choice);
        */
        Path currentPath = Paths.get("");
        String egg_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/egg.bmp";
        String egg_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/egg/";
        String landscape_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/landscape.bmp";
        String landscape_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/landscape/";
        String pattern_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/pattern.bmp";
        String pattern_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/pattern/";
        String zebra_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/zebra.bmp";
        String zebra_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/zebra/";


        System.out.println("----------------------------------\n\t\t\tegg.bmp\n----------------------------------");
        encodeCodecs(egg_inputPath, egg_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tlandscape.bmp\n----------------------------------");
        encodeCodecs(landscape_inputPath, landscape_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tzebra.bmp\n----------------------------------");
        encodeCodecs(zebra_inputPath, zebra_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tpattern.bmp\n----------------------------------");
        encodeCodecs(pattern_inputPath, pattern_outputPath, choice);
        System.out.println("----------------------------------\n\n");
    }

    public static void encodeCodecs(String inputFilePath, String outputFilePath, String choice) throws IOException {
        switch (choice) {
            case "1" -> {
                BMR.encode(inputFilePath, outputFilePath);
                break;
            }
            case "2" -> {
                BMR_deflate.encode(inputFilePath, outputFilePath);
                break;
            }
            case "3" -> {
                BMR_huffman.encode(inputFilePath, outputFilePath);
                break;
            }
            case "4" -> {
                BMR_ppm.encode(inputFilePath, outputFilePath);
                break;
            }
            case "5" -> {
                DeltaHuffman.encode(inputFilePath, outputFilePath);
                break;
            }
            case "6" -> {
                DeltaRLE.encode(inputFilePath, outputFilePath);
                break;
            }
            case "7" -> {
                DeltaRleHuffman.encode(inputFilePath, outputFilePath);
                break;
            }
            case "8" -> {
                PythonInterpreter interpreter = new PythonInterpreter();
                interpreter.execfile("main.py");
                break;
            }
            case "9" -> {
                PPM.encode(inputFilePath, outputFilePath);
                break;
            }
            case "10" -> {
                Deflate.encode(inputFilePath, outputFilePath);
            }
        }
    }


    public static void runDecoder(String choice) throws IOException {
        /*
        Path currentPath = Paths.get("");
        String egg_inputPath =  currentPath.toAbsolutePath().toString() + "/data/original/lena.bmp";
        String egg_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/lena/";
        codec(egg_inputPath, egg_outputPath, choice);
        */
        Path currentPath = Paths.get("");
        String egg_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/egg.bmp";
        String egg_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/egg/";
        String landscape_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/landscape.bmp";
        String landscape_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/landscape/";
        String pattern_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/pattern.bmp";
        String pattern_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/pattern/";
        String zebra_inputPath = currentPath.toAbsolutePath().toString() + "/data/original/zebra.bmp";
        String zebra_outputPath = currentPath.toAbsolutePath().toString() + "/data/output/zebra/";


        System.out.println("----------------------------------\n\t\t\tegg.bmp\n----------------------------------");
        encodeCodecs(egg_inputPath, egg_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tlandscape.bmp\n----------------------------------");
        encodeCodecs(landscape_inputPath, landscape_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tzebra.bmp\n----------------------------------");
        encodeCodecs(zebra_inputPath, zebra_outputPath, choice);
        System.out.println("----------------------------------\n\n----------------------------------\n\t\t\tpattern.bmp\n----------------------------------");
        encodeCodecs(pattern_inputPath, pattern_outputPath, choice);
        System.out.println("----------------------------------\n\n");
    }

    public static void decodeCodecs(String inputFilePath, String outputFilePath, String choice) throws IOException {
        switch (choice) {
            case "1" -> {
                BMR.encode(inputFilePath, outputFilePath);
                BMR.decode(outputFilePath);
                break;
            }
            case "2" -> {
                BMR_ppm.encode(inputFilePath, outputFilePath);
                BMR_ppm.decode(outputFilePath);
                break;
            }
            case "3" -> {
                DeltaHuffman.encode(inputFilePath, outputFilePath);
                DeltaHuffman.decode(outputFilePath);
                break;
            }
            case "4" -> {
                PPM.encode(inputFilePath, outputFilePath);
                PPM.decode(outputFilePath);
                break;
            }
        }
    }
}