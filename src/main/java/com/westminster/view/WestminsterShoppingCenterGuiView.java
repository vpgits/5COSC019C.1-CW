package com.westminster.view;

import com.westminster.dao.ProductDao;
import com.westminster.dao.ShoppingCartDao;
import com.westminster.model.Product;
import com.westminster.model.ProductType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods for WestminsterShoppingCenterGuiView/ GUI.
 */
public class WestminsterShoppingCenterGuiView extends JFrame {
    // instance variables
    private final JButton shoppingCartButton = new JButton("Shopping Cart");
    private final JButton refreshButton = new JButton("Refresh");
    private JComboBox<String> categoryComboBox;
    private final String[] columnNames = {"Product ID", "Name", "Category", "Price(£)", "Info"};
    private final String username;
    private ProductTable productTable;
    private final ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private final ProductDao productDao = new ProductDao();

    private boolean isCartRunning = false;

    /**
     * Constructs a WestminsterShoppingCenterGuiView object with the specified parameters.
     * @param username username of the user
     * @throws ProductDao.ProductDaoException if there is an error with the ProductDao
     */
    public WestminsterShoppingCenterGuiView(String username) throws ProductDao.ProductDaoException {

        setTitle("Westminster Shopping Centre");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.username = username;

        // Top Panel for Category Selection and Shopping Cart
        add(createTopPanel(), BorderLayout.NORTH);

        // Center Panel for Product Table
        add(createCenterPanel(), BorderLayout.CENTER);

        // Bottom Panel for Selected Product Details and Add to Cart Button
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);

        shoppingCartButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ShoppingCartGuiView.getInstance(username);
                }
            });
        });

        refreshButton.addActionListener(e -> {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    productTable.format();
                }
            });
        });

    }

    /**
     * Creates the top panel of the GUI.
     * @return JPanel
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Product Category"));
        categoryComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        topPanel.add(categoryComboBox);
        topPanel.add(shoppingCartButton);
        topPanel.add(refreshButton);
        return topPanel;
    }

    /**
     * Main method.
     * @param args command line arguments
     */
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

    /**
     * Creates the center panel of the GUI.
     * @return JScrollPane
     */
    private JScrollPane createCenterPanel() {
        productTable = new ProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane);
        return scrollPane;
    }

    /**
     * Creates the bottom panel of the GUI.
     * @return JPanel
     */
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
            if (e.getValueIsAdjusting())
                return;
            int activeRow = productTable.getSelectedRow();
            if (activeRow != -1) {
                SwingUtilities.invokeLater(() -> {
                    detailsPanel.removeAll();
                    detailsPanel.add(new DetailsPanel());
                    detailsPanel.revalidate();
                });
            }

        });
        /**
         * Adds the selected product to the shopping cart.
         */
        addToCartButton.addActionListener(e -> {
            int activeRow = productTable.getSelectedRow();
            if (activeRow != -1) {
                String productID = (String) productTable.getValueAt(activeRow, 0);
                shoppingCartDao.addProductToShoppingCart(username, productID, 1);
                ShoppingCartGuiView.getInstance(username).refreshTable(username);
            }

        });

        return bottomPanel;
    }

    /**
     * Gets the data formatted for the table.
     * @param data data to be formatted
     * @return Object[][]
     */
    public Object[][] getDataFormattedForTable(List<Product> data) {
        try {
            return data.stream().map(c -> new Object[]{c.getProductID(), c.getProductName(), c.getType().toString(),
                    String.valueOf(c.getPrice()), c.toStringTable()}).toArray(Object[][]::new);
        } catch (Exception e) {
            throw new WestminsterShoppingCenterGuiError(e.getMessage());
        }
    }

    /**
     * This class is used to throw WestminsterShoppingCenterGuiError exceptions.
     */
    private static class WestminsterShoppingCenterGuiError extends RuntimeException {
        public WestminsterShoppingCenterGuiError(String message) {
            super(message);
        }
    }

    /**
     * This class is used to create the product table.
     */
    private class ProductTable extends JTable {

        public ProductTable() {
            super();
            Object[][] data = getDataFormattedForTable(fetchDataForTable("All"));
            setModel(new DefaultTableModel(data, columnNames));
            format();
            setupTimer();
            setupComboBox();
        }

        /**
         * Returns true if the cell at <code>row</code> and <code>column</code>
         * @param row      the row whose value is to be queried
         * @param column   the column whose value is to be queried
         * @return true if the cell is editable
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        /**
         * Formats the table.
         */
        public void format() {
            refreshTableModel();

            updateTable((String) categoryComboBox.getSelectedItem());
            TableColumn productIdColumn = getColumnModel().getColumn(0);
            TableColumn nameColumn = getColumnModel().getColumn(1);
            TableColumn priceColumn = getColumnModel().getColumn(3);
            TableColumn categoryColumn = getColumnModel().getColumn(2);
            TableColumn infoColumn = getColumnModel().getColumn(4);
            nameColumn.setMinWidth(200);
            productIdColumn.setMinWidth(100);
            categoryColumn.setMinWidth(100);
            priceColumn.setMinWidth(75);
            infoColumn.setMinWidth(400);
            setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);


        }

        /**
         * Refreshes the table model.
         */
        private void refreshTableModel() {
            setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public void setHorizontalAlignment(int alignment) {
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                }

                /**
                 * Returns the <code>Component</code> used for drawing the cell.
                 * @param table  the <code>JTable</code>
                 * @param value  the value to assign to the cell at
                 *                  <code>[row, column]</code>
                 * @param isSelected true if cell is selected
                 * @param hasFocus true if cell has focus
                 * @param row  the row of the cell to render
                 * @param column the column of the cell to render
                 * @return
                 */
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                               boolean hasFocus, int row, int column) {
                    Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                            column);

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

        }

        /**
         * Sets up the timer. for refreshing the table.
         */
        private void setupTimer() {
            Timer timer = new Timer(1000, e -> {
                refreshTableModel();
            });
            timer.start();
        }

        /**
         * Sets up the combo box.
         */
        private void setupComboBox() {
            categoryComboBox.addActionListener(e -> {
                String selectedCategory = (String) categoryComboBox.getSelectedItem();
                assert selectedCategory != null;
                updateTable(selectedCategory);
            });
        }

        /**
         * Updates the table.
         * @param selectedCategory selected category
         */
        private void updateTable(String selectedCategory) {
            setModel(new DefaultTableModel(getDataFormattedForTable(fetchDataForTable(selectedCategory)), columnNames));
            revalidate();
            repaint();
        }

        /**
         * Fetches the data for the table.
         * @param selectedCategory selected category
         * @return ArrayList<Product>
         */
        public static ArrayList<Product> fetchDataForTable(String selectedCategory) {
            ProductDao productDao = new ProductDao();
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

    /**
     * This class is used to create the details panel.
     */
    private class DetailsPanel extends JPanel {
        private final JPanel panel;
        JLabel selectedProductDetailsJLabel = new JLabel("Selected Product-Details");
        JLabel productIdJLabel = new JLabel("Product ID: ");
        JLabel productNameJLabel = new JLabel("Product Name: ");
        JLabel categoryJLabel = new JLabel("Category: ");
        JLabel priceJLabel = new JLabel("Price: ");
        JLabel infoJLabel = new JLabel("Info: ");
        JLabel quantityJLabel = new JLabel("Quantity: ");

        /**
         * Constructs a DetailsPanel object.
         */
        public DetailsPanel() {
            super();
            this.panel = new JPanel();
            panel.setLayout(new GridLayout(7, 1, 0, 10));
            panel.add(selectedProductDetailsJLabel);
            panel.add(productIdJLabel);
            panel.add(productNameJLabel);
            panel.add(categoryJLabel);
            panel.add(priceJLabel);
            panel.add(infoJLabel);
            panel.add(quantityJLabel);
            updatePanel();
            add(panel);

            Timer timer = new Timer(1000, e -> SwingUtilities.invokeLater(this::updatePanel));
            timer.start();
            productTable.getSelectionModel().addListSelectionListener(e -> {
                if (e.getValueIsAdjusting())
                    return;
                SwingUtilities.invokeLater(() -> this.updatePanel());
            });
        }

        /**
         * Updates the panel.
         */
        private void updatePanel() {
            int activeRow = productTable.getSelectedRow();
            if (activeRow == -1) {
                panel.setVisible(false);
                return;
            }
            String productId = (String) productTable.getValueAt(activeRow, 0);
            String productName = (String) productTable.getValueAt(activeRow, 1);
            String category = (String) productTable.getValueAt(activeRow, 2);
            String price = (String) productTable.getValueAt(activeRow, 3);
            String info = (String) productTable.getValueAt(activeRow, 4);

            productIdJLabel.setText("Product ID: " + productId);
            productNameJLabel.setText("Product Name: " + productName);
            categoryJLabel.setText("Category: " + category);
            priceJLabel.setText("Price: " + price);
            infoJLabel.setText("Info: " + info);
            quantityJLabel.setText("Quantity: " + productDao.getCurrentStock(productId));
            panel.revalidate();
            panel.repaint();
            panel.setVisible(true);
            revalidate();
            repaint();
        }
    }
}
