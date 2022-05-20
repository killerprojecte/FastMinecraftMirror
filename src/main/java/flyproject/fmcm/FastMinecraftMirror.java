package flyproject.fmcm;

import flyproject.fmcm.mirror.*;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;
import flyproject.fmcm.utils.HttpUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class FastMinecraftMirror {
    public static Logger logger = LogManager.getRootLogger();
    public static long synctotal = 0L;
    private static Thread main;
    private static Thread test;
    private static Thread forge;
    private static Thread fabric;
    private static Thread optifine;

    public static void main(String[] args) {
        PrintStream info = new PrintStream(System.out) {
            public void println(boolean x) {
                logger.info(x);
            }
            public void println(char x) {
                logger.info(x);
            }
            public void println(char[] x) {
                logger.info(x == null ? null : new String(x));
            }
            public void println(double x) {
                logger.info(x);
            }
            public void println(float x) {
                logger.info(x);
            }
            public void println(int x) {
                logger.info(x);
            }
            public void println(long x) {
                logger.info(x);
            }
            public void println(Object x) {
                logger.info(x);
            }
            public void println(String x) {
                logger.info(x);
            }
            public void print(boolean x) {
                logger.info(x);
            }
            public void print(char x) {
                logger.info(x);
            }
            public void print(char[] x) {
                logger.info(x == null ? null : new String(x));
            }
            public void print(double x) {
                logger.info(x);
            }
            public void print(float x) {
                logger.info(x);
            }
            public void print(int x) {
                logger.info(x);
            }
            public void print(long x) {
                logger.info(x);
            }
            public void print(Object x) {
                logger.info(x);
            }
            public void print(String x) {
                logger.info(x);
            }
        };
        PrintStream warn = new PrintStream(System.out) {
            public void println(boolean x) {
                logger.warn(x);
            }
            public void println(char x) {
                logger.warn(x);
            }
            public void println(char[] x) {
                logger.warn(x == null ? null : new String(x));
            }
            public void println(double x) {
                logger.warn(x);
            }
            public void println(float x) {
                logger.warn(x);
            }
            public void println(int x) {
                logger.warn(x);
            }
            public void println(long x) {
                logger.warn(x);
            }
            public void println(Object x) {
                logger.warn(x);
            }
            public void println(String x) {
                logger.warn(x);
            }
            public void print(boolean x) {
                logger.warn(x);
            }
            public void print(char x) {
                logger.warn(x);
            }
            public void print(char[] x) {
                logger.warn(x == null ? null : new String(x));
            }
            public void print(double x) {
                logger.warn(x);
            }
            public void print(float x) {
                logger.warn(x);
            }
            public void print(int x) {
                logger.warn(x);
            }
            public void print(long x) {
                logger.warn(x);
            }
            public void print(Object x) {
                logger.warn(x);
            }
            public void print(String x) {
                logger.warn(x);
            }
        };
        System.setOut(info);
        System.setErr(warn);
        Runnable tr = () -> {
            try {
                System.out.println("Testing Software (Will print this text twice)...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        test = new Thread(tr);
        Thread testd = new Thread(() -> {
            while (true){
                if (!test.isAlive()){
                    test = new Thread(tr);
                    test.start();
                    return;
                }
            }
        });
        test.start();
        testd.start();
        try {
            Thread.sleep(1001);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.warn("This project is only mirror for minecraft resource server");
        logger.warn("Software is free but don't using in business");
        logger.warn("(c) Copyright 2022-* FastMinecraftMirror Studio & FlyProject Team.");
        logger.warn("If your are using JRE, Please change to JDK!");
        main = new Thread(new MirrorThread());
        main.start();
        forge = new Thread(new ForgeMirror());
        forge.start();
        fabric = new Thread(new FabricMirror());
        fabric.start();
        optifine = new Thread(new OptifineMirror());
        optifine.start();
        Thread d = new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!main.isAlive()){
                    logger.warn("Warning Mirror thread dead! Restarting");
                    main = new Thread(new MirrorThread());
                    main.start();
                }
                if (!forge.isAlive()){
                    logger.warn("Warning Forge Mirror thread dead! Restarting");
                    forge = new Thread(new ForgeMirror());
                    forge.start();
                }
                if (!fabric.isAlive()){
                    logger.warn("Warning Fabric Mirror thread dead! Restarting");
                    fabric = new Thread(new FabricMirror());
                    fabric.start();
                }
                if (!optifine.isAlive()){
                    logger.warn("Warning Optifine Mirror thread dead! Restarting");
                    optifine = new Thread(new OptifineMirror());
                    optifine.start();
                }
            }
        });
        d.start();
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("[Sync] Sync Mirror Total: " + synctotal);
            }
        }).start();
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
