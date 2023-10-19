package project.projectfiles;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Timer;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;

public class SerpentFactory {

    public ArrayList<SerpentView> serpents = new ArrayList<SerpentView>();
    JMapViewer map;
    public static Coordinate serpentSighting= new Coordinate(33.99261904283541,-118.42643737792969);
    int REFRESH_RATE=1000;
    
    public SerpentFactory(JMapViewer jdog, final Layer game){
        map=jdog;
        
        ScheduledExecutorService collision = Executors.newSingleThreadScheduledExecutor();
        collision.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run() {
                
                if (map.gameMode){
                
                synchronized(serpents){
                if (serpents.size()<5){
                    serpents.add(new SerpentView(game, "Serp",serpentSighting, .05));
                    map.addMapMarker(serpents.get(serpents.size()-1));
                    serpents.get(serpents.size()-1).setVisible(true);
                   
                }
                else{
                    for (int i=0;i<serpents.size();i++)
                        if (!serpents.get(i).isAlive){
                            serpents.get(i).setVisible(false);
                            serpents.remove(i);
                        }
                }
                
                
                }
                }
                else{
                    for (int i=0;i<serpents.size();i++)
                        if (!serpents.get(i).isAlive){
                            serpents.get(i).setVisible(false);
                            serpents.remove(i);
                        }
                }
            }
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    }
    

    


}
