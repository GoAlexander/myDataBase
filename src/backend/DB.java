package backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class DB {

	private String path; // path to db.csv
	private String folder;
	private String pathHashMap;
	private String pathHashMapSecond;
	private String pathLastLineNumber;

	long timeStart;
	long timeStop;

	private final int columns = 4; // TODO ACHTUNG!!!
	
	// name, date, price, quantity lengths
	private final int[] fieldsLength = {20, 10, 10, 7}; // TODO ACHTUNG!!!

	Map<String, Integer> hashmap = new HashMap<String, Integer>();
	Map<String, ArrayList<Integer>> hashmapSecond = new HashMap<String, ArrayList<Integer>>();

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

	public void setHashMapSecond(Map<String, ArrayList<Integer>> hashmapSecond) {
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
	
	public Map<String, ArrayList<Integer>> getHashMapSecond() {
		return hashmapSecond;
	}

	// --------------------------------------------------
	// operations with DB
	// --------------------------------------------------

	public String[][] getBySecondField(String key) throws Exception {
		ArrayList<Integer> values = hashmapSecond.get(key);

		String[][] data = new String[hashmapSecond.size()][columns];
		for (int i = 0; i < values.size(); i++) {
			data[i] = getInfo(values.get(i));
		}

		return data;
	}

	public String[] get(String key) throws Exception {
		return getInfo( hashmap.get(key) );
	}

	private String[] getInfo(int lineNumber) throws Exception {
		File file = new File(path);
		String[] object;
		
		RandomAccessFile raf = new RandomAccessFile(file.getAbsoluteFile(), "r");
		
		if ((lineNumber-1) == 0) { //TODO Highly important! Test with first, middle and end lines!!!
			raf.seek( 0 );
		}
		else {
			raf.seek( (50*(lineNumber-1)) + (lineNumber-1) );
		}
		
		String line = raf.readLine();
		raf.close();

		line = line.replace("_","");
		object = line.split(";");
		return object;
	}

	public boolean add(String[] newStr) throws IOException {
		if (hashmap.containsKey(newStr[0])) // If this company already exists.
			return false;

		hashmap.put(newStr[0], lastLineNumber); // put in 1st hashMap
		
		// put in 2nd hashMap
		if (hashmapSecond.containsKey(newStr[1])) { // если такой лист уже инициализировался
			hashmapSecond.get(newStr[1]).add(lastLineNumber);	
		}
		else {
			ArrayList <Integer> value = new ArrayList <Integer>();
			value.add(lastLineNumber);
			hashmapSecond.put(newStr[1], value);
		}		
		
		lastLineNumber++; // "pointer" on last line
		
		// Occupy space with "_"
		for (int i = 0; i < newStr.length; i++) {
			newStr[i] = occupySpace(newStr[i], fieldsLength[i]);
		}

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

	public boolean delete(String key) throws Exception {
		if (hashmap.containsKey(key)) {
			Integer lineNumber = hashmap.get(key);

			String[] line = get(key);
			
			// по номеру строки! (не по индексу)
			hashmapSecond.get(line[1]).remove(lineNumber);	// delete basket from 2nd hashmap
			
			if(hashmapSecond.get(line[1]).isEmpty()) { // delete mapping if ArrayList is empty
				hashmapSecond.remove(line[1]);
			}
			
			hashmap.remove(key); // now delete basket from 1st hashmap
				
			return true;
		}
		return false;
	}
	
	private String occupySpace(String newStr, int length) { //TODO Rewrite? Make more abstract? 
		while (newStr.length() != length) {
			newStr += "_";
		}
		return newStr;
	}

	public boolean deleteBySecondField(String key) {
		if (hashmapSecond.containsKey(key)) {
			
			ArrayList <Integer> keys = hashmapSecond.get(key);
			for (int i = 0; i < keys.size(); i++) { //TODO ACTUNG! Сделать быстрее?
				//hashmap.values().removeAll(Collections.singleton(keys));
				
				hashmap.values().remove(keys.get(i));
			}
			
			hashmapSecond.remove(key);
			return true;
		}
		return false;
	}

	public boolean edit(String key, String[] newStr) throws Exception {
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
	
	// --------------------------------------------------
	// Write + open + backup
	// --------------------------------------------------

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

	public void zip(Path folder, Path zipFilePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	public void unzip(String destinationFolder, String zipFile) throws IOException {
		File directory = new File(destinationFolder);
		if (!directory.exists())
			directory.mkdirs();

		byte[] buffer = new byte[2048];

		FileInputStream fInput = new FileInputStream(zipFile);
		ZipInputStream zipInput = new ZipInputStream(fInput);
		ZipEntry entry = zipInput.getNextEntry();

		while (entry != null) {
			String entryName = entry.getName();
			File file = new File(destinationFolder + File.separator + entryName);
			System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

			// create the directories of the zip directory
			if (entry.isDirectory()) {
				File newDir = new File(file.getAbsolutePath());
				if (!newDir.exists()) {
					boolean success = newDir.mkdirs();
					if (success == false) {
						System.out.println("Problem creating Folder");
					}
				}
			} else {
				FileOutputStream fOutput = new FileOutputStream(file);
				int count = 0;
				while ((count = zipInput.read(buffer)) > 0) {
					// write 'count' bytes to the file output stream
					fOutput.write(buffer, 0, count);
				}
				fOutput.close();
			}
			zipInput.closeEntry();
			entry = zipInput.getNextEntry();
		}

		// close the last ZipEntry
		zipInput.closeEntry();
		zipInput.close();
		fInput.close();

	}
	
}
