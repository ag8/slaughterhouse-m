package org.andrew.slaughterhouse_m;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, List<List<WordFrequency>>> getWordChain(int limit, String filepath) throws IOException {
        Map<String, List<List<WordFrequency>>> wordChain = new HashMap<>();

        List<String> wordsInFile = getWordsInFile(filepath);
        List<String> wordsToMap = wordsInFile.stream().distinct().collect(Collectors.toList()); // No duplicates

//        System.out.println(wordsInFile);

        int c = 0;
        int t = wordsToMap.size();
        int o = -1;
        for (String word : wordsToMap) {
            c++;
            int n = c * 100 / t;
            if (c != o) {
                o = n;
                System.out.print("\rGetting word chains... " + o + "%.");
            }
            List<Integer> indicesOfWord = indices(word, wordsInFile);

            List<List<WordFrequency>> listOfWordFrequenciesForEachDistance = new LinkedList<>();

            for (int distance = 1; distance < limit + 1; distance++) {
                List<WordFrequency> wordFrequenciesForThisDistance = new LinkedList<>();

                for (int index : indicesOfWord) {
                    if (index + distance > wordsInFile.size() - 1) {
                        continue;
                    }

                    String nthWordAfterWord = wordsInFile.get(index + distance);

                    if (!containsWord(wordFrequenciesForThisDistance, nthWordAfterWord)) {
                        wordFrequenciesForThisDistance.add(new WordFrequency(nthWordAfterWord));
                    } else {
                        wordFrequenciesForThisDistance.get(indexOfWordInWordFrequencyList(wordFrequenciesForThisDistance, nthWordAfterWord)).increment();
                    }
                }

                listOfWordFrequenciesForEachDistance.add(wordFrequenciesForThisDistance);
            }

            wordChain.put(word, listOfWordFrequenciesForEachDistance);
        }

        System.out.println(" complete.");
        return wordChain;
    }

    public static List<String> getFirstWordsInFile(int number, String filepath) throws IOException {
        List<String> allWords = getWordsInFile(filepath);

        List<String> firstWords = new LinkedList<>();
        for (int i = 0; i < number; i++) {
            firstWords.add(allWords.get(i));
        }

        return firstWords;
    }

    public static List<String> getAllWordsInFile(String filepath) throws IOException {
        return getWordsInFile(filepath);
    }

    private static List<String> getWordsInFile(String filepath) throws IOException {
        List<String> wordsInFile = new LinkedList<>();

        List<String> linesInFile = FileUtils.readLines(new File(filepath));
        for (String line : linesInFile) {
            String[] words = line.split(" ");

            for (String word : words) {
                if (word.length() != 0) {
                    wordsInFile.add(word);
                }
            }
        }

        return wordsInFile;
    }

    private static int indexOfWordInWordFrequencyList(List<WordFrequency> wordFrequencyList, String word) {
        for (int i = 0; i < wordFrequencyList.size(); i++) {
            if (wordFrequencyList.get(i).getWord().equals(word)) {
                return i;
            }
        }

        return -1;
    }

    private static List<Integer> indices(String word, List<String> words) {
        List<Integer> indices = new LinkedList<>();

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equals(word)) {
                indices.add(i);
            }
        }

        return indices;
    }

    private static int indexOfWord(List<WordFrequency> wordFrequencyList, String word) {
        for (int i = 0; i < wordFrequencyList.size(); i++) {
            if (Objects.equals(wordFrequencyList.get(i).getWord(), word)) {
                return i;
            }
        }

        return -1;
    }

    private static boolean containsWord(List<WordFrequency> wordFrequencyList, String word) {
        for (WordFrequency wf : wordFrequencyList) {
            if (Objects.equals(wf.getWord(), word)) {
                return true;
            }
        }

        return false;
    }

    public static String randomWordFromList(List<String> list) {
        try {
            return list.get(ThreadLocalRandom.current().nextInt(0, list.size()));
        } catch (IllegalArgumentException e) {
            System.out.println(list);
            e.printStackTrace();
        }

        return "";
    }

    public static void pause(long t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
