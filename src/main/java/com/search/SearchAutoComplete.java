package com.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAutoComplete {

    // get a list of words with searchKey
    // ab

    public List<String> search(String word) {
        List<String> res = new ArrayList<>();
        TrieNode trieNode = new TrieNode(true);
        if (!trieNode.startsWith(word)) {
            res.add(word);
            return res;
        }

        for (char c : word.toCharArray()) {
            trieNode = trieNode.getChildren().get(c);
        }

        dfs(word, trieNode, res);

        return res;

    }

    private void dfs(String prefix, TrieNode trieNode, List<String> result) {
        if (trieNode == null)
            return;
        // aba aban ..... .
        if (trieNode.isLeaf()) {
            result.add(prefix);
        }
        for (char c : trieNode.getChildren().keySet()) {
            dfs(prefix + c, trieNode.getChildren().get(c), result);

        }

    }


    public static void main(String[] args) {

        // { a: [aban, abc....], b:[....]

        SearchAutoComplete searchAutoComplete = new SearchAutoComplete();

        Map<String, List<String>> response = new HashMap<>();
        List<String> inputWordList = Arrays.asList("abandon", "abacus", "abstract", "cat", "cats", "ca");

        for (String inputWord : inputWordList) {
            if (!inputWord.isEmpty())
                continue;
            List<String> resultWords = searchAutoComplete.search(inputWord);
            response.put(inputWord, resultWords);
        }

        System.out.println(response.toString());









    }
}
















































