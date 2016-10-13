package backend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import product.Product;

public class ProductGenerator {

	public Product[] createProductArray(int size) {
		Product[] arr = new Product[size];
		String[] arr1 = createRowArray(size);
		Date[] arr2 = createDateArray(size);
		double[] arr3 = createDoubleArray(size);
		int[] arr4 = createNumericArray(size);
		for (int i = 0; i < size; i++)
			arr[i] = new Product(arr1[i], new GregorianCalendar(2016, 11, 20).getTime(), arr3[i], arr4[i]);
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
		for (int i = 0; i < arr.length; i++) {
			BigDecimal x = new BigDecimal(rnd.nextDouble() * 100000);
			x = x.setScale(4, BigDecimal.ROUND_HALF_UP);
			arr[i] = x.doubleValue();
		}
		return arr;
	}

	private String[] createRowArray(int size) {
		String symbols = "abcdefghijklmnopqrstuvwxyz";
		int count;
		String[] arr = new String[size];
		for (int i = 0; i < size; i++) {
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
