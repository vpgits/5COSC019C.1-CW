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

public class WestminsterShoppingCenterGuiView extends JFrame {
    private final JButton shoppingCartButton = new JButton("Shopping Cart");
    private final JButton refreshButton = new JButton("Refresh");
    private JComboBox<String> categoryComboBox;
    private final String[] columnNames = {"Product ID", "Name", "Category", "Price(£)", "Info"};
    private final String username;
    private ProductTable productTable;
    private final ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private final ProductDao productDao = new ProductDao();

    private boolean isCartRunning = false;

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

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Product Category"));
        categoryComboBox = new JComboBox<>(new String[]{"All", "Electronics", "Clothing"});
        topPanel.add(categoryComboBox);
        topPanel.add(shoppingCartButton);
        topPanel.add(refreshButton);
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
            return data.stream().map(c -> new Object[]{c.getProductID(), c.getProductName(), c.getType().toString(),
                    String.valueOf(c.getPrice()), c.toStringTable()}).toArray(Object[][]::new);
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
            format();
            setupTimer();
            setupComboBox();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

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

        private void refreshTableModel() {
            setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public void setHorizontalAlignment(int alignment) {
                    super.setHorizontalAlignment(SwingConstants.CENTER);
                }

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

        private void setupTimer() {
            Timer timer = new Timer(1000, e -> {
                refreshTableModel();
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

    private class DetailsPanel extends JPanel {
        private final JPanel panel;
        JLabel selectedProductDetailsJLabel = new JLabel("Selected Product-Details");
        JLabel productIdJLabel = new JLabel("Product ID: ");
        JLabel productNameJLabel = new JLabel("Product Name: ");
        JLabel categoryJLabel = new JLabel("Category: ");
        JLabel priceJLabel = new JLabel("Price: ");
        JLabel infoJLabel = new JLabel("Info: ");
        JLabel quantityJLabel = new JLabel("Quantity: ");

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
