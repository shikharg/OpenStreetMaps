package project.projectfiles;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;

public class GameState {
    public static Coordinate serpentSighting0= new Coordinate(33.99261904283541,-118.42643737792969);
    public static Coordinate serpentSighting2 = new Coordinate(33.955606201249196,-118.58333587646484);
    
    public  Layer gameLayer = new Layer("Game");
    public  PlaneView plane=new PlaneView(gameLayer,"Plane",serpentSighting0,.05);
    
    public  ArrayList<DefendView> defence = new ArrayList<DefendView>();
    private boolean gameEnded;
    
    private JMapViewer map;
    private SerpentFactory serp;
    private int REFRESH_RATE=100;
    
    
    public GameState(JMapViewer jmap, SerpentFactory serpico){
        map=jmap;
        
        
        serp=serpico;
        
        this.defence=defence;
        for (int i=0;i<5;i++){
           this.defence.add(new DefendView(gameLayer, "defence",serpentSighting0,.2*i));
        }
        for (int i=0;i<5;i++)
            this.defence.get(i).setVisible(true);
       
        
        plane.setVisible(true);
        map.addMapMarker(plane);
        
        for (int i=0;i<5;i++)
            map.addMapMarker(this.defence.get(i));
        
        ScheduledExecutorService collision = Executors.newSingleThreadScheduledExecutor();
        collision.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run() {
                
                if (map.gameMode){ 
                    
                    try{
                    synchronized(serp.serpents){

                        for(int i = 0; i < serp.serpents.size(); i++)
                        {
                            
                            if (serp.serpents.get(i).isAlive){
                                for (int j=0;j<serp.serpents.get(i).snake.xpoints.length;j++){
                                       
                                   
                                    int[]maxMin = plane.getMaxMin();
                                    int[] planeX= {maxMin[0], maxMin[1], maxMin[1], maxMin[0]};
                                    int[] planeY = {maxMin[2], maxMin[2],maxMin[3],maxMin[3]};
                                    Polygon planeBox=new Polygon(planeX,planeY,4);
                                   
                                    if (planeBox.contains(serp.serpents.get(i).snake.xpoints[j], serp.serpents.get(i).snake.ypoints[j])){    
                                        
                                        serp.serpents.get(i).isAlive=false;
                                        serp.serpents.get(i).setVisible(false);
                                        serp.serpents.remove(i);

                                    }
                                    gameEnded=true;
                                    for (int h=0;h<5;h++){
                                        if (defence.get(h).isAlive){
                                            gameEnded=false;
                                        if (defence.get(h).hitBox.contains(serp.serpents.get(i).snake.xpoints[j], serp.serpents.get(i).snake.ypoints[j])){
                                            
                                                
                                                defence.get(h).health-=1;
                                                
                                                serp.serpents.get(i).isAlive=false;
                                                serp.serpents.get(i).setVisible(false);
                                                serp.serpents.remove(i);
                                            }
                                            
                                        }
                                    }
                                    
                                    if (gameEnded){
                                        clearScreen();
                                    }
                                    
                                }
                            }
                        }
                    }
                    }
                    catch(Exception e){
                        
                    }
                    //catch
                    
                    map.repaint();
                }
                
            }

            
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    
    
    }
    
    public void clearScreen(){
        map.gameMode=false;
        plane.setVisible(false);
        
        for (int i=0;i<serp.serpents.size();i++){
            serp.serpents.get(i).isAlive=false;
            serp.serpents.get(i).setVisible(false);
            serp.serpents.remove(i);
        }
        serp.serpents.clear();
        
        JMapViewer.MAX_ZOOM=22;
        JMapViewer.MIN_ZOOM=0;
    }
}
