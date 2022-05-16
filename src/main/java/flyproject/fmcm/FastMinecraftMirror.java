package flyproject.fmcm;

import flyproject.fmcm.mirror.MirrorThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FastMinecraftMirror {
    public static Logger logger = LogManager.getRootLogger();

    public static void main(String[] args) {
        logger.warn("This project is only mirror for minecraft resource server");
        logger.warn("Software is free but don't using in business");
        logger.warn("(c) Copyright 2022-* FastMinecraftMirror Studio & FlyProject Team.");
        new Thread(new MirrorThread()).start();
    }

    public static void saveResource(String name){
        URL resource = FastMinecraftMirror.class.getClassLoader().getResource(name);
        File file = new File(System.getProperty("user.dir") + "/" + name);
        if (file.exists()) return;
        if (resource==null) return;
        try {
            URLConnection connection = resource.openConnection();
            connection.setUseCaches(false);
            saveFile(connection.getInputStream(),file);
            logger.info("[FastMinecraftMirror] Unpacking file: " + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
