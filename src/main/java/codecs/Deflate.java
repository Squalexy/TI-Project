package codecs;
import org.apache.commons.io.FileUtils;
import utils.UsefulMethods;

import java.io.File;
import java.io.IOException;

public class Deflate {
    public static void encode(String inputFilePath, String outputFilePath) throws IOException {
        UsefulMethods utils = new UsefulMethods();

        System.out.println("*** Deflate ***");
        String path = "deflate/";
        outputFilePath = outputFilePath.concat(path);
        String outputPath = outputFilePath.concat("deflate.data");


        long startTime = utils.beginTimer();

        byte[] fileContent = FileUtils.readFileToByteArray(new File(inputFilePath));

        long initialLength = fileContent.length;
        System.out.println("Initial length: " + initialLength);

        byte[] deflateBytes = compressionAlgorithms.Deflate.compressBytes(fileContent);
        FileUtils.writeByteArrayToFile(new File(outputPath), deflateBytes);


        long finalLength = deflateBytes.length;
        System.out.println("Final length: " + finalLength);
        utils.calculateCompressionPercentage(initialLength, finalLength);

        utils.calculateRunningProgramTime(startTime);
    }

    public static void  decode(){
        //CODE
    }
}
