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


                byte[] buffer = new byte[MAX_SIZE];
                while (inputStream.read(buffer) != -1) {
                    outputStream.write(buffer);
                    buffer = new byte[MAX_SIZE];
                }

                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
