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


public class ShoppingCartGuiView extends JFrame {

    private static JFrame existingFrame = null;
    private static ShoppingCartGuiView shoppingCartGuiView;
    private final JTable table;
    String[] columnNames = new String[]{"Product", "Quantity", "Price"};
    private final FooterPanel panel;
    private ShoppingCartDao shoppingCartDao = new ShoppingCartDao();
    private ProductDao productDao = new ProductDao();

    private ShoppingCartGuiView(String username) {
        existingFrame = this;
        setTitle("Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


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

        panel = new FooterPanel(username);
        add(panel, BorderLayout.SOUTH);

        existingFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                existingFrame = null;
            }
        });
    }

    public static void main(String[] args) {

    }


    public static ShoppingCartGuiView getInstance(String username) {
        if (shoppingCartGuiView == null) {
            if (existingFrame != null) {
                existingFrame.toFront();
                existingFrame.repaint();
                JOptionPane.showMessageDialog(existingFrame, "Shopping Cart is already open");
                return null;
            }
            ShoppingCartGuiView shoppingCartGuiView = new ShoppingCartGuiView(username);
            shoppingCartGuiView.setVisible(true);
            return shoppingCartGuiView;
        }
        return shoppingCartGuiView;
    }

    public Object[][] getData(ArrayList<Product> data) {
        return data.stream().map(c ->
                new Object[]{c.getProductID(), c.getAvailableItems(), String.valueOf(c.getPrice())}).toArray(Object[][]::new);
    }

    private class ShoppingCartTable extends JTable {
        public ShoppingCartTable(String username, DefaultTableModel tableModel) {
            super();
            setModel(tableModel);
            TableColumn quantityColumn = getColumnModel().getColumn(1);
            quantityColumn.setCellEditor(new SpinnerEditor(username, tableModel));
        }

    }

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
                public void stateChanged(ChangeEvent e) {
                    lastValue = shoppingCartDao.getCurrentProductStock("test", table.getValueAt(table.getSelectedRow(), 0).toString());
                    int value = Integer.parseInt(spinner.getValue().toString());
                    if (value == lastValue) {
                        return;
                    }
                    if (Integer.parseInt(spinner.getValue().toString()) == 0) {
                        shoppingCartDao.removeProductFromShoppingCart("test", table.getValueAt(table.getSelectedRow(), 0).toString(), lastValue);
                        tableModel.removeRow(table.getSelectedRow());
                    } else {
                        tableModel.setValueAt(spinner.getValue(), table.getSelectedRow(), 1);
                        int deviation = Integer.parseInt(spinner.getValue().toString()) - lastValue;
                        if (deviation != 0) {
                            shoppingCartDao.updateProduct("test", table.getValueAt(table.getSelectedRow(), 0).toString(), deviation);
                        }
                    }
                    lastValue = value;
                    panel.updateFooterPanel(username);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            model.setMaximum(productDao.getCurrentStock(table.getValueAt(row, 0).toString()));
            model.setValue(value);

            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    private class FooterPanel extends JPanel {
        JPanel panel;

        public FooterPanel(String username) {
            super();
            panel = this;
            setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            updateFooterPanel(username);
        }

        public void updateFooterPanel(String username) {
            panel.removeAll();
            JLabel totalPrice = new JLabel("Total: " + shoppingCartDao.getTotalPrice(username) + " £");
            panel.add(totalPrice);
            JLabel threeDiscount = new JLabel((shoppingCartDao.getThreeItemsInSameCategoryDiscount(username) == 0 ? null : "Three Items in same Category Discount (20%): -" + shoppingCartDao.getThreeItemsInSameCategoryDiscount(username) + " £"));
            panel.add(threeDiscount);
            JLabel firstDiscount = new JLabel((shoppingCartDao.getfirstPurchaseDiscount(username) == 0 ? null : "First Purchase Discount (10%): -" + shoppingCartDao.getfirstPurchaseDiscount(username) + " £"));
            panel.add(firstDiscount);
            JLabel finalTotal = new JLabel("Final Total: " + shoppingCartDao.getFinalPrice(username) + " £");
            panel.add(finalTotal);

            JButton checkoutButton = new JButton("Checkout");
            checkoutButton.setEnabled(shoppingCartDao.getTotalPrice(username) != 0);
            panel.add(checkoutButton, BorderLayout.SOUTH);

            panel.revalidate();
            panel.repaint();

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





