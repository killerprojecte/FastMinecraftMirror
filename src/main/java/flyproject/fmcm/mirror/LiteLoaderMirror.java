package flyproject.fmcm.mirror;

import flyproject.fmcm.FastMinecraftMirror;
import flyproject.fmcm.utils.DL;
import flyproject.fmcm.utils.HttpUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteLoaderMirror implements Runnable{
    @Override
    public void run() {
        while (true){
            DL.dlFile_Replace("http://dl.liteloader.com/versions/versions.json","liteloader/versions/versions.json");
            syncLiteLoader("https://repo.mumfrey.com/content/");
            try {
                Thread.sleep(1000L*60L*60L*24L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void syncLiteLoader(String url){
        String html = HttpUtils.doGet(url);
        Pattern pattern = Pattern.compile("<a href=\".*\">");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()){
            String link = matcher.group();
            if (link.equals("../")) continue;
            if (link.endsWith("/")){
                syncLiteLoader(link);
            } else {
                File file = DL.dlFile(url,url.replace("http://repo.mumfrey.com/","liteloader/").replace("https://repo.mumfrey.com/","liteloader/"));
                FastMinecraftMirror.logger.info("[LiteLoader] Sync file " + file.getName());
            }
        }
    }
}
