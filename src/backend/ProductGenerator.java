package backend;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import product.Product;

public class ProductGenerator {

	public static void main(String[] args) throws Exception {

		int size = 100;
		ProductGenerator pg = new ProductGenerator();
		Product[] arr = pg.createProductArray(size);
		for (int i = 0; i < size; i++)
			System.out.println(arr[i].toString());
	}

	public Product[] createProductArray(int size) {
		Product[] arr = new Product[size];
		String[] arr1 = createRowArray(size);
		Date[] arr2 = createDateArray(size);
		double[] arr3 = createDoubleArray(size);
		int[] arr4 = createNumericArray(size);
		for (int i = 0; i < arr.length; i++)
			arr[i] = new Product(arr1[i], arr2[i], arr3[i], arr4[i]);
		return arr;
	}

	private int[] createNumericArray(int size) {
		Random rnd = new Random();
		int[] arr = new int[size];
		for (int i = 0; i < arr.length; i++)
			arr[i] = rnd.nextInt(100000);
		return arr;
	}

	private Date[] createDateArray(int size) {
		Random rnd = new Random();
		Date[] arr = new Date[size];
		for (int i = 0; i < arr.length; i++) {
			int year = rnd.nextInt(116) + 1900;
			int month = rnd.nextInt(11);
			int day = rnd.nextInt(26) + 1;
			arr[i] = new GregorianCalendar(year, month, day).getTime();
		}
		return arr;
	}

	private double[] createDoubleArray(int size) {
		Random rnd = new Random();
		double[] arr = new double[size];
		for (int i = 0; i < arr.length; i++)
			arr[i] = rnd.nextDouble() * 100000;
		return arr;
	}

	private String[] createRowArray(int size) {
		String symbols = "abcdefghijklmnopqrstuvwxyz";
		int count;
		String[] arr = new String[size];
		for (int i = 0; i < arr.length; i++) {
			StringBuilder randString = new StringBuilder();
			count = (int) (Math.random() * 10);
			for (int j = 0; j < count; j++)
				randString.append(symbols.charAt((int) (Math.random() * symbols.length())));
			if (randString.toString().isEmpty())
				i--;
			else
				arr[i] = randString.toString();
		}
		return arr;
	}
}
