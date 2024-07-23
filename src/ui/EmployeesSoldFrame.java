package ui;

import javax.swing.*;

import com.db.DatabaseHandler;

import ui.util.MessageHandler;
import ui.util.ResultSetModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeesSoldFrame extends JDialog implements ActionListener{

    MainFrame parent;
    JTable employeesTable;
    JScrollPane empScroll;
    JPanel empPanel, buttons;
    JButton closeButton;

    public EmployeesSoldFrame(MainFrame parent)
    {
        super(parent, "Lista de empleados y sus productos vendidos", true);
        this.parent = parent;
        init();
        createTable();
    }

    private void init()
    {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
        empPanel = new JPanel(new BorderLayout());
        buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        empScroll = new JScrollPane();
        empPanel.add(empScroll, BorderLayout.CENTER);

        closeButton = new JButton("Cerrar");
        closeButton.addActionListener(this);
        buttons.add(closeButton);

        add(empPanel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void createTable()
    {
        try {
            ResultSet rs = DatabaseHandler.executeSP_W_ResultSet("Caso3");

            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getMetaData().getColumnLabel(i) + "\t|");
            }
            System.out.println("\n");

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t|");
                }
                System.out.println("");
            }

            setupEmployeesView(new ResultSetModel(rs));
        } catch (SQLException e) {
            MessageHandler.showError("Error", "Error al recuperar la información: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setupEmployeesView(ResultSetModel rsm)
    {
        employeesTable = new JTable(rsm);
        empPanel.add(employeesTable.getTableHeader());
        empPanel.add(employeesTable);
        empScroll.setViewportView(employeesTable);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == closeButton)
            dispose();
    }
}
