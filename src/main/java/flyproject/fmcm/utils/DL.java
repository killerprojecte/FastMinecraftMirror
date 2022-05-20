package flyproject.fmcm.utils;

import flyproject.fmcm.FastMinecraftMirror;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class DL {
    public static File dlFile(String url, String save){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        url = url.replace("http://repo.maven.apache.org","https://repo.maven.apache.org");
        if (url==null){
            return file;
        }
        if (file.exists()) return file;
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            connection.setConnectTimeout(3*1000);
            connection.connect();
            InputStream ins = connection.getInputStream();
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
        }
        return null;
    }
    public static File dlFile(String url, String save, String sha){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        url = url.replace("http://repo.maven.apache.org","https://repo.maven.apache.org");
        if (url==null){
            return file;
        }
        if (file.exists()){
            String hash = null;
            try {
                hash = Hash.getSha1(file);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            if (sha.equals(hash)) return file;
        }
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            connection.setConnectTimeout(3*1000);
            connection.connect();
            InputStream ins = connection.getInputStream();
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
        }
        return null;
    }
    public static File dlFile(String url, String save, long size){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        url = url.replace("http://repo.maven.apache.org","https://repo.maven.apache.org");
        if (url==null){
            return file;
        }
        if (file.exists()){
            if (file.length()==size) return file;
        }
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            connection.setConnectTimeout(3*1000);
            connection.connect();
            InputStream ins = connection.getInputStream();
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
        }
        return null;
    }
    public static File dlFileMd5(String url, String save, String md5){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        url = url.replace("http://repo.maven.apache.org","https://repo.maven.apache.org");
        if (url==null){
            return file;
        }
        if (file.exists()){
            String hash = null;
            hash = Hash.md5(file);
            if (md5.equals(hash)) return file;
        }
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36");
            connection.setConnectTimeout(3*1000);
            connection.connect();
            InputStream ins = connection.getInputStream();
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
        }
        return null;
    }
    public static File getStringfile(String url,String save){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        FTS.stf(save,HttpUtils.doGet(url));
        return file;
    }
}
