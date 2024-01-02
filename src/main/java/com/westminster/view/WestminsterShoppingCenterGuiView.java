package com.westminster.view;

import com.westminster.dao.ProductDao;
import com.westminster.dao.ShoppingCartDao;
import com.westminster.model.Product;
import com.westminster.model.ProductType;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WestminsterShoppingCenterGuiView extends JFrame {
    private static final JButton shoppingCartButton = new JButton("Shopping Cart");
    private static JComboBox<String> categoryComboBox;
    private final String[] columnNames = {"Product ID", "Name", "Category", "Price(£)", "Info"};
    private final String username;
    private JTable productTable;
    private final ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private final ProductDao productDao = new ProductDao();

    public WestminsterShoppingCenterGuiView(String username) throws ProductDao.ProductDaoException {

        setTitle("Westminster Shopping Centre");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.username = username;

        // Top Panel for Category Selection and Shopping Cart
        add(createTopPanel(), BorderLayout.NORTH);

        // Center Panel for Product Table
        add(createCenterPanel(), BorderLayout.CENTER);

        // Bottom Panel for Selected Product Details and Add to Cart Button
        add(createBottomPanel(), BorderLayout.SOUTH);

        shoppingCartButton.addActionListener(e -> SwingUtilities.invokeLater(() -> ShoppingCartGuiView.getInstance(username).setVisible(true)));
        setVisible(true);
    }

    private static JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Product Category"));
        categoryComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        topPanel.add(categoryComboBox);
        topPanel.add(shoppingCartButton);
        return topPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WestminsterShoppingCenterGuiView view;
            try {
                view = new WestminsterShoppingCenterGuiView("test");

            } catch (Exception e) {
                throw new WestminsterShoppingCenterGuiError(e.getMessage());
            }
            view.setVisible(true);
        });
    }

    private JScrollPane createCenterPanel() {
        productTable = new ProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane);
        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        JPanel detailsPanel = new DetailsPanel();
        bottomPanel.add(detailsPanel);
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addButtonPanel.add(addToCartButton);
        bottomPanel.add(addButtonPanel);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int activeRow = productTable.getSelectedRow();
            if (activeRow != -1) {
                SwingUtilities.invokeLater(() -> {
                    detailsPanel.removeAll();
                    detailsPanel.add(new DetailsPanel());
                    detailsPanel.revalidate();
                });
            }

        });

        addToCartButton.addActionListener(e -> {
            int activeRow = productTable.getSelectedRow();
            if (activeRow != -1) {
                String productID = (String) productTable.getValueAt(activeRow, 0);
                shoppingCartDao.addProductToShoppingCart(username, productID, 1);
            }

        });

        return bottomPanel;
    }

    public Object[][] getDataFormattedForTable(List<Product> data) {
        try {
            return data.stream().map(c -> new Object[]{c.getProductID(), c.getProductName(), c.getType().toString(), String.valueOf(c.getPrice()), c.toString()}).toArray(Object[][]::new);
        } catch (Exception e) {
            throw new WestminsterShoppingCenterGuiError(e.getMessage());
        }
    }

    private static class WestminsterShoppingCenterGuiError extends RuntimeException {
        public WestminsterShoppingCenterGuiError(String message) {
            super(message);
        }
    }

    private class ProductTable extends JTable {


        public ProductTable() {
            super();
            Object[][] data = getDataFormattedForTable(fetchDataForTable("All"));
            setModel(new DefaultTableModel(data, columnNames));
            setupTimer();
            setupComboBox();

        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        private void setupTimer() {
            Timer timer = new Timer(1000, e -> {
                setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        String productId = table.getValueAt(row, 0).toString();
                        int stock = productDao.getCurrentStock(productId);

                        if (stock < 3) {
                            renderer.setForeground(Color.RED); // Items with reduced availability in red
                        } else {
                            renderer.setForeground(Color.BLACK); // Other items in default color
                        }
                        return renderer;
                    }
                });
                revalidate();
                repaint();
            });
            timer.start();
        }

        private void setupComboBox() {
            categoryComboBox.addActionListener(e -> {
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                assert selectedCategory != null;
                updateTable(selectedCategory);
            });
        }

        private void updateTable(String selectedCategory) {
            setModel(new DefaultTableModel(getDataFormattedForTable(fetchDataForTable(selectedCategory)), columnNames));
            revalidate();
            repaint();
        }

        private ArrayList<Product> fetchDataForTable(String selectedCategory) {
            ArrayList<Product> products = new ArrayList<>();
            try {
                switch (selectedCategory) {
                    case "All" -> {
                        products.addAll(productDao.getProducts(ProductType.Electronics));
                        products.addAll(productDao.getProducts(ProductType.Clothing));
                    }
                    case "Clothing" -> products.addAll(productDao.getProducts(ProductType.Clothing));
                    case "Electronics" -> products.addAll(productDao.getProducts(ProductType.Electronics));
                }
            } catch (ProductDao.ProductDaoException e) {
                throw new WestminsterShoppingCenterGuiError(e.getMessage());
            }
            return products;
        }
    }

    private class DetailsPanel extends JPanel {
        private final JPanel panel;

        public DetailsPanel() {
            super();
            this.panel = new JPanel();
            panel.setLayout(new GridLayout(7, 1, 0, 10));
            updatePanel();
            add(panel);

            Timer timer = new Timer(1000, e -> updatePanel());
            timer.start();
        }

        private void updatePanel() {
            int activeRow = productTable.getSelectedRow();
            if (activeRow == -1) {
                panel.setVisible(false);
                return;
            }
            panel.removeAll();
            String productID = (String) productTable.getValueAt(activeRow, 0);
            String productName = (String) productTable.getValueAt(activeRow, 1);
            String category = (String) productTable.getValueAt(activeRow, 2);
            String price = (String) productTable.getValueAt(activeRow, 3);
            String info = (String) productTable.getValueAt(activeRow, 4);

            panel.add(new JLabel("Selected Product-Details"));
            panel.add(new JLabel("Product ID: " + productID));
            panel.add(new JLabel("Product Name: " + productName));
            panel.add(new JLabel("Category: " + category));
            panel.add(new JLabel("Price: " + price));
            panel.add(new JLabel("Info: " + info));
            panel.add(new JLabel("Quantity: " + productDao.getCurrentStock(productID)));
            panel.revalidate();
            panel.repaint();
            panel.setVisible(true);
        }
    }
}





