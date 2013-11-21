package jp.co.wap.exam;

import java.util.*;

/**
 * The Queue class represents an immutable first-in-first-out (FIFO) queue of objects.
 * We use linked nodes. Each node has a data and reference of next node.
 * Keep track of first and last node.
 * We just enqueue new elements at tail and dequeue from head.
 * The idea is to use same nodes repeatedly as the queue changes
 * @param <E>
 * sukritsangwan@gmail.com
 */
public class PersistentQueue<E> {
	
	// node of the linked list
	private class Node<E> {
		E data;
		Node<E> nxt;
		
		Node(E e) {
			data = e;
			nxt = null;
		}
	}
	
	// keep track of head as well as tail for constant time insertion, deletion
	private Node<E> head;
	private Node<E> tail;
	// queue must be immutable, and since we are using same nodes repeatedly
	// so we do branching when a single node is involved in 2 or more queues
	private List<Node<E>> branchHeads;
	private List<Node<E>> branchTails;
	// keep track of size, so that we don't have to count
	private int siz;
	
	public PersistentQueue() {
		head = null;
		tail = null;
		siz = 0;
		branchHeads = new ArrayList<Node<E>>();
		branchTails = new ArrayList<Node<E>>();
	}
	
	// private constructor to return while enqueuing
	// add element to the tail, keep track of branches in case a single node is used in more than 1 queues separately
	private PersistentQueue(Node<E> h, Node<E> t, E e, int n, List<Node<E>> bHeads, List<Node<E>> bTails) {
		branchHeads = new ArrayList<Node<E>>(bHeads);
		branchTails = new ArrayList<Node<E>>(bTails);
		tail = new Node<E>(e);
		if(null != t) {
			// if nxt pointer of tail has already been assigned, do branching
			if(null == t.nxt)
				t.nxt = tail;
			else {
				branchTails.add(t);
				branchHeads.add(tail);
			}
		}
		head = h;
		if(null == head)
			head = tail;
		siz = n;
	}
	
	// private constructor to return in dequeue
	private PersistentQueue(Node<E> h, Node<E> t, int n, List<Node<E>> bHeads, List<Node<E>> bTails) {
		head = h;
		tail = t;
		if(null == h)
			tail = head;
		siz = n;
		branchHeads = new ArrayList<Node<E>>(bHeads);
		branchTails = new ArrayList<Node<E>>(bTails);
	}
	
	/**
	 * Returns the queue that adds an item into the tail of this queue without modifying this queue.
	 * <pre>
	 * e.g.
	 * When this queue represents the queue (2, 1, 2, 2, 6) and we enqueue the value 4 into this queue,
	 * this method returns a new queue (2, 1, 2, 2, 6, 4)
	 * and this object still represents the queue (2, 1, 2, 2, 6) .
	 * </pre>
	 * If the element e is null, throws IllegalArgumentException.
	 * @param e
	 * @return
	 * @throws IllegalArgumentException
	 */
	public PersistentQueue<E> enqueue(E e) {
		if(e == null)
			throw new IllegalArgumentException();
		return new PersistentQueue<E>(head, tail, e, siz + 1, branchHeads, branchTails);
	}
	/**
	 * Returns the queue that removes the object at the head of this queue without modifying this queue.
	 * <pre>
	 * e.g.
	 * When this queue represents the queue (7, 1, 3, 3, 5, 1) ,
	 * this method returns a new queue (1, 3, 3, 5, 1)
	 * and this object still represents the queue (7, 1, 3, 3, 5, 1) .
	 * </pre>
	 * if the tail of this queue has already been assigned next
	 * do branching, rather than reassigning the next pointer (to preserve immutability)
	 * If this queue is empty, throws java.util.NoSuchElementException.
	 * @return
	 * @throws NoSuchElementException
	 */
	public PersistentQueue<E> dequeue() {
		if(0 == siz)
			throw new NoSuchElementException();
		// check if branching has happened and we have reached a branch
		if(!branchTails.isEmpty() && branchTails.get(0) == head) {
			return new PersistentQueue(branchHeads.get(0), tail, siz - 1, branchHeads.subList(1, branchHeads.size()), branchTails.subList(1, branchTails.size()));
		}
		return new PersistentQueue(head.nxt, tail, siz - 1, branchHeads, branchTails);
	}
	
	/**
	 * Looks at the object which is the head of this queue without removing it from the queue.
	 * <pre>
	 * e.g.
	 * When this queue represents the queue (7, 1, 3, 3, 5, 1),
	 * this method returns 7 and this object still represents the queue (7, 1, 3, 3, 5, 1)
	 * </pre>
	 * If the queue is empty, throws java.util.NoSuchElementException.
	 * @return
	 * @throws NoSuchElementException
	 */
	public E peek() {
		if(0 == siz)
			throw new NoSuchElementException();
		return head.data;
	}
	
	/**
	 * Returns the number of objects in this queue.
	 * @return
	 */
	public int size() {
		return siz;
	}
}
