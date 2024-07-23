package ui.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

/**
 * ResultSet Model for displaying SQL Data into a table
 */
public class ResultSetModel extends AbstractTableModel {

    private ResultSet rs;

    public ResultSetModel(ResultSet rs) {
        this.rs = rs;
    }

    @Override
    public int getRowCount() {
        int count = 0;

        try {
            rs.beforeFirst();
            while (rs.next()) {
                count++;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return count;
    }

    @Override
    public int getColumnCount() {
        int count = 0;

        try {
            count = rs.getMetaData().getColumnCount();
        } catch (SQLException e) {
            // TODO: handle exception
        }

        return count;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            rs.first();
            rs.absolute(rowIndex+1); 
            return rs.getObject(columnIndex+1);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    @Override
    public String getColumnName(int columnIndex) {
        try {
            return rs.getMetaData().getColumnLabel(columnIndex+1);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }

}
