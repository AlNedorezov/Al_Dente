package com.innopolis.al_dente;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHelper {

    private static final String CHAR_SET = "utf-8";

    private static final String TEMPORARY_FILE_PREFIX = "`";

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

                for (int i = 0; i < lines.length; i++){

                    String str = lines[i];
                    if (i == lines.length - 1) {
                        
                        writer.write(str);
                    }
                    else{

                        writer.write(str + newLine);
                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void updateFile (String path, String content){

        deleteFile(path);
        createNewFile(path, content);
    }

    public void fillTemporaryFile (String header, String path, String content){

        path = path.replace(header, TEMPORARY_FILE_PREFIX + header); //update path to temporary file path
        deleteFile(path); //delete of exists
        createNewFile(path, content);
    }

    public void removeTemporaryFile (String header, String path){

        path = path.replace(header, TEMPORARY_FILE_PREFIX + header); //update path to temporary file path
        deleteFile(path); //delete of exists
    }

    public boolean deleteFile(String path){

        File file = new File(path);

        return file.delete();

    }

    public String getFileContent(String path){

        try {

            byte[] encoded = Files.readAllBytes(Paths.get(path));

            return new String(encoded, Charset.forName(CHAR_SET));

        }catch (Exception e){}

        return null;
    }
}
