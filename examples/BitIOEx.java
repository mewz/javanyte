import com.mewz.io.BitInputStream;
import com.mewz.io.BitOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/*
@Author: Jason Hullinger
@Email: sshjason at gmail dot com
@Purpose: Simple example of using a BitInputStream and a BitOutputStream
*/

public class BitIOEx {
	
	public BitIOEx(String strIn, String filename){
		BitInputStream bis = null;
		BitOutputStream bos = null;
		try{
			bis = new BitInputStream(new ByteArrayInputStream(strIn.getBytes()));
			bos = new BitOutputStream(new FileOutputStream(new File(filename)));

			System.out.println("Bit values for: " + strIn);

			int bitVal = 0;
			while((bitVal = bis.read()) != -1){
				System.out.print(bitVal);
				bos.write(bitVal);
			}

			System.out.println();
		}
		catch(IOException ioe){
			System.err.println("IOError: " + ioe);
		}
		finally{
			try{
				bis.close();
				bos.close();
			}
			catch(IOException ioe){}
		}
	}
	
	public static void main(String[] args){
		BitIOEx bitIO = new BitIOEx("Hello, World!", "helloworld.txt");
	}
}