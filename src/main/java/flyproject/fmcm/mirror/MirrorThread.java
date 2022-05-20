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
            FastMinecraftMirror.logger.info("Checked in " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            try {
                Thread.sleep(1000*60*60*2L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
