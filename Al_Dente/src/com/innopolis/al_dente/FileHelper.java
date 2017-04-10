package com.innopolis.al_dente;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    private static final String CHAR_SET = "utf-8";

    private static FileHelper instance;

    private FileHelper(){}

    public static FileHelper getInstance(){

        if (instance == null){

            instance = new FileHelper();
        }

        return instance;
    }

    public void createNewFile(String path, String content){

        String lines[] = content.split("\\r?\\n");

        String newLine = System.getProperty("line.separator");

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), CHAR_SET))) {

                for (String str : lines){

                    writer.write(str + newLine);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public String getFileContent(String path){

        try {

            byte[] encoded = Files.readAllBytes(Paths.get(path));

            return new String(encoded, Charset.forName(CHAR_SET));

        }catch (Exception e){}

        return null;
    }
}
