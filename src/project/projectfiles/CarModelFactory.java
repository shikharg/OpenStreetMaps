package project.projectfiles;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CarModelFactory {
    private JSONArray data;
    public CarModelFactory(JSONArray data)
    {
        this.data = data;
    }
    public ArrayList<CarModel> createModels()
    {
        ArrayList<CarModel> carModels = new ArrayList<CarModel>();
        for(int i = 0; i < this.data.size(); i++)
        {
           
            JSONObject objectData = (JSONObject)data.get(i);
            Object id = objectData.get("id");
            Number idString = null;
            if(id instanceof Number) idString = (Number)id;
            Object sp = objectData.get("speed");
            Double speed = null;
            if(sp instanceof Double) speed = (Double)sp;
            String directionString = null, onOffRampString = null, freewayString = null;
            Object dir = objectData.get("direction");
            Object onOff = objectData.get("on/off ramp");
            Object freeway = objectData.get("freeway");
            if(dir instanceof String) directionString = (String)dir;
            if(onOff instanceof String) onOffRampString = (String)onOff;
            if(freeway instanceof String)freewayString = (String)freeway;
            String onOffRampStr = onOffRampString.replaceAll("[^A-Za-z0-9]", "");
            
            CarModel temp = new CarModel(idString.toString(), speed, directionString, onOffRampStr, freewayString);
            carModels.add(temp);
           
        }
        return carModels;
    }
}
