import java.io.*;
import java.util.*;

public class Huff {

	static List<Integer> keys;
	static List<Integer> freq;
	static List<Integer> values = new ArrayList<Integer>();

	static String[] coding = new String[256];
	static TreeNode huffman;
	static PrintWriter writer = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Read characters from the uncompressed file and store in 'values' arrayList
		int inbits;
		BitInputStream bits = null;
		try {
			bits = new BitInputStream(new FileInputStream("data.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((inbits = bits.readBits(8)) != -1) {
				//System.out.println(inbits); // put writes one character
				values.add(inbits);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Assign keys to frequencies using the values arrayList
		keys = new ArrayList<Integer>();
		freq = new ArrayList<Integer>();
		for (int i = 0; i < values.size(); i++) {
			boolean exists = false;
			for (int j = 0; j < keys.size(); j++) {
				if (values.get(i) == keys.get(j)) {
					exists = true;

					freq.set(j, freq.get(j) + 1);

					break;
				}
			}

			if (!exists) {
				keys.add(keys.size(), values.get(i));

				freq.add(keys.size() - 1, 1);
			}
		}

		// Sort lists by frequency
		for (int i = 0; i < keys.size(); i++) {
			for (int j = i + 1; j < keys.size(); j++) {
				if (freq.get(j) < freq.get(i)) {
					int tmp = keys.get(i);
					int tmp2 = freq.get(i);
					keys.set(i, keys.get(j));
					freq.set(i, freq.get(j));
					keys.set(j, tmp);
					freq.set(j, tmp2);
				}

			}
		}

		huffman = null;

		huffman = huffTree(huffman, 0);
		encodeTable(coding, huffman, "");

		writeEncoding(coding, values);
		
		//Create a PrintWriter object to be used later on.
		try {
			writer = new PrintWriter("decompressed.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		readInput();
		writer.close();
		
		System.out.println("Values:" + values.size());

	}

	public static TreeNode huffTree(TreeNode aNode, int index) {
		if (index == keys.size())
			return aNode;

		if (aNode == null) {
			aNode = new TreeNode(keys.get(index), freq.get(index), null, null);
		}

		else {
			aNode = new TreeNode(aNode.myValue + keys.get(index),
					aNode.myWeight + freq.get(index), new TreeNode(
							keys.get(index), freq.get(index), null, null),
					aNode);
		}

		aNode = huffTree(aNode, index + 1);

		return aNode;
	}

	public static void encodeTable(String[] coding, TreeNode aNode, String s) {

		if (!aNode.isLeaf()) {
			encodeTable(coding, aNode.myLeft, s + '0');
			encodeTable(coding, aNode.myRight, s + '1');
		} else if (aNode.isLeaf()) {
			coding[aNode.myValue] = s;
		}
	}

	public static void writeEncoding(String[] coding, List<Integer> values) {
		BitOutputStream bit = null;
		bit = new BitOutputStream("encodedData.txt");

		for (int i = 0; i < values.size(); i++) {

			String s = coding[values.get(i)];

			for (int j = 0; j < s.length(); j++) {
				if (s.charAt(j) == '0') {

					try {
						bit.write((int) s.charAt(j));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (s.charAt(j) == '1') {
					try {
						bit.write((int) s.charAt(j));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			try {
				bit.write(' ');
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		bit.close();
	}

	public static void getValue(String s) {
		for (int i = 0; i < coding.length; i++) {

			if (s.equals(coding[i])) {
				String a = (Character.toString((char) i));
				writer.print(a);
				//System.out.println(a);

				return;
			}
		}
	}

	public static void readInput() {
		FileReader file = null;
		try {
			file = new FileReader(new File(("encodedData.txt")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader ex = new BufferedReader(file);
		try {
			char tmp = '*';
			String word = "";

			do {
				int tmp1 = ex.read();

				if (tmp1 == -1) {
					//System.out.println(word);
					getValue(word);
					word = "";
					break;
				} else {
					tmp = (char) (tmp1);
				}
				// System.out.println(tmp);
				if (tmp == ' ') {
					//System.out.println(word);
					getValue(word);
					word = "";

				} else if (tmp == -1) {
					//System.out.println(word);
					getValue(word);
					word = "";
					break;
				} else {
					word += tmp;
				}
			} while (tmp != -1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
