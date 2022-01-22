package eus.blankcard.decklearn.gamification;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import eus.blankcard.decklearn.models.card.CardModel;

public class Buffer {

	List<CardModel> buffer;
	Lock mutex;
	Condition bufferFull, bufferEmpty;
	int inpos, outpos, bufferAmount;
	private final static int BUFFER_SIZE = 1;

	public Buffer() {

		buffer = new ArrayList<>();

		mutex = new ReentrantLock();
		bufferEmpty = mutex.newCondition();
		bufferFull = mutex.newCondition();

		inpos = 0;
		outpos = 0;
		bufferAmount = 0;
	}

	public void putValue(CardModel value) throws InterruptedException {
		mutex.lock();

		if (bufferAmount == BUFFER_SIZE) {
			bufferFull.await();
		}

		buffer.add(inpos, value);
		inpos = (inpos + 1) % BUFFER_SIZE;
		bufferAmount++;
		bufferEmpty.signalAll();

		mutex.unlock();
	}

	public CardModel getValue() throws InterruptedException {
		CardModel value;
		mutex.lock();

		if (bufferAmount == 0) {
			bufferEmpty.await();
		}

		value = buffer.get(outpos);
		outpos = (outpos + 1) % BUFFER_SIZE;
		bufferAmount--;
		bufferFull.signalAll();

		mutex.unlock();
		return value;
	}
}
