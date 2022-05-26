package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;

import java.io.File;
import java.util.Map;

public class ForgeMirror implements Runnable{

    @Override
    public void run() {
        while (true){
            File forgefile = DL.dlFile_Replace("https://download.mcbbs.net/maven/net/minecraftforge/forge/json","forge/net/minecraftforge/forge/json");
            String forge = FTS.fts(forgefile);
            JsonObject fo = new JsonParser().parse(forge).getAsJsonObject();
            String upath = fo.get("webpath").getAsString().replace("http://files.minecraftforge.net/maven","https://maven.minecraftforge.net");
            JsonObject builds = fo.get("number").getAsJsonObject();
            for (Map.Entry<String, JsonElement> je : builds.entrySet()){
                JsonObject bo = builds.get(je.getKey()).getAsJsonObject();
                String mcversion = bo.get("mcversion").getAsString();
                String forgeversion = bo.get("version").getAsString();
                FastMinecraftMirror.logger.info("[Forge] Sync " + mcversion + "-" + forgeversion);
                JsonArray forgearray = bo.get("files").getAsJsonArray();
                String root = mcversion + "-" + forgeversion;
                if (!bo.get("branch").isJsonNull()){
                    if (!bo.get("branch").getAsString().equals("")){
                        root = root + "-" + bo.get("branch").getAsString();
                    }
                }
                String path = root + "/forge-" + mcversion + "-" + forgeversion + "-";
                if (!bo.get("branch").isJsonNull()){
                    if (!bo.get("branch").getAsString().equals("")){
                        path = path + "-" + bo.get("branch").getAsString() + "-";
                    }
                }
                for (int i = 0;i<forgearray.size();i++){
                    JsonArray fa = forgearray.get(i).getAsJsonArray();
                    if (fa.get(0).isJsonNull()) continue;
                    String type = fa.get(0).getAsString();
                    if (fa.get(1).isJsonNull()) continue;
                    String name = fa.get(1).getAsString();
                    if (fa.get(2).isJsonNull()) continue;
                    String md5 = fa.get(2).getAsString();
                    DL.dlFileMd5(upath + path + name + "." + type,"forge/net/minecraftforge/forge/" + path + name + "." + type,md5);
                }
            }
            FastMinecraftMirror.logger.info("[Forge] Checked updates!");
            try {
                Thread.sleep(1000L*60L*60L*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
