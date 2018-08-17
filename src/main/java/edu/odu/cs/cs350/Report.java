package edu.odu.cs.cs350;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//Worker
public class Report
{
	private static final String FILENAME2 = "test.txt";
	private static final String FILENAME = "test.json";

    /**
     * @throws IOException
     *
     */
	public static boolean cleanJSON() throws IOException{
		File file = new File(FILENAME);
		FileWriter writer = new FileWriter(file, false);
        writer.close();
        return true;
	}
	public static boolean cleanText() throws IOException{
		File file = new File(FILENAME2);
		FileWriter writer = new FileWriter(file, false);
        writer.close();
        return true;
	}
	public static void printJSON(JSONObject obj) throws IOException{
		BufferedWriter bw = null;
		FileWriter fw = null;

		//try {

			//String data = "test";
			File file = new File(FILENAME);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String json = gson.toJson(obj);

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(json);
			bw.newLine();
		//} catch (IOException e) {
			//e.printStackTrace();
		//} finally {
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		//}
	}


    /**
     * @throws IOException
     *
     */
	public static void printText(String pageNames, double imgSize, double imgTotalSize,boolean check) throws IOException{
		BufferedWriter bw = null;
		FileWriter fw = null;

		//try {
		String size = String.format("%.2f",imgSize);
		String totalSize = String.format("%.2f",imgTotalSize);
			String data = size + " " + pageNames;

			File file = new File(FILENAME2);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(data);
			bw.newLine();
			if (check==true)
				bw.write(totalSize);
		//} catch (IOException e) {
			//e.printStackTrace();
		//}
			//finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	//}
	public static void printFilenames() {
		System.out.println("JSON Filename: " + FILENAME);
		System.out.println("Text Filename: " + FILENAME2);
	}
}
