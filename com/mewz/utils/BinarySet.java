package com.mewz.utils;

/*
- to do: more docs
*/

public class BinarySet {	
	private byte[] bits;
	
	/* factory methods due to creating the class with either bits or bytes */
	private BinarySet(byte[] bits){
		super();
		this.bits = bits;
	}
	
	public static BinarySet createBinarySetWithBits(byte[] bits){
		return new BinarySet(bits);
	}
	
	public static BinarySet createBinarySetWithBytes(byte[] bytes){
		byte[] t_bits = new byte[bytes.length * 8];
		for(int i = 0; i < bytes.length; i ++){
			byte val = bytes[i];
			for(int j = 0; j < 8; j ++){
				t_bits[(i * 8) + j] = ((val & 128) == 0) ? (byte)0 : (byte)1;
				val <<= 1;
			}
		}
		return new BinarySet(t_bits);
	}
	
	/* get a string representation of the bit array */
	public String toString(){
		int strLen = this.length();
		char str[] = new char[strLen];
		//let's at least cut this in half
		for(int i = 0, j = strLen / 2; j < strLen; i ++, j ++){
			str[i] = (this.bits[i] == 0) ? '0' : '1';
			str[j] = (this.bits[j] == 0) ? '0' : '1';
		}
		//if it's an odd length, set the middle
		if(strLen % 2 != 0){
			str[(strLen / 2) + (strLen % 2)] = (this.bits[(strLen / 2) + (strLen % 2)] == 0) ? '0' : '1';
		}
		return new String(str);
	}
	
	/* Set method */
	public void setBitAtIndex(int index, byte b){
		//don't catch index out of bounds - let it fail at runtime and fix at the caller level
		//we will make sure it's only ever a 0 or 1 though
		this.bits[index] = (b == 0) ? (byte)0 : (byte)1;
	}
	
	/* Get methods */
	//get a byte starting at an index as either signed or unsigned (-127 to 127 or 0 to 255)
	public byte getByteAtIndex(int index, boolean unsigned){
		byte val = 0;
		int max = (index + 8 < this.bits.length) ? index + 8 : this.bits.length;
		for(int i = index, j = 0; i < max; i ++, j ++){
			val += Math.pow(2, 7 - j) * this.bits[i];
		}
		if(unsigned){
			val = (byte)(0x000000FF & val);
		}
		return val;
	}
	
	//get a bit at an index
	public byte getBitAtIndex(int index){
		//don't catch index out of bounds - let it fail at runtime and fix at the caller level
		return this.bits[index];
	}
	
	//get the as bits
	public byte[] getBits(){
		return this.bits;
	}
	
	//get as bytes
	public byte[] getBytes(){
		//there is the possiblity that we will chop something off if we allow inserting individual bits in the future.
		byte bytes[] = new byte[this.bits.length / 8];
		for(int index = 0, count = 0; count < bytes.length; index += 8, count ++){
			bytes[count] = this.getByteAtIndex(index, false);
		}
		return bytes;
	}
	
	//get as unsigned bytes
	public byte[] getUnsignedBytes(){
		//there is the possiblity that we will chop something off if we allow inserting individual bits in the future.
		byte bytes[] = new byte[this.bits.length / 8];
		for(int index = 0, count = 0; index < this.bits.length; index += 8, count ++){
			bytes[count] = this.getByteAtIndex(index, true);
		}
		return bytes;
	}
	
	/* Get the bit array length */
	public int length(){
		return this.bits.length;
	}
	
	/* bitwise operators */
	//and this BinarySet with another BinarySet
	public void and(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a & b); }
		};
		this.bitwise(set, cb);
	}
	
	//or this Binary with another BinarySet
	public void or(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a | b); }
		};
		this.bitwise(set, cb);
	}
	
	//xor this Binary with another BinarySet
	public void xor(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a ^ b); }
		};
		this.bitwise(set, cb);
	}
		
	//private method to perform bitwise operations
	private void bitwise(BinarySet set, BitwiseCallback callback){
		int min = (set.length() <= this.length()) ? set.length() : this.length();
		for(int i = 0; i < min; i ++){
			this.setBitAtIndex(i, callback.bitwiseOperand(this.getBitAtIndex(i), set.getBitAtIndex(i)));
		}
	}
	
	/* private interface as a callback for the bitwise operators */
	private interface BitwiseCallback {
		public byte bitwiseOperand(byte a, byte b);
	}

}