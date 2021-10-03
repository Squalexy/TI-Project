package codecs;


import compressionAlgorithms.arithmeticAndPPM.*;
import org.apache.commons.io.FileUtils;
import utils.*;

import java.io.File;
import java.io.IOException;


public class PPM {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();
        System.out.println("*** PPM ***");

        String path = "ppm/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("ppm.data");;

        long startTime = utils.beginTimer();

        byte[] encodedList = FileUtils.readFileToByteArray(new File(inputFilePath));
        long initialSize = encodedList.length;
        System.out.println("Initial length: " + initialSize);


        outputPath = outputPath.replace("/", File.separator);
        File f = new File(outputPath);
        f.getParentFile().mkdirs();
        f.createNewFile();
        PpmCompress.main(inputFilePath, outputPath);
        byte[] decodedBytes = FileUtils.readFileToByteArray(new File(outputPath));

        long finalSize = decodedBytes.length;
        System.out.println("Final length: " + finalSize);
        utils.calculateCompressionPercentage(initialSize, finalSize);
        utils.calculateRunningProgramTime(startTime);
    }

    public static void decode(String inputFilePath) throws IOException{
        String path = "ppm/";
        inputFilePath = inputFilePath.concat(path);
        String outputPath = inputFilePath.concat("ppm.bmp");

        // --------------------- PPM DECODER ----------------------- //
        String inputPath = inputFilePath.concat("ppm.data");
        PpmDecompress.main(inputPath, outputPath);
    }
}
