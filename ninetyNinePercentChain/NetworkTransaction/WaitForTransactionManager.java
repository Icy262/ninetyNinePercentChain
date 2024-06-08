package ninetyNinePercentChain.NetworkTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.Transaction;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Utils.BlockFile;

/*
 * Keeps a list of waiting threads. Every time we get a new block, we check each of the waiting conditions to see if they match the block. If the condition of a waiting thread is met, we notify it.
 */

public class WaitForTransactionManager {
	private static ArrayList<WaitForTransaction> waiting=new ArrayList<WaitForTransaction>();
	/*
	Name: addWait
	Description: Adds a waiting thread to the list of waiting threads
	Precondition: None
	Postcondition: WaitForTransaction added to the list of waiting threads
	*/
	public static void addWait(WaitForTransaction waiter) {
		waiting.add(waiter); //Adds the WaitForTransaction to the list of threads waiting for transactions
	}
	/*
	Name: update
	Description: Checks if the incoming block matches the wait condition of any of the waiting theads. If it does, we notify that thread.
	Precondition: Block is a valid block
	Postcondition: Thread notified if its wait condition is met
	*/
	public static void update(Block block) {
		for(int i=0; i<waiting.size(); i++) { //For each thread waiting,
			for(int ii=0; ii<block.getNumTransactions(); ii++) { //For each transaction in the block,
				Transaction currentTransaction=block.getTransaction(ii); //The current transaction we are checking. Saves having to get it multiple times
				boolean sendAddressMet=false;
				boolean recieveAddressMet=false;
				boolean amountMet=false;
				WaitForTransaction currentWaiting=waiting.get(i);
				for(int iii=0; iii<currentTransaction.getTINLength(); iii++) { //For each TIN,
					TransactionIn TIN=currentTransaction.getTIN(iii); //Buffer for current TIN we are checking. This saves us from needing to get the TIN multiple times
					if(Arrays.equals(BlockFile.getTOUT(TIN.getPreviousOutBlock(), TIN.getPreviousOutTransaction(), TIN.getPreviousOutOutputNumber()).getNextTransactionPublicKey(), currentWaiting.getSendAddress())) { //If the TOUT address matches the send address,
						sendAddressMet=true; //Sets sendAddressMet flag to true
						break; //Matches, so no need to check the rest of the TINs
					}
				}
				for(int iii=0; iii<currentTransaction.getTOUTLength(); iii++) { //For each TOUT,
					TransactionOut TOUT=currentTransaction.getTOUT(iii); //Buffer for the current TOUT we are checking. This saves us from needing to get the TOUT multiple times
					if(Arrays.equals(TOUT.getNextTransactionPublicKey(), currentWaiting.getRecieveAddress())) { //If the recieve address matches the TOUT address,
						recieveAddressMet=true; //Sets recieveAddressMet flag to true
						if(TOUT.getValue()==currentWaiting.getValue()) { //If the value of the matching TOUT is the correct value,
							amountMet=true; //Sets amountMet flag to true
							break; //Matches, so no need to check the rest of the TOUTs
						}
					}
				}
				if(sendAddressMet&&recieveAddressMet&&amountMet) { //If sender, reciever, and value are all correct,
					synchronized(currentWaiting) { //Allows us to call notify on this thread
						currentWaiting.notify(); //Wakes the thread up, because the transaction has been recieved
					}
				}
			}
		}
	}
}