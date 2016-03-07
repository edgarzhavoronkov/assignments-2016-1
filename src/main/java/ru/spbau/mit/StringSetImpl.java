package ru.spbau.mit;

/**
 * Created by edgar
 * on 16.02.16.
 */
public class StringSetImpl implements StringSet {
    private StringSetNode root;

    public StringSetImpl() {
        root = new StringSetNode();
    }

    @Override
    public boolean add(String element) {
        if (this.contains(element)) {
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
        if (!this.contains(element)) {
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

    private StringSetNode getLastNode(String element, Action action) {
        StringSetNode currentNode = root;
        int len = element.length();
        for (int i = 0; i < len && currentNode != null; ++i) {
            char currentSymbol = element.charAt(i);
            switch (action) {
                case ADD: {
                    currentNode.setSize(currentNode.getSize() + 1);
                    StringSetNode child = currentNode.getChild(currentSymbol);
                    if (child == null) {
                        child = new StringSetNode();
                        currentNode.setChild(currentSymbol, child);
                    }
                    currentNode = child;
                    break;
                }
                case REMOVE: {
                    currentNode.setSize(currentNode.getSize() - 1);
                    StringSetNode child = currentNode.getChild(currentSymbol);
                    if (child.getSize() == 0) {
                        currentNode.setChild(currentSymbol, null);
                    }
                    currentNode = child;
                    break;
                }
                case DEFAULT: {
                    currentNode = currentNode.getChild(currentSymbol);
                    break;
                }
            }
        }
        return currentNode;
    }

    private enum Action {ADD, REMOVE, DEFAULT}

    private static class StringSetNode {
        private final static int ALPHABET_SIZE = 'z' - 'a' + 1;

        private StringSetNode[] children = new StringSetNode[2 * ALPHABET_SIZE];
        private boolean isFinal;
        private int size;

        public StringSetNode() {
            this.isFinal = false;
            this.size = 0;
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
    }
}
