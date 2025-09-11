package ninetyNinePercentChain.Block.Hashing;

/*
 * In order for a merkle tree to be generated, the object must be hashable. This interface includes a hash method. If this method is implemented, the class is able to be made into a merkle tree. Block, Transaction, TransactionIn, and TransactionOut all implement this interface.
 */

public interface MerkleTreeable {
	/*
	Name: hash
	Description: Generates the hash of the object and returns it
	Precondition: All values initialized.
	Postcondition: Returns the hash value of the object as a byte array.
	*/
	public byte[] hash();
}