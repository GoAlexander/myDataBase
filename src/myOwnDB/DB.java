package myOwnDB;

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

class DB {
	// open DB -> GUI (вызвать метод setPath(String))
	// create DB -> GUI (просто создать новый файл и протолкнуть в этот класс
	// path)
	// delete DB -> GUI (удалить файл и поставить path = null;)
	// save DB

	// add new element (check unique key)
	// delete (by key or 1 value)
	// search by key or 1 value
	// edit ->
	// backup of file !!! -> GUI (copy current file and change name of new file
	// and ...)
	// restoring from backup file
	// export in *.xlsx - > GUI (throws jTable)
	// + timers to analyze

	// use HashMap or array or unique elements

	private String path;
	
	Map<String, Integer> hashmap = new HashMap<String, Integer>();
	private int lastLineNumber = 1; //TODO I can calculate it! http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
								//or http://stackoverflow.com/questions/1277880/how-can-i-get-the-count-of-line-in-a-file-in-an-efficient-way
	

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setLastLineNumber(int lastLineNumber) {
		this.lastLineNumber = lastLineNumber;
	}
	
	public void setHashMap(Map<String, Integer> hashmap) { //TODO ok?
		this.hashmap = hashmap;
	}
	
	//TODO second HashMap
	
	//--------------------------------------------------

	public int getLineNumber() {
		return lastLineNumber;
	}
	
	public int getHashMapSize() {
		return hashmap.size();
	}
	
	public Map<String, Integer> getHashMap() {
		return hashmap;
	}
	
	//--------------------------------------------------
	
	public String[] get(String key) throws Exception {
		int value = hashmap.get(key);
		
		return getInfo(value);
	}
	
	private String[] getInfo(int lineNumber) throws Exception {
		File file = new File(path);
		String[] object;

		BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
		LineNumberReader lnr =  new LineNumberReader(in);
		
		String line = lnr.readLine();
		in.close();
		lnr.close();
		
		object = line.split(";");
		return object;
	}
	
	
	public void add(String[] newStr) throws IOException {
		if (hashmap.containsKey(newStr[0])) // If this company already exists.
			return;
		
		hashmap.put(newStr[0], lastLineNumber);
		lastLineNumber++;

		try (FileWriter fw = new FileWriter(path, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
				out.print(String.join(";", newStr));
				out.println(); 
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}		
	}
	
	public void delete(String key) {
		hashmap.remove(key);
	}
	
	public void edit(String key, String[] newStr) throws IOException {
		delete(key);
		add(newStr);
	}
	
	

}
