import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Parser {
	public Parser() {

	}

	public int parse(String fileName, short[] instructionMemory,HashMap<Short,String> binaryDictionary ) {
		try {
			File myFile = new File(fileName);
			Scanner myReader = new Scanner(myFile);
			int numberOfInstructions = 0;

			while (myReader.hasNextLine()) {
				String input=myReader.nextLine();
				short instruction = assemble(input);
				binaryDictionary.put(instruction, input);
				instructionMemory[numberOfInstructions++] = instruction;
			}
			myReader.close();
			return numberOfInstructions;

		} catch (FileNotFoundException e) {
			System.out.println("File not found!!");
			return -1;

		}
	}

	public short assemble(String instruction) {
		short inst = 0;
		String[] array = instruction.split(" ");
		switch (array[0]) {
		case ("SUB"):
			inst = (short) (0b0001 << 12);
			break;
		case ("MUL"):
			inst = (short) (0b0010 << 12);
			break;
		case ("LDI"):
			inst = (short) (0b0011 << 12);
			break;
		case ("BEQZ"):
			inst = (short) (0b0100 << 12);
			break;
		case ("AND"):
			inst = (short) (0b0101 << 12);
			break;
		case ("OR"):
			inst = (short) (0b0110 << 12);
			break;
		case ("JR"):
			inst = (short) (0b0111 << 12);
			break;
		case ("SLC"):
			inst = (short) (0b1001 << 12);
			break;
		case ("LB"):
			inst = (short) (0b1010 << 12);
			break;
		case ("SB"):
			inst = (short) (0b1011 << 12);
			break;
		default:
			break;

		}

		inst += ((Integer.parseInt(array[1].substring(1)) - 1) << 6);

		if (array[2].contains("R")) {

			inst += (Integer.parseInt(array[2].substring(1)) - 1);

		}

		else {
			inst += Integer.parseInt(array[2]);
		}

		return inst;

	}
}
