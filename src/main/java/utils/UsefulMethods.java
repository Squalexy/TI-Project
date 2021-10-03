package utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsefulMethods {
    public static ArrayList<List<Integer>> separateFileInBlocks(int divisionNumber, int divisionLength, ArrayList<Integer> encodedList){
        int begginingOfString = 0;
        ArrayList<List<Integer>> parts = new ArrayList<>();
        for(int i = 0; i < divisionNumber; i++) {
            if (i + 1 == divisionNumber){ parts.add(encodedList.subList(begginingOfString, encodedList.size())); }
            else{ parts.add(encodedList.subList(begginingOfString, begginingOfString + divisionLength)); }
            begginingOfString += divisionLength;
        }

        return parts;
    }

    public static ArrayList<Integer> createAlphabet(){
        ArrayList<Integer> alphabet = new ArrayList<>();
        for(int i = 0; i < 256; i++){
            alphabet.add(i);
        }
        return alphabet;
    }

    public static ArrayList<Integer> openFileAsIntegerArray(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));

        ArrayList<Integer> encodedList = new ArrayList<>();
        for(int i = 0; i < fileContent.length; i++){
            encodedList.add(fileContent[i] & 0xff);
        }

        return encodedList;
    }

    public static ArrayList<Integer> openEncodedFile(String filePath, String outputFilePath) throws IOException{
        System.out.println(outputFilePath);
        File f = new File(outputFilePath);
        ArrayList<Integer> indexes = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            indexes = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a abrir ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a ler ficheiro.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro a converter objeto.");
        }

        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));

        int index = 0;
        ArrayList<Integer> decodedList = new ArrayList<>();
        if(indexes != null && !indexes.isEmpty()){
            for(int i = 0; i < fileContent.length; i++) {
                if (index < indexes.size()) {
                    if (indexes.get(index) == i) {
                        int number = (fileContent[i++] << 8) |
                                ((fileContent[i] & 0xFF) << 0);
                        decodedList.add(number);
                        index++;
                    } else {
                        decodedList.add(fileContent[i] & 0xff);
                    }
                }else {
                    decodedList.add(fileContent[i] & 0xff);
                }
            }
        }else{
            for(int i = 0; i < fileContent.length; i++){
                decodedList.add(fileContent[i] & 0xff);
            }
        }


        return decodedList;
    }

    public static byte[] closeFileEncoded(List<Integer> listaFinal, String outputFilePath, String indexesPath) throws IOException {
        ArrayList<Integer> indexes2byteValues = new ArrayList<>();

        int contador = 0;
        for(Integer number: listaFinal) {
            int numero = number.intValue();
            if(numero < 0 || numero > 255) { contador += 2; }
            else{ contador += 1; }
        }


        byte[] decodedBytes = new byte[contador];
        int index = 0;
        for(Integer number: listaFinal) {
            int numero = number.intValue();
            if(numero < 0 || numero > 255) {
                indexes2byteValues.add(index);
                decodedBytes[index++] = (byte)(numero >> 8);
                decodedBytes[index++] = (byte)((numero >> 0) & 0xff);

            }else{ decodedBytes[index++] = (byte) (numero & 0xff); }
        }
        FileUtils.writeByteArrayToFile(new File(outputFilePath), decodedBytes);


        indexesPath = indexesPath.replace("/", File.separator);
        File f = new File(indexesPath);
        f.getParentFile().mkdirs();
        f.createNewFile();
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(indexes2byteValues);
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }


        return decodedBytes;
    }

    public static byte[] closeFileDecoded(List<Integer> listaFinal, String outputFilePath) throws IOException {
        ArrayList<Integer> indexes2byteValues = new ArrayList<>();

        int contador = 0;
        for(Integer number: listaFinal) {
            int numero = number.intValue();
            if(numero < 0 || numero > 255) { contador += 2; }
            else{ contador += 1; }
        }

        byte[] decodedBytes = new byte[contador];
        int index = 0;
        for(Integer number: listaFinal) {
            int numero = number.intValue();
            if(numero < 0 || numero > 255) {
                indexes2byteValues.add(index);
                decodedBytes[index++] = (byte)(numero >> 8);
                decodedBytes[index++] = (byte)((numero >> 0) & 0xff);

            }else{ decodedBytes[index++] = (byte) (numero & 0xff); }
        }
        FileUtils.writeByteArrayToFile(new File(outputFilePath), decodedBytes);

        return decodedBytes;
    }

    public static void calculateCompressionPercentage(long inicialLength, long finalLength){
        double compression = ((double)(inicialLength - finalLength) * 100) / inicialLength;
        System.out.printf("Compression: %.2f%%\n", compression);
    }


    public static long beginTimer(){
        long startTime = System.nanoTime();
        return startTime;
    }

    public static void calculateRunningProgramTime(long startTime){
        long endTime = System.nanoTime();

        // get difference of two nanoTime values
        long timeElapsed = endTime - startTime;

        // 1 second = 1_000_000_000 nano seconds
        double elapsedTimeInSecond = (double) timeElapsed / 1_000_000_000;
        //System.out.println(elapsedTimeInSecond + " seconds");

        // TimeUnit
        long convert = TimeUnit.SECONDS.convert(timeElapsed, TimeUnit.NANOSECONDS);
        System.out.println("Program running time: " + convert + " second(s)");
    }
}
