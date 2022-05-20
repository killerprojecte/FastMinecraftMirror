package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;

import java.io.File;

public class FabricMirror implements Runnable{
    @Override
    public void run() {
        while (true){
            DL.dlFile("https://meta.fabricmc.net/v1/versions/game","fabricmeta/v1/versions/game.json");
            DL.dlFile("https://meta.fabricmc.net/v1/versions/loader","fabricmeta/v1/versions/loader.json");
            DL.dlFile("https://meta.fabricmc.net/v2/versions/loader","fabricmeta/v2/versions/loader.json");
            File gamejson = DL.dlFile("https://meta.fabricmc.net/v2/versions/game","fabricmeta/v2/versions/game.json");
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
                    JsonObject in = vjo.get("intermediary").getAsJsonObject();
                    String name = loader.get("maven").getAsString();
                    String[] larg = name.split(":");
                    String pac = larg[0].replace(".","/");
                    String software = larg[1];
                    String ver = larg[2];
                    //Intermediary
                    String iname = in.get("maven").getAsString();
                    String[] ilarg = iname.split(":");
                    String ipac = ilarg[0].replace(".","/");
                    String isoftware = ilarg[1];
                    String iver = ilarg[2];
                    FastMinecraftMirror.logger.info("[Fabric] Sync version: " + version + "-" + ver);
                    DL.getStringfile("https://meta.fabricmc.net/v2/versions/loader/" + version + "/" + ver,"fabricmeta/v2/versions/loader/" + version + "/" + ver + ".json");
                    DL.dlFile("https://maven.fabricmc.net/" + pac + "/" + software + "/" + ver + "/" + software + "-" + ver + ".jar","fabric/" + pac + "/" + software + "/" + ver + "/" + software + "-" + ver + ".jar");
                    DL.dlFile("https://maven.fabricmc.net/" + ipac + "/" + isoftware + "/" + iver + "/" + isoftware + "-" + iver + ".jar","fabric/" + ipac + "/" + isoftware + "/" + iver + "/" + isoftware + "-" + iver + ".jar");
                    JsonObject liba = vjo.get("launcherMeta").getAsJsonObject().get("libraries").getAsJsonObject();
                    for (JsonElement libe : liba.get("client").getAsJsonArray()){
                        JsonObject libo = libe.getAsJsonObject();
                        String n = libo.get("name").getAsString();
                        if (!libo.has("url")) continue;
                        String u = libo.get("url").getAsString();
                        String[] la = n.split(":");
                        String ru = u + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar";
                        DL.dlFile(ru,"fabric/" + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar");
                    }
                    for (JsonElement libe : liba.get("common").getAsJsonArray()){
                        JsonObject libo = libe.getAsJsonObject();
                        String n = libo.get("name").getAsString();
                        if (!libo.has("url")) continue;
                        String u = libo.get("url").getAsString();
                        String[] la = n.split(":");
                        String ru = u + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar";
                        DL.dlFile(ru,"fabric/" + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar");
                    }
                    for (JsonElement libe : liba.get("server").getAsJsonArray()){
                        JsonObject libo = libe.getAsJsonObject();
                        String n = libo.get("name").getAsString();
                        if (!libo.has("url")) continue;
                        String u = libo.get("url").getAsString();
                        String[] la = n.split(":");
                        String ru = u + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar";
                        DL.dlFile(ru,"fabric/" + la[0].replace(".","/") + "/" + la[1] + "/" + la[2] + "/" + la[1] + "-" + la[2] + ".jar");
                    }
                }
            }
            try {
                Thread.sleep(1000L*60L*60L*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
