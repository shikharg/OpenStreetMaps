package project.projectfiles;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;



public class ApplicationMapViewer extends JPanel {
    private static final int REFRESH_RATE = 25;
    private List<CarUnit> cars;
    private JMapViewer newMap;
    public ApplicationMapViewer(int width, int height)
    {
        this.cars = Collections.synchronizedList(new ArrayList<CarUnit>()); 
        newMap = new JMapViewer();
        newMap.setScrollWrapEnabled(true);
        newMap.setFocusable(true);
        newMap.setPreferredSize(new Dimension(width,height));
        newMap.setDisplayPosition(new Coordinate(34.05, -118.25), 12);
        this.setSize(new Dimension(width, height));
        this.add(newMap);
        this.setVisible(true);
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable()
        {

            @Override
            public void run() {
                synchronized(cars){
                Iterator<CarUnit> it = cars.iterator();
                while(it.hasNext()){
                       CarUnit car = it.next();
                       car.setElapsedTime(car.getElapsedTime() + REFRESH_RATE);
                       car.run();
                }
                
                repaint();
                }
            }
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    }
    public void refreshData(ArrayList<CarModel> carModels)
    {
       synchronized(this.cars){
        this.cars.clear();
        int listCounter = 0;
        synchronized(carModels){
            Iterator<CarModel> it = carModels.iterator();
            while(it.hasNext()){
            Layer layer = new Layer("Car" + Integer.toString(listCounter));
            CarUnit car = new CarUnit(it.next(), layer, Integer.toString(listCounter++));
            this.cars.add(car);
            }

        }
        List<MapMarker> syncList = Collections.synchronizedList(new ArrayList<MapMarker>());
        synchronized(syncList){
        Iterator<CarUnit> it = this.cars.iterator();
        while(it.hasNext()){
           syncList.add(it.next().car);
        }
        this.newMap.setMapMarkerList(syncList);
        this.newMap.repaint();
        this.repaint();
        }
        }
    }
}
