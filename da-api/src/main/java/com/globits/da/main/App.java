package com.globits.da.main;

import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
@Setter
public class App {


    public static void main(String[] args) throws IOException {


        App app = new App();

        FileInputStream input = null;
        FileOutputStream output = null;

        try {
            File file = new File("input.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            input = new FileInputStream("input.txt");
            output = new FileOutputStream("output.txt");
            int c;
            while ((c = input.read()) != -1) {
                System.out.print((char) c);
                output.write(c);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }

    }


}
