package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.Copy;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;
import flyproject.fmcm.utils.Hash;

import java.io.File;

public class PurpurSync implements Runnable{
    @Override
    public void run() {
        File jfile = DL.dlFile("https://api.purpurmc.org/v2/purpur","purpur/v2/purpur");
        String json = FTS.fts(jfile);
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        JsonArray ja = jo.get("versions").getAsJsonArray();
        for (JsonElement v : ja){
            String ver = v.getAsString();
            FastMinecraftMirror.logger.info("[Server] Sync Purpur version: " + ver);
            File vf = DL.dlFile("https://api.purpurmc.org/v2/purpur/" + ver,"purpur/v2/purpur/" + ver);
            String vj = FTS.fts(vf);
            JsonObject vjo = new JsonParser().parse(vj).getAsJsonObject();
            JsonArray vja = vjo.get("builds").getAsJsonObject().get("all").getAsJsonArray();
            for (JsonElement ve : vja){
                String build = ve.getAsString();
                File bf = DL.dlFile("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build,"purpur/v2/purpur/" + ver + "/" + build);
                String bj = FTS.fts(bf);
                JsonObject bjo = new JsonParser().parse(bj).getAsJsonObject();
                String status = bjo.get("result").getAsString();
                if (!status.equalsIgnoreCase("SUCCESS")) continue;
                String md5 = bjo.get("md5").getAsString();
                File dl = DL.dlFileMd5("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/download",md5);
                Copy.copy(dl,"purpur/v2/purpur/" + ver + "/" + build + "/purpur-" + ver + "-" + build + ".jar");
            }
            {
                String build = "latest";
                File bf = DL.dlFile("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build,"purpur/v2/purpur/" + ver + "/" + build);
                String bj = FTS.fts(bf);
                JsonObject bjo = new JsonParser().parse(bj).getAsJsonObject();
                String status = bjo.get("result").getAsString();
                String latest = bjo.get("build").getAsString();
                if (!status.equalsIgnoreCase("SUCCESS")) continue;
                String md5 = bjo.get("md5").getAsString();
                File dl = DL.dlFileMd5("https://api.purpurmc.org/v2/purpur/" + ver + "/" + build + "/download","purpur/v2/purpur/" + ver + "/" + build + "/download",md5);
                Copy.copy(dl,"purpur/v2/purpur/" + ver + "/" + build + "/purpur-" + ver + "-" + latest + ".jar");
            }
        }
    }
}
