package flyproject.fmcm.mirror;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.FTS;

import java.io.File;
import java.util.Map;

public class SyncVersion {
    public static void sync(String json){
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        //AssetsIndex
        String aindex = jo.get("assetIndex").getAsJsonObject().get("url").getAsString();
        JsonObject aij = new JsonParser().parse(FTS.fts(DL.dlFile(aindex,aindex.replaceFirst("https://launchermeta.mojang.com","launchermeta")))).getAsJsonObject();
        JsonObject aioj = aij.get("objects").getAsJsonObject();
        for (Map.Entry<String, JsonElement> map : aioj.entrySet()){
            JsonObject dlj = aioj.get(map.getKey()).getAsJsonObject();
            String hash = dlj.get("hash").getAsString();
            File resource = DL.dlFile("https://resources.download.minecraft.net/" + hash.substring(0,2) + "/" + hash,"resources/" + hash.substring(0,2) + "/" + hash);
        }
        JsonObject dlj = jo.get("downloads").getAsJsonObject();
        String client = dlj.get("client").getAsJsonObject().get("url").getAsString();
        String client_mapping = dlj.get("client_mappings").getAsJsonObject().get("url").getAsString();
        String server = dlj.get("server").getAsJsonObject().get("url").getAsString();
        String server_mapping = dlj.get("server_mappings").getAsJsonObject().get("url").getAsString();
        DL.dlFile(client,client.replaceFirst("https://launcher.mojang.com","launcher"));
        DL.dlFile(client_mapping,client_mapping.replaceFirst("https://launcher.mojang.com","launcher"));
        DL.dlFile(server,server.replaceFirst("https://launcher.mojang.com","launcher"));
        DL.dlFile(server_mapping,server_mapping.replaceFirst("https://launcher.mojang.com","launcher"));
    }
}
