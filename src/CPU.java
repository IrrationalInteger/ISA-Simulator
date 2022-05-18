import java.util.HashMap;
import java.util.zip.ZipEntry;

public class CPU {
	private int clock = 1;
	private short pc = 0;
	private byte statusReg = 0;
	private byte[] registerFile = new byte[64];
	private int opCode;
	private int destReg;
	private int srcReg;
	private byte arg1;
	private byte arg2;
	private short nextInstruction;
	private boolean fetched = false;
	private boolean decoded = false;
	private boolean jumped = false;
	HashMap<Short, String> binaryDictionary = new HashMap<Short, String>();

	public short getPc() {
		return pc;
	}

	public int getClock() {
		return clock;
	}

	public void setPc(short pc) {
		this.pc = pc;
	}

	public byte getStatusReg() {
		return statusReg;
	}

	public void setStatusReg(byte statusReg) {
		this.statusReg = statusReg;
	}

	public byte[] getRegisterFile() {
		return registerFile;
	}

	public void setRegisterFile(byte[] registerFile) {
		this.registerFile = registerFile;
	}

	public void executeInstruction(byte[] dataMemory) {
		if (decoded) {
			short instruction = (short) ((short) (opCode << 12)
					+ (short) (destReg << 6) + (short) (srcReg));
			byte tmp = 0;
			short prevPc;
			System.out.println("EXECUTING " + toBinaryString(instruction, 16)
					+ " (" + binaryDictionary.get(instruction) + ")");
			switch (opCode) {
			case (0b0000):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 + arg2);
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0001):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 - arg2);
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0010):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 * arg2);
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0011):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) arg2;
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0100):
				prevPc = (short) (pc);
				pc += (arg1 == 0) ? (arg2 - 1) : 0;
				fetched = (arg1 == 0) ? false : true;
				decoded = (arg1 == 0) ? false : true;
				jumped = (arg1 == 0) ? true : false;
				if (jumped)
					System.out.println("Program counter changed from "
							+ toBinaryString(prevPc, 16) + " (" + prevPc
							+ ") to " + toBinaryString(pc, 16) + " (" + pc
							+ ")\n");
				break;
			case (0b0101):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 & arg2);
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0110):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 | arg2);
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b0111):
				prevPc = (short) (pc - 2);
				pc = (short) ((arg1 << 8) + arg2);
				fetched = false;
				decoded = false;
				jumped = true;
				System.out.println("Program counter changed from "
						+ toBinaryString(prevPc, 16) + " (" + prevPc + ") to "
						+ toBinaryString(pc, 16) + " (" + pc + ")\n");
				break;
			case (0b1000):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 << arg2 | arg1 >>> (8 - arg2));
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b1001):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) (arg1 >>> arg2 | arg1 << (8 - arg2));
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b1010):
				tmp = registerFile[destReg];
				registerFile[destReg] = (byte) dataMemory[arg2];
				System.out.println("R" + (destReg + 1) + " changed from "
						+ toBinaryString(tmp, 16) + " (" + tmp + ") to "
						+ toBinaryString(registerFile[destReg], 16) + " ("
						+ registerFile[destReg] + ")");
				break;
			case (0b1011):
				tmp = dataMemory[arg2];
				dataMemory[arg2] = arg1;
				System.out.println("Memory[" + arg2 + "] changed from "
						+ toBinaryString(tmp, 8) + " (" + tmp + ") to "
						+ toBinaryString(dataMemory[arg2], 8) + " ("
						+ dataMemory[arg2] + ")");
				break;
			default:
				break;

			}
			byte prevSREG = statusReg;
			if (opCode == 0b0000 || opCode == 0b0001 || opCode == 0b0010
					|| opCode == 0b0101 || opCode == 0b0110 || opCode == 0b1000
					|| opCode == 0b1001) {
				if (registerFile[destReg] == 0)
					statusReg |= 0b1;
				else
					statusReg &= 0b11111110;

				if (registerFile[destReg] < 0)
					statusReg |= 0b100;
				else
					statusReg &= 0b11111011;

				if (opCode == 0b000) {
					if (arg1 + arg2 > Byte.MAX_VALUE)
						statusReg |= 0b10000;
					else
						statusReg &= 0b11101111;
				}
				if (opCode == 0b000 || opCode == 0b001) {

					if ((registerFile[destReg] >>> 7 != arg1 >>> 7)&&((opCode == 0b000 && arg1 >>> 7 == arg2 >>> 7)
							|| ((opCode == 0b001) && registerFile[destReg] >>> 7 == arg2 >>> 7)))
						statusReg |= 0b1000;
					else
						statusReg &= 0b11110111;

					if ((((statusReg >> 2) & (~(statusReg >> 3))) | ((~(statusReg >> 2)) & (statusReg >> 3))) % 2 == 1)
						statusReg |= 0b10;
					else
						statusReg &= 0b11111101;

				}
			}
			if (prevSREG != statusReg) {
				System.out.print("Status Register changed from "
						+ toBinaryString(prevSREG, 8) + " to "
						+ toBinaryString(statusReg, 8) + "\n");
				if ((prevSREG & 0b1) != (statusReg & 0b1))
					System.out.print("Z Flag changed from " + (prevSREG & 0b1)
							+ " to " + (statusReg & 0b1) + "\n");
				if ((prevSREG >>> 1 & 0b1) != (statusReg >>> 1 & 0b1))
					System.out.print("S Flag changed from "
							+ (prevSREG >>> 1 & 0b1) + " to "
							+ (statusReg >>> 1 & 0b1) + "\n");
				if ((prevSREG >>> 2 & 0b1) != (statusReg >>> 2 & 0b1))
					System.out.print("N Flag changed from "
							+ (prevSREG >>> 2 & 0b1) + " to "
							+ (statusReg >>> 2 & 0b1) + "\n");
				if ((prevSREG >>> 3 & 0b1) != (statusReg >>> 3 & 0b1))
					System.out.print("V Flag changed from "
							+ (prevSREG >>> 3 & 0b1) + " to "
							+ (statusReg >>> 3 & 0b1) + "\n");
				if ((prevSREG >>> 4 & 0b1) != (statusReg >>> 4 & 0b1))
					System.out.print("C Flag changed from "
							+ (prevSREG >>> 4 & 0b1) + " to "
							+ (statusReg >>> 4 & 0b1) + "\n");
			}
		}
	}

	public void decodeInstruction() {
		if (fetched) {
			opCode = (short) (nextInstruction >>> 12 & 0b1111);
			destReg = (nextInstruction >>> 6) & 0b111111;
			srcReg = (nextInstruction) & 0b111111;
			arg1 = registerFile[(nextInstruction >> 6) & 0b111111];
			arg2 = ((opCode >= 0b0 && opCode < 0b11) || (opCode >= 0b101 && opCode < 0b1000)) ? registerFile[srcReg]
					: (byte) (srcReg);
			decoded = true;
			String[] decodedIns = binaryDictionary.get(nextInstruction).split(
					" ");
			System.out.println("DECODING "
					+ toBinaryString(nextInstruction, 16) + " ("
					+ binaryDictionary.get(nextInstruction) + ") ");
			System.out.println("Opcode= " + toBinaryString((short) opCode, 4)
					+ " (" + decodedIns[0] + "), R1= "
					+ toBinaryString((short) destReg, 6) + " (" + decodedIns[1]
					+ "), R2/IMM= " + toBinaryString((short) srcReg, 6) + " ("
					+ decodedIns[2] + ")");

		}
	}

	public void fetchInstruction(short[] instructionMemory) {
		if (!jumped) {

			nextInstruction = instructionMemory[pc++];
			System.out.println("FETCHING "
					+ toBinaryString(nextInstruction, 16) + " ("
					+ binaryDictionary.get(nextInstruction)
					+ ") from Instruction Memory");
			fetched = true;
			System.out.println("Program Counter changed from "
					+ CPU.toBinaryString((short) (pc - 1), 16) + " ("
					+ (pc - 1) + ")" + " to " + CPU.toBinaryString(pc, 16)
					+ " (" + pc + ")");
		}
		System.out
				.print("\n---------------------------------------------------------------\n");
		jumped = false;
	}

	public void incrementClock() {
		clock++;
	}

	public static String toBinaryString(short instruction, int length) {
		String inst = Integer.toBinaryString(instruction & 0xFFFF);
		int len = inst.length();
		for (int i = 0; i < length - len; i++) {
			inst = "0" + inst;
		}
		return inst;
	}
}
