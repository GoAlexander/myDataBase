package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import backend.DB;
import backend.ExcelWorker;

import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;

public class GUI extends JFrame {

	private GUIModel theAppModel = new GUIModel();
	private static final long serialVersionUID = 1L;
	private JButton insertButton, deleteByNameButton, findByNameButton, btnSaveDatabase, btnLoadDatabase,
			btnDeleteDatabase, editButton, deleteByDateButton, findByDateButton, btnNewDatabase, createBackupButton,
			loadBackupButton, createExcelButton;
	private DB myDB = new DB();
	private final int columns = 4;
	private String[] columnNames = { "Name", "Order date", "Price (roubles)", "Quantity" };

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
					if (myDB.getFolder() != null) {
						try {
							myDB.writeToFolder(myDB.getFolder());
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "Save error");
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
					try {
						myDB.writeToFolder(folder);
						return;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can`t save this database");
						return;
					}
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
						if (myDB.getFolder() != null) {
							try {
								myDB.writeToFolder(myDB.getFolder());
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "Save error");
								return;
							}
						}
						myDB.openFromFolder(fileChooser.getSelectedFile().toString());
						theAppModel.setDataVector(myDB.getData(), columnNames);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Can`t load this database");
				}
			}
		});

		btnDeleteDatabase = new JButton("Delete database");
		btnDeleteDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this database?",
						"Delete", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					try {
						File file = new File(myDB.getFolder());
						if (!file.exists()) {
							JOptionPane.showMessageDialog(null, "Can`t delete this database");
							return;
						}
						if (file.isDirectory()) {
							for (File f : file.listFiles())
								f.delete();
							file.delete();
						} else {
							file.delete();
						}
						theAppModel.setRowCount(0);
						JOptionPane.showMessageDialog(null, "Deletion successful");
						myDB.setFolder(null);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can`t delete this database");
					}
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

		findByDateButton = new JButton("Find by date");
		findByDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateWindow dateWindow = new DateWindow();
				dateWindow.setVisible(true);
				// wait to cancel the window
				dateWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String date = dateWindow.getSelectedDate();
						if (date != null) {
							try {
								ArrayList<String[]> data = myDB.getBySecondField(date);
								if (data == null)
									JOptionPane.showMessageDialog(null, "The date " + date + " doesn`t exit!");
								else {
									String[][] tmp = new String[data.size()][columns];
									for (int i = 0; i < data.size(); i++)
										tmp[i] = data.get(i);
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

		deleteByDateButton = new JButton("Delete by date");
		deleteByDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateWindow dateWindow = new DateWindow();
				dateWindow.setVisible(true);
				// wait to cancel the window
				dateWindow.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						String date = dateWindow.getSelectedDate();
						if (date != null) {
							try {
								if (myDB.deleteBySecondField(date) == false)
									JOptionPane.showMessageDialog(null, "The date " + date + " doesn`t exit!");
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
								if (!myDB.add(newProduct))
									JOptionPane.showMessageDialog(null,
											"The name " + newProduct[0] + " already exists!");
								else {
									System.out.println(myDB.getHashMap());
									System.out.println(myDB.getHashMapSecond());
									theAppModel.setDataVector(myDB.getData(), columnNames);
								}
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
										JOptionPane.showMessageDialog(null,
												"The name " + newProduct[0] + " already exists!");
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
		createBackupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select destination");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(createBackupButton) == JFileChooser.APPROVE_OPTION) {
					String folder = chooser.getSelectedFile().toString();
					try {
						myDB.zip(new File(myDB.getFolder()).toPath(), new File(folder + "/db.backup").toPath());
						return;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can`t create backup for this database");
						return;
					}
				}
			}
		});

		loadBackupButton = new JButton("Load backup");
		loadBackupButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new java.io.File("."));
				fileChooser.setDialogTitle("Select a backup");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				try {
					if (fileChooser.showOpenDialog(loadBackupButton) == JFileChooser.APPROVE_OPTION) {
						if (myDB.getFolder() != null) {
							try {
								myDB.writeToFolder(myDB.getFolder());
							} catch (IOException e1) {
								JOptionPane.showMessageDialog(null, "Save error");
								return;
							}
						}
						myDB.unzip("./db_tmp", fileChooser.getSelectedFile().toString());
						myDB.openFromFolder("./db_tmp");
						theAppModel.setDataVector(myDB.getData(), columnNames);

					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Can`t load this backup");
				}
			}
		});

		createExcelButton = new JButton("Import to .xls");
		createExcelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select destination");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showSaveDialog(createExcelButton) == JFileChooser.APPROVE_OPTION) {
					String folder = chooser.getSelectedFile().toString();
					try {
						ExcelWorker tmp = new ExcelWorker();
						tmp.importToExcel(folder, myDB.getData());
						return;
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Can`t create .xls for this database");
						return;
					}
				}
			}
		});

		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(0, 6, 1, 0));
		panel2.add(insertButton);
		panel2.add(editButton);
		panel2.add(deleteByNameButton);
		panel2.add(deleteByDateButton);
		panel2.add(findByNameButton);
		panel2.add(findByDateButton);

		JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayout(0, 3, 1, 0));
		panel5.add(createBackupButton);
		panel5.add(loadBackupButton);
		panel5.add(createExcelButton);

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
					try {
						myDB.writeToFolder(myDB.getFolder());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "Can`t save this database");
					}
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
