package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InsertWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JPanel panel_1;
	protected JLabel lblAddNewProduct;
	private JLabel lblName;
	protected JTextField tfPrice;
	private JLabel lblQuantity;
	protected JTextField tfDate;
	private JLabel lblPrice;
	protected JTextField tfQuantity;
	private JLabel lblDate;
	protected JTextField tfName;
	private JButton btnOk;
	private String[] newProduct;

	public String[] getNewProduct() {
		return newProduct;
	}

	public InsertWindow() {
		newProduct = new String[4];
		newProduct[0] = "-1";
		setBounds(100, 100, 500, 125);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		lblAddNewProduct = new JLabel("Add new product:");
		lblAddNewProduct.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAddNewProduct.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblAddNewProduct);

		panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new GridLayout(2, 4, 0, 0));

		lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblName);

		lblDate = new JLabel("Date");
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblDate);

		lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblPrice);

		lblQuantity = new JLabel("Quantity");
		lblQuantity.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblQuantity);

		tfName = new JTextField("");
		panel_1.add(tfName);

		tfDate = new JTextField("");
		panel_1.add(tfDate);

		tfPrice = new JTextField("");
		panel_1.add(tfPrice);

		tfQuantity = new JTextField("");
		panel_1.add(tfQuantity);

		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tfName.getText().isEmpty() || tfQuantity.getText().isEmpty() || tfPrice.getText().isEmpty()
						|| tfDate.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Fill all the fields!");
					return;
				} else if (!tfQuantity.getText().matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Wrong quantity format!");
					return;
				} else if (!tfPrice.getText().matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Wrong price format!");
					return;
				} else if (!tfDate.getText().matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
					JOptionPane.showMessageDialog(null, "Wrong data format! Example: 16-01-1997");
					return;
				} else {
					newProduct[0] = tfName.getText();
					newProduct[1] = tfDate.getText();
					newProduct[2] = tfPrice.getText();
					newProduct[3] = tfQuantity.getText();
					dispose();
				}
			}
		});
		getContentPane().add(btnOk, BorderLayout.SOUTH);
	}

}
