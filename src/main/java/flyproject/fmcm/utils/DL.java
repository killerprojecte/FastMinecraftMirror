package flyproject.fmcm.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

public class DL {
    public static File dlFile(String url, String save){
        try (InputStream ins = new URL(url).openStream()) {
            File file = new File(System.getProperty("user.dir") + "/" + save);
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File dlFile(String url, String save, String sha){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (file.exists()){
            String hash = null;
            try {
                hash = Hash.getSha1(file);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (sha.equals(hash)) return file;
        }
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static File dlFile(String url, String save, long size){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (file.exists()){
            if (file.length()==size) return file;
        }
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
