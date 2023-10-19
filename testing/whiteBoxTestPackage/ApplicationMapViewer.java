package project.projectfiles;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;


import org.openstreetmap.gui.jmapviewer.Coordinate;

import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;



public class ApplicationMapViewer extends JPanel {
    private static final int REFRESH_RATE = 25;
    private ArrayList<CarUnit> cars;
    private JMapViewer newMap;
    public ApplicationMapViewer(int width, int height)
    {
        this.cars = new ArrayList<CarUnit>();
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
                for(int i = 0; i < cars.size(); i++)
                {
                       cars.get(i).setElapsedTime(cars.get(i).getElapsedTime() + REFRESH_RATE);
                       cars.get(i).run();
                }}
                repaint();
            }
        }, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
    }
    public void refreshData(ArrayList<CarModel> carModels)
    {
        this.cars.clear();
        synchronized(this.cars){
        for(int i = 0; i < carModels.size(); i++)
        {
            Layer layer = new Layer("Car" + Integer.toString(i));
            CarUnit car = new CarUnit(carModels.get(i), layer, Integer.toString(i));
            System.out.println("Car number is: " + Integer.toString(i));
            this.cars.add(car);
        }
        newMap.removeAllMapMarkers();
        for(CarUnit car: this.cars)
        {
           newMap.addMapMarker(car.car);
        }
        this.newMap.repaint();
        this.repaint();
        }
    }
}
