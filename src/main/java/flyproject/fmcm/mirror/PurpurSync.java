package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.*;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class PurpurSync implements Runnable{
    @Override
    public void run() {
        String json = HttpUtils.doGet("https://api.purpurmc.org/v2/purpur");
        FTS.stf("purpur/v2/purpur.json",json);
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        JsonArray ja = jo.get("versions").getAsJsonArray();
        for (JsonElement v : ja){
            String ver = v.getAsString();
            FastMinecraftMirror.logger.info("[Server] Sync Purpur version: " + ver);
            String vj = HttpUtils.doGet("https://api.purpurmc.org/v2/purpur/" + ver);
            FTS.stf("purpur/v2/purpur/" + ver + ".json",vj);
            JsonObject vjo = new JsonParser().parse(vj).getAsJsonObject();
            JsonArray vja = vjo.get("builds").getAsJsonObject().get("all").getAsJsonArray();
            for (JsonElement ve : vja){
                String build = ve.getAsString();
                String bj = HttpUtils.doGet("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build);
                FTS.stf("purpur/v2/purpur/" + ver + "/" + build + ".json",bj);
                JsonObject bjo = new JsonParser().parse(bj).getAsJsonObject();
                String status = bjo.get("result").getAsString();
                if (!status.equalsIgnoreCase("SUCCESS")) continue;
                String md5 = bjo.get("md5").getAsString();
                HttpUtils.downLoadFromUrl("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/download",md5);
                Copy.copy("purpur/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/purpur-" + ver + "-" + build + ".jar");
            }
            {
                String build = "latest";
                String bj = HttpUtils.doGet("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build);
                FTS.stf("purpur/v2/purpur/" + ver + "/" + build + ".json",bj);
                JsonObject bjo = new JsonParser().parse(bj).getAsJsonObject();
                String status = bjo.get("result").getAsString();
                String latest = bjo.get("build").getAsString();
                if (!status.equalsIgnoreCase("SUCCESS")) continue;
                String md5 = bjo.get("md5").getAsString();
                HttpUtils.downLoadFromUrl("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/download",md5);
                Copy.copy("purpur/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/purpur-" + ver + "-" + latest + ".jar");
            }
        }
    }
}
