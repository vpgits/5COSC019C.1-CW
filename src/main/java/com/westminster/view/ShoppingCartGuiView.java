package com.westminster.view;

import com.westminster.dao.ProductDao;
import com.westminster.dao.ShoppingCartDao;
import com.westminster.model.Product;
import com.westminster.model.ShoppingCart;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class ShoppingCartGuiView extends JFrame {

    private static ShoppingCartGuiView shoppingCartGuiView;
    private JTable table;
    private DefaultTableModel tableModel;
    private FooterPanel panel;

    private ShoppingCartGuiView(String username) {
        setTitle("Shopping Cart");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        /**
         * Table
         */
        String[] columnNames = new String[]{"Product", "Quantity", "Price"};
        tableModel = new DefaultTableModel(getData(ShoppingCartDao.getProductsInShoppingCart(username)), columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };


        table = new JTable(tableModel);
        TableColumn quantityColumn = table.getColumnModel().getColumn(1);
        quantityColumn.setCellEditor(new SpinnerEditor(username));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);


        panel = new FooterPanel();
        panel.updateFooterPanel(username);
        JButton checkoutButton = new JButton("Checkout");
        panel.add(checkoutButton, BorderLayout.SOUTH);
        add(panel, BorderLayout.SOUTH);

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShoppingCartDao.checkout(username);
                JOptionPane.showMessageDialog(null, "Checkout Successful");
                dispose();
            }
        });
    }

    // Rest of the code remains the same


    public static ShoppingCartGuiView getInstance(String username) {
        if (shoppingCartGuiView == null) {
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

    class SpinnerEditor extends DefaultCellEditor {
        private int lastValue = 1;
        private JSpinner spinner;
        private SpinnerNumberModel model;

        public SpinnerEditor(final String username) {

            super(new JTextField());
            model = new SpinnerNumberModel(1, 0, 100, 1);
            spinner = new JSpinner(model);
            spinner.setBorder(null);

            spinner.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    int lastValue = ShoppingCartDao.getCurrentProductStock("test", table.getValueAt(table.getSelectedRow(), 0).toString());
                    int value = Integer.valueOf(spinner.getValue().toString());
                    if (value == lastValue) {
                        return;
                    }
                    if (Integer.valueOf(spinner.getValue().toString()) == 0) {
                        ShoppingCartDao.removeProductFromShoppingCart("test", table.getValueAt(table.getSelectedRow(), 0).toString(), 1);
                        tableModel.removeRow(table.getSelectedRow());
                    } else {
                        tableModel.setValueAt(spinner.getValue(), table.getSelectedRow(), 1);
                        int deviation = Integer.valueOf(spinner.getValue().toString()) - lastValue;
                        if (deviation != 0) {
//                            ShoppingCartDao.addProductToShoppingCart("test", table.getValueAt(table.getSelectedRow(), 0).toString(), deviation);
//                        } else if (deviation < 0) {
//                            ShoppingCartDao.removeProductFromShoppingCart("test", table.getValueAt(table.getSelectedRow(), 0).toString(), deviation * -1);
                            ShoppingCartDao.updateProduct("test", table.getValueAt(table.getSelectedRow(), 0).toString(), deviation);
                        }
                    }
                    lastValue = value;
                    panel.updateFooterPanel(username);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            model.setMaximum(Integer.valueOf(ProductDao.getCurrentStock(table.getValueAt(row, 0).toString())));
            model.setValue(value);

            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    private class FooterPanel extends JPanel {
        private JPanel panel;

        public FooterPanel() {
            super();
            this.panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            add(panel);
        }

        public void updateFooterPanel(String username) {
            panel.removeAll();
            JLabel totalPrice = new JLabel("Total: " + ShoppingCartDao.getTotalPrice(username) + " £");
            panel.add(totalPrice);
            JLabel threeDiscount =  new JLabel((ShoppingCartDao.getThreeItemsInSameCategoryDiscount(username) == 0 ? null : "Three Items in same Category Discount (20%): -" + ShoppingCartDao.getThreeItemsInSameCategoryDiscount(username) + " £"));
            panel.add(threeDiscount);
            JLabel firstDiscount = new JLabel((ShoppingCartDao.getfirstPurchaseDiscount(username) == 0 ? null : "First Purchase Discount (10%): -" + ShoppingCartDao.getfirstPurchaseDiscount(username) + " £"));
            panel.add(firstDiscount);
            JLabel finalTotal = new JLabel("Final Total: " + ShoppingCartDao.getFinalPrice(username) + " £");
            panel.add(finalTotal);
            panel.revalidate();
            panel.repaint();
        }

    }

}





