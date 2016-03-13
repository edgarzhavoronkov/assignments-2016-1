package ru.spbau.mit;

import java.io.IOException;
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
        private static final int BUFFER_SIZE = Integer.BYTES + 1;

        private static final byte BYTE_TRUE = 0x01;
        private static final byte BYTE_FALSE = 0x00;

        private static final int BYTE_MODULO = 0xff;

        private int size;
        private boolean isFinal;
        private StringSetNode[] children = new StringSetNode[2 * ALPHABET_SIZE];

        StringSetNode() {
            size = 0;
            isFinal = false;
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
            try {
                byte[] bytesToWrite = new byte[BUFFER_SIZE];
                for (int i = 0; i < Integer.BYTES; ++i) {
                    bytesToWrite[i] = (byte) ((size >> (Byte.SIZE * (Integer.BYTES - i - 1))) & BYTE_MODULO);
                }

                if (isFinal) {
                    bytesToWrite[BUFFER_SIZE - 1] = BYTE_TRUE;
                } else {
                    bytesToWrite[BUFFER_SIZE - 1] = BYTE_FALSE;
                }

                out.write(bytesToWrite);
                for (StringSetNode node : children) {
                    if (node != null) {
                        out.write(new byte[]{BYTE_TRUE});
                        node.serialize(out);
                    } else {
                        out.write(new byte[]{BYTE_FALSE});
                    }
                }
            } catch (IOException e) {
                throw new SerializationException();
            }
        }

        @Override
        public void deserialize(InputStream in) {
            try {
                byte[] buffer = new byte[1];

                size = 0;
                for (int i = 0; i < BUFFER_SIZE - 1; ++i) {
                    in.read(buffer);
                    size |= ((BYTE_MODULO & buffer[0]) << Byte.SIZE * (Integer.BYTES - i - 1));
                }

                in.read(buffer);
                isFinal = buffer[0] == BYTE_TRUE;

                for (int i = 0; i < 2 * ALPHABET_SIZE; ++i) {
                    in.read(buffer);
                    if (buffer[0] == BYTE_TRUE) {
                        children[i] = new StringSetNode();
                        children[i].deserialize(in);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException();
            }
        }
    }
}
