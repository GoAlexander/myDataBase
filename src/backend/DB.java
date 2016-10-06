package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DB {

	private String path; // path to db.csv
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
		this.path = folder + "/db.csv";
		this.pathHashMap = folder + "/hashmap.hm";
		this.pathHashMapSecond = folder + "/hashmapSecond.hm";
		this.pathLastLineNumber = folder + "/lastLineNumber.txt";
	}

	public String getFolder() {
		return folder;
	}

	public String getPath() {
		return path;
	}

	public String getPathHashMap() {
		return pathHashMap;
	}

	public String getPathHashMapSecond() {
		return pathHashMapSecond;
	}

	public String getPathLastLineNumber() {
		return pathLastLineNumber;
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

	// TODO implement sync of hashmaps!
	// TODO test it!
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

		String line = new String();
		for (int i = 0; i < lineNumber; i++)
			line = lnr.readLine();
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

	// TODO implement sync of hashmaps!
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

	private void write(String path, Object object) throws IOException {
		File currentFile = new File(path);
		if (currentFile.exists())
			currentFile.delete();
		currentFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(object);
		oos.close();
		fos.close();
	}

	public void writeToFolder(String folder) throws IOException {
		// write db.csv
		File currentFile = new File(folder + "/db.csv");
		if (currentFile.exists())
			currentFile.delete();
		currentFile.createNewFile();
		FileInputStream is = new FileInputStream(new File(path));
		FileOutputStream os = new FileOutputStream(currentFile);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		is.close();
		os.close();

		// write hashmap.hm
		write(folder + "/hashmap.hm", hashmap);
		// write hashmapSecond.hm
		write(folder + "/hashmapSecond.hm", hashmapSecond);
		// write lastLineNumber.txt
		write(folder + "/lastLineNumber.txt", lastLineNumber);

		setFolder(folder);
	}

	public void openFromFolder(String folder) throws IOException, ClassNotFoundException {
		// open hashmap.hm
		FileInputStream fis = new FileInputStream(folder + "/hashmap.hm");
		ObjectInputStream ois = new ObjectInputStream(fis);
		hashmap = (HashMap) ois.readObject();
		ois.close();
		fis.close();
		// open hashmapSecond.hm
		fis = new FileInputStream(folder + "/hashmapSecond.hm");
		ois = new ObjectInputStream(fis);
		hashmapSecond = (HashMap) ois.readObject();
		ois.close();
		fis.close();
		// open lastLineNumber.txt
		fis = new FileInputStream(folder + "/lastLineNumber.txt");
		ois = new ObjectInputStream(fis);
		lastLineNumber = (Integer) ois.readObject();
		ois.close();
		fis.close();

		setFolder(folder);
	}

}
