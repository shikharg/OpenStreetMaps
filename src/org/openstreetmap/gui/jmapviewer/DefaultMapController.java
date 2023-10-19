package org.openstreetmap.gui.jmapviewer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapRectangle;

import project.projectfiles.CarUnit;
import project.projectfiles.CarView;
import project.projectfiles.GameState;
import project.projectfiles.PlaneView;
import project.projectfiles.Route;
import project.projectfiles.RouteGenerator;
import project.projectfiles.SerpentFactory;


public class DefaultMapController extends JMapController implements MouseMotionListener,
MouseWheelListener, KeyListener, MouseListener {

    private static final int MOUSE_BUTTONS_MASK = MouseEvent.BUTTON3_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK
    | MouseEvent.BUTTON2_DOWN_MASK;

    private static final int MAC_MOUSE_BUTTON3_MASK = MouseEvent.CTRL_DOWN_MASK | MouseEvent.BUTTON1_DOWN_MASK;
    public DefaultMapController(JMapViewer map) {
        super(map);
        this.routeGenerator = new RouteGenerator(map);
    }
    public  Layer gameLayer = new Layer("Game");
    private Point lastDragPoint;

    private boolean isMoving = false;
    private boolean enableMapMarkers = false;
    private boolean movementEnabled = true;

    private int movementMouseButton = MouseEvent.BUTTON3;
    private int movementMouseButtonMask = MouseEvent.BUTTON3_DOWN_MASK;
    private long lastClick;
    private boolean wheelZoomEnabled = true;
    private boolean doubleClickZoomEnabled = true;
    private RouteGenerator routeGenerator;
    private Coordinate currentRoutePosition;
    private CarUnit car;
    SerpentFactory serpico;
    private boolean played=false;
    
    private GameState state;    
    

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (wheelZoomEnabled) {
           map.setZoom(map.getZoom() - e.getWheelRotation(), e.getPoint());
        }
    }

    public boolean isMovementEnabled() {
        return movementEnabled;
    }

    /**
     * Enables or disables that the map pane can be moved using the mouse.
     *
     * @param movementEnabled
     */
    public void setMovementEnabled(boolean movementEnabled) {
        this.movementEnabled = movementEnabled;
    }

    public int getMovementMouseButton() {
        return movementMouseButton;
    }

    /**
     * Sets the mouse button that is used for moving the map. Possible values
     * are:
     * <ul>
     * <li>{@link MouseEvent#BUTTON1} (left mouse button)</li>
     * <li>{@link MouseEvent#BUTTON2} (middle mouse button)</li>
     * <li>{@link MouseEvent#BUTTON3} (right mouse button)</li>
     * </ul>
     *
     * @param movementMouseButton
     */

    public boolean isWheelZoomEnabled() {
        return wheelZoomEnabled;
    }

    public void setWheelZoomEnabled(boolean wheelZoomEnabled) {
        this.wheelZoomEnabled = wheelZoomEnabled;
    }

    public boolean isDoubleClickZoomEnabled() {
        return doubleClickZoomEnabled;
    }

    public void setDoubleClickZoomEnabled(boolean doubleClickZoomEnabled) {
        this.doubleClickZoomEnabled = doubleClickZoomEnabled;
    }

    public void mouseEntered(MouseEvent e) {
       
    }

    public void mouseExited(MouseEvent e) {
    }


    /**
     * Replies true if we are currently running on OSX
     *
     * @return true if we are currently running on OSX
     */
    public static boolean isPlatformOsx() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase().startsWith("mac os x");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        int key = e.getKeyCode();
        double diffx = 0, diffy = 0;
        
        
        if(key == KeyEvent.VK_DOWN)
        { 
            if (!map.gameMode)
                diffy = 5;
            else if(map.gameMode){

                if (state.plane.direction!=3){

                    if (state.plane.direction==0){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }

                    else if  (state.plane.direction==1){
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }

                    else if  (state.plane.direction==2){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    state.plane.direction=3;
                }
               
                if (!state.plane.hitYWall){
                for (int i=0;i<state.plane.plane.xpoints.length;i++)
                    state.plane.plane.ypoints[i]= state.plane.plane.ypoints[i]+6;
                
                state.plane.yspeed+=1;
                }
            }
            
        }
        else if(key == KeyEvent.VK_UP)
        {
            
          
            
            if (!map.gameMode)
                diffy = -5;
            
            else if(map.gameMode){
                
                if (state.plane.direction !=1){
                    if (state.plane.direction==2){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==3){
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==0){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    state.plane.direction=1;
                }
                if (!state.plane.hitYWall){
                    for (int i=0;i<state.plane.plane.xpoints.length;i++)
                        state.plane.plane.ypoints[i]= state.plane.plane.ypoints[i]-6;

                    state.plane.yspeed-=1;
                }
            }

        }
        else if(key == KeyEvent.VK_LEFT)
        {
            
            if (!map.gameMode)
                diffx = -5;
            
            else if(map.gameMode){
                
                if (state.plane.direction !=0){
                    if (state.plane.direction==3){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==2){
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==1){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    state.plane.direction=0;
                }
                
                if (!state.plane.hitXWall){
            for (int i=0;i<state.plane.plane.xpoints.length;i++)
                state.plane.plane.xpoints[i]= state.plane.plane.xpoints[i]-6;
            
            state.plane.xspeed-=1;
                }
                
            }
            
            
        }
        else if(key == KeyEvent.VK_RIGHT)
        {
            
            
            
            
            if (!map.gameMode)
                diffx = 5;
            
            
            else if(map.gameMode){
                
                if (state.plane.direction !=2){
                    if (state.plane.direction==1){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==0){
                        state.plane.reflect(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    
                    else if  (state.plane.direction==3){
                        state.plane.transpose(state.plane.plane.xpoints,state.plane.plane.ypoints);
                    }
                    state.plane.direction=2;
                }
                
                if (!state.plane.hitXWall){
                for (int i=0;i<state.plane.plane.xpoints.length;i++)
                    state.plane.plane.xpoints[i]= state.plane.plane.xpoints[i]+6;
                
                state.plane.xspeed+=1;
                }
                
            }
            
           
        }
        
        
        map.repaint();
        map.moveMap((int)diffx, (int)diffy);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

     //   map.addMapMarker(new MapMarkerDot(alpha.getLat(), alpha.getLon()));
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
       //synchronized(this){
        // TODO Auto-generated method stub
       // System.out.println("Count of listeners: " + arg0.getSource().toString());
        NumberFormat nf = new DecimalFormat("#0.0");
        if(this.enableMapMarkers)
        {
            Coordinate alpha = map.getPosition(arg0.getPoint());
            MapMarker marker = new MapMarkerDot(alpha.getLat(), alpha.getLon());
            map.addMapMarker(marker);
            this.enableMapMarkers = false;
            Route route = routeGenerator.generateRoute(this.currentRoutePosition, alpha);
            map.addMapPolygon(route.route);
            map.repaint();
            String dist = nf.format(route.distance);
            String perf = nf.format(route.perfectTime);
            String reg = nf.format((double)(route.distance/(this.car.speed/3600)));
            JOptionPane.showMessageDialog(null, "The distance to selected destination is: " + dist+ " miles. \n The time to the destination at speed limits is: " + perf + " seconds. \n The time to destination at current car speed is: " + reg + " seconds.", "Route data", JOptionPane.DEFAULT_OPTION);
         
            map.removeMapPolygon(route.route);
            map.removeMapMarker(marker);
            map.repaint();
            // car.resume();
        }
        else {
        if(System.currentTimeMillis() < this.lastClick + 100) return;
        
        Coordinate alpha = map.getPosition(new Point(arg0.getX(), arg0.getY()));

        Coordinate serpentSighting = new Coordinate(33.96101677566642,-118.49166870117188);
        
        
        List<MapMarker> cars = Collections.synchronizedList(map.getMapMarkerList());   
        try{
        for(MapMarker carView: cars)
        {
            
            if(carView instanceof CarView)
            {
               if(((CarView) carView).contains(alpha)){
                  this.car = ((CarView)carView).getCar();
                  this.currentRoutePosition = car.model.getPosition();
                  Object[] options = { "Find route", "Play game" };
                  String speed = nf.format(car.speed);
                  int a = JOptionPane.showOptionDialog(null, "The current speed of this car is " + speed + " mph.", "Car Information",
                  JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                  null, options, options[0]);
                  if(a == 0)
                  {
                      this.enableMapMarkers = true;
                  }
                  else if (a==1){
                      if (!played){ 
                      Object[] options1 = { "Ok"};
                      final ImageIcon icon = new ImageIcon("AceMiller.jpg");
                      JOptionPane.showMessageDialog(null, "Is this Ace Pilot Miller? We've got a problem! Giant, winged, Communist sea serpents are coming in from the Pacific to destroy CS201! You've got to do something! Go chop them up in your propeller!",
                              "O", JOptionPane.INFORMATION_MESSAGE, icon);
                      
                      
                     
                     
                      played=true;
                      map.setDisplayPosition(serpentSighting,12);
                      map.setTileGridVisible(false);
                      JMapViewer.MAX_ZOOM=12;
                      JMapViewer.MIN_ZOOM=12;
                      map.gameMode=true;
                      serpico = new SerpentFactory(map, gameLayer);
                      state=null;
                      state = new GameState(map, serpico);
                     }
                     
                     else{
                         Object[] options2 = { "Ok, sorry"};
                         int b = JOptionPane.showOptionDialog(null, "You had your chance",
                         null, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                         null, options2, options2[0]);
                     }
                      
                      
                      
                      map.repaint();
                      
                      
                  }
            
               }
            
            }
            
            
            
            //try
        }
        }
        catch(ConcurrentModificationException e){
            
        }
            
        }
        lastClick = System.currentTimeMillis();
     //   }
    }
}
