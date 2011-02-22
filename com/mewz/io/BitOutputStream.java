package com.mewz.io;

import java.io.OutputStream;
import java.io.IOException;

/*
@Author: Jason Hullinger
@Email: sshjason at gmail dot com
@Purpose: Takes an OutputStream and writes bits to bytes.
*/

public class BitOutputStream extends OutputStream {
	private OutputStream outputStream;
	private int byteVal;
	private int byteIndex;
	private boolean autoflush;
	
	/*
	Contructor that takes an OutputStream and writes bits out as bytes. Autoflush is enabled which will write to the outputstream every 8 bits.
	@param outputStream the OutputStream in which to write bits to bytes
	@throws IOException if an I/O error occured
	*/
	public BitOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
		this.byteIndex = 0;
		this.byteVal = 0;
		this.autoflush = true;
	}
	
	/*
	Contructor that takes an OutputStream and writes bits out as bytes.
	@param outputStream the OutputStream in which to write bits to bytes.
	@param autoflush if true, will write to the outputstream at ever 8 bits for 1 byte. Otherwise call flush.
	@throws IOException if an I/O error occured
	*/
	public BitOutputStream(OutputStream outputstream, boolean autoflush){
		this.outputStream = outputstream;
		this.byteIndex = 0;
		this.byteVal = 0;
		this.autoflush = autoflush;
	}
	
	/*
	Write, or queue up the next bit. If the input value is 0, the value is 0, anything else is 1
	@param b the bit to be written
	@throws IOException if an I/O error occured
	@throws NullPointerException is b is null
	*/
	public void write(int b) throws IOException{
		this.byteVal += Math.pow(2, 7 - byteIndex++) * ((b == 0) ? 0 : 1);
	
		if(this.byteIndex == 8){
			this.byteIndex = 0;
			if(this.autoflush){
				this.outputStream.write(this.byteVal);
			}
		}
		if(byteIndex == 0){
			this.byteVal = 0;
		}
	}
	
	/*
	Write, or queue up the next bits from the input array starting at offset ending at offset + len
	@param b the bit array containing bits to be written
	@param off the offset at which to begin writing in the input array
	@param len the number of bits to write
	@throws IOException if an I/O error occured
	@throws NullPointerException is b is null
	@throws IndexOutOfBoundsException if off or len is less than 0 or if len+off is greater then the input buffer
	*/
	public void write(byte[] b, int off, int len) throws IOException {
		for(int i = off; i < off+len; i ++){
			this.write(b[i]);
		}
	}
	
	/*
	Write, or queue up the next bits from the input array
	@param b the bit array containing bits to be written
	@throws IOException if an I/O error occured
	@throws NullPointerException is b is null
	*/
	public void write(byte[] b) throws IOException {
		for(int i = 0; i < b.length; i ++){
			this.write(b[i]);
		}
	}
	
	/*
	This will cause the outputstream to write the result of the bits being appended. This will also reset the byte value.
	Only use if you do not wish to have written 8 bit bytes.
	@throws IOException if an I/O error occured
	*/
	public void flush() throws IOException {
		//write out whatever is in byteVal and reset everything
		this.outputStream.write(this.byteVal);
		this.byteIndex = 0;
		this.byteVal = 0;
	}
}