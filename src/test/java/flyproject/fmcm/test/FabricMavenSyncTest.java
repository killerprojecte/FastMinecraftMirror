package flyproject.fmcm.test;

import flyproject.fmcm.utils.HttpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FabricMavenSyncTest {
    public static void main(String[] args) {
        String main_html = HttpUtils.doGet("https://maven.fabricmc.net/");
        Pattern pattern = Pattern.compile("\">.*</a>");
        Matcher matcher = pattern.matcher(main_html);
        while (matcher.find()){
            String data = matcher.group().replace("\">","").replace("</a>","");
            if (data.equals("../")) continue;
            System.out.println(data);
        }
    }
}
