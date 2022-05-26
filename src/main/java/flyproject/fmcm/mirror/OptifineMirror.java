package flyproject.fmcm.mirror;

import com.google.gson.*;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;
import flyproject.fmcm.utils.HttpUtils;

import java.io.File;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OptifineMirror implements Runnable{
    @Override
    public void run() {
        while (true){
            JsonArray ja = new JsonArray();
            String html = HttpUtils.doGet("https://optifine.net/downloads");
            Pattern pattern = Pattern.compile("colMirror'><a href=\".*\">");
            Matcher matcher = pattern.matcher(html);
            while (matcher.find()){
                String url = matcher.group().replace("colMirror'><a href=\"","").replace("\">","");
                String filename = url.replace("http://optifine.net/adloadx?f=","");
                FastMinecraftMirror.logger.info("[Optifine] Sync Optifine File: " + filename);
                String[] arg = filename.replace(".jar","").split("_");
                String name = arg[0];
                if (!name.equals("OptiFine")){
                    name = name + "_" + arg[1];
                    String mcversion = arg[2];
                    String type = arg[3] + "_" + arg[4];
                    StringBuilder version = new StringBuilder();
                    for (int i = 5;i<arg.length;i++){
                        version.append(arg[i]);
                        if (i<(arg.length-1)){
                            version.append("_");
                        }
                    }
                    JsonObject jo = new JsonObject();
                    jo.addProperty("name",name);
                    jo.addProperty("mcversion",mcversion);
                    jo.addProperty("type",type);
                    jo.addProperty("version",version.toString());
                    jo.addProperty("file",filename);
                    jo.addProperty("url","https://optifine.fastmcmirror.org/" + filename);
                    ja.add(jo);
                } else {
                    String mcversion = arg[1];
                    String type = arg[2] + "_" + arg[3];
                    StringBuilder version = new StringBuilder();
                    for (int i = 4;i<arg.length;i++){
                        version.append(arg[i]);
                        if (i<(arg.length-1)){
                            version.append("_");
                        }
                    }
                    JsonObject jo = new JsonObject();
                    jo.addProperty("name",name);
                    jo.addProperty("mcversion",mcversion);
                    jo.addProperty("type",type);
                    jo.addProperty("version",version.toString());
                    jo.addProperty("file",filename);
                    jo.addProperty("url","https://optifine.fastmcmirror.org/" + filename);
                    ja.add(jo);
                }
                String adload = HttpUtils.doGet(url);
                Pattern adp = Pattern.compile("<a href=.*>Download</a>");
                Matcher adm = adp.matcher(adload);
                while (adm.find()){
                    String dl = adm.group().replace("<a href='","").replace("' onclick='onDownload()'>Download</a>","");
                    DL.dlFile(dl,"optifine/" + filename);
                }
            }
            String json = new Gson().toJson(ja);
            FTS.stf("optifine/versionList.json",json);
            try {
                Thread.sleep(1000L*60L*60L*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
