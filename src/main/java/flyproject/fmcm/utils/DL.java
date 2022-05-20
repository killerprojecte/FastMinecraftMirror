package flyproject.fmcm.utils;

import flyproject.fmcm.FastMinecraftMirror;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class DL {
    public static File dlFile(String url, String save){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (url==null){
            return file;
        }
        if (file.exists()) return file;
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
            dlFile(url,save);
        }
        return null;
    }
    public static File dlFile(String url, String save, String sha){
        File file = new File(System.getProperty("user.dir") + "/" + save);
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
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
            dlFile(url,save,sha);
        }
        return null;
    }
    public static File dlFile(String url, String save, long size){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (url==null){
            return file;
        }
        if (file.exists()){
            if (file.length()==size) return file;
        }
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
            dlFile(url,save,size);
        }
        return null;
    }
    public static File dlFileMd5(String url, String save, String md5){
        File file = new File(System.getProperty("user.dir") + "/" + save);
        if (url==null){
            return file;
        }
        if (file.exists()){
            String hash = null;
            hash = Hash.md5(file);
            if (md5.equals(hash)) return file;
        }
        try (InputStream ins = new URL(url).openStream()) {
            Path target = file.toPath();
            Files.createDirectories(target.getParent());
            Files.copy(ins, target, StandardCopyOption.REPLACE_EXISTING);
            FastMinecraftMirror.synctotal++;
            return file;
        } catch (IOException e) {
            FastMinecraftMirror.logger.error("[ERROR] Can't reslove file " + url + " Retrying");
            dlFile(url,save,md5);
        }
        return null;
    }
}
