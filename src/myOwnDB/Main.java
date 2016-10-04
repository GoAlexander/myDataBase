package myOwnDB;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		DB myDB = new DB();
		myDB.setPath("./db.csv");
		myDB.setLastLineNumber(1);
		
		String [] str = {"Jolla", "2011", "Finland", "private", "50"};
		myDB.add(str);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		String [] str2 = {"Red Hat", "1993", "USA", "public", "4500"};
		myDB.add(str2);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		String [] str3 = myDB.get("Jolla");
		for ( int i = 0; i < str3.length; i++) {
			System.out.println(str3[i]);
		}
		
		System.out.println();
		
		myDB.add(str);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		myDB.add(str);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		myDB.add(str);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		String [] str4 = {"MS", "1975", "USA", "public", "114000"};
		myDB.add(str4);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		
		System.out.println();
		
		myDB.delete("MS");
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("Yeah! MS deleted from hashMap!!!");
		
		System.out.println();
		
		myDB.add(str4);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("Yeah! MS added again!");
		
		System.out.println();
		
		String [] str5 = {"Red Hat", "1993", "Russia", "public", "999999"};
		myDB.edit("Red Hat", str5);
		System.out.println(myDB.getLineNumber());
		System.out.println(myDB.getHashMapSize());
		System.out.println("Red Hat changed!");
		
		System.out.println();
		
	}
	


}
