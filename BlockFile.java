import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

class BlockFile {
	public static Block readBlock(int blockNum) {
		FileInputStream blockFile=new FileInputStream("./blockchain/"+String.valueOf(blockNum)+".ser");
		ObjectInputStream blockReader=new FileInputStream(blockFile);
		Block block=(Block) blockReader.readObject();
		blockReader.close();
		blockFile.close();
		return block;
	}
	public static void writeBlock(Block toWrite) {
		FileOutputStream blockFile=new FileOutputStream("./blockchain/"+String.valueOf(toWrite.index)+".ser");
		ObjectOutputStream blockWriter=new ObjectOutputStream(blockFile);
		blockWriter.writeObject(toWrite);
		blockWriter.close();
		blockFile.close();
	}
}
