package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DB {

	private String path;
	private final int columns = 4; // TODO ACHTUNG!!!

	Map<String, Integer> hashmap = new HashMap<String, Integer>();
	private int lastLineNumber = 1; // TODO I can calculate it!
	// http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	// or
	// http://stackoverflow.com/questions/1277880/how-can-i-get-the-count-of-line-in-a-file-in-an-efficient-way

	// --------------------------------------------------
	// sets
	// --------------------------------------------------

	public void setPath(String path) {
		this.path = path;
	}

	public void setLastLineNumber(int lastLineNumber) {
		this.lastLineNumber = lastLineNumber;
	}

	public void setHashMap(Map<String, Integer> hashmap) {
		this.hashmap = hashmap;
	}

	// TODO second HashMap

	// --------------------------------------------------
	// gets
	// --------------------------------------------------

	public int getLineNumber() {
		return lastLineNumber;
	}

	public int getHashMapSize() {
		return hashmap.size();
	}

	public Map<String, Integer> getHashMap() {
		return hashmap;
	}

	// --------------------------------------------------
	// operations with DB
	// --------------------------------------------------

	public String[] get(String key) throws Exception {
		int value = hashmap.get(key);

		return getInfo(value);
	}

	private String[] getInfo(int lineNumber) throws Exception {
		File file = new File(path);
		String[] object;

		BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		LineNumberReader lnr = new LineNumberReader(in);

		String line = lnr.readLine();
		in.close();
		lnr.close();

		object = line.split(";");
		return object;
	}

	public boolean add(String[] newStr) throws IOException {
		if (hashmap.containsKey(newStr[0])) // If this company already exists.
			return false;
		hashmap.put(newStr[0], lastLineNumber);
		lastLineNumber++;

		try (FileWriter fw = new FileWriter(path, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.print(String.join(";", newStr));
			out.println();
			return true;
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		return false;
	}

	public boolean delete(String key) {
		if (hashmap.containsKey(key)) {
			hashmap.remove(key);
			return true;
		}
		return false;
	}

	public boolean edit(String key, String[] newStr) throws IOException {
		if (hashmap.containsKey(key)) {
			delete(key);
			add(newStr);
			return true;
		}
		return false;
	}

	// --------------------------------------------------
	// get all file for jTable
	// --------------------------------------------------

	public Object[][] getData() throws Exception {
		Object[][] data = new Object[hashmap.size()][columns];
		int i = 0;
		for (int value : hashmap.values()) {
			data[i] = getInfo(value);
			i++;
		}
		return data;
	}

}
