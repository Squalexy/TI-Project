package transforms;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    https://rosettacode.org/wiki/Burrows%E2%80%93Wheeler_transform#Python
 */
public class BWT {

    private static int bwt(List<Integer> s) {
        List<Integer> ss = new ArrayList<>(s);
        List<List<Integer>> table = new ArrayList<>();
        for (int i = 0; i < ss.size(); i++) {
            List<Integer> before = ss.subList(i, ss.size());
            List<Integer> after = ss.subList(0, i);

            List<Integer> soma = new ArrayList<>();
            for(Integer b: before){
                soma.add(b);
            }for(Integer a: after){
                soma.add(a);
            }
            table.add(soma);
        }

        table.sort(comparator);
        int index = -1;
        for(int i = 0; i < table.size(); i++){
            if((table.get(i)).equals(s)){
                index = i;
                break;
            }
        }

        int contador = 0;
        for (List<Integer> str : table) {
            s.set(contador++, str.get(str.size() - 1));
        }

        return index;
    }

    private static Comparator<? super List<Integer>> comparator = (list1, list2) -> {
        for (int i = 0; i < list1.size(); i++) {
            int value = Integer.compare(list1.get(i), list2.get(i));
            if (value != 0)
                return value;
        }
        return 0;
    };

    private static List<Integer> ibwt(List<Integer> r, int index) {
        int len = r.size();
        List<List<Integer>> table = new ArrayList<>();

        List<Integer> listZero = new ArrayList<>();
        for (int i = 0; i < len; ++i) {
            table.add(listZero);
        }
        for (int j = 0; j < len; ++j) {
            for (int i = 0; i < len; ++i) {
                List<Integer> list = new ArrayList<>();
                list.add(r.get(i));
                if(!table.get(i).isEmpty()){
                    for(Integer t: table.get(i)){
                        list.add(t);
                    }
                }
                table.set(i,list);
            }
            table.sort(comparator);
        }

        for (List<Integer> row : table) {
            if(table.indexOf(row) == index){
                return row;
            }
        }
        return null;
    }

    public static void mainBWTencoder(ArrayList<List<Integer>> tests, String path) throws IOException {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < tests.size(); i++) {
            try {
                indexes.add(bwt(tests.get(i)));
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        path = path.replace("/", File.separator);
        File f = new File(path);
        f.getParentFile().mkdirs();
        f.createNewFile();

        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(indexes);
            oos.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Erro a criar ficheiro.");
        } catch (IOException ex) {
            System.out.println("Erro a escrever para o ficheiro.");
        }
    }
    public static void mainBWTdecoder(ArrayList<List<Integer>> tests, String path) {
        File f = new File(path);
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


        for (int i = 0; i < tests.size(); i++) {
            try {
                tests.set(i, ibwt( tests.get(i), indexes.get(i) ) );
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}