package org.ninetyNinePercentChain.Utils;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.ninetyNinePercentChain.Block.Block;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

public class BlockFile {
	public static Block readBlock(int blockNum) {
		try {
			FileInputStream blockFile=new FileInputStream("./blockchain/"+String.valueOf(blockNum)+".ser");
			ObjectInputStream blockReader=new ObjectInputStream(blockFile);
			Block block=(Block) blockReader.readObject();
			blockReader.close();
			blockFile.close();
			return block;
		} catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public static void writeBlock(Block toWrite) {
		try {
			FileOutputStream blockFile=new FileOutputStream("./blockchain/"+String.valueOf(toWrite.getIndex())+".ser");
			ObjectOutputStream blockWriter=new ObjectOutputStream(blockFile);
			blockWriter.writeObject(toWrite);
			blockWriter.close();
			blockFile.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public static boolean blockExists(int index) {
		try {
			FileInputStream block=new FileInputStream("./blockchain/"+index+".ser");
			block.close();
			return true;
		} catch(FileNotFoundException e) {
			return false;
		} catch(IOException e) {
			System.out.println(e);
			return true;
		}
	}
	public static int getHighestIndex() {
		File directory=new File("./blockchain");
		return directory.listFiles().length-1;
	}
}