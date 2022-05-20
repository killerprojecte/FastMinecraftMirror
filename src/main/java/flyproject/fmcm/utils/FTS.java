package flyproject.fmcm.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FTS {
    public static String fts(File file){
        String con = null;
        try {
            con = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return con;
    }
    public static void stf(String file,String text){
        File f = new File(System.getProperty("user.dir") + "/" + file);
        if (!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileUtils.writeStringToFile(f,text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
