package flyproject.fmcm.mirror;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SyncVersion {
    public static void sync(String json){
        JsonObject jo = new JsonParser().parse(json).getAsJsonObject();
        //AssetsIndex
        String aindex = jo.get("assetIndex").getAsJsonObject().get("url").getAsString();
    }
}
