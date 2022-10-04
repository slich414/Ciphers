package pl.slichota;

import java.io.*;

public class Utility {

    public static String readTextFromFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String text = bufferedReader.readLine();
        bufferedReader.close();
        return text;
    }
    public static void writeTextToFile(File file, String result) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(result);
        bufferedWriter.close();
    }

    public static int countInverse(int coefficient){
        int inverse = 0;
        for(int i=0; i<=25; i++){
            int result;
            result = Math.floorMod((coefficient*i), 26);
            if(result == 1){
                inverse = i;
                break;
            }
        }
        return inverse;
    }
}
