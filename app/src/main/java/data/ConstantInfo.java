package data;

import java.util.ArrayList;
import java.util.List;

public class ConstantInfo {
    String type;
    public String name;
    List<Keyword> keywords;
    public String comments;

    public ConstantInfo() {
        keywords = new ArrayList<Keyword>();
    }

    @Override
    public String toString() {
        return "ConstantInfo{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", keywords=" + keywords +
                ", comments='" + comments + '\'' +
                '}';
    }
}
