package data;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles all data requests and delegates parsing as needed.
 */
public class FileInfoFactory {
    private static Map<String, InfoObject> classes;
    private static Map<String, InfoObject> interfaces;
    private static Map<String, List<InfoObject>> packagesHashMap;
    private static List<String> packages;
    private static int count = 0;

    private static final String TAG = "FileInfoFactory";

    public static final String CONSTANTS = "constants";
    public static final String CLASS = "class";
    public static final String PACKAGE = "package";
    public static final String METHODS = "methods";
    public static final String FIELDS = "fields";
    public static final String CONSTRUCTORS = "constructors";
    public static final String SEARCH_KEY = "SEARCH PKG";


    static {
        classes = new ConcurrentHashMap<String, InfoObject>();
        interfaces = new ConcurrentHashMap<>();
        packagesHashMap = new ConcurrentHashMap<>();
        packages = new Vector<>();
    }


    public static InfoObject parseFile(JSONObject json) {
        InfoObject obj;
        try {
            Keyword fileType = getFileType(json);
            switch (fileType) {
                case CLASS:
                    obj = parseClass(json);
                    break;
                case INTERFACE:
                    obj = parseInterface(json);
                    break;
                default:
                    System.err.println("Unknown file type: " + fileType);
                    return null;
            }
            String pkg = obj.getPkg();
            if (!packagesHashMap.containsKey(pkg))
                packagesHashMap.put(pkg, new Vector<InfoObject>());
            List<InfoObject> pkgList = packagesHashMap.get(pkg);
            pkgList.add(obj);
            if (!packages.contains(pkg))
                packages.add(pkg);
            return obj;
        } catch (IllegalArgumentException e) {
            Log.wtf(TAG, "Enum error", e);
        }
        return null;
    }

    private static ClassInfo parseClass(JSONObject f) {
        ClassInfo info = new ClassInfo(f);
        putValue(classes, info);
        return info;
    }

    private static InterfaceInfo parseInterface(JSONObject f) {
        InterfaceInfo info = new InterfaceInfo(f);
        putValue(interfaces, info);
        return info;
    }

    private static Keyword getFileType(JSONObject jsonObject) {
        return Keyword.CLASS;
    }

    private static InfoObject checkForExisting(String f) {
        InfoObject prev = getValue(classes, f);
        if (prev != null)
            return prev;
        return getValue(interfaces, f);
    }

    private static boolean haveParsed(String f) {
        return checkForExisting(f) != null;
    }

    private static String getFileNamePath(File f) {
        return f.getAbsolutePath();
    }

    public static Map<String, InfoObject> getClasses() {
        return classes;
    }

    public static Map<String, InfoObject> getInterfaces() {
        return interfaces;
    }

    private static <V extends InfoObject> V getValue(Map<String, V> map,
                                                     String key) {
        return map.get(key);
    }


    private static <V extends InfoObject> V putValue(Map<String, V> map, V value) {
        return map.put(value.getFullName(), value);
    }

    /**
     * @param in the full name of the object, i.e. java.lang.Object
     */
    public static InfoObject get(String in) {
        InfoObject obj = interfaces.get(in);
        if (obj != null)
            return obj;
        else
            return classes.get(in);
    }

    public static InfoObject get(String pkg, int ind) {
        return getPackagesHashMap().get(pkg).get(ind);
    }

    public static Map<String, List<InfoObject>> getPackagesHashMap() {
        return packagesHashMap;
    }

    public static List<String> getPackages() {
        return packages;
    }

    public static String getPackage(int i) {
        return getPackages().get(i);
    }

    public static void sortPackages() {
        Collections.sort(packages);
    }

    public static List<? extends Map<String, InfoObject>> getSearchResults(String query) {
        List<Map<String, InfoObject>> list = new ArrayList<>();
        Set<Map.Entry<String, InfoObject>> set = classes.entrySet();
        set.addAll(interfaces.entrySet());

        Iterator<Map.Entry<String, InfoObject>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, InfoObject> entry = iterator.next();
            if (entry.getKey().toLowerCase().contains(query.toLowerCase())) {
                //add partial match
                HashMap<String, InfoObject> map = new HashMap<>();
                map.put(SEARCH_KEY, entry.getValue());
                list.add(map);
            }
        }
        return list;
    }

    public static int countPackages() {
        return packages.size();
    }

    public static int countClassesIn(int i) {
        return packagesHashMap.get(getPackage(i)).size();
    }

    public static List<InfoObject> get(int i) {
        return packagesHashMap.get(getPackage(i));
    }

}
