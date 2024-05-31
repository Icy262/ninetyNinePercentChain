package ninetyNinePercentChain.NetworkTransaction;

import java.util.ArrayList;
import java.util.Arrays;

import ninetyNinePercentChain.Block.Block;

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
		for(int i=0; i<waiting.size(); i++) {
			for(int ii=0; ii<block.getTOUTLength(); ii++) {
				if(Arrays.equals(waiting.get(i).getAddress(), block.getTOUT(ii).getNextTransactionPublicKey())&&waiting.get(i).getValue()==block.getTOUT(ii).getValue()) {
					waiting.get(i).notify();
				}
			}
		}
	}
}