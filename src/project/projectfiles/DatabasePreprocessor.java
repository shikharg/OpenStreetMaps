package project.projectfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabasePreprocessor {
    private List<CarModel> data;
    public DatabasePreprocessor(List<CarModel> a)
    {
        this.data = a;
    }
   public Map processDataForAggregateAverages(String key1, String key2)
    {
        int aggregateNumberOfCars = this.data.size();
        double averageSpeedOfCars = 0.0;

        for(CarModel a: this.data)
        {
            double speed = a.getSpeed();
            averageSpeedOfCars+=speed;
        }

        averageSpeedOfCars/=aggregateNumberOfCars;
        Map<String, Number> processedData = new HashMap<String, Number>();
        processedData.put(key1, aggregateNumberOfCars);
        processedData.put(key2, averageSpeedOfCars);
        return processedData;

    }
   /****
    * Returns a map with each freeway name as a key, containing an array of two numbers: average speed and total number of cars on that freeway
    * @param key1
    * @param key2
    * @return
    */
   public Map processDataForFreewayAverages()
   {
       Map<String, Double[]> newMap = new HashMap<String, Double[]>();
       Double _405avg[] = new Double[2];
       Double _101avg[] = new Double[2];
       Double _10avg[] = new Double[2];
       Double _105avg[] = new Double[2];
       zero(_405avg, _101avg, _10avg, _105avg);
       newMap.put("405", _405avg);
       newMap.put("101", _101avg);
       newMap.put("10", _10avg);
       newMap.put("105", _105avg);

       for(CarModel model: this.data)
       {
           if(newMap.containsKey(model.getFreeway().trim()))
           {
               Double runningTotal[] = newMap.get(model.getFreeway());
               runningTotal[0]++;
               runningTotal[1]+=model.getSpeed();
               newMap.put(model.getFreeway(), runningTotal);
           }
       }
       Double vals405[] = newMap.get("405");
       vals405[1]/=vals405[0];
       newMap.put("405", vals405);
       
       Double vals101[] = newMap.get("101");
       vals101[1]/=vals101[0];
       newMap.put("101", vals101);
       
       Double vals10[] = newMap.get("10");
       vals10[1]/=vals10[0];
       newMap.put("10", vals10);
       
       Double vals105[] = newMap.get("105");
       vals105[1]/=vals105[0];
       newMap.put("105", vals105);
       return newMap;
   }
   private void zero(Double[]...doubles)
   {
       for(int i = 0; i < doubles.length; i++)
       {
           for(int j = 0; j < doubles[i].length; j++)
           {
               doubles[i][j] = new Double(0);
           }
       }
   }
}
