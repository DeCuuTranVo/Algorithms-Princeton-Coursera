import java.util.NoSuchElementException;

public class ArrayDeque {
    private String[] dequeArray;
    private Integer dequeSize;

    // construct an empty deque
    public ArrayDeque() {
        dequeArray = new String[1];
        dequeSize = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return dequeSize == 0;
    }

    // return the number of items on the deque
    public int size() {
        return dequeSize;
    }

    // Resize array to avoid overflow issues
    public void resize(int capacity) {
        String copy[] = new String[capacity];
        for (int i = 0; i < this.dequeSize; i++) {
            copy[i] = this.dequeArray[i];
        }
        this.dequeArray = copy;
    }

    // add the item to the front
    public void addFirst(String item) {
        if (this.isEmpty()) { // if empty deque
            dequeArray[0] = item;
            dequeSize++;
        } else { // if deque already has some nodes
            if (this.dequeSize == this.dequeArray.length) {
                resize(2 * this.dequeArray.length);
            }
            dequeArray[dequeSize] = item;
            dequeSize++;
        }
    }

    // // add the item to the back
    // public void addLast(Item item)

    // remove and return the item from the front
    public String removeFirst() {
        if (this.isEmpty()) { // if empty deque
            throw new NoSuchElementException("Empty Deque");
        } else {// deque already has some nodes
            this.dequeSize--;
            String removedItem = this.dequeArray[this.dequeSize];
            this.dequeArray[this.dequeSize] = null; // avoid loitering

            if ((this.dequeSize > 0) && (this.dequeSize == this.dequeArray.length / 4)) {
                resize(this.dequeArray.length / 2);
            }
            return removedItem;
        }
    }

    // // remove and return the item from the back
    // public Item removeLast()

    // // return an iterator over items in order from front to back
    // public Iterator<Item> iterator()

    // unit testing (required)
    public static void main(String[] args) {
        // Create empty deque
        ArrayDeque myArrayDeque = new ArrayDeque();

        // Test empty deque
        Boolean dequeEmpty = myArrayDeque.isEmpty();
        System.out.println("The deque is empty ? -> " + dequeEmpty);

        String firstItem = "one";
        String secondItem = "two";
        String thirdItem = "three";
        //// Test queue function
        // Add items to deque
        myArrayDeque.addFirst(firstItem);
        myArrayDeque.addFirst(secondItem);
        myArrayDeque.addFirst(thirdItem);

        // Test size of deque
        Integer dequeSize = myArrayDeque.size();
        System.out.println("Size of the deque: " + dequeSize);

        for (int i = 0; i < dequeSize; i++) {
            String removedItem = myArrayDeque.removeFirst();
            System.out.println(i);
            System.out.println(removedItem);
        }

    }

}