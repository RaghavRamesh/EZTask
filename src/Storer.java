//@author Rajalakshmi Ramachandran A0088634U

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Storer {

	public static void writeFile(String[][] array, String fileName) {
		try {
			BufferedWriter buffer = new BufferedWriter(new FileWriter(fileName));
			int i = 0, j = 0;
			int max = 0;
			if (fileName == "calendarList.txt")
				max = 5;
			else if (fileName == "priorityList.txt")
				max = 3;
			for (i = 0; i < array.length; i++)
				for (j = 0; j < max; j++) {
					buffer.write(array[i][j]);
					buffer.write("\n");
				}
			buffer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String[][] readFile(String fileName) {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(fileName));
			int max = 0, i = 0, counter = 0, j = 0, k = 0;

			if (fileName == "calendarList.txt")
				max = 5;
			else if (fileName == "priorityList.txt")
				max = 3;
			String[] array = new String[max];
			array[i] = buffer.readLine();
			while (array[i] != null) {
				i++;
				if (i == max) {
					i = 0;
					counter++;
				}
				array[i] = buffer.readLine();
			}
			String[][] arrayToBePassed = new String[counter][max];
			i = 0;
			buffer.close();
			BufferedReader buffer2 = new BufferedReader(
					new FileReader(fileName));
			array[i] = buffer2.readLine();
			while (array[i] != null) {
				arrayToBePassed[j][i] = array[i];
				i++;
				if (i == max) {
					i = 0;
					j++;
				}
				array[i] = buffer2.readLine();
			}
			buffer2.close();
			return arrayToBePassed;
		} catch (IOException e) {
			System.out.println("File could not be read!");
		}
		return null;

	}

}
