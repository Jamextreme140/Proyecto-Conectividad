package ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.ResultSet;

import javax.swing.*;

import com.db.DatabaseHandler;
import com.db.QueryBuilder;
import com.db.util.QueryType;

import ui.util.MessageHandler;
import ui.util.ResultSetModel;

public class NewOrderForm extends JDialog implements ActionListener {

    MainFrame parent;
    JPanel fields, buttons;
    JTextField empID, amount, productUPC, idCedis;
    JLabel[] labels = { new JLabel("Empleado"), new JLabel("Cantidad"), new JLabel("Producto"),
            new JLabel("CEDIS") };
    JButton addBtn, cancelBtn;

    public NewOrderForm(MainFrame parent) {
        super(parent, "Nuevo pedido", true);
        this.parent = parent;
        this.setModal(true);
        this.setSize(300, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(0, 1, 2, 2));
        init();
    }

    private void init() {
        fields = new JPanel(new GridLayout(0, 1, 5, 5));
        buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        empID = new JTextField();
        amount = new JTextField();
        productUPC = new JTextField();
        idCedis = new JTextField();

        for (int i = 0; i < labels.length; i++) {
            fields.add(labels[i]);
            switch(i)
            {
                case 0 -> {fields.add(empID);}
                case 1 -> {fields.add(amount);}
                case 2 -> {fields.add(productUPC);}
                case 3 -> {fields.add(idCedis);}
            }
        }

        addBtn = new JButton("Agregar");
        addBtn.addActionListener(this);
        cancelBtn = new JButton("Cancelar");
        cancelBtn.addActionListener(this);
        buttons.add(addBtn);
        buttons.add(cancelBtn);

        add(fields, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addBtn)
        {
            try {
                int a = DatabaseHandler.executeOperation(QueryBuilder.buildQuery(
                        QueryType.INSERT,
                        new String[] { "empleado_id", "cantidad", "producto_upc", "id_cedis" }, 
                        "Ventas.pedido",
                        "(" + empID.getText() + ", " + amount.getText() + ", " + productUPC.getText() + ", "
                                + idCedis.getText() + ")"));
                System.out.println(a + " rows affected");
                parent.updateOrdersView(new ResultSetModel(updateOrdersView()));
                this.dispose();
            } catch (SQLException e1) {
                MessageHandler.showError("Error", "Error al agregar pedido: " + e1.getMessage());
                e1.printStackTrace();
            }
            return;
        }
        else if(e.getSource() == cancelBtn)
        {
            this.dispose();
            return;
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

}
