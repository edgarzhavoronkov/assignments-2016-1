package ru.spbau.mit;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by edgar
 * on 13.03.16.
 */

public class StringSetImpl implements StringSet, StreamSerializable {
    private StringSetNode root;

    public StringSetImpl() {
        root = new StringSetNode();
    }

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }
        StringSetNode currentNode = getLastNode(element, Action.ADD);
        currentNode.setSize(currentNode.getSize() + 1);
        currentNode.setFinal(true);
        return true;
    }

    @Override
    public boolean contains(String element) {
        StringSetNode currentNode = getLastNode(element, Action.DEFAULT);
        return currentNode != null && currentNode.isFinal();
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }
        StringSetNode currentNode = getLastNode(element, Action.REMOVE);
        currentNode.setFinal(false);
        currentNode.setSize(currentNode.getSize() - 1);
        return true;
    }

    @Override
    public int size() {
        return root.getSize();
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        StringSetNode currentNode = getLastNode(prefix, Action.DEFAULT);
        return currentNode.size;
    }


    @Override
    public void serialize(OutputStream out) {
        //TODO
    }

    @Override
    public void deserialize(InputStream in) {
        //TODO
    }

    private StringSetNode getLastNode(String element, Action action) {
        StringSetNode currentNode = root;
        int len = element.length();
        for (int i = 0; i < len && currentNode != null; ++i) {
            char currentSymbol = element.charAt(i);
            StringSetNode child = currentNode.getChild(currentSymbol);
            switch (action) {
                case ADD:
                    currentNode.setSize(currentNode.getSize() + 1);
                    if (child == null) {
                        child = new StringSetNode();
                        currentNode.setChild(currentSymbol, child);
                    }
                    break;
                case REMOVE:
                    currentNode.setSize(currentNode.getSize() - 1);
                    if (child.getSize() == 0) {
                        currentNode.setChild(currentSymbol, null);
                    }
                    break;
                case DEFAULT:
                    break;
            }
            currentNode = child;
        }
        return currentNode;
    }

    private enum Action {ADD, REMOVE, DEFAULT}

    private static class StringSetNode implements StreamSerializable {
        private final static int ALPHABET_SIZE = 'z' - 'a' + 1;

        private StringSetNode[] children = new StringSetNode[2 * ALPHABET_SIZE];
        private boolean isFinal;
        private int size;

        public StringSetNode() {
            isFinal = false;
            size = 0;
        }

        public StringSetNode getChild(char c) {
            if (Character.isLowerCase(c)) {
                return children[c - 'a'];
            }
            return children[ALPHABET_SIZE + Character.toLowerCase(c) - 'a'];
        }

        public void setChild(char c, StringSetNode val) {
            if (Character.isLowerCase(c)) {
                children[c - 'a'] = val;
                return;
            }
            children[ALPHABET_SIZE + Character.toLowerCase(c) - 'a'] = val;
        }

        public int getSize() {
            return size;
        }

        public boolean isFinal() {
            return isFinal;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setFinal(boolean aFinal) {
            isFinal = aFinal;
        }

        @Override
        public void serialize(OutputStream out) {
            //TODO
        }

        @Override
        public void deserialize(InputStream in) {
            //TODO
        }
    }
}
