package com.mewz.examples;

import com.mewz.utils.BinarySet;
import com.mewz.io.BitArrayInputStream;

public class BinarySetEx {
	private BinarySet binSet;

	public BinarySetEx(String str){
		binSet = BinarySet.createBinarySetWithBytes(str.getBytes());
	}
	
	public void printBytes(){
		System.out.println(new String(binSet.getBytes()));
	}
	
	public void printBits(){
		this.printByteArray(binSet.getBits());
	}
	
	public void testBitByteConversion(){
		byte[] bits = binSet.getBits();
		BinarySet b = BinarySet.createBinarySetWithBits(bits);
		String str = new String(b.getBytes());
		System.out.println(str);
	}
	
	public void testBitwise(){
		byte[] b1 = {0, 0, 0, 1, 1, 0, 0, 0};
		byte[] b2 = {0, 0, 0, 1, 0, 0, 0};
		
		BinarySet bset1 = BinarySet.createBinarySetWithBits(b1);
		BinarySet bset2 = BinarySet.createBinarySetWithBits(b2);
		
		System.out.println(bset1.toString());
		System.out.println(bset2.toString());
		bset1.xor(bset2);
		System.out.println(bset1.toString());
	}
	
	public void printByteArray(byte[] b){
		for(int i = 0; i < b.length; i ++){
			System.out.print(b[i]);
		}
		System.out.println();
	}
	
	public void testIO(byte[] b){
		try{
			BitArrayInputStream bis = new BitArrayInputStream(b, false);
			int res = 0;
			while((res = bis.read()) != -1){
				System.out.print(res);
			}
			System.out.println();
		}
		catch(java.io.IOException ioe){
			System.err.println(ioe.toString());
		}
	}
	
	public static void main(String[] args){
		String str = "Hello";
		BinarySetEx binStr = new BinarySetEx(str);
		binStr.printBytes();
		binStr.printBits();
		binStr.testBitByteConversion();
		binStr.testBitwise();
		binStr.testIO(str.getBytes());
	}
}