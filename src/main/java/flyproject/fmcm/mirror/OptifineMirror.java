package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;

import java.io.File;

public class OptifineMirror implements Runnable{
    @Override
    public void run() {
        while (true){
            File jfile = DL.getStringfile("https://download.mcbbs.net/optifine/versionList","optifine/versionList.json");
            String js = FTS.fts(jfile);
            JsonArray ja = new JsonParser().parse(js).getAsJsonArray();
            for (JsonElement je : ja){
                JsonObject jo = je.getAsJsonObject();
                String mcversion = jo.get("mcversion").getAsString();
                String type = jo.get("type").getAsString();
                String patch = jo.get("patch").getAsString();
                FastMinecraftMirror.logger.info("[Optifine] Sync " + mcversion + "_" + type + "_" + patch);
                DL.dlFile("https://download.mcbbs.net/maven/com/optifine/" + mcversion + "/OptiFine_" + mcversion + "_" + type + "_" + patch + ".jar","optifine/OptiFine_" + mcversion + "_" + type + "_" + patch + ".jar");
            }
            try {
                Thread.sleep(1000L*60L*60L*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
