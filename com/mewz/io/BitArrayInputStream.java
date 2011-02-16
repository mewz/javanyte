package com.mewz.io;

import java.io.InputStream;
import java.io.IOException;

public class BitArrayInputStream extends InputStream{

	protected byte[] bitbuf;
	protected int count;
	protected int pos;
	
	public BitArrayInputStream(byte[] bytes, boolean asBits){
		if(asBits) {
			this.bitbuf = bytes;
		}
		else {
			this.bitbuf = new byte[bytes.length * 8];
			for(int i = 0; i < bytes.length; i ++){
				int val = bytes[i];
				for(int j = 0; j < 8; j ++){
					this.bitbuf[(i * 8) + j] = (byte)((val & 128) == 0 ? 0 : 1);
					val <<= 1;
				}
			}
		}
	}

	public int read() throws IOException {
		if(this.bitbuf == null) throw new IOException("input byte buffer is null");
		if(this.pos < this.bitbuf.length && this.pos >= 0){
			return this.bitbuf[this.pos++];
		}
		this.pos = -1;
		return this.pos;
	}
	
	public boolean markSupported(){
		return false;
	}
}