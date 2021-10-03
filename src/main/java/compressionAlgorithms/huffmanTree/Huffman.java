package compressionAlgorithms.huffmanTree;

import java.io.*;
import java.io.IOException;
import java.util.*;

/*
    REFERENCES:
     - https://www.techiedelight.com/huffman-coding/
     - main.py Developed by Marco Simões on 07/12/2020
 */
public class Huffman {
    // Traverse the compressionAlgorithms.huffmanTree.Huffman Tree and store compressionAlgorithms.huffmanTree.Huffman Codes in a map.
    public static void encode(Node root, String str,
                              Map<Integer, String> huffmanCode) {
        if (root == null) {
            return;
        }

        // Found a leaf node
        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }

        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);


    }

    // Traverse the compressionAlgorithms.huffmanTree.Huffman Tree and decode the encoded string
    public static List<Integer> decode(String filePath, List<Byte> encodedData) throws IOException {
        Map<Integer, String> huffmanCode = openHashFromFile(filePath);
        Map<Long[], Integer> newMap = new HashMap<>();
        for (Map.Entry<Integer, String> entry : huffmanCode.entrySet()) {
            Long[] numberAndSize = {Long.parseLong(entry.getValue(), 2), (long) entry.getValue().length()};
            newMap.put(numberAndSize, entry.getKey());
        }

        List<Integer> decodedData = new ArrayList<>();
        List<Integer> masks = new ArrayList<>(Arrays.asList(128, 64, 32, 16, 8, 4, 2, 1));

        long buffer = 0;
        int size = 0;
        for (Byte b : encodedData) {
            int shift = 7;
            for (Integer m : masks) {
                buffer = (buffer << 1) + ((b & m) >> shift--);
                size++;

                for (Long[] entry : newMap.keySet()){
                    if(entry[0] == buffer && entry[1] == size){
                        Integer value = newMap.get(entry);
                        if (value.intValue() == -256) {
                            return decodedData;
                        }

                        decodedData.add(value.intValue());

                        buffer = 0;
                        size = 0;
                        break;
                    }
                }

            }
        }
        return decodedData;
    }


    // Utility function to check if compressionAlgorithms.huffmanTree.Huffman Tree contains only a single node
    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    // Builds compressionAlgorithms.huffmanTree.Huffman Tree and decode given input text
    public static List<Byte> buildHuffmanTree(List<Integer> text, String filePath) throws IOException {
        // Base case: empty string
        if (text == null || text.size() == 0) {
            return null;
        }

        // Count frequency of appearance of each character
        // and store it in a map

        text.add(-256); //EOF

        Map<Integer, Integer> freq = new HashMap<>();
        for (Integer c : text) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Create a priority queue to store live nodes of compressionAlgorithms.huffmanTree.Huffman tree
        // Notice that highest priority item has lowest frequency

        PriorityQueue<Node> pq;
        pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

        // Create a leaf node for each character and add it
        // to the priority queue.

        for (var entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        // do till there is more than one node in the queue
        while (pq.size() != 1) {
            // Remove the two nodes of highest priority
            // (lowest frequency) from the queue

            Node left = pq.poll();
            Node right = pq.poll();

            // Create a new internal node with these two nodes as children
            // and with frequency equal to the sum of the two nodes
            // frequencies. Add the new node to the priority queue.

            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        // root stores pointer to root of compressionAlgorithms.huffmanTree.Huffman Tree
        Node root = pq.peek();

        // Traverse the compressionAlgorithms.huffmanTree.Huffman tree and store the compressionAlgorithms.huffmanTree.Huffman codes in a map
        Map<Integer, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);
        saveHashToFile(huffmanCode, filePath);


        List<Byte> encodedData = new ArrayList<>();

        int buffer = 0;
        int size = 0;
        for (Integer s : text) {
            int codeSize = huffmanCode.get(s).length();
            String value = huffmanCode.get(s);

            buffer = (buffer << codeSize) + Integer.parseInt(value, 2);

            size += codeSize;
            while (size >= 8) {
                int byte_ = buffer >> (size - 8);
                encodedData.add((byte) byte_);

                buffer = buffer - (byte_ << (size - 8));
                size -= 8;
            }
        }
        if (size > 0) {
            int byte_ = buffer << (8 - size);
            encodedData.add((byte) byte_);
        }

        return encodedData;
    }

    public static void saveHashToFile(Map<Integer, String> hash, String path) throws IOException {
        path = path.replace("/", File.separator);
        File f = new File(path);
        f.getParentFile().mkdirs();
        f.createNewFile();

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hash);
            oos.close();
            fos.close();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static Map<Integer, String> openHashFromFile(String filePath) throws IOException{
        Map<Integer, String> map = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (HashMap<Integer, String>) ois.readObject();
            ois.close();
            fis.close();
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }catch(ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return map;
    }

}