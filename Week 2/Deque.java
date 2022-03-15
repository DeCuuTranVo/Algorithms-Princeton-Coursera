import java.util.Iterator;
import java.util.NoSuchElementException;

// create a queue first -> do with stack
public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private Integer size;

    private class Node {
        Item item;
        Node previous;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        this.first = null;
        this.last = null;
        this.size = 0;
    }

    // // is the deque empty?
    public boolean isEmpty() {
        if (first == null) {
            return true;
        }
        return false;
    }

    // return the number of items on the deque
    public int size() {
        return this.size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null item");
        }

        if (this.isEmpty()) { // if deque is empty
            // Create a last node
            this.last = new Node();

            // Point the first reference to that node
            this.first = this.last;

            // Define that node
            this.first.item = item;
            this.first.previous = null;
            this.first.next = null;
        } else {
            // Hold a reference to the oldFirst node
            Node oldFirst = this.first;

            // Create a new node
            this.first = new Node();

            // Define the new node
            this.first.item = item;

            // The first node have no precedent node
            this.first.previous = null;

            // Point the first node to the oldFirst node
            this.first.next = oldFirst;

            // Point the old first node toward the first node
            oldFirst.previous = this.first;
        }
        // increase the size of deque
        this.size++;
    }

    // add the item to the back
    public void addLast(Item item) { // Enqueue
        if (item == null) {
            throw new IllegalArgumentException("Null item");
        }

        if (this.isEmpty()) { // if empty deque
            // Create first node
            this.first = new Node();

            // Point the last reference to that node
            this.last = this.first;

            // Define that node
            this.last.item = item;
            this.last.previous = null;
            this.last.next = null;
        } else { // if deque already have some nodes
            // Create reference to a current last node
            Node oldLast = this.last;

            // Create a new last node (node added in last position)
            this.last = new Node();
            this.last.item = item;
            this.last.previous = oldLast;

            // The last node have no subsequent node
            this.last.next = null;

            // Point the current last node toward the new last node
            oldLast.next = this.last;
        }
        this.size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) { // if empty deque
            throw new NoSuchElementException("Empty deque");
        } else if (this.size() == 1) { // if deque only have 1 node // dont worry about previous-next relationship
            // Store the only node
            Node oldFirstNode = this.first;
            // Remove first and last reference to that node
            this.first = null;
            this.last = null;
            // Reduce size of deque
            this.size--;
            // Return value of the stored node
            return oldFirstNode.item;
        } else { // if deque have some nodes
            // Store the first node
            Node oldFirstNode = this.first;
            // Point the first reference to the secondFirst node
            this.first = this.first.next;
            // Remove reference from new first node to the prevous first node
            this.first.previous = null;
            // Reduce size of deque
            this.size--;
            // Return value of the stored node
            return oldFirstNode.item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) { // Empty deque
            throw new NoSuchElementException("Empty deque");
        } else if (this.size() == 1) { // Only one node in deque
            // Store the only node
            Node oldLastNode = this.last;
            // Remove first and last reference to that node
            this.first = null;
            this.last = null;
            // Decrease size count
            this.size--;
            // Return the stored node's item
            return oldLastNode.item;
        } else {
            // Store the last node
            Node oldLastNode = this.last;
            // Point the last reference to the secondLast node
            this.last = this.last.previous;
            // Remove reference from new last node to the following last node
            this.last.next = null;
            // Reduce size of the deque
            this.size--;
            // Return value of the stored node
            return oldLastNode.item;
        }
    }

    // return an iterator over items in the deque
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No following node left");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        // Create empty deque
        Deque<String> myDeque = new Deque<String>();

        // Test empty deque
        Boolean dequeEmpty = myDeque.isEmpty();
        System.out.println("The deque is empty ? -> " + dequeEmpty);

        String firstItem = "one";
        String secondItem = "two";
        String thirdItem = "three";
        //// Test queue function
        // Add items to deque
        myDeque.addLast(firstItem);
        myDeque.addLast(secondItem);
        myDeque.addLast(thirdItem);

        // Test size of deque
        Integer dequeSize = myDeque.size();
        System.out.println("Size of the deque: " + dequeSize);

        // Print the items of the deque
        Iterator<String> i = myDeque.iterator();
        while (i.hasNext()) {
            String s = i.next();
            System.out.println(s);
        }

        // Remove items from deque
        myDeque.removeFirst();
        myDeque.removeFirst();
        myDeque.removeFirst();

        // Test size of deque
        dequeSize = myDeque.size();
        System.out.println("Size of the deque: " + dequeSize);

        // Print the items of the deque
        for (String s : myDeque) {
            System.out.println(s);
        }

        // //// Test stack function and removeLast
        // Add items to deque
        myDeque.addFirst(firstItem);
        myDeque.addFirst(secondItem);
        myDeque.addFirst(thirdItem);

        // Test size of deque
        dequeSize = myDeque.size();
        System.out.println("Size of the deque: " + dequeSize);

        // Print the items of the deque
        for (String s : myDeque) {
            System.out.println(s);
        }

        // Remove items from deque
        myDeque.removeLast();
        myDeque.removeLast();
        myDeque.removeLast();

        // Test size of deque
        dequeSize = myDeque.size();
        System.out.println("Size of the deque: " + dequeSize);

        // Print the items of the deque
        for (String s : myDeque) {
            System.out.println(s);
        }

    }
}