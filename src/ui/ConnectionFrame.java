package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.db.DatabaseHandler;
import com.db.QueryBuilder;
import com.db.util.QueryType;

import ui.util.MessageHandler;
import ui.util.ResultSetModel;

public class ConnectionFrame extends JDialog implements ActionListener {
    JTextField[] fields;
    JPasswordField psw;
    JLabel[] labels;
    JButton conBtn;
    MainFrame parent;

    public ConnectionFrame(MainFrame parent) {
        super(parent, "Conexion a la base de datos", true);
        this.parent = parent;
        init();
    }

    public void init() {
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new GridLayout(0, 1, 2, 2));
        setupUI();
    }

    void setupUI() {
        fields = new JTextField[4];
        labels = new JLabel[5];
        for (int i = 0; i < labels.length; i++) {
            switch (i) {
                case 0 -> {
                    labels[i] = new JLabel("Nombre de servidor");
                }
                case 1 -> {
                    labels[i] = new JLabel("Puerto");
                }
                case 2 -> {
                    labels[i] = new JLabel("Nombre de la Base de Datos");
                }
                case 3 -> {
                    labels[i] = new JLabel("Usuario");
                }
                case 4 -> {
                    labels[i] = new JLabel("Contraseña");
                }
            }
            add(labels[i], BorderLayout.CENTER);
            if(i < 4)
            {
                fields[i] = new JTextField();
                fields[i].setText("");
                add(fields[i], BorderLayout.CENTER); 
            }
        }
        psw = new JPasswordField();
        add(psw, BorderLayout.CENTER);

        conBtn = new JButton("Conectar");
        conBtn.addActionListener(this);
        add(conBtn, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == conBtn) {
            try {
                DatabaseHandler.connect(fields[0].getText(), fields[1].getText(), fields[2].getText(),
                        fields[3].getText(),
                        new String(psw.getPassword()));
                parent.getConnect().setEnabled(false);
                parent.getDisconnect().setEnabled(true);
                parent.getNewOrder().setEnabled(true);
                parent.getUpdateOrder().setEnabled(true);
                //parent.getOrderDetails().setEnabled(true);
                //parent.getViewProducts().setEnabled(true);
                parent.getDeleteOrder().setEnabled(true);
                parent.getUpdateView().setEnabled(true);
                updateParent();
                this.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                MessageHandler.showError("Error de Conexion", "No se pudo realizar la conexion");
            }
        }

    }

    private void updateParent()
    {
        ResultSet rs;
        try{
            rs = DatabaseHandler.executeSelect(QueryBuilder.buildQuery(QueryType.SELECT, null, "Ventas.pedido", null));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }
        ResultSetModel rsm = new ResultSetModel(rs);

        parent.setupOrdersView(rsm);
    }
}
