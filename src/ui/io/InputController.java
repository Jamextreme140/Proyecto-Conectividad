package ui.io;

import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.db.DatabaseHandler;
import com.db.QueryBuilder;
import com.db.util.QueryType;

import ui.ConnectionFrame;
import ui.EmployeesSoldFrame;
import ui.MainFrame;
import ui.NewOrderForm;
import ui.util.MessageHandler;
import ui.util.ResultSetModel;

public class InputController implements ActionListener {

    private MainFrame view;

    public InputController(MainFrame view) {
        this.view = view;
    }

    private void onClose()
    {
        disconnectDB();
        System.exit(0);
    }

    private void disconnectDB()
    {
        try {
            DatabaseHandler.disconnect();
            System.out.println("Connection Closed");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private ResultSet updateOrdersView()
    {
        ResultSet rs;
        try {
            rs = DatabaseHandler.executeSelect(QueryBuilder.buildQuery(QueryType.SELECT, null, "Ventas.pedido", null));
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.getConnect())
        {
            new ConnectionFrame(view);
            return;
        }
        else if(e.getSource() == view.getDisconnect())
        {
            disconnectDB();
            view.getConnect().setEnabled(true);
            view.getDisconnect().setEnabled(false);
            view.getNewOrder().setEnabled(false);
            //view.getOrderDetails().setEnabled(false);
            //view.getOrderDetails().setEnabled(false);
            view.getUpdateOrder().setEnabled(false);
            view.getDeleteOrder().setEnabled(false);
            return;
        }
        else if(e.getSource() == view.getExit())
        {
            onClose();
            return;
        }
        else if(e.getSource() == view.getEmpSold())
        {
            try {
                if(DatabaseHandler.isClosed()) return;
                new EmployeesSoldFrame(view);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return;
        }

        if(e.getSource() == view.getNewOrder())
        {
            new NewOrderForm(view);
            return;
        }
        else if(e.getSource() == view.getUpdateOrder())
        {
            String value = JOptionPane.showInputDialog(view, "Ingresa el nuevo valor de la fila seleccionada", "Modificar campo", JOptionPane.INFORMATION_MESSAGE);
            if(value != null && !value.isBlank())
            {
                int row = view.getOrdersView().getSelectedRow();
                int column = view.getOrdersView().getSelectedColumn();
                if(row == -1) return;
                if(column == 0) return;
                System.out.println("Row: " + row + " | Column: " + column);
                try {
                    DatabaseHandler.executeOperation(QueryBuilder.buildQuery(QueryType.UPDATE,
                            new String[] { view.getOrdersView().getColumnName(column) + " = " + value },
                            "Ventas.pedido", "WHERE id = " + view.getOrdersView().getValueAt(row, 0).toString()));
                    view.updateOrdersView(new ResultSetModel(updateOrdersView()));
                } catch (SQLException e1) {
                    MessageHandler.showError("Error", "Error al actualizar pedido: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
            return;
        }
        else if(e.getSource() == view.getDeleteOrder())
        {
            int option = JOptionPane.showConfirmDialog(view, "¿Esta seguro de querer borrar la fila seleccionada?", "Eliminar pedido", JOptionPane.YES_NO_OPTION);
            if(option == 0)
            {
                int row = view.getOrdersView().getSelectedRow();
                //int column = view.getOrdersView().getSelectedColumn();
                if(row == -1) return;
                try {
                    DatabaseHandler.executeOperation(QueryBuilder.buildQuery(QueryType.DELETE, 
                    null, 
                    "Ventas.pedido", "WHERE id = " + view.getOrdersView().getValueAt(row, 0)));
                    view.updateOrdersView(new ResultSetModel(updateOrdersView()));
                } catch (SQLException e1) {
                    MessageHandler.showError("Error", "Error al eliminar el pedido: " + e1.getMessage());
                    e1.printStackTrace();
                }
            }
            return;
        }
        else if(e.getSource() == view.getUpdateView())
        {
            view.updateOrdersView(new ResultSetModel(updateOrdersView()));
            return;
        }
    }
}
