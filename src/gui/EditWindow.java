package gui;

public class EditWindow extends InsertWindow {

	private static final long serialVersionUID = 1L;

	public EditWindow(String[] info) {
		super();
		lblAddNewProduct.setText("Edit this product:");
		tfName.setEditable(false);
		tfName.setText(info[0]);
		tfQuantity.setText(info[1]);
		tfPrice.setText(info[2]);
		tfDate.setText(info[3]);
	}

}
