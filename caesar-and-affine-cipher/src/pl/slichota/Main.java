package pl.slichota;

import java.io.IOException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println(args[0]);
        System.out.println(args[1]);

        String encryption = args[0];
        if(!checkIfEncryptionModeIsCorrect(encryption)){
            throw new IllegalArgumentException();
        }

        String mode = args[1];
        if(!checkIfWorkModeIsCorrect(mode)){
            throw new IllegalArgumentException();
        }

        switch(encryption){
            case "-c":
                switch (mode) {
                    case "-e" -> CaesarCipher.encryption();
                    case "-d" -> CaesarCipher.decryption();
                    case "-j" -> CaesarCipher.cryptoAnalysisUsingHelpingText();
                    case "-k" -> CaesarCipher.cryptoAnalysis();
                    default -> System.out.println("Something went wrong");
                }
                break;
            case "-a":
                switch (mode) {
                    case "-e" -> AffineCipher.encryption();
                    case "-d" -> AffineCipher.decryption();
                    case "-j" -> AffineCipher.cryptoAnalysisUsingHelpingText();
                    case "-k" -> AffineCipher.cryptoAnalysis();
                    default -> System.out.println("Something went wrong");
                }
                break;
            default:
                System.out.println("Something went wrong");
        }

    }

    public static Boolean checkIfWorkModeIsCorrect(String s){
        return s.equals("-e") || s.equals("-d") || s.equals("-k");
    }
    public static Boolean checkIfEncryptionModeIsCorrect(String s){
        return s.equals("-c") || s.equals("-a");
    }
}
