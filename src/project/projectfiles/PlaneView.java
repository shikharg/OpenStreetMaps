package project.projectfiles;

//License: GPL. For details, see Readme.txt file.

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapObjectImpl;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.Style;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import org.openstreetmap.gui.jmapviewer.interfaces.MapPolygon;

import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.io.File;
import java.io.IOException;

import org.openstreetmap.gui.jmapviewer.interfaces.MapRectangle;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker.STYLE;

public class PlaneView extends MapObjectImpl implements MapPolygon, MapMarker {

 Coordinate coord;
 double radius;
 STYLE markerStyle;
 public Polygon plane;
 public int direction;
 public int xMax;
 public int xMin;
 public int yMax;
 public int yMin;
 
public int xspeed=0;
 public int yspeed=0;
 
private int xCenter;
private int yCenter;

public boolean hitXWall;
public boolean hitYWall;

 public PlaneView(Layer layer, String name, Coordinate coord, double radius) {
     
     super(layer);
     
     int yPoly[] = { /*left wing*/300, 310, 360, 360, /*propellor*/ 370, 365, 365, 370, 375, 375, 370, /*propellor */  380, 380, /*right wing*/ 430, 440, 430, /*tail*/ 380, 370, /*tail propellor*/ 375,375, 370, 365, 365, 370,  /*bottom of left wing*/ 360, 310, 300};
     
     int xPoly[] = {              300, 290, 290, 260,               250, 225, 245, 250, 225, 245, 250,                 260, 290,                290, 300, 310,          310, 380,                    375,385, 380, 375, 385, 380,                          310, 310, 300};
     
     //the coordinates draw the polygon in a clockwise fashion, 300, 300 is the reference point here
     
     
     
     
     
     for (int x=0;x<xPoly.length;x++){
         xPoly[x]= (int) Math.ceil(xPoly[x]*(.50));
     }
     for (int y=0;y<yPoly.length;y++)
         yPoly[y]= (int) Math.ceil(yPoly[y]*(.50));
     
     for (int x=0;x<xPoly.length;x++){
         xPoly[x]= xPoly[x]+300;
     }
     for (int y=0;y<yPoly.length;y++)
         yPoly[y]= yPoly[y]+100;
     
     
     xMin=xPoly[5];
     xMax=xPoly[22];
     yMin=yPoly[0];
     yMax=yPoly[14];
     
     xCenter = xMin + (xMax-xMin)/2;
     yCenter= yMin + (yMax-yMin)/2;
     
     
     this.coord = new Coordinate(coord.getLat(),coord.getLon());
     this.direction=0;
     markerStyle= STYLE.VARIABLE;
     plane = new Polygon();
     plane.xpoints=xPoly;
     plane.ypoints=yPoly;
     
     setVisible(false);
     hitXWall=false;
     hitYWall=false;
     
 }




public Coordinate getCoordinate(){
     return coord;
 }
 public double getLat() {
     return coord.getLat();
 }

 public double getLon() {
     return coord.getLon();
 }

 public double getRadius() {
     return radius;
 }

 public STYLE getMarkerStyle() {
     return markerStyle;
 }
 

 
 public void faceUp(int[] xPoly, int[] yPoly){
     
     if (direction==0){
         transpose(xPoly, yPoly);
     }
     if (direction==3){
         reflect(xPoly, yPoly);
     }
     if (direction==2){
         transpose(xPoly, yPoly);
         reflect(xPoly, yPoly);
     }
     
     
     
     this.direction=1;
    
     
 }
 
 public void faceDown(int[] xPoly, int[] yPoly){
     
    
     if (this.direction==2){
         transpose(xPoly, yPoly);
     }
     else if (this.direction==1){
         reflect(xPoly, yPoly);
     }
     else if (this.direction==0){
         transpose(xPoly, yPoly);
         
         reflect(xPoly, yPoly);
     }
     
   
     this.direction=3;
    
     
 }
 
 public void faceLeft(int[] xPoly, int[] yPoly){
     
     if (direction==1){
         transpose(xPoly, yPoly);
     }
     if (direction==2){
         reflect(xPoly, yPoly);
     }
     if (direction==3){
         transpose(xPoly, yPoly);
         reflect(xPoly, yPoly);
     }
     
     this.direction=0;
    
     
 }
 
public void faceRight(int[] xPoly, int[] yPoly){
     
    if (direction==3){
        transpose(xPoly, yPoly);
    }
    if (direction==0){
        reflect(xPoly, yPoly);
    }
    if (direction==1){
        transpose(xPoly, yPoly);
        reflect(xPoly, yPoly);
    }
    
    
     
     
     
     this.direction=2;
    
     
 }

