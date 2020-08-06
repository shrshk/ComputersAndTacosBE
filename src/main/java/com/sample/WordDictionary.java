package com.sample;

import java.util.HashMap;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isLeaf;

    TrieNode() {
        children = new HashMap<>();
        isLeaf = false;
    }

    void insert(String word) {
        TrieNode current = this;
        for (char c : word.toCharArray()) {
            if (!current.children.containsKey(c)) {
                current.children.put(c, new TrieNode());
            }
            current = current.children.get(c);
        }

        current.isLeaf = true;
    }
}

public class WordDictionary {

    TrieNode trieNode;

    /**
     * Initialize your data structure here.
     */
    public WordDictionary() {
        this.trieNode = new TrieNode();
    }

    /**
     * Adds a word into the data structure.
     */
    public void addWord(String word) {
        trieNode.insert(word);
    }

    /**
     * Returns if the word is in the data structure. A word could contain the
     * dot character '.' to represent any one letter.
     */
    public boolean search(String word) {
        return match(word.toCharArray(), 0, trieNode);
    }

    private boolean match(char[] chars, int idx, TrieNode node) {
        if (idx == chars.length)
            return node.isLeaf;
        if (chars[idx] != '.')
            return node.children.get(chars[idx]) != null && match(chars, idx + 1, node.children.get(chars[idx]));
        for (char childChar : node.children.keySet()) {
            if (node.children.get(childChar) != null) {
                if (match(chars, idx + 1, node.children.get(childChar)))
                    return true;
            }

        }
        return false;
    }

}