package project.projectfiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class CarController {
    private CarUnit car;
    private static final int CONV_LONG_LAT_TO_MILE = 69;
    private static final double KM_TO_MILE = 0.621371;
    private CarGraph router;
    private List<Coordinate> coordinates;
    private int counter;
    private XMLReader exitParser;
    private int animationRateInMilliseconds;
    public CarController(CarUnit e)
    {
        this.car = e;
        this.router = new CarGraph();
        String freeway = this.car.model.getFreeway();
        this.coordinates = this.router.generateRoute(freeway+this.car.model.getDirection() + "Route"+".xml");
        this.car.car.coord = this.coordinates.get(this.counter);
        this.exitParser = new XMLReader(freeway + this.car.model.getDirection() + ".xml");
       
        this.setCarColor();
        this.findClosestExit();
    }
    
    public void actionOnClick()
    {
        /*final JDialog carDialogPane = new JDialog();
        carDialogPane.setSize(new Dimension(400, 400));
        carDialogPane.setTitle("Car Information");
        carDialogPane.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel dialogPanel = new JPanel();
        JLabel speed = new JLabel("Speed: " + this.car.speed + " mph ");
        JButton routing = new JButton("Find route");
        routing.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent me) {
                // TODO Auto-generated method stub
                returns.bool = true;
                carDialogPane.setVisible(false);
                return;
           
            }
        });
        dialogPanel.add(speed);
        dialogPanel.add(routing);
        carDialogPane.add(dialogPanel);
        carDialogPane.setLocationRelativeTo(null);
        carDialogPane.setVisible(true);*/
    }
    public void update()
    {
        if(proportionalTimeAlgorithm()){
        this.counter++;
        if(this.counter < this.coordinates.size())
        {
            int directedMotion = this.counter;
          
            this.car.model.setPosition(this.coordinates.get(directedMotion));
            
            this.car.render();
        }
        }
    }
    /****
     * Determines using the distance between coordinates how fast the car will be moving on the map
     */
    public boolean proportionalTimeAlgorithm()
    {
        if(this.counter < this.coordinates.size() - 1){
        double distance = findDistanceBetween(this.coordinates.get(this.counter), this.coordinates.get(this.counter+1));
        int timeInterval = (int)(distance/(this.car.model.getSpeed()/3600000));
       
        long elapsedTime = this.car.model.getElapsedTime();
        if(elapsedTime >= timeInterval)
        {
        this.car.model.setElapsedTime(elapsedTime-timeInterval);
        return true;
        }
        else
        {
            return false;
        }
        }
        else
        {
            return true;
        }
    }
    /****
     * implements the Haversine formula in Java to determine the distance (in miles) between two coordinates
     * @return double distance between points in miles
     */
    private double findDistanceBetween(Coordinate a, Coordinate b)
    {
        final int RADIUS = 6371; //Earth's radius in kilometers
        double DELTA_LATITUDE = Math.toRadians(a.getLat() - b.getLat());
        double DELTA_LONGITUDE = Math.toRadians(a.getLon() - b.getLon());
        double d = Math.sin(DELTA_LATITUDE / 2) * Math.sin(DELTA_LATITUDE / 2) + 
                Math.cos(Math.toRadians(a.getLat())) * Math.cos(Math.toRadians(b.getLat())) * 
                Math.sin(DELTA_LONGITUDE / 2) * Math.sin(DELTA_LONGITUDE/ 2);
        double c = 2 * Math.atan2(Math.sqrt(d), Math.sqrt(1-d));
        return c*RADIUS*KM_TO_MILE;
    }
    public void setCarColor()
    {
        double speed = this.car.model.getSpeed();
        if(speed <= 20.0)
        {
            this.car.car.setBackColor(Color.RED);
        }
        else if(speed > 20.0 && speed <= 50.0)
        {
            this.car.car.setBackColor(Color.YELLOW);
        }
        else if(speed > 50.0)
        {
            this.car.car.setBackColor(Color.GREEN);
        }
    }
    public void findClosestExit()
    {
            String exit = this.car.model.getOnOffRamp();
        Coordinate exitCoord = null;
       Map<String, ArrayList<String>> parsedData = this.exitParser.parseByTagsAssociative("exit", "stop", "lat", "lon");
       try{
        if(parsedData.containsKey(exit)){
        exitCoord = new Coordinate(Double.parseDouble(parsedData.get(exit).get(0)), Double.parseDouble(parsedData.get(exit).get(1)));
        int indexOfMinimum = -1;
        double minimum = Double.MAX_VALUE;

        for(int i = 0; i < this.coordinates.size(); i++)
        {
            if(findDistanceBetween(exitCoord, this.coordinates.get(i)) < minimum)
            {
                indexOfMinimum = i;
                minimum = findDistanceBetween(exitCoord, this.coordinates.get(i));
            }
        }
        this.counter = indexOfMinimum;
        this.car.model.setPosition(this.coordinates.get(indexOfMinimum));
        this.car.render();
        }
        }
       catch(Exception e)
       {
           e.printStackTrace();
       }
    }
}
