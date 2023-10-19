package project.projectfiles;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HistoricalDataViewer extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private SQLFluxCapacitor SQLReader;
    
    private Map<String, Double[]> dataMap;
    //overall graph
    private Object [][] overallTrafficVol;
    private Object [][] overallAvgSpeed;
    //10 graph
    private Object [][] trafficVol_10;
    private Object [][] avgSpeed_10;
    //405 graph
    private Object [][] trafficVol_405;
    private Object [][] avgSpeed_405;
    //105 graph
    private Object [][] trafficVol_105;
    private Object [][] avgSpeed_105;
    //101 graph
    private Object [][] trafficVol_101;
    private Object [][] avgSpeed_101;
    
    //Timer: Updates every 3 minutes (miliseconds)
    private int delay = 180000;
    
    //format SQL Data
    private void formatOverallData() {
        try {
            //pull data and iterate through
            dataMap = SQLReader.retrieveData();            
            Iterator<?> it = (Iterator<?>) dataMap.entrySet().iterator();
            
            //initialize arrays
            //if database is empty, seed data arrays with a bunch of zeros
            if(dataMap.size() == 0) {
                overallTrafficVol = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
                overallAvgSpeed = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
            }
            else {
                overallTrafficVol = new Object[dataMap.size()][2];
                overallAvgSpeed = new Object[dataMap.size()][2];
            }
            
            int row = 0;
            for(Entry<String, Double[]> entry : dataMap.entrySet()) {
                //convert key from hh:mm to hours
                String time = entry.getKey();
                String [] hhmm = time.split(":");
                int hours = Integer.parseInt(hhmm[0]);
                int mins = Integer.parseInt(hhmm[1]);
                double result = ((double)hours * 60 + (double)mins) / 60;
                
                //store times
                overallTrafficVol[row][0] = result;
                overallAvgSpeed[row][0] = result;
                //store avg speed or number of cars
                overallTrafficVol[row][1] = entry.getValue()[0];
                overallAvgSpeed[row][1] = entry.getValue()[1];
                row++;
            }
        }
        catch (SQLException e) {            
            e.printStackTrace();
        }
    }

    private void format10Data() {
        try {
            dataMap = SQLReader.retrieveData("10");            
            Iterator<?> it = (Iterator<?>) dataMap.entrySet().iterator();
            
            if(dataMap.size() == 0) {
                trafficVol_10 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
                avgSpeed_10 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
            }
            else {
                trafficVol_10 = new Object[dataMap.size()][2];
                avgSpeed_10 = new Object[dataMap.size()][2];
            }
            int row = 0;
            for(Entry<String, Double[]> entry : dataMap.entrySet()) {
                String time = entry.getKey();
                String [] hhmm = time.split(":");
                int hours = Integer.parseInt(hhmm[0]);
                int mins = Integer.parseInt(hhmm[1]);
                double result = ((double)hours * 60 + (double)mins) / 60;
                
                trafficVol_10[row][0] = result;
                avgSpeed_10[row][0] = result;
                //store avg speed or number of cars
                trafficVol_10[row][1] = entry.getValue()[0];
                avgSpeed_10[row][1] = entry.getValue()[1];
                row++;
            }
        }
        catch (SQLException e) {            
            e.printStackTrace();
        }
    }
    
    private void format405Data() {
        try {
            dataMap = SQLReader.retrieveData("405");            
            Iterator<?> it = (Iterator<?>) dataMap.entrySet().iterator();
            
            if(dataMap.size() == 0) {
                trafficVol_405 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
                avgSpeed_405 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
            }
            else {
                trafficVol_405 = new Object[dataMap.size()][2];
                avgSpeed_405 = new Object[dataMap.size()][2];
            }
            int row = 0;
            for(Entry<String, Double[]> entry : dataMap.entrySet()) {
                String time = entry.getKey();
                String [] hhmm = time.split(":");
                int hours = Integer.parseInt(hhmm[0]);
                int mins = Integer.parseInt(hhmm[1]);
                double result = ((double)hours * 60 + (double)mins) / 60;
                
                trafficVol_405[row][0] = result;
                avgSpeed_405[row][0] = result;
                //store avg speed or number of cars
                trafficVol_405[row][1] = entry.getValue()[0];
                avgSpeed_405[row][1] = entry.getValue()[1];
                row++;
            }
        }
        catch (SQLException e) {            
            e.printStackTrace();
        }
    }
    
    private void format105Data() {
        try {
            dataMap = SQLReader.retrieveData("105");            
            Iterator<?> it = (Iterator<?>) dataMap.entrySet().iterator();
            
            if(dataMap.size() == 0) {
                trafficVol_105 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
                avgSpeed_105 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
            }
            else {
                trafficVol_105 = new Object[dataMap.size()][2];
                avgSpeed_105 = new Object[dataMap.size()][2];
            }
            int row = 0;
            for(Entry<String, Double[]> entry : dataMap.entrySet()) {
                String time = entry.getKey();
                String [] hhmm = time.split(":");
                int hours = Integer.parseInt(hhmm[0]);
                int mins = Integer.parseInt(hhmm[1]);
                double result = ((double)hours * 60 + (double)mins) / 60;
                
                trafficVol_105[row][0] = result;
                avgSpeed_105[row][0] = result;
                //store avg speed or number of cars
                trafficVol_105[row][1] = entry.getValue()[0];
                avgSpeed_105[row][1] = entry.getValue()[1];
                row++;
            }
        }
        catch (SQLException e) {            
            e.printStackTrace();
        }
    }
    
    private void format101Data() {
        try {
            dataMap = SQLReader.retrieveData("101");            
            Iterator<?> it = (Iterator<?>) dataMap.entrySet().iterator();
            
            if(dataMap.size() == 0) {
                trafficVol_101 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
                avgSpeed_101 = new Object[][] {
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                        { 0.0, 0.0, 0.0, 0.0, 0.0 },
                      };
            }
            else {
                trafficVol_101 = new Object[dataMap.size()][2];
                avgSpeed_101 = new Object[dataMap.size()][2];
            }
            int row = 0;
            for(Entry<String, Double[]> entry : dataMap.entrySet()) {
                String time = entry.getKey();
                String [] hhmm = time.split(":");
                int hours = Integer.parseInt(hhmm[0]);
                int mins = Integer.parseInt(hhmm[1]);
                double result = ((double)hours * 60 + (double)mins) / 60;
                
                trafficVol_101[row][0] = result;
                avgSpeed_101[row][0] = result;
                //store avg speed or number of cars
                trafficVol_101[row][1] = entry.getValue()[0];
                avgSpeed_101[row][1] = entry.getValue()[1];
                row++;
            }
        }
        catch (SQLException e) {            
            e.printStackTrace();
        }
    }
    
    private void panel() {
        SQLReader = new SQLFluxCapacitor();
        formatOverallData();
        format10Data();
        format405Data();
        format105Data();
        format101Data();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));
        
        //adds cards
        final JPanel cardManager = new JPanel();
        final CardLayout cl = new CardLayout();
        cardManager.setLayout(cl);
        
        //add segmented freeway volume data and empty string for other graphs
        String empty = " ";
        String segmentedVolume = "Vol. by Freeway: " +
                "(I10) " + trafficVol_10[trafficVol_10.length - 1][1] + " | " + 
                "(I405) " + trafficVol_405[trafficVol_405.length - 1][1] + " | " +
                "(I105) " + trafficVol_405[trafficVol_405.length - 1][1] + " | " +
                "(US101) " + trafficVol_405[trafficVol_405.length - 1][1] + " | ";
        
        //overall graphs
        cardManager.add(new GraphFactory("Average Number of Cars on the Road", "Traffic Volume (Overall)", "Time (in Hours)", "Avg. Number of Cars", segmentedVolume, overallTrafficVol), "graph1");
        cardManager.add(new GraphFactory("Average Speed of Cars on the Road", "Average Speed (Overall)", "Time (in Hours)", "Speed (in Miles Per Hour)", empty, overallAvgSpeed), "graph2");
        //10 graph
        cardManager.add(new GraphFactory("Average Speed of Cars on the Interstate 10", "Average Speed (Interstate 10)", "Time (in Hours)", "Speed (in Miles Per Hour)", empty, avgSpeed_10), "graph3");
        //405 graph
        cardManager.add(new GraphFactory("Average Speed of Cars on the Interstate 405", "Average Speed (Interstate 405)", "Time (in Hours)", "Speed (in Miles Per Hour)", empty, avgSpeed_405), "graph4");
        //105 graph
        cardManager.add(new GraphFactory("Average Speed of Cars on the Interstate 105", "Average Speed (Interstate 105)", "Time (in Hours)", "Speed (in Miles Per Hour)", empty, avgSpeed_105), "graph5");
        //101 graph
        cardManager.add(new GraphFactory("Average Speed of Cars on the US Route 101", "Average Speed (US Route 101)", "Time (in Hours)", "Speed (in Miles Per Hour)", empty, avgSpeed_101), "graph6");
        
        //Menu Bar handles switching between cards + exporting
        JMenuBar jmb = new JMenuBar();
        
        final JMenuItem graph1Button = new JMenuItem("Overall Traffic Vol.");
        final JMenuItem graph2Button = new JMenuItem("Overall Avg. Speed");
        final JMenuItem graph3Button = new JMenuItem("I10 Avg. Speed");
        final JMenuItem graph4Button = new JMenuItem("I405 Avg. Speed");
        final JMenuItem graph5Button = new JMenuItem("I105 Avg. Speed");
        final JMenuItem graph6Button = new JMenuItem("US101 Avg. Speed");
        
        graph1Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        graph2Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        graph3Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        graph4Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        graph5Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        graph6Button.setFont(new Font("sansserif", Font.PLAIN, 10));
        
        graph1Button.setBackground(Color.LIGHT_GRAY);
        graph1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph1");
                
                //change color
                graph1Button.setBackground(Color.LIGHT_GRAY);
                graph2Button.setBackground(Color.WHITE);
                graph3Button.setBackground(Color.WHITE);
                graph4Button.setBackground(Color.WHITE);
                graph5Button.setBackground(Color.WHITE);
                graph6Button.setBackground(Color.WHITE);
            }       
        });
            
        graph2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph2");
                
                //change color
                graph1Button.setBackground(Color.WHITE);
                graph2Button.setBackground(Color.LIGHT_GRAY);
                graph3Button.setBackground(Color.WHITE);
                graph4Button.setBackground(Color.WHITE);
                graph5Button.setBackground(Color.WHITE);
                graph6Button.setBackground(Color.WHITE);
            }       
        });
        
        graph3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph3");
                
                //change color
                graph1Button.setBackground(Color.WHITE);
                graph2Button.setBackground(Color.WHITE);
                graph3Button.setBackground(Color.LIGHT_GRAY);
                graph4Button.setBackground(Color.WHITE);
                graph5Button.setBackground(Color.WHITE);
                graph6Button.setBackground(Color.WHITE);
            }       
        });
        
        graph4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph4");
                
                //change color
                graph1Button.setBackground(Color.WHITE);
                graph2Button.setBackground(Color.WHITE);
                graph3Button.setBackground(Color.WHITE);
                graph4Button.setBackground(Color.LIGHT_GRAY);
                graph5Button.setBackground(Color.WHITE);
                graph6Button.setBackground(Color.WHITE);
            }       
        });
        
        graph5Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph5");
                
                //change color
                graph1Button.setBackground(Color.WHITE);
                graph2Button.setBackground(Color.WHITE);
                graph3Button.setBackground(Color.WHITE);
                graph4Button.setBackground(Color.WHITE);
                graph5Button.setBackground(Color.LIGHT_GRAY);
                graph6Button.setBackground(Color.WHITE);
            }       
        });
        
        graph6Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                cl.show(cardManager, "graph6");
                
                //change color
                graph1Button.setBackground(Color.WHITE);
                graph2Button.setBackground(Color.WHITE);
                graph3Button.setBackground(Color.WHITE);
                graph4Button.setBackground(Color.WHITE);
                graph5Button.setBackground(Color.WHITE);
                graph6Button.setBackground(Color.LIGHT_GRAY);
            }       
        });
        
        jmb.add(graph1Button);
        jmb.add(graph2Button);
        jmb.add(graph3Button);
        jmb.add(graph4Button);
        jmb.add(graph5Button);
        jmb.add(graph6Button);
        
        add(jmb, BorderLayout.SOUTH);
        add(cardManager, BorderLayout.CENTER);
    }
    
    public HistoricalDataViewer() {
        //call up UI
        panel();
        
        //Repaint Component on a timer
        new Timer(delay, this).start();
    }
    
    public void refreshData(Map avg_data, Map freeway_partitioned_data) {
       try {
           this.SQLReader.updateAggregateData(avg_data);
           this.SQLReader.updateFreewaySpecificData(freeway_partitioned_data);
       } 
       catch (SQLException e) {
         
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        //remove all components, update, then add them again
        removeAll();        
        panel();
        
        revalidate();
        repaint();
    }
}