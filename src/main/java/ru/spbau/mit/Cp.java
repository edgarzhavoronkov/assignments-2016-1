package ru.spbau.mit;

import java.io.*;

/**
 * Created by edgar
 * on 01.03.16.
 */

public class Cp {

    private static final int MAX_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: cp src dst");
        } else {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(
                        new FileInputStream(args[0])
                        , MAX_SIZE
                );
                BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(args[1])
                        , MAX_SIZE
                );


                while (inputStream.available() > 0) {
                    char buffer = (char) inputStream.read();
                    outputStream.write(buffer);
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
