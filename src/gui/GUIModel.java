package gui;

import javax.swing.table.DefaultTableModel;

public class GUIModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public int getRowByKey(Object object) {
		for (int i = 0; i < getRowCount(); i++) {
			if (getValueAt(i, 0).toString().equals(object.toString()))
				return i;
		}
		return -1;
	}

	public String getLastRowKey() {
		if (getRowCount() == 0)
			return null;
		return getValueAt(getRowCount() - 1, 0).toString();
	}

	public String[] getInfo(int row) {
		if (row == -1)
			return null;
		String[] info = new String[getColumnCount()];
		for (int i = 0; i < getColumnCount(); i++)
			info[i] = getValueAt(row, i).toString();
		return info;
	}

}
