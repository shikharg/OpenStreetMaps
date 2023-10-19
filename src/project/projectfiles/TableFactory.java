package project.projectfiles;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class TableFactory extends JScrollPane {
	private static final long serialVersionUID = 1L;
	
	public TableFactory(Object [][] dataArray, Object [] columnNames) {
		//Initialize TableModel
		DefaultTableModel dtm = new DefaultTableModel(dataArray, columnNames) {
			private static final long serialVersionUID = 1L;
			@Override //set all cells to non-editable
			public boolean isCellEditable(int row, int column) {
					return false;
			}
		};
		
		//Initialize Table
		JTable jt = new JTable(dtm);
		jt.setRowHeight(20);
		jt.setFocusable(false);
		jt.setRowSelectionAllowed(false);
		jt.getTableHeader().setReorderingAllowed(false);
		jt.setGridColor(Color.GRAY);
		jt.setShowGrid(true);
		//set column width
		TableColumnModel tcm = jt.getColumnModel();
		tcm.getColumn(0).setMaxWidth(100);
		tcm.getColumn(1).setMaxWidth(150);
		jt.setColumnModel(tcm);
		//set viewport
		jt.setPreferredScrollableViewportSize(new Dimension(250, 340));
		
		//setup Scrolling
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setViewportView(jt);
	}
}
