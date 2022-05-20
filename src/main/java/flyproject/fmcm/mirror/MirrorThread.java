package flyproject.fmcm.mirror;

import com.google.gson.*;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;
import flyproject.fmcm.utils.Hash;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MirrorThread implements Runnable{
    @Override
    public void run() {
        FastMinecraftMirror.logger.info("Starting Mirror!");
        while (true){
            File java = DL.dlFile("https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json","launchermeta/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json");
            String javajson = FTS.fts(java);
            JsonObject javao = new JsonParser().parse(javajson).getAsJsonObject();
            File json = DL.dlFile("http://launchermeta.mojang.com/mc/game/version_manifest.json","launchermeta/mc/game/version_manifest.json");
            DL.dlFile("http://launchermeta.mojang.com/mc/game/version_manifest_v2.json","launchermeta/mc/game/version_manifest_v2.json");
            String jcon = null;
            try {
                jcon = FileUtils.readFileToString(json, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JsonObject jo = new JsonParser().parse(jcon).getAsJsonObject();
            for (int i = 0;i<jo.getAsJsonArray("versions").size();i++){
                JsonObject vo = jo.getAsJsonArray("versions").get(i).getAsJsonObject();
                String version = vo.get("id").getAsString();
                String vurl = vo.get("url").getAsString();
                File vjs = DL.dlFile(vurl,vurl.replaceFirst("https://launchermeta.mojang.com","launchermeta"));
                FastMinecraftMirror.logger.info("[Mojang] Sync Minecraft Version: " + version);
                String vcon = FTS.fts(vjs);
                SyncVersion.sync(vcon);
            }
            File forgefile = DL.dlFile("https://download.mcbbs.net/maven/net/minecraftforge/forge/json","forge/net/minecraftforge/forge/json");
            String forge = FTS.fts(forgefile);
            JsonObject fo = new JsonParser().parse(forge).getAsJsonObject();
            String upath = fo.get("webpath").getAsString();
            JsonObject builds = fo.get("number").getAsJsonObject();
            for (Map.Entry<String, JsonElement> je : builds.entrySet()){
                JsonObject bo = builds.get(je.getKey()).getAsJsonObject();
                String mcversion = bo.get("mcversion").getAsString();
                String forgeversion = bo.get("version").getAsString();
                FastMinecraftMirror.logger.info("[Forge] Sync " + mcversion + "-" + forgeversion);
                JsonArray forgearray = bo.get("files").getAsJsonArray();
                String root = mcversion + "-" + forgeversion;
                if (!bo.get("branch").isJsonNull()){
                    root = root + "-" + bo.get("branch").getAsString();
                }
                String path = root + "/forge-" + mcversion + "-" + forgeversion + "-";
                if (!bo.get("branch").isJsonNull()){
                    path = path + "-" + bo.get("branch").getAsString() + "-";
                }
                for (int i = 0;i<forgearray.size();i++){
                    JsonArray fa = forgearray.get(i).getAsJsonArray();
                    String type = fa.get(0).getAsString();
                    String name = fa.get(1).getAsString();
                    String md5 = fa.get(2).getAsString();
                    DL.dlFileMd5(upath + path + name + "." + type,"forge/" + path + name + "." + type,md5);
                }
            }
            FastMinecraftMirror.logger.info("Checked in " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                Thread.sleep(1000*60*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
