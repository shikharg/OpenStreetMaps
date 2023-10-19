package project.projectfiles;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class ExportWriter {
	private Object [][] data;
	
	public ExportWriter(Object[][] data) {
		this.data = data;
	}
	
	public void export(JComponent parent) {
		try {
			//User Input
			String fileName = JOptionPane.showInputDialog(parent, "Please enter desired Filename.", 
				"Export to .CSV", JOptionPane.QUESTION_MESSAGE
			);
			fileName += ".csv";
			
			if(!fileName.equals("null.csv")) {
		         //write file
	            FileWriter fw = new FileWriter(fileName);
	            PrintWriter pw = new PrintWriter(fw);
	            
	            //===Output Format===//
	            // Column 1: data, data, data
	            // Column 2: data, data, data
	            //===================//
	            //loops over rows of col1
	            for(int i = 0; i < data.length; i++) {
	                pw.print(data[i][0] + ",");
	            }
	            pw.println();
	            //loops over rows of col2
	            for(int i = 0; i < data.length; i++) {
	                pw.print(data[i][1] + ",");
	            }
	            
	            pw.flush();
	            pw.close();
	            fw.close();
			}
		}
		catch(NullPointerException npe) {
			System.out.println("npe: " + npe.getMessage());
		}
		catch(IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
}
