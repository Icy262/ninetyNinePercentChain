package ninetyNinePercentChain.Utils;
import java.security.MessageDigest;

public class SHA256Hash {
	/*
	Name: hash
	Description: Takes the byte[] toHash, computes the SHA256 hash of it, and returns that value.
	Precondition: toHash is not null.
	Postcondition: toHash hashed with SHA256 and value returned.
	*/
	public static byte[] hash(byte[] toHash) {
		byte[] hashedValue=null; //Stores the hash
		try {
			MessageDigest digest=MessageDigest.getInstance("SHA-256"); //SHA-256
			hashedValue=digest.digest(toHash); //Hashes toHash with SHA256 and stores it in hashedValue
		} catch(Exception e) {
			System.out.println(e);
		}
		return hashedValue; //Returns the value of hashedValue
	}
}