package com.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchAutoComplete {

	private final static Logger logger = Logger.getLogger(SearchAutoComplete.class.getName());
	private static final String DICTIONARY_URL = System.getenv("DICTIONARY_URL") != null
			? System.getenv("DICTIONARY_URL")
			: "https://raw.githubusercontent.com/first20hours/google-10000-english/master/google-10000-english.txt";


	public List<String> search(String word) {
		
		List<String> result = new ArrayList<>();
		TrieNode trieNode = buildTrieNode();

		if (!trieNode.startsWith(word) || trieNode.search(word)) {
			result.add(word);
			return result;
		}

		for (char c : word.toCharArray()) {
			trieNode = trieNode.children.get(c);
		}

		dfs(word, trieNode, result);

		return result;
	}

	private void dfs(String prefix, TrieNode node, List<String> result) {

		if (node == null || node.isLeaf) {
			result.add(prefix);
			return;
		}

		for (char c : node.children.keySet()) {
			dfs(prefix + c, node.children.get(c), result);
		}

	}

	private TrieNode buildTrieNode() {
		TrieNode trieNode = new TrieNode();
		String[] words = getWordDictionary(DICTIONARY_URL);

		for (String word : words) {
			if (word.length()>1)
				trieNode.insert(word);
		}
		
		return trieNode;
	}

	private String[] getWordDictionary(String dictionaryUrl) {

		Request request = new Request.Builder()
				.url(dictionaryUrl)
				.build();

		Response response;
		String[] words = null;
		try {
			response = new OkHttpClient().newCall(request).execute();
			String myString = Objects.requireNonNull(response.body()).string();

			words = myString.split(System.getProperty("line.separator"));

		} catch (Exception e) {
			logger.error("Error occured building Dictionary" + e.getMessage());
		}

		return words;

	}

	// only for testing
	public static void main(String[] args) {
		SearchAutoComplete searchAutoComplete = new SearchAutoComplete();
		Map<String, List<String>> response = new HashMap<>();

		List<String> inputWordList = Arrays.asList("s","f");

		for (String inputWord : inputWordList) {
			if (inputWord.isEmpty())
				continue;
			List<String> resultWords = searchAutoComplete.search(inputWord);
			response.put(inputWord, resultWords);
		}
		System.out.println(response.toString());
	}

}
