package backend;

import java.util.GregorianCalendar;

import product.Product;

public class Main {

	public static void main(String[] args) throws Exception {

		DB myDB = new DB();
		myDB.setPath("./db.csv");
		myDB.setLastLineNumber(1);

		Product one = new Product("iPhone 5S", 12, 52989, new GregorianCalendar(2016, 12, 31).getTime());
		myDB.add(one.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		Product two = new Product("iPhone 6S", 7, 39539.89, new GregorianCalendar(2016, 04, 24).getTime());
		myDB.add(two.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		String[] str3 = myDB.get("iPhone 6S");
		for (int i = 0; i < str3.length; i++) {
			System.out.println(str3[i]);
		}

		System.out.println();

		myDB.add(one.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		myDB.add(one.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		myDB.add(one.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		Product three = new Product("Samsung S7", 2, 49990.79, new GregorianCalendar(2014, 6, 28).getTime());
		myDB.add(three.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());

		System.out.println();

		myDB.delete("Samsung S7");
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("Yeah! Samsung S7 deleted from hashMap!!!");

		System.out.println();

		myDB.add(three.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("Yeah! Samsung S7 added again!");

		System.out.println();

		Product four = new Product("iPhone 6S", 999, 999999, new GregorianCalendar(2016, 04, 24).getTime());
		myDB.edit("iPhone 6S", four.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("iPhone 6S changed!");

		System.out.println();
		
		Product five = new Product("Phone1", 12345, 999999, new GregorianCalendar(2016, 04, 24).getTime());
		myDB.add(five.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println(myDB.getHashMapSecondSize());

		System.out.println();
		
		Product six = new Product("Phone2", 12345, 999999, new GregorianCalendar(2016, 04, 24).getTime());
		myDB.add(six.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println(myDB.getHashMapSecondSize());

		System.out.println();
		
		Product seven = new Product("Phone3", 12345, 999999, new GregorianCalendar(2016, 04, 24).getTime());
		myDB.add(seven.getInfo());
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println(myDB.getHashMapSecondSize());

		System.out.println();
		
		
		myDB.delete(12345);
		System.out.println("Not implemented yet!");
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println(myDB.getHashMapSecondSize());

		System.out.println();
		

	}

}
