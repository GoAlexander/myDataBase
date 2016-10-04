package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class QuantityWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel lblQuantity;
	private JTextField textQuantityField;
	private JButton btnOk;
	private int quantity = -1;

	public int getSelectedQuantity() {
		return quantity;
	}

	public QuantityWindow() {

		setBounds(40, 40, 225, 90);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		lblQuantity = new JLabel("Quantity");

		textQuantityField = new JTextField();
		textQuantityField.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(lblQuantity, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textQuantityField, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(textQuantityField, GroupLayout.PREFERRED_SIZE, 24,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblQuantity))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = textQuantityField.getText();
				if (!s.matches("[0-9]+")) {
					JOptionPane.showMessageDialog(null, "Wrong format!");
					return;
				} else {
					quantity = Integer.parseInt(s);
					dispose();
				}
			}
		});
		getContentPane().add(btnOk, BorderLayout.SOUTH);

	}

}
