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

public class DL {
    public static File dlFile(String url, String save){
        File file = new File(System.getProperty("user.dir") + "/" + save);
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
        if (file.exists()){
            if (FastMinecraftMirror.checks_map.containsKey(file)) return file;
            FastMinecraftMirror.checks_map.put(file,new String[]{sha,url,save});
            return file;
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
}
