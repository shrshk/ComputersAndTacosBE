package com.search;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
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

}