 public void paint(Graphics g, Point position, int radio) {
     int size_h = radio;
     int size = size_h * 2;

     if (g instanceof Graphics2D && getBackColor()!=null) {
         Graphics2D g2 = (Graphics2D) g;
         Composite oldComposite = g2.getComposite();
         g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
         g2.setPaint(getBackColor());
         g.fillOval(position.x - size_h, position.y - size_h, size, size);
         g2.setComposite(oldComposite);
     }
     ////PLANE PART *********************
     
     
     for (int i=0;i<plane.xpoints.length;i++)
         if (plane.xpoints[i] < 0 || plane.xpoints[i]>800)
             hitXWall=true;
     
     for (int i=0;i<plane.ypoints.length;i++)
         if (plane.ypoints[i] < 0 || plane.ypoints[i]>600)
             hitYWall=true;
     
     for (int i=0;i<plane.xpoints.length;i++){
         if (!hitXWall)
             plane.xpoints[i]= plane.xpoints[i]+xspeed;
         else{
             xspeed=-xspeed;
             plane.xpoints[i]= plane.xpoints[i]+xspeed;
             hitXWall=false;
         }
     }
     
     for (int i=0;i<plane.ypoints.length;i++){
         if (!hitYWall)
             plane.ypoints[i]= plane.ypoints[i]+yspeed;
         else{
             yspeed=-yspeed;
             plane.ypoints[i]= plane.ypoints[i]+yspeed;
             hitYWall=false;
         }
     }
     
     
     for (int i=0;i<plane.xpoints.length;i++)
         plane.ypoints[i]= plane.ypoints[i]+yspeed;
     
     
     
     
     g.setColor(new Color(112,49,101));
     
     g.fillPolygon(plane.xpoints, plane.ypoints, plane.xpoints.length);
     xMin=plane.xpoints[5];
     xMax=plane.xpoints[22];
     yMin=plane.ypoints[0];
     yMax=plane.ypoints[14];
     
     
     if(getLayer()==null||getLayer().isVisibleTexts()) 
         paintText(g, position);
 }

public int[] getMaxMin(){
    int[] maxMin = {plane.xpoints[5],
    plane.xpoints[22],
    plane.ypoints[0],
    yMax=plane.ypoints[14]};
    return maxMin;
}
 
 public void setLat(double lat) {
     if(coord==null) coord = new Coordinate(lat,0);
     else coord.setLat(lat);
 }
 
 public void setLon(double lon) {
     if(coord==null) coord = new Coordinate(0,lon);
     else coord.setLon(lon);
 }
 public boolean contains(Coordinate a)
 {
     return (a.getLat() <= this.getLat()+this.radius && a.getLat() >= this.getLat() - this.radius  && a.getLon() <= this.getLon() + this.radius && a.getLon() >= this.getLon() - this.radius);
 }




@Override
public List<? extends ICoordinate> getPoints() {
    // TODO Auto-generated method stub
    return null;
}




@Override
public void paint(Graphics g, List<Point> points) {
    // TODO Auto-generated method stub
    
}




@Override
public void paint(Graphics g, Polygon polygon) {
    // TODO Auto-generated method stub
    
}

public void transpose(int[] xPoly, int[]yPoly){
    
    xMin=xPoly[5];
    xMax=xPoly[22];
    yMin=yPoly[0];
    yMax=yPoly[14];
    
    xCenter = xMin + (xMax-xMin)/2;
    yCenter= yMin + (yMax-yMin)/2;
    
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]-xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]-yCenter;
        
    }
    
    int[] dummy;
    dummy=xPoly;
    xPoly=yPoly;
    yPoly=dummy;
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]+xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]+yCenter;
        
    }
    
    plane.xpoints=xPoly;
    plane.ypoints=yPoly;
}

public void reflect(int[] xPoly, int[]yPoly){
    
    xMin=xPoly[5];
    xMax=xPoly[22];
    yMin=yPoly[0];
    yMax=yPoly[14];
    
    xCenter = xMin + (xMax-xMin)/2;
    yCenter= yMin + (yMax-yMin)/2;
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]-xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]-yCenter;
        
    }
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= -xPoly[i];
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= -yPoly[i];
        
    }
    
    for (int i=0;i<xPoly.length;i++){
        xPoly[i]= xPoly[i]+xCenter;
        
    }
    for (int i=0;i<xPoly.length;i++){
        yPoly[i]= yPoly[i]+yCenter;
        
    }
    
    plane.xpoints=xPoly;
    plane.ypoints=yPoly;
}


}