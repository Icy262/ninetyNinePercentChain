import java.security.MessageDigest;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

public class SHA256Hash {
    public SHA256Hash() {
    }
    public static byte[] hash(Object toHash) {
        byte[] hashedValue=null;
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256"); //SHA-256
            ByteArrayOutputStream byteArray=new ByteArrayOutputStream();
            ObjectOutputStream toHashSerialized=new ObjectOutputStream(byteArray);
            toHashSerialized.writeObject(toHash);
            hashedValue=digest.digest(byteArray.toByteArray());
        } catch(Exception e) {
            System.out.println(e);
        }
        return hashedValue;
    }
}
