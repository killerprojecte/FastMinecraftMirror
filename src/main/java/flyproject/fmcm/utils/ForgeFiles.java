package flyproject.fmcm.utils;

import java.util.ArrayList;
import java.util.List;

public class ForgeFiles {
    private final List<String[]> list = new ArrayList<>();
    public ForgeFiles(){
    }
    public void addFile(String suffix,String type,String md5){
        list.add(new String[]{suffix,type,md5});
    }
    public List<String[]> get(){
        return list;
    }
}
