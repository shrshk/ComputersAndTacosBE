package com.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchAutoComplete {

	public List<String> search(String word) {

		List<String> result = new ArrayList<>();
		TrieNode trieNode = new TrieNode(true);

		if (!trieNode.startsWith(word) || trieNode.search(word)) {
			result.add(word);
			return result;
		}

		for (char c : word.toCharArray()) {
			trieNode = trieNode.getChildren().get(c);
		}

		dfs(word, trieNode, result);

		return result;
	}

	private void dfs(String prefix, TrieNode node, List<String> result) {

		if (node == null || node.isLeaf()) {
			result.add(prefix);
			return;
		}

		for (char c : node.getChildren().keySet()) {
			dfs(prefix + c, node.getChildren().get(c), result);
		}

	}

	// only for testing
	public static void main(String[] args) {
		SearchAutoComplete searchAutoComplete = new SearchAutoComplete();
		Map<String, List<String>> response = new HashMap<>();

		List<String> inputWordList = Arrays.asList("s", "f");

		for (String inputWord : inputWordList) {
			if (inputWord.isEmpty())
				continue;
			List<String> resultWords = searchAutoComplete.search(inputWord);
			response.put(inputWord, resultWords);
		}
		System.out.println(response.toString());
	}

}
