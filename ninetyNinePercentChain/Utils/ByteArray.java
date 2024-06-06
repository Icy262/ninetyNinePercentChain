package ninetyNinePercentChain.Utils;
public class ByteArray {
	/*
	Name: merge
	Description: Takes two byte arrays as input, merges them, at returns the merged byte arrays as output.
	Precondition: Arrays are not null.
	Postcondition: array2 appended to end of array1 and returned
	*/
	public static byte[] merge(byte[] array1, byte[] array2) {
		byte[] mergedArray=new byte[array1.length+array2.length]; //Creates a new byte[] that is large enough to hold both arrays
		for(int i=0; i<array1.length; i++) { //Iterates through array1,
			mergedArray[i]=array1[i]; //Copies the data from array1 to merged array
		}
		for(int i=array1.length; i<mergedArray.length; i++) { //Starts at the end of the first array. Iterates through array2,
			mergedArray[i]=array2[i-array1.length]; //Copies the data from array2 into the merged array. The index used for array2 is i-array1.length because we start with i=array1.length.
		}
		return mergedArray; //Return the merged array
	}
}