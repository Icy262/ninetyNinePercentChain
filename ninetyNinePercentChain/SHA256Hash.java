package ninetyNinePercentChain;
import java.security.MessageDigest;

class SHA256Hash {
	public SHA256Hash() {
	}
	public static byte[] hash(byte[] toHash) {
		byte[] hashedValue=null;
		try {
			MessageDigest digest=MessageDigest.getInstance("SHA-256"); //SHA-256
			hashedValue=digest.digest(toHash);
		} catch(Exception e) {
			System.out.println(e);
		}
		return hashedValue;
	}
}