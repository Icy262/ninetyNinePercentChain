package ninetyNinePercentChain.Keys;
import java.security.KeyPair;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class KeyPairManager {
	/*
	Name: readKey
	Description: Opens the file with the name keyName.ser in the keystore. It reads the object and casts it to a KeyPair. It returns this.
	Precondition: keyName is the name of a valid key file
	Postcondition: KeyPair returned
	*/
	public static KeyPair readKey(String keyName) {
		try {
			FileInputStream keyFile=new FileInputStream("./keys/"+keyName+".ser"); //Opens the file
			ObjectInputStream keyReader=new ObjectInputStream(keyFile); //Opens a object input stream on top of the FileInputStream
			KeyPair key=(KeyPair) keyReader.readObject(); //Reads the key from the file and casts it to a KeyPair
			keyReader.close(); //Closes the ObjectInputStream
			keyFile.close(); //Closes the FileInputStream
			return key; //Returns the key
		} catch(Exception e) {
			System.out.println(e);
			return null;
		}
	}
	/*
	Name: writeKey
	Description: Takes a KeyPair to store and a name for the KeyPair. Serializes and writes the KeyPair to keyName.ser
	Precondition: keyName is not null
	Postcondition: KeyPair is written to a file named keyName.ser
	*/
	public static void writekey(KeyPair toWrite, String keyName) {
		try {
			new File("keys").mkdir(); //Makes a new keys directory, if one does not already exist
			FileOutputStream keyFile=new FileOutputStream("./keys/"+keyName+".ser"); //Opens a new file with the name keyName.ser
			ObjectOutputStream keyWriter=new ObjectOutputStream(keyFile); //Opens a new ObjectOutputStream to the file
			keyWriter.writeObject(toWrite); //Writes the KeyPair
			keyWriter.close(); //Flushes and closes the ObjectOutputStream
			keyFile.close(); //Flushes and closes the FileOutputStream
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
