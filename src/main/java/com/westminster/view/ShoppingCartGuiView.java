package com.westminster.view;

import com.westminster.dao.ProductDao;
import com.westminster.dao.ShoppingCartDao;
import com.westminster.model.Product;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class contains methods for ShoppingCartGuiView/ GUI.
 */
public class ShoppingCartGuiView extends JFrame {
    // instance variables
    private static JFrame existingFrame = null;
    private static ShoppingCartGuiView shoppingCartGuiView;
    private final JTable table;
    String[] columnNames = new String[]{"Product", "Quantity", "Price"};
    private final FooterPanel panel;
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private ProductDao productDao = new ProductDao();

    /**
     * Constructs a ShoppingCartGuiView object with the specified parameter.
     * @param username username of the user
     */
    private ShoppingCartGuiView(String username) {
        existingFrame = this;
        setTitle("Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        //custom table model
        DefaultTableModel tableModel = new DefaultTableModel(getData(shoppingCartDao.getProductsInShoppingCart(username)), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };


        //Table for the shopping cart
        table = new ShoppingCartTable(username, tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        //Footer panel for prices
        panel = new FooterPanel(username);
        add(panel, BorderLayout.SOUTH);

        //Window listener to set existingFrame to null when closed
        existingFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                existingFrame = null;
            }
        });
    }

    public static void main(String[] args) {

    }
    /**
     * Refreshes the table.
     * @param username username of the user
     */
    public void refreshTable(String username) {
        DefaultTableModel tableModel = new DefaultTableModel(getData(shoppingCartDao.getProductsInShoppingCart(username)), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        table.setModel(tableModel);
        TableColumn quantityColumn = table.getColumnModel().getColumn(1);
        quantityColumn.setCellEditor(new SpinnerEditor(username, tableModel));
        table.setModel(tableModel);
        table.repaint();
    }

    /**
     * Returns an instance of ShoppingCartGuiView.
     * @param username username of the user
     * @return an instance of ShoppingCartGuiView
     */
    public static ShoppingCartGuiView getInstance(String username) {
        if (shoppingCartGuiView == null) {
            if(existingFrame != null){
                existingFrame.dispose();
            }
            ShoppingCartGuiView shoppingCartGuiView = new ShoppingCartGuiView(username);
            shoppingCartGuiView.setVisible(true);
            return shoppingCartGuiView;
        }
        return shoppingCartGuiView;
    }

    /**
     * gets data from the shopping cart product list.
     * @param data shopping cart product list
     * @return data from the shopping cart product list
     */
    public Object[][] getData(ArrayList<Product> data) {
        return data.stream().map(c ->
                new Object[]{c.getProductID(), c.getAvailableItems(), String.valueOf(c.getPrice())}).toArray(Object[][]::new);
    }
    /**
     * This class contains methods for ShoppingCartTable/ table.
     */
    private class ShoppingCartTable extends JTable {
        public ShoppingCartTable(String username, DefaultTableModel tableModel) {
            super();
            setModel(tableModel);
            TableColumn quantityColumn = getColumnModel().getColumn(1);
            quantityColumn.setCellEditor(new SpinnerEditor(username, tableModel));
        }

    }
    /**
     * This class contains methods for SpinnerEditor/ spinner.
     */
    private class SpinnerEditor extends DefaultCellEditor {
        private int lastValue = 1;
        private final JSpinner spinner;
        private final SpinnerNumberModel model;

        public SpinnerEditor(final String username, DefaultTableModel tableModel) {

            super(new JTextField());
            model = new SpinnerNumberModel(1, 0, 100, 1);
            spinner = new JSpinner(model);
            spinner.setBorder(null);

            spinner.addChangeListener(new ChangeListener() {
                @Override
                public synchronized void  stateChanged(ChangeEvent e) {
                    lastValue = shoppingCartDao.getCurrentProductStock("test", table.getValueAt(table.getSelectedRow(), 0).toString());
                    int value = Integer.parseInt(spinner.getValue().toString());
                    if (value == lastValue || table.getSelectedRow() == -1) {
                        return;
                    }
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow == -1 || selectedRow >= tableModel.getRowCount()) {
                        return; // No selection or invalid row
                    }
                    if (Integer.parseInt(spinner.getValue().toString()) == 0) {
                        shoppingCartDao.removeProductFromShoppingCart("test", table.getValueAt(table.getSelectedRow(), 0).toString(), lastValue);
                        Product product = (Product) productDao.getProduct((String) table.getValueAt(table.getSelectedRow(), 0));
                        if (table.getColumnCount() > 2) {
                            table.setValueAt(String.format("%.2f", (int) spinner.getValue() * product.getPrice()), table.getSelectedRow(), 2);
                        }

                        panel.updateFooterPanel(username);
                        tableModel.removeRow(table.getSelectedRow());
                        refreshTable(username);
                    } else {
                        if (table.getColumnCount() > 1) {
                            tableModel.setValueAt(spinner.getValue(), table.getSelectedRow(), 1);
                        }
                        int deviation = Integer.parseInt(spinner.getValue().toString()) - lastValue;
                        if (deviation != 0) {
                            Product product = (Product) productDao.getProduct((String) table.getValueAt(table.getSelectedRow(), 0));
                            if (table.getColumnCount() > 2) {
                                table.setValueAt(String.format("%.2f", (int) spinner.getValue() * product.getPrice()), table.getSelectedRow(), 2);
                            }
                            shoppingCartDao.updateProduct("test", table.getValueAt(table.getSelectedRow(), 0).toString(), deviation);
                            panel.updateFooterPanel(username);
                        }
                    }
                    lastValue = value;
                }
            });
        }
        /**
         * Returns the spinner.
         * @param table table
         * @param value value
         * @param isSelected isSelected
         * @param row row
         * @param column column
         * @return the spinner
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            model.setMaximum(productDao.getCurrentStock(table.getValueAt(row, 0).toString()));
            model.setValue(value);

            return spinner;
        }
        /**
         * Returns the spinner value.
         * @return the spinner value
         */
        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    /**
     * This class contains methods for FooterPanel/ footer panel.
     */
    private class FooterPanel extends JPanel {
        JPanel panel; //panel

        /**
         * Constructs a FooterPanel object with the specified parameter.
         * @param username username of the user
         */
        public FooterPanel(String username) {
            super();
            panel = this;
            setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            updateFooterPanel(username);
        }

        /**
         * Updates the footer panel.
         * @param username username of the user
         */
        public void updateFooterPanel(String username) {
            panel.removeAll();
            JLabel totalPrice = new JLabel(String.format("Total: %.2f £", shoppingCartDao.getTotalPrice(username)));
            panel.add(totalPrice);
            JLabel threeDiscount = new JLabel(shoppingCartDao.getThreeItemsInSameCategoryDiscount(username) == 0 ? null : String.format("Three Items in same Category Discount (20%%): -%.2f £", shoppingCartDao.getThreeItemsInSameCategoryDiscount(username)));
            panel.add(threeDiscount);
            JLabel firstDiscount = new JLabel(shoppingCartDao.getfirstPurchaseDiscount(username) == 0 ? null : String.format("First Purchase Discount (10%%): -%.2f £", shoppingCartDao.getfirstPurchaseDiscount(username)));
            panel.add(firstDiscount);
            JLabel finalTotal = new JLabel(String.format("Final Total: %.2f £", shoppingCartDao.getFinalPrice(username)));
            panel.add(finalTotal);

            JButton checkoutButton = new JButton("Checkout");
            checkoutButton.setEnabled(shoppingCartDao.getTotalPrice(username) != 0);
            panel.add(checkoutButton, BorderLayout.SOUTH);

            panel.revalidate();
            panel.repaint();
            /**
                * Action listener for checkout button
                */
            checkoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shoppingCartDao.checkout(username);
                    JOptionPane.showMessageDialog(null, "Checkout Successful");
                    dispose();
                }
            });
        }
    }

}





