package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;
import flyproject.fmcm.utils.HttpUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FabricMirror implements Runnable{
    @Override
    public void run() {
        while (true){
            DL.dlFile_Replace("https://meta.fabricmc.net/v1/versions/game","fabricmeta/v1/versions/game.json");
            DL.dlFile_Replace("https://meta.fabricmc.net/v1/versions/loader","fabricmeta/v1/versions/loader.json");
            DL.dlFile_Replace("https://meta.fabricmc.net/v2/versions/loader","fabricmeta/v2/versions/loader.json");
            File gamejson = DL.dlFile_Replace("https://meta.fabricmc.net/v2/versions/game","fabricmeta/v2/versions/game.json");
            String json = FTS.fts(gamejson);
            JsonArray ja = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement je : ja){
                JsonObject jo = je.getAsJsonObject();
                String version = jo.get("version").getAsString();
                File vf = DL.getStringfile("https://meta.fabricmc.net/v2/versions/loader/" + version,"fabricmeta/v2/versions/loader/" + version + ".json");
                String vj = FTS.fts(vf);
                JsonArray vja = new JsonParser().parse(vj).getAsJsonArray();
                for (JsonElement vje : vja){
                    JsonObject vjo = vje.getAsJsonObject();
                    JsonObject loader = vjo.get("loader").getAsJsonObject();
                    String name = loader.get("maven").getAsString();
                    String[] larg = name.split(":");
                    String ver = larg[2];
                    FastMinecraftMirror.logger.info("[Fabric] Sync version: " + version + "-" + ver);
                    DL.getStringfile("https://meta.fabricmc.net/v2/versions/loader/" + version + "/" + ver,"fabricmeta/v2/versions/loader/" + version + "/" + ver + ".json");
                }
                syncMaven("https://maven.fabricmc.net/");
            }

            try {
                Thread.sleep(1000L*60L*60L*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void syncMaven(String url){
        String main_html = HttpUtils.doGet(url);
        Pattern pattern = Pattern.compile("<a href=\".*\">");
        Matcher matcher = pattern.matcher(main_html);
        while (matcher.find()){
            String data = matcher.group().replace("<a href=\"","").replace("\">","");
            if (data.equals("../")) continue;
            if (data.endsWith("/")){
                syncMaven(url + data);
            } else {
                FastMinecraftMirror.logger.info("[Fabric-Maven] Sync Maven File: " + data);
                DL.dlFile(url + data,url.replace("https://maven.fabricmc.net/","fabric/") + data);
            }
        }
    }
}
