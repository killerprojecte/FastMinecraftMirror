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
            DL.dlFile("https://resources.download.minecraft.net/" + hash.substring(0,2) + "/" + hash,"resources/" + hash.substring(0,2) + "/" + hash,size);
        }
        //Sync Cient & Server & Mappings
        JsonObject dlj = jo.get("downloads").getAsJsonObject();
        if (dlj.has("server")){
            String client_mapping = dlj.get("server").getAsJsonObject().get("url").getAsString();
            DL.dlFile(client_mapping,client_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("client_mappings").getAsJsonObject().get("sha1").getAsString());
        }
        if (dlj.has("client")){
            String client_mapping = dlj.get("client").getAsJsonObject().get("url").getAsString();
            DL.dlFile(client_mapping,client_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("client_mappings").getAsJsonObject().get("sha1").getAsString());
        }
        if (dlj.has("client_mappings")){
            String client_mapping = dlj.get("client_mappings").getAsJsonObject().get("url").getAsString();
            DL.dlFile(client_mapping,client_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("client_mappings").getAsJsonObject().get("sha1").getAsString());
        }
        if (dlj.has("server_mappings")){
            String server_mapping = dlj.get("server_mappings").getAsJsonObject().get("url").getAsString();
            DL.dlFile(server_mapping,server_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("server_mappings").getAsJsonObject().get("sha1").getAsString());
        }
        if (dlj.has("windows_server")){
            String server_mapping = dlj.get("windows_server").getAsJsonObject().get("url").getAsString();
            DL.dlFile(server_mapping,server_mapping.replaceFirst("https://launcher.mojang.com","launcher"),dlj.get("server_mappings").getAsJsonObject().get("sha1").getAsString());
        }
        //Lib download
        JsonArray lijo = jo.get("libraries").getAsJsonArray();
        for (int i = 0;i<lijo.size();i++){
            JsonObject djo = lijo.get(i).getAsJsonObject().get("downloads").getAsJsonObject();
            if (djo.has("artifact")){
                JsonObject ajo = djo.get("artifact").getAsJsonObject();
                String path = ajo.get("path").getAsString();
                String url = ajo.get("url").getAsString();
                String sha = ajo.get("sha1").getAsString();
                DL.dlFile(url,"libraries/" + path,sha);
            }
            if (djo.has("classifiers")){
                JsonObject cjo = djo.get("classifiers").getAsJsonObject();
                for (Map.Entry<String,JsonElement> map : cjo.entrySet()){
                    String path = cjo.get(map.getKey()).getAsJsonObject().get("path").getAsString();
                    String sha = cjo.get(map.getKey()).getAsJsonObject().get("sha1").getAsString();
                    String url = cjo.get(map.getKey()).getAsJsonObject().get("url").getAsString();
                    DL.dlFile(url,"libraries/" + path,sha);
                }
            }
        }
    }
}
