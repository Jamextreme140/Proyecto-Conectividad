package ui;

import javax.swing.*;
import java.awt.*;
import ui.io.InputController;
import ui.util.ResultSetModel;

public class MainFrame extends JFrame{
    
    //Fields
    JMenuBar menuBar;
    JMenu options, view;
    JMenuItem connect, disconnect, exit, empSold;
    JPanel ordersPanel, buttonsPanel, miscButtonsPanel;
    JTable ordersView;
    JScrollPane ordersViewScroll;
    JButton newOrder, viewProducts, orderDetails, updateOrder, deleteOrder, updateView;
    
    public MainFrame()
    {
        // TODO: Put a proper title
        super("Gallo Giro");
        init();
    }
    
    /**
     * Setup the main frame and initializes the components
     */
    public void init()
    {
        this.setSize(1000, 720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupUI();
    }

    /**
     * Initializes the components and add it to the frame
     */
    public void setupUI()
    {
        setupMenuBar();
        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        ordersPanel = new JPanel(new BorderLayout());
        miscButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        newOrder = new JButton("Nuevo Pedido");
        //viewProducts = new JButton("Productos");
        //orderDetails = new JButton("Detalles de pedido");
        updateOrder = new JButton("Actualizar Pedido");
        deleteOrder = new JButton("Eliminar Pedido");
        updateView = new JButton("Actualizar");

        for(JButton btn : new JButton[]{newOrder, updateOrder, deleteOrder})
        {
            btn.setEnabled(false);
            buttonsPanel.add(btn);
        }
        
        updateView.setEnabled(false);
        miscButtonsPanel.add(updateView);

        ordersViewScroll = new JScrollPane();
        ordersPanel.add(ordersViewScroll, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.NORTH);
        add(ordersPanel, BorderLayout.CENTER);
        add(miscButtonsPanel, BorderLayout.SOUTH);
    }

    private void setupMenuBar()
    {
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        options = new JMenu("Opciones");
        menuBar.add(options);
        view = new JMenu("Ver");
        menuBar.add(view);

        connect = new JMenuItem("Conectar");
        connect.setEnabled(true);
        options.add(connect);
        disconnect = new JMenuItem("Desconectar");
        disconnect.setEnabled(false);
        options.add(disconnect);
        exit = new JMenuItem("Salir");
        options.add(exit);

        empSold = new JMenuItem("Producto vendido por empleado");
        view.add(empSold);
    }

    public void setupOrdersView(ResultSetModel rsm)
    {
        ordersView = new JTable(rsm);
        ordersPanel.add(ordersView.getTableHeader());
        ordersPanel.add(ordersView);
        ordersViewScroll.setViewportView(ordersView);
        revalidate();
        repaint();
    }

    public void updateOrdersView(ResultSetModel rsm)
    {
        ordersView.setModel(rsm);
    }
    
    /**
     * Toggles the UI visibility
     */
    public void showUI()
    {   
        this.setVisible(!this.isVisible());
        openConnectDialog();
    }

    private void openConnectDialog()
    {
        new ConnectionFrame(this);
    }

    public void implementListeners(InputController ctrl)
    {
        for(JMenuItem menuItem : new JMenuItem[]{connect, disconnect, exit, empSold})
        {
            menuItem.addActionListener(ctrl);
        }

        for(JButton btn : new JButton[]{newOrder, updateOrder, deleteOrder, updateView})
        {
            btn.addActionListener(ctrl);
        }
    }

    public JMenuItem getConnect() {
        return connect;
    }

    public JMenuItem getDisconnect() {
        return disconnect;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public JMenuItem getEmpSold() {
        return empSold;
    }

    public JButton getNewOrder() {
        return newOrder;
    }
    /*
    public JButton getViewProducts() {
        return viewProducts;
    }
    
    public JButton getOrderDetails() {
        return orderDetails;
    }
    */
    public JButton getUpdateOrder() {
        return updateOrder;
    }

    public JButton getDeleteOrder() {
        return deleteOrder;
    }

    public JButton getUpdateView() {
        return updateView;
    }

    public JTable getOrdersView() {
        return ordersView;
    }
}
