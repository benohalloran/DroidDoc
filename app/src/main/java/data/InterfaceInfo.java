package data;

import org.json.JSONObject;

public class InterfaceInfo extends InfoObject {

    public InterfaceInfo(JSONObject json) {
        super(json);
    }

    @Override
    protected void initializeFeilds() {
        // Interface no additional fields to specify, do nothing
    }

    @Override
    protected void parseLine(String readLine) {
        parseMethodLine(readLine);
    }

    @Override
    protected Keyword myType() {
        return Keyword.INTERFACE;
    }

}
