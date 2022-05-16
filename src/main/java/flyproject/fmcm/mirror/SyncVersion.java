package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
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
            long size = dlj.get("size").getAsLong();
            File resource = DL.dlFile("https://resources.download.minecraft.net/" + hash.substring(0,2) + "/" + hash,"resources/" + hash.substring(0,2) + "/" + hash,size);
        }
        //Sync Cient & Server & Mappings
        JsonObject dlj = jo.get("downloads").getAsJsonObject();
        String client = dlj.get("client").getAsJsonObject().get("url").getAsString();
        String client_mapping = dlj.get("client_mappings").getAsJsonObject().get("url").getAsString();
        String server = dlj.get("server").getAsJsonObject().get("url").getAsString();
        String server_mapping = dlj.get("server_mappings").getAsJsonObject().get("url").getAsString();
        DL.dlFile(client,client.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("client").getAsJsonObject().get("sha1").getAsString());
        DL.dlFile(client_mapping,client_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("client_mappings").getAsJsonObject().get("sha1").getAsString());
        DL.dlFile(server,server.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("server").getAsJsonObject().get("sha1").getAsString());
        DL.dlFile(server_mapping,server_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("server_mappings").getAsJsonObject().get("sha1").getAsString());
        //Lib download
        JsonArray lijo = jo.get("libraries").getAsJsonArray();
        for (int i = 0;i<lijo.size();i++){
            JsonObject djo = lijo.get(i).getAsJsonObject().get("downloads").getAsJsonObject().get("artifact").getAsJsonObject();
            String path = djo.get("path").getAsString();
            String url = djo.get("url").getAsString();
            String sha = djo.get("sha1").getAsString();
            DL.dlFile(url,"libraries/" + path,sha);
        }
    }
}
