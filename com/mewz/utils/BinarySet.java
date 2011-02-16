package com.mewz.utils;

/*
@Author: Jason Hullinger
@Email: sshjason at gmail doc com
@Purpose: Data collection for handling bits
*/

public class BinarySet {	
	private byte[] bits;
	
	/*
	Private constructor - use static create factory like methods to create with bits or bytes
	*/
	private BinarySet(byte[] bits){
		super();
		this.bits = bits;
	}
	
	/*
	Create a new BinarySet with a buffer of bits
	@param bits the buffer of bits
	@return BinarySet
	*/
	public static BinarySet createBinarySetWithBits(byte[] bits){
		return new BinarySet(bits);
	}
	
	/*
	Create a new BinarySet with a buffer of bytes. This will convert the bytes into a buffer of bits
	@return BinarySet
	*/
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
	
	/*
	@return String representation of the bit buffer
	*/
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
	
	/*
	Set a bit at an index of the bit buffer.
	@param index the index in which we will insert
	@param b the bit value to set. If the value does not equal a 0, it will be a 1
	@throws IndexOutOfBoundsException if index is less than zero or greater than the bit buffer length
	*/
	public void setBitAtIndex(int index, byte b){
		//don't catch index out of bounds - let it fail at runtime and fix at the caller level
		//we will make sure it's only ever a 0 or 1 though
		this.bits[index] = (b == 0) ? (byte)0 : (byte)1;
	}
	
	/*
	Get a byte starting at an index as either signed or unsigned. This will attemp to read in 8 bits to create the byte 
	if index + 8 is less than the bit buffer. Otherwise it will read in up to the bit buffer length or zero if index is 
	greater than the bit buffer length.
	@param index the index at where to start adding up the byte from the bit buffer 
	@unsigned true for an unsigned byte, false for a signed byte
	@throws IndexOutOfBoundsException if index is less than zero
	*/
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
	
	/*
	Get a bit at the index of the bit buffer
	@param index the index of the bit buffer to get
	@return the value of the bit buffer at index. It will either be a 0 or 1.
	@throws IndexOutOfBoundsException if index is less than zero or greater than the bit buffer length
	*/
	public byte getBitAtIndex(int index){
		//don't catch index out of bounds - let it fail at runtime and fix at the caller level
		return this.bits[index];
	}
	
	/*
	Get the bit buffer
	@return the bit buffer containing 0 or 1's
	*/
	public byte[] getBits(){
		return this.bits;
	}

	/*
	Convert the bit buffer into bytes. If the bit buffer is not divisible by 8, the remainder will converted
	@return the converted bit buffer as a byte buffer
	*/
	public byte[] getBytes(){
		int bytesLen = this.bits.length / 8;
		if(this.bits.length % 8 != 0){
			bytesLen ++;
		}
		byte bytes[] = new byte[bytesLen];
		for(int index = 0, count = 0; count < bytes.length; index += 8, count ++){
			bytes[count] = this.getByteAtIndex(index, false);
		}
		return bytes;
	}
	
	/*
	Convert the bit buffer into unsigned bytes. If the bit buffer is not divisible by 8, the remainder will converted
	@return the converted bit buffer as a byte buffer
	*/
	public byte[] getUnsignedBytes(){
		int bytesLen = this.bits.length / 8;
		if(this.bits.length % 8 != 0){
			bytesLen ++;
		}
		byte bytes[] = new byte[bytesLen];
		for(int index = 0, count = 0; count < bytes.length; index += 8, count ++){
			bytes[count] = this.getByteAtIndex(index, true);
		}
		return bytes;
	}
	
	/*
	Get the length of the bit buffer
	@return the length of the bit buffer
	*/
	public int length(){
		return this.bits.length;
	}
	
	/*
	Will do a bitwise operator 'and' on the bit buffer or the BinarySet passed in on this binary set's bit buffer.
	If a bit buffer length from both BinarySet's are not equal, it will go up to the smalles bit buffer length
	@param the BinarySet containing a the bit buffer to bitwise 'and' the current bit buffer
	*/
	public void and(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a & b); }
		};
		this.bitwise(set, cb);
	}
	
	/*
	Will do a bitwise operator 'or' on the bit buffer or the BinarySet passed in on this binary set's bit buffer.
	If a bit buffer length from both BinarySet's are not equal, it will go up to the smalles bit buffer length
	@param the BinarySet containing a the bit buffer to bitwise 'or' the current bit buffer
	*/
	public void or(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a | b); }
		};
		this.bitwise(set, cb);
	}
	
	/*
	Will do a bitwise operator 'xor' on the bit buffer or the BinarySet passed in on this binary set's bit buffer.
	If a bit buffer length from both BinarySet's are not equal, it will go up to the smalles bit buffer length
	@param the BinarySet containing a the bit buffer to bitwise 'xor' the current bit buffer
	*/
	public void xor(BinarySet set){
		BitwiseCallback cb = new BitwiseCallback(){
			public byte bitwiseOperand(byte a, byte b){ return (byte)(a ^ b); }
		};
		this.bitwise(set, cb);
	}
		
	/*
	Private method that takes a BinarySet containing a bit buffer to perform a bitwise operation to the current bit buffer.
	@param set the BinarySet containing a the bit buffer to performa  bitwise operation to the current bit buffer
	@param callback the BitwiseCallback that contains an implementation of the method bitwiseOperand(byte a, byte b) for the current operation
	*/
	private void bitwise(BinarySet set, BitwiseCallback callback){
		int min = (set.length() <= this.length()) ? set.length() : this.length();
		for(int i = 0; i < min; i ++){
			this.setBitAtIndex(i, callback.bitwiseOperand(this.getBitAtIndex(i), set.getBitAtIndex(i)));
		}
	}
	
	/*
	Private interface used to perform bitwise operations.
	*/
	private interface BitwiseCallback {
		public byte bitwiseOperand(byte a, byte b);
	}

}