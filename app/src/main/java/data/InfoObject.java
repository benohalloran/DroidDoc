package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Parent for file-bound info objects, namely interface and class info objects.
 */
public abstract class InfoObject implements Comparable<InfoObject> {
    protected String pkg;
    protected String name;
    protected List<Keyword> modifiers;
    protected List<MethodInfo> methods;

    protected String parent;
    protected List<String> interfaces;

    private static String[] LoopParse = {FileInfoFactory.METHODS,
            FileInfoFactory.CONSTANTS, FileInfoFactory.FIELDS};

    private static final Keyword[] NON_MODIFIERS = {Keyword.CLASS,
            Keyword.INTERFACE};

    public InfoObject(JSONObject reader) {
        initializeFeilds();
        modifiers = new ArrayList<Keyword>();
        methods = new ArrayList<MethodInfo>();
        interfaces = new ArrayList<String>();
        parse(reader);
        Collections.sort(methods);
    }

    private void parse(JSONObject input) {
        // parse the header lines, which are common to both Class and Interface
        //TODO parse constructor lines
        parseJavaFile(input);
        parseClassHeader(input);
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
           /* while (input.ready()) {
                String l = input.readLine();
                //TODO for some reason 'l' is null a lot
                if (l != null)
                    parseLine(l.trim()); //These are only method and field lines, enum values
            }*/
    }

    /**
     * Parsing for content lines. Type-specific
     */
    protected abstract void parseLine(String readLine);

    protected abstract void initializeFeilds();

    //Get the type represented by this class. This is used so keywords like class and interface aren't listed as modifiers.
    protected abstract Keyword myType();

    private void parseJavaFile(JSONObject readLine) {
        try {
            name = readLine.getString(FileInfoFactory.CLASS);
            pkg = readLine.getString(FileInfoFactory.PACKAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
      /*  String[] tokens = readLine.split("\"");
        javaFile = tokens[1];*/
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

    // This might/probably have a bug when interfaces extend interfaces.
    private void parseClassHeader(JSONObject readLine) {
        return; //TODO impelement
       /* boolean seenExtends = false;
        boolean seenImplements = false;
        String[] words = readLine.replace("{", "").split(" ");
        for (String word : words) {
            if (!addKeyword(modifiers, word)) {
                if (className == null)
                    className = word;
                else if (word.equals("extends"))
                    seenExtends = true;
                else if (word.equals("implements"))
                    if (seenImplements)
                        throw new IllegalStateException(
                                "Found implements twice in file");
                    else
                        seenImplements = true;
                else {
                    if (seenExtends)
                        parent = word;
                    else
                        interfaces.add(word);
                }
            }
        }
        if (parent == null)
            parent = "java.lang.Object";
            */
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

    protected <T> String listToString(List<T> list) {
        return Arrays.toString(list.toArray());
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

    public void setModifiers(List<Keyword> modifiers) {
        this.modifiers = modifiers;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
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
