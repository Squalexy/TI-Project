package compressionAlgorithms.huffmanTree;

class Node {
    Integer ch;
    Integer freq;
    Node left = null, right = null;

    Node(Integer ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(Integer ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}