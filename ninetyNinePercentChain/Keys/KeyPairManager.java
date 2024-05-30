package ninetyNinePercentChain.Keys;
import java.security.KeyPair;
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
			FileInputStream keyFile=new FileInputStream("./keys/"+keyName+".ser");
			ObjectInputStream keyReader=new ObjectInputStream(keyFile);
			KeyPair key=(KeyPair) keyReader.readObject();
			keyReader.close();
			keyFile.close();
			return key;
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
			FileOutputStream keyFile=new FileOutputStream("./keys/"+keyName+".ser");
			ObjectOutputStream keyWriter=new ObjectOutputStream(keyFile);
			keyWriter.writeObject(toWrite);
			keyWriter.close();
			keyFile.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
