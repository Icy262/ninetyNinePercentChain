package ninetyNinePercentChain.NetworkTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Block;
import ninetyNinePercentChain.Block.TransactionIn;
import ninetyNinePercentChain.Block.TransactionOut;
import ninetyNinePercentChain.Utils.BlockFile;

public class WaitForTransactionManager {
	private static ArrayList<WaitForTransaction> waiting=new ArrayList<WaitForTransaction>();
	/*
	Name: addWait
	Description: Adds a waiting thread to the list of waiting threads
	Precondition: None
	Postcondition: WaitForTransaction added to the list of waiting threads
	*/
	public static void addWait(WaitForTransaction waiter) {
		waiting.add(waiter);
	}
	/*
	Name: update
	Description: Checks if the incoming block matches the wait condition of any of the waiting theads. If it does, we notify that thread.
	Precondition: Block is a valid block
	Postcondition: Thread notified if its wait condition is met
	*/
	public static void update(Block block) {
		if(waiting.size()==0) {
			return;
		}
		for(int i=0; i<waiting.size(); i++) { //For each transaction waiting,
			for(int ii=0; ii<block.getNumTransactions(); ii++) { //For each transaction in the block,
				for(int iii=0; i<block.getTransaction(ii).getTOUTLength(); iii++) { //For each TOUT in the transaction,
					boolean sendAddressMet=false;
					boolean recieveAddressMet=false;
					boolean amountMet=false;
					WaitForTransaction currentWaiting=waiting.get(i);
					TransactionOut TOUT=block.getTransaction(ii).getTOUT(iii);
					TransactionIn TIN=block.getTransaction(ii).getTIN(iii);
					for(int iiii=0; iiii<block.getTransaction(ii).getTINLength(); iiii++) {
						if(Arrays.equals(BlockFile.getTOUT(TIN.getPreviousOutBlock(), TIN.getPreviousOutTransaction(), TIN.getPreviousOutOutputNumber()).getNextTransactionPublicKey(), currentWaiting.getSendAddress())) {
							sendAddressMet=true;
							break;
						}
					}
					if(Arrays.equals(TOUT.getNextTransactionPublicKey(), currentWaiting.getRecieveAddress())) {
						recieveAddressMet=true;
					}
					if(TOUT.getValue()==currentWaiting.getValue()) {
						amountMet=true;
					}
					if(sendAddressMet&&recieveAddressMet&&amountMet) {
						waiting.get(i).notify();
					}
				}
			}
		}
	}
}