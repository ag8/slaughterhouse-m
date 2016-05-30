package org.andrew.slaughterhouse_m;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main {
    private static List<String> prose = new LinkedList<>();
    private static int distance = 10;
    private static int limit = 100000;
    private static String filepath = "sample.txt";
    private static String outputFilepath = "distance_10.txt";

    private static Map<String, List<List<WordFrequency>>> wordChains = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new Main().init();

        System.out.println(new Main().run(filepath));
    }

    private void init() throws IOException {
        prose.addAll(Utils.getFirstWordsInFile(distance - 1, filepath).stream().collect(Collectors.toList()));
    }

    @NotNull
    private String run(String filepath) throws IOException, ClassNotFoundException {
//        int distance = 5;

        System.out.println("Preparing to get word chains...");
        Utils.pause(500);

        File wordChainCheck = new File("cdkv.wchn");
        if(wordChainCheck.exists() && !wordChainCheck.isDirectory()) {
            File file = new File("cdkv.wchn");
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            wordChains = (HashMap<String, List<List<WordFrequency>>>)s.readObject();
            s.close();
        } else {
            wordChains = Utils.getWordChain(distance, filepath);
            File file = new File("cdkv.wchn");
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(wordChains);
            s.flush();
        }

//        for (String word : wordChains.keySet()) {
//            System.out.println("\tWord: " + word);
//
//            List<List<WordFrequency>> wordChain = wordChains.get(word);
//
//            for (int distance = 0; distance < 5; distance++) {
//                System.out.println("\t\tDistance: " + (distance + 1));
//                List<WordFrequency> wordFrequenciesAtDistance = wordChain.get(distance);
//
//                for (WordFrequency wf : wordFrequenciesAtDistance) {
//                    System.out.println("\t\t\t" + wf);
//                }
//            }
//        }

        for (int i = distance; i < limit; i++) {
//            System.out.println("Prose so far: " + prose);

            List<String> possibleWords = new ArrayList<>();

            for (int j = i - distance; j < i - 1; j++) {
//                System.out.println("\ti=" + i + ", j=" + j + ". Distance get: " + (i - j - 2));
                String previousWord = prose.get(j);
                List<WordFrequency> wordFrequencies = wordChains.get(previousWord).get(i - j - 2);
                for (WordFrequency wordFrequency : wordFrequencies) {
                    int factor = (int)Math.pow(2, distance - (i - j));
//                    System.out.println("\t\tA possible word is " + wordFrequency.getWord() + ". factor is " + factor + ".");
                    for (int k = 0; k < wordFrequency.getFrequency() * factor; k++) {
                        possibleWords.add(wordFrequency.getWord());
                    }
                }
            }

//            System.out.println(possibleWords);
            String nextWord;
            if (possibleWords.size() != 0) {
                nextWord = Utils.randomWordFromList(possibleWords);
            } else {
                break;
            }
            prose.add(nextWord);
        }

        PrintWriter writer = new PrintWriter(outputFilepath, "UTF-8");
        int c = 0;
        for (String word : prose) {
            c++;
            if (c % 20 == 0) {
                writer.println();
            }
            writer.print(word + " ");
        }
        writer.close();

        System.out.println();

        return "I am smart";
    }
}
