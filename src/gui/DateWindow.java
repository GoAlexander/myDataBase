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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class DateWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private JLabel lblDate;
	private JTextField textDateField;
	private JButton btnOk;
	private String date = new String("");
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-y");

	public String getSelectedDate() {
		return date;
	}

	public DateWindow() {

		setBounds(40, 40, 225, 90);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		lblDate = new JLabel("Order date");

		textDateField = new JTextField();
		textDateField.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(lblDate, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textDateField, GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(textDateField, GroupLayout.PREFERRED_SIZE, 24,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDate))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = textDateField.getText();
				if (!s.matches("^\\d\\d-\\d\\d-\\d\\d\\d\\d$")) {
					JOptionPane.showMessageDialog(null, "Wrong format!");
					return;
				} else {
					date = dateFormat.format(s);
					dispose();
				}
			}
		});
		getContentPane().add(btnOk, BorderLayout.SOUTH);

	}

}
