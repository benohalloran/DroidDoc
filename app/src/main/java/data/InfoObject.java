package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Parent for file-bound info objects, namely interface and class info objects.
 * Handles all common parsing between the two.
 */
public abstract class InfoObject implements Comparable<InfoObject> {
    private String pkg;
    private String name;
    private final List<Keyword> modifiers;
    private final List<MethodInfo> methods;

    private String parent;
    private List<String> interfaces;

    private static String[] LoopParse = {FileInfoFactory.METHODS,
            FileInfoFactory.CONSTANTS, FileInfoFactory.FIELDS};

    public InfoObject(JSONObject reader) {
        initializeFields();
        modifiers = new ArrayList<>();
        methods = new UniqueList<MethodInfo>() {
            @Override
            public boolean add(MethodInfo object) {
                if (contains(object)) {
                    int index = 0;
                    for (; index < size() && !get(index).equals(object); index++){} ;
                    MethodInfo old = remove(index);
                    MethodInfo add =
                            old.comments.length() > object.comments.length() ? old : object;
                    return super.addBase(add);
                } else
                    return super.add(object);
            }
        };
        interfaces = new ArrayList<>();
        parse(reader);
        Collections.sort(methods); //Parsing is now done. Sort the methods in alphabetical order
    }

    private void parse(JSONObject input) {
        // parse the header lines, which are common to both Class and Interface
        parseJavaFile(input);
        for (String key : LoopParse) {
            try {
                JSONArray array = input.getJSONArray(key);
                int CIEL = array.length();
                for (int i = 0; i < CIEL; i++) {
                    String s = array.getString(i).trim();
                    parseLine(s);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Parsing for content lines. Type-specific
     */
    protected abstract void parseLine(String readLine);

    protected abstract void initializeFields();

    //Get the type represented by this class.
    // This is used so keywords like class and interface aren't listed as modifiers.
    protected abstract Keyword myType();

    private void parseJavaFile(JSONObject readLine) {
        try {
            name = readLine.getString(FileInfoFactory.CLASS);
            pkg = readLine.getString(FileInfoFactory.PACKAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected boolean addKeyword(List<Keyword> modifiers, String word) {
        try {
            Keyword key = Keyword.getEnum(word.toUpperCase());
            if (key != myType() || modifiers != this.modifiers)
                modifiers.add(key);
            return true;
        } catch (IllegalArgumentException e) {
            return word.equals("class");
        }
    }

    protected final void parseMethodLine(String line) {
        String oLine = line;
        if (!line.contains("(")) {
            return;
        }
        MethodInfo info = new MethodInfo();
        String[] words = line.split(" ");
        if (words[0].equals("From")) {

            assert (words[0].equals("From"));
            assert (words[1].equals("class") || words[1].equals("interface"));
            info.fromClass = words[2]; //
            line = (line.replace(words[0] + " " + words[1] + " " + words[2], "")).trim();
            words = line.split(" ");
        }
        int index;
        int low = 0;
        for (index = 0; index < words.length; index++) {
            low += words[index].length();
            if (Keyword.isKeyword(words[index]))
                info.keyWords.add(Keyword.getEnum(words[index]));
            else
                break;
        }
        String[] preArgs = line.substring(0, line.indexOf("(")).trim().split(" ");
        info.retType = preArgs[preArgs.length - 2];
        info.methodName = preArgs[preArgs.length - 1];

        String[] parens = oLine.substring(oLine.indexOf("("), oLine.indexOf(")")).split(" ");
        for (int i = 0; i < parens.length; i += 2) {
            info.args.add(parens[i].replace("(", ""));
        }
        int indStr;
        if ((indStr = line.indexOf(")")) != -1)
            info.comments = line.substring(indStr + 1).trim();
        else
            info.comments = "Unspecified";

        methods.add(info);
    }

    protected <T> boolean contains(T[] arr, T key) {
        for (T t : arr)
            if (t.equals(key))
                return true;
        return false;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return pkg + "." + name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Keyword> getModifiers() {
        return modifiers;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
        this.interfaces = interfaces;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public int compareTo(InfoObject infoObject) {
        return name.compareTo(infoObject.name);
    }
}
