import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

class BlockFile {
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
			FileOutputStream blockFile=new FileOutputStream("./blockchain/"+String.valueOf(toWrite.index)+".ser");
			ObjectOutputStream blockWriter=new ObjectOutputStream(blockFile);
			blockWriter.writeObject(toWrite);
			blockWriter.close();
			blockFile.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}