package ninetyNinePercentChain.Block.Hashing;
public interface MerkleTreeable {
	/*
	Name: hash
	Description: Generates the hash of the object and returns it
	Precondition: None.
	Postcondition: Returns the hash value of the object as a byte array.
	*/
	public byte[] hash();
}