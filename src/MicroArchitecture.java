import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class MicroArchitecture {
	byte[] dataMemory = new byte[2048];
	short[] instructionMemory = new short[1024];
	int numberOfInstructions = 0;

	CPU myCPU = new CPU();
	Parser Parser = new Parser();

	public void loadInstructions() {

		while (true) {
			Scanner sc = new Scanner(System.in);
			System.out.print("Please enter your file path including the file extension:");
			String fileName = sc.nextLine();
			numberOfInstructions = Parser.parse(fileName, instructionMemory, myCPU.binaryDictionary);
			if (numberOfInstructions != -1)
				break;

		}
	}

	public void start() {
		loadInstructions();
		run();
	}

	public void run() {
		while (myCPU.getPc() < numberOfInstructions) {
			System.out.println("---------------------------------------------------------------\n");
			sleep(2000);
			System.out.print("Clock Cycle: "+ myCPU.getClock()+"\n");
			System.out.println("Program Counter: "+ CPU.toBinaryString(myCPU.getPc(), 16)+" ("+myCPU.getPc()+")\n");
			myCPU.executeInstruction(dataMemory);
			myCPU.decodeInstruction();
			if (myCPU.getPc() < numberOfInstructions)
				myCPU.fetchInstruction(instructionMemory);
			myCPU.incrementClock();
			if (myCPU.getPc() == numberOfInstructions) {
				System.out.println("---------------------------------------------------------------\n");
				sleep(2000);
				System.out.print("Clock Cycle: "+ myCPU.getClock()+"\n");				
				System.out.println("Program Counter: "+ CPU.toBinaryString(myCPU.getPc(), 16)+" ("+myCPU.getPc()+")\n");
				myCPU.executeInstruction(dataMemory);
				myCPU.decodeInstruction();
				myCPU.incrementClock();
				System.out.println("---------------------------------------------------------------\n");
				sleep(2000);
				System.out.print("Clock Cycle: "+ myCPU.getClock()+"\n");
				System.out.println("Program Counter: "+ CPU.toBinaryString(myCPU.getPc(), 16)+" ("+myCPU.getPc()+")\n");
				myCPU.executeInstruction(dataMemory);
				myCPU.incrementClock();
			}
		}
		System.out.print("\nProgram Counter : "+CPU.toBinaryString(myCPU.getPc(), 16)+"\n");
		System.out.print("\nStatus Register : "+CPU.toBinaryString(myCPU.getStatusReg(), 8)+"\n");
		System.out.print("\nRegister File : ");
		printContent(myCPU.getRegisterFile(), 64);
		System.out.print("\nData Memory : ");
		printContent(dataMemory, 2048);
		System.out.print("\nInstruction Memory : ");
		printContent(instructionMemory, 1024);
		sleep(1000);
		System.out.print("\nCredits : ");
		sleep(1000);
		System.out.print("\nAmr Mohamed");
		sleep(1000);
		System.out.print("\nAmr Esmaeel");
		sleep(1000);
		System.out.print("\nMohamed Osama");
		sleep(1000);
		System.out.print("\nAbdelraheman Khaled");
	}
	
	public void sleep(int x){
		try{
			Thread.sleep(x);
		}
		catch(Exception ex){
			
		}
	}
	public void printContent(byte[] content ,int size){
		System.out.print("[");
		for(int i=0;i<size-1;i++){
			System.out.print(CPU.toBinaryString(content[i],8)+", ");
		}
		System.out.print(CPU.toBinaryString(content[size-1],8)+"]\n");
	}
	
	public void printContent(short[] content ,int size){
		System.out.print("[");
		for(int i=0;i<size-1;i++){
			System.out.print(CPU.toBinaryString(content[i],16)+", ");
		}
		System.out.print(CPU.toBinaryString(content[size-1],16)+"]\n");
	}

	public static void main(String[] args) {

		MicroArchitecture myMicroArchitecture = new MicroArchitecture();
		myMicroArchitecture.start();

	}
}
