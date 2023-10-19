package project.projectfiles;


import org.json.simple.JSONArray;

public class JSONArrayWrapper {
    public JSONArray map;
    public JSONArrayWrapper(JSONArray m)
    {
        this.map = m;
    }
    public JSONArrayWrapper()
    {
        
    }
    public void setJSONArray(JSONArray j)
    {
        this.map = j;
    }
    public JSONArray getJSONArray()
    {
        return this.map;
    }
}
