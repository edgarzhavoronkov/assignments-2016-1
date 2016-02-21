package ru.spbau.mit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edgar
 * on 16.02.16.
 */
public class StringSetImpl implements StringSet {
    private static class StringSetNode {
        char symbol;
        String currentString;
        StringSetNode parent;
        Map<Character, StringSetNode> children;
        int prefixCounter;
        boolean isFinal;

        public StringSetNode(
                char symbol,
                String currentString,
                StringSetNode parent,
                Map<Character, StringSetNode> children,
                boolean isFinal) {
            this.symbol = symbol;
            this.currentString = currentString;
            this.parent = parent;
            this.children = children;
            this.isFinal = isFinal;
        }
    }

    private StringSetNode root;
    private int size;

    public StringSetImpl() {
        size = 0;
        this.root = new StringSetNode(
                ' ',
                "",
                null,
                new HashMap<Character, StringSetNode>(),
                false);
        root.prefixCounter = 0;
    }

    @Override
    public boolean add(String element) {
        if (this.contains(element)) {
            return false;
        } else {
            StringSetNode currentNode = root;
            int i = 0;
            int len = element.length();
            while (i < len) {
                char currentSymbol = element.charAt(i);
                StringSetNode node = currentNode.children.get(currentSymbol);
                if (node != null) {
                    currentNode = node;
                } else {
                    StringSetNode child = new StringSetNode(
                            currentSymbol,
                            element.substring(0, i + 1),
                            currentNode,
                            new HashMap<Character, StringSetNode>(),
                            false);
                    currentNode.children.put(currentSymbol, child);
                    currentNode = child;
                }
                i++;
            }
            currentNode.isFinal = true;
            size++;
            while (currentNode.parent != null) {
                currentNode.prefixCounter += 1;
                currentNode = currentNode.parent;
            }
            return true;
        }

    }

    @Override
    public boolean contains(String element) {
        StringSetNode currentNode = root;
        int i = 0;
        int len = element.length();
        while (i < len) {
            char currentSymbol = element.charAt(i);
            StringSetNode node = currentNode.children.get(currentSymbol);
            if (node != null) {
                currentNode = node;
                i++;
            } else {
                return false;
            }
        }
        return currentNode.isFinal;
    }

    @Override
    public boolean remove(String element) {
        if (this.contains(element)) {
            StringSetNode currentNode = getLastNode(element);
            currentNode.isFinal = false;
            while (currentNode.parent != null) {
                currentNode.prefixCounter -= 1;
                currentNode = currentNode.parent;
            }
            size--;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.equals("")) {
            return size + 1;
        }
        StringSetNode currentNode = getLastNode(prefix);
        return currentNode.prefixCounter;
    }

    private StringSetNode getLastNode(String element) {
        StringSetNode currentNode = root;
        int i = 0;
        int len = element.length();
        while (i < len) {
            char currentSymbol = element.charAt(i);
            StringSetNode node = currentNode.children.get(currentSymbol);
            if (node != null) {
                currentNode = node;
                i++;
            }
        }
        return currentNode;
    }
}
