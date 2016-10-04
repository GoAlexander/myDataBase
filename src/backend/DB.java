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

	private String path; //path to db.csv
	private String folder;
	private String pathHashMap;
	private String pathHashMapSecond;
	private String pathLastLineNumber;

	long timeStart;
	long timeStop;

	private final int columns = 4; // TODO ACHTUNG!!!

	Map<String, Integer> hashmap = new HashMap<String, Integer>();
	Map<Integer, Integer[]> hashmapSecond = new HashMap<Integer, Integer[]>();

	private int lastLineNumber = 1; // I can calculate it:
	// http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	// http://stackoverflow.com/questions/1277880/how-can-i-get-the-count-of-line-in-a-file-in-an-efficient-way

	// --------------------------------------------------
	// sets
	// --------------------------------------------------
	
	public void setFolder(String folder) {
		this.folder = folder;
		this.path = folder + "/db.csv"; //TODO ACHTUNG! Check ending of folder path! About: ./folder/ or ./folder !!!  
		this.pathHashMap = folder + "/hashmap.hm";
		this.pathHashMapSecond = folder + "/hashmapSecond.hm";
		this.pathLastLineNumber = folder + "/lastLineNumber.txt";
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setLastLineNumber(int lastLineNumber) {
		this.lastLineNumber = lastLineNumber;
	}

	public void setHashMap(Map<String, Integer> hashmap) {
		this.hashmap = hashmap;
	}

	public void setHashMapSecond(Map<Integer, Integer[]> hashmapSecond) {
		this.hashmapSecond = hashmapSecond;
	}

	// --------------------------------------------------
	// gets
	// --------------------------------------------------

	public int getLineNumber() {
		return lastLineNumber;
	}

	public int getHashMapSize() {
		return hashmap.size();
	}
	
	public int getHashMapSecondSize() {
		return hashmapSecond.size();
	}

	public Map<String, Integer> getHashMap() {
		return hashmap;
	}

	// --------------------------------------------------
	// operations with DB
	// --------------------------------------------------

	//TODO implement sync of hashmaps!
	//TODO test it!
	public String[][] get(Integer key) throws Exception {
		Integer[] values = hashmapSecond.get(key);
		
		String[][] data = new String[hashmapSecond.size()][columns];
		for (int i = 0; i < values.length; i++) {
			data[i] = getInfo(values[i]);
		}
		
		return data;
	}
	
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
	
	//TODO implement sync of hashmaps!
	//TODO test it!
	public boolean delete(Integer key) {
		if (hashmapSecond.containsKey(key)) {
			hashmapSecond.remove(key);
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

	public String[][] getData() throws Exception {
		String[][] data = new String[hashmap.size()][columns];
		int i = 0;
		for (int value : hashmap.values()) {
			data[i] = getInfo(value);
			i++;
		}
		return data;
	}
	
	
	// --------------------------------------------------
	// timers
	// --------------------------------------------------
	public void timerStart() {
		timeStart = System.currentTimeMillis();
	}
	
	public long timerStop() {
		timeStop = System.currentTimeMillis();
		return timeStop - timeStart;
	}

}
