package com.vizrt.interview;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment {

	public static void main(String[] args) throws IOException {

		 String fileName = args[0];

		StringBuilder sb = new StringBuilder();

		// Using InputStreamReader to parse total file char by char
		InputStreamReader io = new InputStreamReader(new FileInputStream(fileName));
		while (true) {
			int in = io.read();
			if (in == -1)
				break;
			if (!((in == 10) || (in == 13))) // Finding /n , /r and canceling
												// them out of main string
				sb.append(((char) in));
		}
		String str = sb.toString(); // Getting the main Bigger String to parse

		/*
		 * This part is for Plugins those worked
		 */

		// Here, getting Regex and comparing with total string to find out
		// occurrence count of Load ok Init ok.

		Pattern p = Pattern.compile(getWorkedReg(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(str);
		int count = 0;
		while (m.find())
			count++;
		System.out.println("Number Of Plugins Loaded Correctly: " + count);

		/*
		 * This part is for Plugins those did not work
		 */

		// Here getting Regex and comparing with total string to find out
		// occurrence count of Error .

		p = Pattern.compile(getErrorRegex(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		m = p.matcher(str);
		sb = new StringBuilder(); // StringBuilder, so that if no ERROR, a
									// message could be shown, after looping.

		while (m.find()) {

			String name = getVipName(m.group(3));// group(3) holds the full path
													// of that .vip file in
													// which error happened
			// MatroxFileWriter.vip -check,whether to output or ignore.
			if (name.trim().equals("MatroxFileWriter.vip")) {
				if (IfMatches(str, "Hardware: System   Video")) {
					// Video or VGA -check
					if (!(IfMatches(str, "Matrox DSX.utils version is empty."))) {
						// Matrox DSX.utils installed or not -check
						sb.append(("  " + name + " " + m.group(4).trim().replaceAll(" +", " ")).trim() + "\n");
					}
				}
			} else {
				// if not MatroxFileWriter.vip then normal output
				sb.append(("  " + name + " " + m.group(4).trim().replaceAll(" +", " ") + "\n").trim() + "\n");
			}

		}

		
		if (sb.toString().length() != 0){
			System.out.println("ERROR MESSAGE:\n" + sb.toString());
			}
		else
			System.out.println("No ERROR's found");

	}

	/**
	 * 
	 * @param string
	 *            This is the target string
	 * @param subString
	 *            This is that string, that need's to be matched.
	 * @return boolean If matches, returns true, if false.
	 */
	private static boolean IfMatches(String string, String subString) {
		Pattern p;
		Matcher m;
		p = Pattern.compile(subString, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
		m = p.matcher(string);
		if (m.find()) {
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param string
	 *            This is the target string
	 * @return String If matches, returns the file.vip name
	 */
	private static String getVipName(String string) {

		String reg = "((\\d*)(\\_*)(?:[a-z][a-z]+)((\\_*)(\\d*)(?:[a-z][a-z]+)*)*)(\\.)(vip)";
		Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher m = p.matcher(string);
		if (m.find())
			return m.group(0);
		else
			return "";
	}

	/**
	 * 
	 * @return String
	 * 			that holds a regex to match ERROR Message part.
	 */
	private static String getErrorRegex() {
		String re1 = "(ERROR: (\\w+)? ('[^']+'))";
		String re2 = "(.+?(?=(?:C:)))";
		return re1 + re2;
	}

	/**
	 * 
	 * This method is to match those plugins who are working perfectly known by
	 * "Load ok. Init ok".
	 *
	 * @return string that match regex of
	 *         "\\Vizrt\\Viz3\\Plugin\\fileName.vip: Load ok. Init ok."
	 */
	private static String getWorkedReg() {

		String re2 = "\\\\"; // Uninteresting: c
		String re3 = ".*?"; // Non-greedy match on filler
		String re4 = "(\\\\)"; // Any Single Character 1
		String re5 = "(Vizrt)"; // Word 1
		String re6 = "(\\\\)"; // Any Single Character 2
		String re7 = "(Viz3)"; // Alphanum 1
		String re8 = "(\\\\)"; // Any Single Character 3
		String re9 = "(Plugin)"; // Word 2
		String re10 = "(\\\\)"; // Any Single Character 4
		String re11 = "((\\d*)(\\_*)(?:[a-z][a-z]+)((\\_*)(\\d*)(?:[a-z][a-z]+)*)*)"; // Word
																						// 3
		String re12 = "(\\.)"; // Any Single Character 5
		String re13 = "(vip)"; // Word 4
		String re14 = "(:)"; // Any Single Character 6
		String re15 = "(\\s+)"; // White Space 1
		String re16 = "(Load)"; // Word 3
		String re17 = "( )"; // White Space 2
		String re18 = "(ok)"; // Word 4
		String re19 = "(\\.)"; // Any Single Character 5
		String re20 = "(\\s+)"; // White Space 3
		String re21 = "(Init)"; // Word 5
		String re22 = "(\\s+)"; // White Space 4
		String re23 = "(ok)"; // Word 6
		String re24 = "(\\.)"; // Any Single Character 6
		return re2 + re3 + re4 + re5 + re6 + re7 + re8 + re9 + re10 + re11 + re12 + re13 + re14 + re15 + re16 + re17
				+ re18 + re19 + re20 + re21 + re22 + re23 + re24;
	}


}
