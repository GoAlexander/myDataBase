package product;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
	private String name;
	private Integer quantity;
	private Double price;
	private Date date;
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-y");

	public Product(String name, Date date, double price, int quantity) {
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.date = date;
	}

	public String[] getInfo() {
		return new String[] { name, dateFormat.format(date), new DecimalFormat("#0.0000").format(price), quantity.toString() };
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return name + ";" + dateFormat.format(date) + ";" + new DecimalFormat("#0.0000").format(price) + ";" + quantity.toString();
	}

}
