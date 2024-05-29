package org.ninetyNinePercentChain.Block.Hashing;
import java.util.ArrayList;

import org.ninetyNinePercentChain.Utils.ByteArray;
import org.ninetyNinePercentChain.Utils.SHA256Hash;

public class MerkleTree<T extends MerkleTreeable> {
	private ArrayList<byte[]> hash; //This stores the input byte[]s and as we begin to merge the byte[]s together and hash them, it stores the process work
	/*
	Name: genTree
	Description: Generates the Merkle root of the input
	Precondition: The ArrayList of byte[] must contain at least one element.
	Postcondition: Returns the Merkle root hash of the tree.
	*/
	public byte[] genTree() {
		while(hash.size()>1) {
			balanceTree();
			for(int i=0; i<hash.size()/2; i++) {
				hash.set(i, SHA256Hash.hash(ByteArray.merge(hash.get(i*2), hash.get((i*2)+1))));
			}
			trimList();
		}
		return hash.get(0);
	}
	/*
	Name: balanceTree
	Description: Makes the number of inputs even by duplicating the last hash if necessary.
	Precondition: None.
	Postcondition: If the number of hashes is odd, duplicates the last hash to make it even.
	*/
	private void balanceTree() { //Makes number of inputs even
		if(hash.size()%2==1) {
			hash.add(hash.get(hash.size()-1));
		}
	}
	/*
	Name: trimList
	Description: Chops off the second half of the hash list.
	Precondition: None.
	Postcondition: Second half of the hash list removed
	*/
	private void trimList() {
		int amountToChop=hash.size()/2;
		for(int i=hash.size()-1; i>=amountToChop; i--) {
			hash.remove(i);
		}
	}
	/*
	Name: MerkleTree
	Description: Initializes a MerkleTree instance with the given list of objects to hash.
	Precondition: toHash is not null
	Postcondition: The ArrayList of input objects has been hashed and stored in the ArrayList of byte[]s.
	*/
	public MerkleTree(ArrayList<T> toHash) {
		hash=new ArrayList<byte[]>();
		for(int i=0; i<toHash.size(); i++) {
			hash.add(i, toHash.get(i).hash());
		}
	}
}