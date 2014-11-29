package data;

import java.util.ArrayList;
import java.util.List;

public class MethodInfo implements Comparable<MethodInfo> {
    public String methodName;
    public String fromClass = null;
    public List<Keyword> keyWords;
    public List<String> args;
    public String retType;
    public String comments = null;

    public MethodInfo() {
        keyWords = new ArrayList<Keyword>();
        args = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "methodName='" + methodName + '\'' +
                ", fromClass='" + fromClass + '\'' +
                ", keyWords=" + keyWords +
                ", args=" + args +
                ", retType='" + retType + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }

    @Override
    public int compareTo(MethodInfo methodInfo) {
        return methodName.compareTo(methodInfo.methodName);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MethodInfo) {
            MethodInfo other = (MethodInfo) o;

            return other.methodName.equals(methodName) &&
                    ((other.args.size() == args.size()) && other.args.containsAll(args));
        }
        return false;
    }
}
