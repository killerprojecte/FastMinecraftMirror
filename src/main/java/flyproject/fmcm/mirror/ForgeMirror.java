package flyproject.fmcm.mirror;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgeMirror implements Runnable{

    @Override
    public void run() {
        while (true){
            /*int num = 1;
            JsonObject jo = new JsonObject();
            JsonObject build = new JsonObject();
            String root = "https://files.minecraftforge.net/net/minecraftforge/forge/";
            String main_html = HttpUtils.doGet(root);
            Pattern mainpattern = Pattern.compile("<a href=\"index_.*\">");
            Matcher main_matcher = mainpattern.matcher(main_html);
            while (main_matcher.find()){
                Map<String, ForgeFiles> map = new HashMap<>();
                String index = main_matcher.group().replace("<a href=\"","").replace("\">","");
                String version = index.replace("index_","").replace(".html","");
                String index_html = HttpUtils.doGet(root + index);
                Pattern pattern = Pattern.compile("<a class=\"info-link\" data-toggle=\"popup\" href=\".*\" title=\"Direct Download\">");
                Matcher matcher = pattern.matcher(index_html);
                while (matcher.find()){
                    String dl = matcher.group().replace("<a class=\"info-link\" data-toggle=\"popup\" href=\"","").replace("\" title=\"Direct Download\">","");
                    File file = DL.dlFile(dl,dl.replace("https://maven.minecraftforge.net/","forge/"));
                    String[] arg = file.getName().split("-");
                    String forgeversion = arg[2];
                    String type = arg[3];
                    String filesuffix = file.getName().split(".")[file.getName().split(".").length];
                    if (!map.containsKey(forgeversion)){
                        ForgeFiles forgeFiles = new ForgeFiles();
                        forgeFiles.addFile(filesuffix,type, Hash.md5(file));
                        map.put(forgeversion,forgeFiles);
                    } else {
                        ForgeFiles forgeFiles = map.get(forgeversion);
                        forgeFiles.addFile(filesuffix,type, Hash.md5(file));
                        map.put(forgeversion,forgeFiles);
                    }
                }
                Pattern other_pattern = Pattern.compile("<a href=\".*.txt\">");
                Matcher other_matcher = other_pattern.matcher(index_html);
                while (other_matcher.find()){
                    String link = other_matcher.group().replace("<a href=\"","").replace("\">","");
                    File file = DL.dlFile(link,link.replace("https://maven.minecraftforge.net/","forge/"));
                    String[] arg = file.getName().split("-");
                    String forgeversion = arg[2];
                    String type = arg[3];
                    String filesuffix = file.getName().split(".")[file.getName().split(".").length];
                    if (!map.containsKey(forgeversion)){
                        ForgeFiles forgeFiles = new ForgeFiles();
                        forgeFiles.addFile(filesuffix,type, Hash.md5(file));
                        map.put(forgeversion,forgeFiles);
                    } else {
                        ForgeFiles forgeFiles = map.get(forgeversion);
                        forgeFiles.addFile(filesuffix,type, Hash.md5(file));
                        map.put(forgeversion,forgeFiles);
                    }
                }
                while (num<=map.size()){
                    build.addProperty();
                }
            }*/
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
                        path = path + bo.get("branch").getAsString() + "-";
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
