import com.mewz.utils.BinarySet;

/*
@Author: Jason Hullinger
@Email: sshjason at gmail dot com
@Purpose: Simple example of using BinarySet
*/

public class BitSetEx {

	/*
	Creates a BinarySet with the bytes from a string and returns the bit array
	*/
	public byte[] getBitBufferForString(String strIn){
		BinarySet binSet = BinarySet.createBinarySetWithBytes(strIn.getBytes());
		return binSet.getBits();
	}
	
	/*
	Creates a BinarySet with a bit array and returns the resulting bytes
	*/
	public byte[] getByteBufferFromBitBuffer(byte[] bits){
		BinarySet binSet = BinarySet.createBinarySetWithBits(bits);
		return binSet.getBytes();
	}
	
	/*
	Tests and bitwise operation
	*/
	public void testAnd(){
		byte[] byte1 = {1, 0, 0, 0, 0, 0, 1};
		byte[] byte2 = {1, 0, 0, 0, 0, 0, 0};
		BinarySet binSet = BinarySet.createBinarySetWithBits(byte1);
		binSet.and(BinarySet.createBinarySetWithBits(byte2));
		System.out.println("and'ed result: " + binSet.toString());
	}
	/*
	Tests or bitwise operation
	*/	
	public void testOr(){
		byte[] byte1 = {1, 0, 0, 0, 0, 0, 1};
		byte[] byte2 = {1, 0, 0, 0, 0, 0, 0};
		BinarySet binSet = BinarySet.createBinarySetWithBits(byte1);
		binSet.or(BinarySet.createBinarySetWithBits(byte2));
		System.out.println("or'ed result: " + binSet.toString());
	}
	
	/*
	Tests xor bitwise operation
	*/
	public void testXor(){
		byte[] byte1 = {1, 0, 0, 0, 0, 0, 1};
		byte[] byte2 = {1, 0, 0, 0, 0, 0, 0};
		BinarySet binSet = BinarySet.createBinarySetWithBits(byte1);
		binSet.xor(BinarySet.createBinarySetWithBits(byte2));
		System.out.println("xor'ed result: " + binSet.toString());
	}
	
	/*
	Helper method that prints the contents of an array
	*/
	public void printBitArray(byte[] bits){
		for(int i = 0; i < bits.length; i ++){
			System.out.print(bits[i]);
		}
		System.out.println();
	}
	
	public static void main(String[] args){
		String str = "Hello, World!";
		BitSetEx bitSet = new BitSetEx();
		
		//convert str String to a bit buffer and print it.
		byte[] helloBits = bitSet.getBitBufferForString(str);
		System.out.println("bits for string: " + str);
		bitSet.printBitArray(helloBits);
		
		//now fetch the byte array from the bit array and print the result
		byte[] helloBytes = bitSet.getByteBufferFromBitBuffer(helloBits);
		System.out.println("Converted back to bytes:\n" + new String(helloBytes));
		
		//test some bitwise operators
		bitSet.testAnd();
		bitSet.testOr();
		bitSet.testXor();
	}
}