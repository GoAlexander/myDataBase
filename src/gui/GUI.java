package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import backend.DB;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class GUI extends JFrame {

	private GUIModel theAppModel = new GUIModel();
	private static final long serialVersionUID = 1L;
	private JButton insertButton, deleteByNameButton, findByNameButton, btnSaveDatabase, btnLoadDatabase,
			btnDeleteDatabase, editButton, deleteByQuantityButton, findByQuantityButton, btnNewDatabase,
			createBackupButton, loadBackupButton;
	private DB myDB = new DB();
	private String[] columnNames = { "Name", "Quantity", "Price (roubles)", "Order date" };

	private Component buildGUI() {

		// ---------------------------------------------------
		// Table creation
		Container contentPane = this.getContentPane();
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));

		JTable tbl = new JTable(theAppModel);
		panel.add(new JScrollPane(tbl));
		contentPane.add(panel, "Center");

		btnNewDatabase = new JButton("New Database");
		btnNewDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select destination");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(btnNewDatabase) == JFileChooser.APPROVE_OPTION) {
					String folder = chooser.getSelectedFile().toString();
					File[] file = new File[] { new File(folder + "/db.csv"), new File(folder + "/hashmap.hm"),
							new File(folder + "/hashmapSecond.hm"), new File(folder + "/lastLineNumber.txt") };
					for (int i = 0; i < file.length; i++) {
						if (!file[i].exists())
							try {
								file[i].createNewFile();
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "File error");
								return;
							}
						else {
							JOptionPane.showMessageDialog(null, "File already exists");
							return;
						}
					}
					myDB.setFolder(folder);
					theAppModel.setDataVector(null, columnNames);
				}
			}
		});

		btnSaveDatabase = new JButton("Save Database");
		btnSaveDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select destination");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(btnSaveDatabase) == JFileChooser.APPROVE_OPTION) {
					String folder = chooser.getSelectedFile().toString();
					// TODO write
					// theAppModel.write(folder);
				}
			}
		});

		btnLoadDatabase = new JButton("Load Database");
		btnLoadDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle("Select a file");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				try {
					if (fileChooser.showOpenDialog(btnLoadDatabase) == JFileChooser.APPROVE_OPTION) {
						myDB.setFolder(fileChooser.getSelectedFile().toString());
						theAppModel.setDataVector(myDB.getData(), columnNames);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Can`t load this file");
				}
			}
		});

		btnDeleteDatabase = new JButton("Delete database");
		btnDeleteDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this database?",
						"Delete", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					File file = new File(myDB.getFolder());
					if (!file.exists()) {
						JOptionPane.showMessageDialog(null, "Can`t delete this file");
						return;
					}
					if (file.isDirectory()) {
						for (File f : file.listFiles())
							f.delete();
						file.delete();
					} else {
						file.delete();
					}
					JOptionPane.showMessageDialog(null, "Deletion successful");
				}
				if (reply == JOptionPane.NO_OPTION) {
				}
			}
		});

		findByNameButton = new JButton("Find by name");
		findByNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NameWindow nameWindow = new NameWindow();
				nameWindow.setVisible(true);
				// wait to cancel the window
				nameWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String name = nameWindow.getSelectedName();
						if (name != null) {
							try {
								if (myDB.get(name) == null)
									JOptionPane.showMessageDialog(null, "The name " + name + " doesn`t exit!");
								else {
									String[][] tmp = { myDB.get(name) };
									theAppModel.setDataVector(tmp, columnNames);
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Search error");
							}
						}

					}

				});
			}
		});

		findByQuantityButton = new JButton("Find by quantity");
		findByQuantityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuantityWindow quantityWindow = new QuantityWindow();
				quantityWindow.setVisible(true);
				// wait to cancel the window
				quantityWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						int quantity = quantityWindow.getSelectedQuantity();
						if (quantity != -1) {

						}
					}
				});
			}
		});

		deleteByNameButton = new JButton("Delete by name");
		deleteByNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NameWindow nameWindow = new NameWindow();
				nameWindow.setVisible(true);
				// wait to cancel the window
				nameWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String name = nameWindow.getSelectedName();
						if (name != null) {
							try {
								if (myDB.delete(name) == false)
									JOptionPane.showMessageDialog(null, "The name " + name + " doesn`t exit!");
								else
									theAppModel.setDataVector(myDB.getData(), columnNames);
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Delete error");
							}
						}

					}

				});
			}
		});

		deleteByQuantityButton = new JButton("Delete by quantity");
		deleteByQuantityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuantityWindow quantityWindow = new QuantityWindow();
				quantityWindow.setVisible(true);
				// wait to cancel the window
				quantityWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						int quantity = quantityWindow.getSelectedQuantity();
						if (quantity != -1) {

						}
					}
				});
			}
		});

		// ---------------------------------------------------
		// Menu section
		insertButton = new JButton("Insert");
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InsertWindow insertWindow = new InsertWindow();
				insertWindow.setVisible(true);
				// wait to cancel the window
				insertWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String[] newProduct = insertWindow.getNewProduct();
						if (!newProduct[0].equals("-1")) {
							try {
								if (myDB.add(newProduct) == false)
									JOptionPane.showMessageDialog(null,
											"The name " + newProduct[0] + " is already taken!");
								else
									theAppModel.setDataVector(myDB.getData(), columnNames);
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null, "Insert error");
							}
						}

					}
				});
			}
		});

		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tbl.getSelectedRow() != -1) {
					String key = tbl.getValueAt(tbl.getSelectedRow(), 0).toString();
					int index = theAppModel.getRowByKey(key);
					EditWindow editWindow = new EditWindow(theAppModel.getInfo(index));
					editWindow.setVisible(true);
					// wait to cancel the window
					editWindow.addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent e) {
							String[] newProduct = editWindow.getNewProduct();
							if (!newProduct[0].equals("-1")) {
								try {
									if (myDB.edit(key, newProduct) == false)
										JOptionPane.showMessageDialog(null, "The name " + key + " doesn`t exit!");
									else
										theAppModel.setDataVector(myDB.getData(), columnNames);
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "Edit error");
								}
							}

						}
					});
				}
			}
		});

		createBackupButton = new JButton("Create backup");

		loadBackupButton = new JButton("Load backup");

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0, 6, 1, 0));
		panel2.add(insertButton);
		panel2.add(editButton);
		panel2.add(deleteByNameButton);
		panel2.add(deleteByQuantityButton);
		panel2.add(findByNameButton);
		panel2.add(findByQuantityButton);

		JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayout(0, 2, 1, 0));
		panel5.add(createBackupButton);
		panel5.add(loadBackupButton);

		JPanel panel4 = new JPanel();
		contentPane.add(panel4, BorderLayout.SOUTH);
		panel4.setLayout(new GridLayout(2, 1));
		panel4.add(panel2);
		panel4.add(panel5);

		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(0, 4, 0, 0));
		panel3.add(btnNewDatabase);
		panel3.add(btnSaveDatabase);
		panel3.add(btnLoadDatabase);
		panel3.add(btnDeleteDatabase);
		contentPane.add(panel3, BorderLayout.NORTH);

		return null;
	}

	public GUI() {

		setTitle("Product database");
		setSize(900, 380);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int reply = JOptionPane.showConfirmDialog(null, "Save changes before you exit?", "Exit",
						JOptionPane.YES_NO_CANCEL_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					btnSaveDatabase.doClick();
					System.exit(0);
				}
				if (reply == JOptionPane.CANCEL_OPTION) {

				}
				if (reply == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
		});

		this.buildGUI();

	}

	public static void main(String[] args) {
		GUI theFrame = new GUI();
		theFrame.setVisible(true);
	}

}
