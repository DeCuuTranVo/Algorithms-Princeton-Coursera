import java.util.NoSuchElementException;
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queueArray;
    private Integer storageSize;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queueArray = (Item[]) new Object[1];
        storageSize = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return storageSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return storageSize;
    }

    // Resize array to avoid overflow issues
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.storageSize; i++) {
            copy[i] = this.queueArray[i];
        }
        this.queueArray = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item");
        }
        if (this.isEmpty()) { // if empty deque
            queueArray[0] = item;
            storageSize++;
        } else { // if deque already has some nodes
            if (this.storageSize == this.queueArray.length) {
                resize(2 * this.queueArray.length);
            }
            queueArray[storageSize] = item;
            storageSize++;
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) { // if empty queue
            throw new NoSuchElementException("Empty Queue");
        } else {// queue already has some nodes
            // Chose a random position to remove
            Integer randomPosition = StdRandom.uniform(this.storageSize);
            // System.out.println("randomPosition: " + randomPosition);

            // Reduce storage size, lower the pointer in the array
            this.storageSize--;

            // Get the removed item
            Item removedItem = this.queueArray[randomPosition];
            // Clear the reference to that removed item
            if (randomPosition == this.storageSize) { // If the final position is chosen
                this.queueArray[randomPosition] = null; // avoid loitering
            } else { // If other positions are chosen
                this.queueArray[randomPosition] = this.queueArray[this.storageSize]; // swap position to ensure the
                                                                                     // continuity of array content
                this.queueArray[this.storageSize] = null; // avoid loitering
            }

            // Resize the queue to save memory
            if ((this.storageSize > 0) && (this.storageSize == this.queueArray.length / 4)) {
                resize(this.queueArray.length / 2);
            }
            // Return the removed item
            return removedItem;
        }
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) { // if empty queue
            throw new NoSuchElementException("Empty Deque");
        } else {// queue already has some nodes
            // Chose a random position to sampled
            Integer randomPosition = StdRandom.uniform(this.storageSize);
            // System.out.println("randomPosition: " + randomPosition);

            // Get the sampled item
            Item removedItem = this.queueArray[randomPosition];

            // Return the sampled item
            return removedItem;
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ReverseArrayIterator();
    }

    private class ReverseArrayIterator implements Iterator<Item> {
        // Create a permutation array (permutate the index sequence)
        private int[] randomArray = StdRandom.permutation(storageSize);
        // Let the current position at the end of the random array
        private int currentPosition = randomArray.length;

        public boolean hasNext() {
            // If the current position does not go to the end of the random array, return
            // tree
            return currentPosition > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No following node left");
            }
            // Decrease the current position
            currentPosition--;
            // Go to the random element on the queue array based on the permutation order
            return queueArray[randomArray[currentPosition]];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Create empty deque
        RandomizedQueue<String> myRandomQueue = new RandomizedQueue<String>();

        // Test empty deque
        Boolean queueEmpty = myRandomQueue.isEmpty();
        System.out.println("The deque is empty ? -> " + queueEmpty);

        String firstItem = "one";
        String secondItem = "two";
        String thirdItem = "three";
        String fourthItem = "four";
        String fifthItem = "five";
        String sixthItem = "six";
        String seventhItem = "seven";

        //// Test queue function
        // Add items to deque
        myRandomQueue.enqueue(firstItem);
        myRandomQueue.enqueue(secondItem);
        myRandomQueue.enqueue(thirdItem);
        myRandomQueue.enqueue(fourthItem);
        myRandomQueue.enqueue(fifthItem);
        myRandomQueue.enqueue(sixthItem);
        myRandomQueue.enqueue(seventhItem);

        // Test size of deque
        Integer queueSize = myRandomQueue.size();
        System.out.println("Size of the deque: " + queueSize);

        // Test iterable method trial 1
        StdOut.println("Test iterable section 1");
        Iterator<String> index_one = myRandomQueue.iterator();
        while (index_one.hasNext()) {
            String s = index_one.next();
            StdOut.println(s);
        }

        // Test iterable method trial 2
        StdOut.println("Test iterable section 2");
        Iterator<String> index_two = myRandomQueue.iterator();
        while (index_two.hasNext()) {
            String s = index_two.next();
            StdOut.println(s);
        }

        // Test sample method
        StdOut.println("Test sample section");
        for (int i = 0; i < queueSize; i++) {
            String sampledItem = myRandomQueue.sample();
            // System.out.println(i);
            System.out.println(sampledItem);
            // System.out.println("Size of the deque: " + myRandomQueue.size());
        }

        // Test dequeue method
        StdOut.println("Test dequeue section");
        for (int i = 0; i < queueSize; i++) {
            String removedItem = myRandomQueue.dequeue();
            // System.out.println(i);
            System.out.println(removedItem);
            // System.out.println("Size of the deque: " + myRandomQueue.size());
        }

        // // Test permutation
        // StdOut.println("Test permutation");
        // int[] randomArray = StdRandom.permutation(7); //
        // for (int i = 0; i < randomArray.length; i++) {
        // System.out.println(randomArray[i]);
        // }
    }

}