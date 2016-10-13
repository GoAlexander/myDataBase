package backend;

import java.io.IOException;
import java.util.GregorianCalendar;

import product.Product;

public class Timer {

	private static long ariphmetic_Average(long[] time_results, int number_of_tries) {
		int count = 0;
		long result = 0;
		for (int i = 0; i < number_of_tries; i++) {
			result = time_results[i] + result;
			++count;
		}
		if (count > 0)
			result = result / count;
		return result;
	}

	public static void main(String[] args) {

		int size = 12000;
		long[] time_results = new long[size];
		ProductGenerator pg = new ProductGenerator();
		DB myDB = new DB();
		myDB.setPath("./db.csv");
		myDB.setLastLineNumber(1);

		Product[] arr = pg.createProductArray(size);
		for (int i = 0; i < size; i++)
			System.out.println(arr[i].toString());

		/*
		 * long[] time_results = new long[size]; for (int tries = 0; tries <
		 * size; tries++) { myDB.timerStart(); try {
		 * myDB.add(arr[tries].getInfo()); } catch (IOException e) {
		 * e.printStackTrace(); } time_results[tries] = myDB.timerStop(); } long
		 * add_result = ariphmetic_Average(time_results, size);
		 */

		long add_result = 0;
		myDB.timerStart();
		for (int tries = 0; tries < size; tries++) {
			try {
				myDB.add(arr[tries].getInfo());
			} catch (IOException e) {
				e.printStackTrace();
			}
			add_result = myDB.timerStop();
		}

		System.out.println();
		System.out.println("Size of database is " + size);
		// System.out.println("Addition result (one operation): " + add_result +
		// "ms");
		System.out.println("Addition result (" + size + " operations): " + add_result + "ms");

		Product one = new Product("iPhone 5S", new GregorianCalendar(2014, 11, 28).getTime(), 12.0000, 123);
		try {
			myDB.add(one.getInfo());
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int tries = 0; tries < size; tries++) {
			myDB.timerStart();
			try {
				myDB.get("iPhone 5S");
			} catch (Exception e) {
				e.printStackTrace();
			}
			time_results[tries] = myDB.timerStop();
		}
		long find_result = ariphmetic_Average(time_results, size);

		for (int tries = 0; tries < size; tries++) {
			myDB.timerStart();
			try {
				myDB.oldGet("iPhone 5S");
			} catch (Exception e) {
				e.printStackTrace();
			}
			time_results[tries] = myDB.timerStop();
		}
		long find_old_result = ariphmetic_Average(time_results, size);


		System.out.println("Find by name result: " + find_result + "ms");
		System.out.println("Find by name old result (without seek): " + find_old_result + "ms");

		myDB.timerStart();
		try {
			myDB.delete("iPhone 5S");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long delete_result = myDB.timerStop();

		System.out.println("Delete by name result: " + delete_result + "ms");

		Product two = new Product("iPhone 6S", new GregorianCalendar(2016, 11, 20).getTime(), 134.0000, 156);
		Product three = new Product("iPhone 7S", new GregorianCalendar(2016, 11, 20).getTime(), 134.0000, 156);
		try {
			myDB.add(three.getInfo());
			myDB.add(two.getInfo());
			myDB.add(one.getInfo());
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int tries = 0; tries < size; tries++) {
			myDB.timerStart();
			try {
				myDB.getBySecondField("20-12-2016");
			} catch (Exception e) {
				e.printStackTrace();
			}
			time_results[tries] = myDB.timerStop();
		}
		long find_result2 = ariphmetic_Average(time_results, size);

		System.out.println("Find by date result (2 elems): " + find_result2 + "ms");

		myDB.timerStart();
		try {
			myDB.deleteBySecondField("20-12-2016");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long delete_result2 = myDB.timerStop();

		System.out.println("Delete by date result (2 elems): " + delete_result2 + "ms");

	}

}
