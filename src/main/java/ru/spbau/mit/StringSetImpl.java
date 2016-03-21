package ru.spbau.mit;

import java.io.*;

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
        root.serialize(out);
    }

    @Override
    public void deserialize(InputStream in) {
        root.deserialize(in);
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
                default:
                    break;
            }
            currentNode = child;
        }
        return currentNode;
    }

    private enum Action {ADD, REMOVE, DEFAULT}

    private static class StringSetNode implements StreamSerializable {
        private static final int ALPHABET_SIZE = 'z' - 'a' + 1;

        private int size;
        private boolean isFinal;
        private StringSetNode[] children = new StringSetNode[2 * ALPHABET_SIZE];

        StringSetNode() {
            size = 0;
            isFinal = false;
        }

        StringSetNode getChild(char c) {
            if (Character.isLowerCase(c)) {
                return children[c - 'a'];
            }
            return children[ALPHABET_SIZE + Character.toLowerCase(c) - 'a'];
        }

        void setChild(char c, StringSetNode val) {
            if (Character.isLowerCase(c)) {
                children[c - 'a'] = val;
                return;
            }
            children[ALPHABET_SIZE + Character.toLowerCase(c) - 'a'] = val;
        }

        int getSize() {
            return size;
        }

        boolean isFinal() {
            return isFinal;
        }

        void setSize(int size) {
            this.size = size;
        }

        void setFinal(boolean aFinal) {
            isFinal = aFinal;
        }

        @Override
        public void serialize(OutputStream out) {
            try {
                doSerialize(out);
            } catch (IOException e) {
                throw new SerializationException();
            }
        }

        @Override
        public void deserialize(InputStream in) {
            try {
                doDeserialize(in);
            } catch (IOException e) {
                throw new SerializationException();
            }
        }

        private void doSerialize(OutputStream out) throws IOException {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBoolean(isFinal);
            for (StringSetNode node : children) {
                if (node != null) {
                    dos.writeBoolean(true);
                    node.serialize(out);
                } else {
                    dos.writeBoolean(false);
                }
            }
        }

        private void doDeserialize(InputStream in) throws IOException {
            DataInputStream dis = new DataInputStream(in);
            size = 0;
            isFinal = dis.readBoolean();
            for (int i = 0; i < 2 * ALPHABET_SIZE; ++i) {
                boolean nodeExists = dis.readBoolean();
                if (nodeExists) {
                    children[i] = new StringSetNode();
                    children[i].deserialize(in);
                    if (children[i].isFinal) {
                        size++;
                    }
                }
            }
        }
    }
}
