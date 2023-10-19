package project.projectfiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

//contains the graph, table of data, and export button
public class GraphFactory extends JPanel {
	private static final long serialVersionUID = 1L;
	
	//graphing data
	private Object [][] dataArray;
	
	//converts data into a graphable format
	private XYDataset dataReader(String dataTitle, Object[][] data) {		
		XYSeries graphData = new XYSeries(dataTitle);
		
		//add data
		for(int i = 0; i < data.length; i++) {
		    double x = (Double) data[i][0];
		    double y = (Double) data[i][1];
		    graphData.add(x,y);
		}
		        
		//puts data in an array for use elsewhere
		List l = graphData.getItems();
		//intialize array
		dataArray = new Object[l.size()][2];
		for(int i = 0; i < l.size(); i++) {
			XYDataItem item = (XYDataItem) l.get(i);			
			//add to array
			double inputX = item.getXValue();
			BigDecimal roundInputX = new BigDecimal(inputX).setScale(2, RoundingMode.HALF_UP);
			dataArray [i][0] = roundInputX.doubleValue();
			//truncate if needed
			double inputY = item.getYValue();
			BigDecimal roundInputY = new BigDecimal(inputY).setScale(2, RoundingMode.HALF_UP);
			dataArray [i][1] = roundInputY.doubleValue();
		}
		
		//add it to the entire data set
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(graphData);
		
		return dataset;
	}
		
	public GraphFactory(String dataTitle, String graphTitle, String xLabel, String yLabel, String segmentedVolume, Object[][] data) {
		setLayout(new FlowLayout());
		
		//get data and make chart
		XYDataset dataset = dataReader(dataTitle, data);
		JFreeChart jfc = ChartFactory.createXYLineChart(graphTitle, xLabel, yLabel, 
			dataset, PlotOrientation.VERTICAL, true, false, false
		);
		//format chart
		ChartPanel cp = new ChartPanel(jfc);
		
        XYPlot plot = jfc.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        plot.setRenderer(renderer);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        //make table
        Object [] headers = {xLabel, yLabel};
        JScrollPane jsp = new TableFactory(dataArray, headers);
        
        final JPanel secondPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        //add export functionality
        final JButton exportButton = new JButton("Export to Data CSV");
        exportButton.setPreferredSize(new Dimension(270, 70));
        exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExportWriter ew = new ExportWriter(dataArray);
				ew.export(secondPanel);
			}
        });
        //add segmented freeway stats
        //HTML to wrap the text
        JLabel freewayVol = new JLabel("<html><body style='width: 490px'>" + segmentedVolume + "</html>");
        freewayVol.setPreferredSize(new Dimension(500, 70));
        freewayVol.setFont(new Font("sansserif", Font.BOLD, 13));
        
        secondPanel.add(freewayVol);
        secondPanel.add(exportButton);
        
        //header + current stats
        JPanel headerPanel = new JPanel();        
        JLabel currentCarVol = new JLabel("Current " + graphTitle + " is " + dataArray[dataArray.length - 1][1] + " ");
        currentCarVol.setFont(new Font("sansserif", Font.PLAIN, 20));
        headerPanel.add(currentCarVol);
        
        //add header
        headerPanel.setPreferredSize(new Dimension(780, 75));
        add(headerPanel);
        //add secondpanel
        secondPanel.setPreferredSize(new Dimension(780, 75));
        add(secondPanel);
        //add chart
        cp.setPreferredSize(new Dimension(500, 340));
        add(cp);        
        //add table
        add(jsp);
	}
	
	//Getter
	public Object[][] getDataArray() {
		return dataArray;
	}
}
