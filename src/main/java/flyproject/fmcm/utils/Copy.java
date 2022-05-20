package flyproject.fmcm.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Copy {
    public static void copy(String source, String target){
        File ta = new File(System.getProperty("user.dir") + "/" + target);
        ta.getParentFile().mkdirs();
        try {
            Files.copy(new File(System.getProperty("user.dir") + "/" + source).toPath(),ta.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
