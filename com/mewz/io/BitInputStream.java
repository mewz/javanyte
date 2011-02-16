package com.mewz.io;

import java.io.InputStream;
import java.io.IOException;

/*
* @Author: Jason Hullinger
* @Email: sshjason at gmail dot com
* @Purpose: Take an InputStream and reads the bits
*/
public class BitInputStream extends InputStream{
	private InputStream inputStream;
	private int bitIndex;
	private byte currentByte;
	private byte byteVal;

	/*
	* Construct with another InputStream.
	* @see BitInputStream bis = new BitInputStream(new ByteArrayInputStream(new String("hello").getBytes()))
	*/
	public BitInputStream(InputStream inputStream){
		this.inputStream = inputStream;
	}
	
	/*
	Reads the next bit from the input stream. The value is returned as either 0 or 1 or -1 if the end of the stream has been reached.
	@return the next byte of data, or -1 because the end of the stream has been reached
	@throws IOException if an I/O error occured
	*/
	public int read() throws IOException{
		//I realize I could consolidate this into one if, however, the problem is in reading the first byte
		//I don't wish to read it in any other place such as the constructor because it'll have to either 
		//throw an IOException, or I'll have to catch it on read()
		if(this.bitIndex == 8){
			this.bitIndex = 0;
		}
		if(bitIndex == 0){
			//we are casting down from an int which could lead to an int overflow
			//however, the javadocs say read, which returns an int, will read the next byte
			this.currentByte = (byte)this.inputStream.read();
			this.byteVal = this.currentByte;
		}
		if(this.currentByte == -1) return -1;
		
		int val = ((this.byteVal & 128) == 0) ? (byte)0 : (byte)1;
		this.byteVal <<= 1;
		this.bitIndex ++;
		return val;
	}
	
	/*
	Reads some number of bits from the input stream and stores them into the buffer bits. The number of bits read is the return value.
	@param bits the buffer into which the bits are stored
	@return the number of bits read, or -1 if there is no more data to be read.
	@throws IOException if an I/O error occured
	@throws NullPointerException if bits is null
	*/
	public int read(byte[] bits) throws IOException {
		if(this.currentByte < 0) return -1;
		int i;
		for(i = 0; i < bits.length; i ++){
			int b = this.read();
			if(b < 0){
				break;
			}
			bits[i] = (byte)b;
		}
		return i;
	}
	
	/*
	Reads up to len bits of data from the input stream and stores them into the buffer bits. An attempt is made to read in len bits.
	See the general contract for read in InputStream
	@param bits the buffer into which to store the bits
	@param off the offset at which to store the bits
	@param len the number of bits to read in
	@return the number of bits read, or -1 if there is no more data becasue the end of the stream has been reached
	@throws IOException if an I/O error occured
	@throws NullPointerException is bits is null
	@throws IndexOutOfBoundsException if off or len is less than 0 or if len+off is greater then the input buffer
	*/
	public int read(byte[] bits, int off, int len) throws IOException {
		if(this.currentByte < 0) return -1;
		int i;
		for(i = off; i < off + len; i ++){
			int b = this.read();
			if(b < 0){
				break;
			}
			bits[i] = (byte)b;
		}
		return i;
	}
	
	/*
	Closes the InputStream
	*/
	public void close() throws IOException {
		this.inputStream.close();
		this.currentByte = -1;
	}
	
	/*
	Mark is currently not supported
	@return mark is not supported so always false
	*/
	public boolean markSupported() {
		return false;
	}
	
}