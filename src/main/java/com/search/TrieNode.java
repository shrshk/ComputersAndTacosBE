package com.search;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrieNode {
	private final static Logger logger = Logger.getLogger(TrieNode.class.getName());
	private static final String DICTIONARY_URL = System.getenv("DICTIONARY_URL") != null
			? System.getenv("DICTIONARY_URL")
			: "https://raw.githubusercontent.com/first20hours/google-10000-english/master/google-10000-english.txt";
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static String trieNodeStr;

	private Map<Character, TrieNode> children;
	private boolean isLeaf;

	TrieNode(boolean fromString) {
		if (!fromString) {
			children = new HashMap<>();
			isLeaf = false;
			return;
		}

		if (trieNodeStr == null)
			trieNodeStr = buildTrieNodeStr();

		TrieNode trieNode = gson.fromJson(trieNodeStr, TrieNode.class);

		children = trieNode.children;
		isLeaf = trieNode.isLeaf;
	}

	private void insert(String word) {
		TrieNode current = this;
		for (char c : word.toCharArray()) {
			if (!current.children.containsKey(c)) {
				current.children.put(c, new TrieNode(false));
			}
			current = current.children.get(c);
		}

		current.isLeaf = true;
	}

	private String buildTrieNodeStr() {
		TrieNode trieNode = new TrieNode(false);
		String[] words = getWordDictionary();

		for (String word : words) {
			trieNode.insert(word);
		}

		return gson.toJson(trieNode);
	}

	private String[] getWordDictionary() {

		Request request = new Request.Builder().url(DICTIONARY_URL).build();

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

	public boolean startsWith(String key) {
		TrieNode current = this;
		for (int i = 0; i < key.length(); i++) {
			current = current.children.get(key.charAt(i));
			if (current == null)
				return false;
		}

		return true;
	}

	public boolean search(String key) {
		TrieNode current = this;
		for (int i = 0; i < key.length(); i++) {
			current = current.children.get(key.charAt(i));
			if (current == null)
				return false;
		}

		return current.isLeaf;
	}

	public Map<Character, TrieNode> getChildren() {
		return children;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

}