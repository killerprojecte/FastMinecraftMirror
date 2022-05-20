package flyproject.fmcm.utils;

import flyproject.fmcm.FastMinecraftMirror;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils {
    public static String doGet(String URL){
        HttpURLConnection conn = null;
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        try{
            URL url = new URL(URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(60000);
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
            if (200 == conn.getResponseCode()){
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = br.readLine()) != null){
                    result.append(line);
                }
            }else{
                System.out.println("ResponseCode is an error code:" + conn.getResponseCode());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(br != null){
                    br.close();
                }
                if(is != null){
                    is.close();
                }
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
            conn.disconnect();
        }
        return result.toString();
    }
    public static void downLoadFromUrl(String urllink,String save){
        try {
            File file = new File(save);
            if (file.exists()) return;
            URL url = new URL(urllink);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(3*1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            if (conn.getResponseCode()!=200){
                FastMinecraftMirror.logger.error("Response: " + conn.getResponseCode());
            }
            InputStream inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);
            File saveDir = new File(save);
            if(!saveDir.getParentFile().exists()){
                saveDir.getParentFile().mkdir();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downLoadFromUrl(String urllink,String save,String md5){
        try {
            File file = new File(save);
            if (file.exists()){
                if (Hash.md5(file).equalsIgnoreCase(md5)) return;
                file.delete();
            }
            URL url = new URL(urllink);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(3*1000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            if (conn.getResponseCode()!=200){
                FastMinecraftMirror.logger.error("Response: " + conn.getResponseCode());
            }
            InputStream inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);
            File saveDir = new File(save);
            if(!saveDir.getParentFile().exists()){
                saveDir.getParentFile().mkdir();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if(fos!=null){
                fos.close();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
