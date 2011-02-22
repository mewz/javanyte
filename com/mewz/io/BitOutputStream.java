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
	*/
	public BitOutputStream(OutputStream outputstream, boolean autoflush){
		this.outputStream = outputstream;
		this.byteIndex = 0;
		this.byteVal = 0;
		this.autoflush = autoflush;
	}
	
	/*
	Take in a bit as in int of which the value is either 0, or anything else will be a 1.
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
	
	public void write(byte[] b, int off, int len) throws IOException {
		for(int i = off; i < off+len; i ++){
			this.write(b[i]);
		}
	}
	
	public void write(byte[] b) throws IOException {
		for(int i = 0; i < b.length; i ++){
			this.write(b[i]);
		}
	}
	
	public void flush() throws IOException {
		//write out whatever is in byteVal and reset everything
		this.outputStream.write(this.byteVal);
		this.byteIndex = 0;
		this.byteVal = 0;
	}

	public void close() throws IOException {
		this.outputStream.close();
	}	
}