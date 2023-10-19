package project.projectfiles;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class IntelligentTrafficApplication extends JFrame {
    public static final String JSON_URL = "http://www-scf.usc.edu/~csci201/mahdi_project/project_data.json";
    public static final Object errorButtonOptions[] = {"Refresh", "Exit"};
    public static final int REFRESH_TIME = 180;
    public static void main(String[] args)
    {
        final JSONArrayWrapper jsonArray = new JSONArrayWrapper();
        final ApplicationFrame mainApplication = new ApplicationFrame("Intelligent Traffic Application", 800, 670);
        final JSONReader jsonParser = new JSONReader(JSON_URL, jsonArray);
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
             try {
                jsonParser.run();
            } catch (IOException e) {
                int choice = JOptionPane.showOptionDialog(mainApplication,
                        "Unable to retrieve available car data. Refresh?",
                        "Data Retrieval Error",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,    
                        errorButtonOptions,
                        errorButtonOptions[0]);
                if(choice == 1){
                System.exit(1);
                }
                else
                {
                    run();
                }
            }
             jsonArray.setJSONArray(jsonParser.getJSONArray().map);
             mainApplication.refreshData(jsonArray.getJSONArray());
            }
        }, 0, REFRESH_TIME, TimeUnit.SECONDS);

        
     
    }
}
