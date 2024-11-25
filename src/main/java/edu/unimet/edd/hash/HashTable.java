package edu.unimet.edd.hash;

import edu.unimet.edd.listeners.HashTableListener;
import edu.unimet.edd.tree.GenericSet;
import edu.unimet.edd.utils.Person;
import edu.unimet.edd.tree.Tree;
import edu.unimet.edd.utils.LinkedListListeners;
import edu.unimet.edd.utils.LoadJson;

/**
 * HashTable class that implements a hash table using chaining for collision
 * handling. The table uses LinkedLists to store entries at each index in case
 * of collisions.
 */
public class HashTable {

    private static final int DEFAULT_CAPACITY = 16; // Default initial capacity
    private LinkedList[] table; // Array of LinkedLists to store entries
    private int size; // Current size of the table
    private double loadFactor; // Load factor for resizing
    private LinkedListListeners listeners;
    private BucketLinkedList[] buckets;
    private static HashTable instance;
    private int capacity;

    /**
     * Constructor to initialize the hash table with default capacity.
     */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor to initialize the hash table with a specific capacity.
     *
     * @param capacity The initial capacity of the hash table.
     */
    public HashTable(int capacity) {
        this.table = new LinkedList[capacity];
        this.buckets = new BucketLinkedList[capacity];
        this.size = 0;
        this.loadFactor = 0.75; // Default load factor
//        this.listeners = new ListenerLinkedList(); // Initialize listeners list
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList(); // Initialize each bucket with an empty LinkedList
        }

        // Initialize each bucket
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new BucketLinkedList();
        }

        this.listeners = new LinkedListListeners();

    }

    
   /**
    * Returns the singleton instance of the HashTable class. If the instance does
    * not exist, it creates a new one and returns it.
    * <p>
    * This method ensures that only one instance of the HashTable is created and
    * reused throughout the application.
    * </p>
    *
    * @return the singleton instance of the HashTable.
    */
    public static HashTable getInstance() {
        if (instance == null) {
            instance = new HashTable();
        }
        return instance;
    }

    /**
     * Synchronizes the data from another HashTable into this one.
     *
     * @param source The source HashTable to copy data from.
     */
    public void syncData(HashTable source) {
        if (source == null) {
            return;
        }

        // Clear all buckets
        for (int i = 0; i < capacity; i++) {
            buckets[i].clear();
        }

        // Copy data from source buckets
        for (int i = 0; i < source.capacity; i++) {
            BucketLinkedList sourceBucket = source.buckets[i];
            BucketLinkedList clonedBucket = sourceBucket.cloneBucket();
            buckets[i] = clonedBucket;
        }
    }

    /**
     * Adds a listener to the HashTable.
     *
     * @param listener The listener to add.
     */
    public void addListener(HashTableListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all registered listeners of a HashTable update.
     */
    private void notifyListeners() {
        listeners.notifyListeners();
    }

    /**
     * Removes duplicate entries from the HashTable. If two or more persons have
     * the same name, only the first one found will remain in the table, and the
     * others will be removed.
     */
    public void removeDuplicates() {
        Person[] allPeople = getAllPeople();
        GenericSet<String> seenNames = new GenericSet<>();

        for (Person person : allPeople) {
            if (person == null || person.getName() == null) {
                continue; // Ignorar personas nulas
            }

            if (seenNames.contains(person.getName())) {
                boolean removed = remove(person.getName());
                if (removed) {
                    System.out.println("Removed duplicate: " + person.getName());
                }
            } else {
                seenNames.add(person.getName());
            }
        }
    }

    /**
     * Method to update the father's name in each person’s setFather attribute.
     * This method will use the father's name and construct the full name to
     * update the `setFather` of the child.
     */
//    public void updateFatherNames() {
        // Get all people stored in the hash table
//        Person[] allPeople = getAllPeople();
//
//        // Iterate over each person
//        for (Person person : allPeople) {
//            if (person != null && person.getFather() != null) {
//                // Get the father's name from the person
//                String fatherName = person.getFather();
//
//                // Fetch the father object from the hash table using the father's name
//                Person father = get(normalizeName(fatherName));
//                
//                if (father == null) {
//                    System.out.println("El padre de: " + person.getName() + " Es null");
//                }
//
//                // If the father exists in the table
//                if (father != null) {
//                    // Construct the full name of the father
//                    String fatherFullName = getFullName(father.getName(), father.getOfHisName());
//
//                    // Update the child's setFather with the full name of the father
//                    person.setFather(fatherFullName);
//
//                    // Put the updated person back into the hash table
//                    put(person.getName(), person);
//                }
//            }
//        }
//    }

    
    /**
     * Normalizes a name by standardizing the format for comparison. This method
     * handles names with commas and other special characters.
     *
     * @param name The name to normalize.
     * @return The normalized name.
     */
    private String normalizeName(String name) {
        if (name == null) {
            return null;
        }
        // Remove commas and extra spaces, then convert to lowercase for consistent comparison
        return name.trim().replace(",", "").toLowerCase();
    }
    
    /**
     * Constructs the full name of a person by combining their name and "Of his
     * name" value.
     *
     * @param name The base name of the person.
     * @param ofHisName The value of "Of his name" (e.g., "First").
     * @return The full name in the format "name, [Of his name] of his name".
     */
    private String getFullName(String name, String ofHisName) {
        if (ofHisName == null || ofHisName.isEmpty()) {
            return name; // If "Of his name" is not provided, return the base name
        }
        return name + ", " + ofHisName + " of his name";
    }

    /**
     * Method to insert a key-value pair into the hash table. If the key already
     * exists, it updates the value.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     */
    public void put(String key, Person value) {
        if (size >= table.length * loadFactor) {
            resize(table.length * 2); // Resize if load factor is exceeded
        }

        int index = getIndex(key);
        LinkedList bucket = table[index];

        // Avoid duplicates: If key exists, update the existing entry
        if (!bucket.containsKey(key)) {
            bucket.add(key, value);  // Only add if key does not already exist
            size++;
            notifyListeners(); // Notify listeners after the update
        }
    }

    /**
     * Method to retrieve a value by its key.
     *
     * @param key The key of the entry.
     * @return The value associated with the key, or null if not found.
     */
    public Person get(String key) {
        int index = getIndex(key);
        return table[index].get(key);
    }

    /**
     * Method to check if a key exists in the hash table.
     *
     * @param key The key to search for.
     * @return true if the key exists, false otherwise.
     */
    public boolean containsKey(String key) {
        int index = getIndex(key);
        return table[index].containsKey(key);
    }

    /**
     * Resize the hash table when the load factor threshold is exceeded.
     *
     * @param newCapacity The new capacity of the table after resizing.
     */
    private void resize(int newCapacity) {
        LinkedList[] newTable = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new LinkedList();
        }

        // Rehashing all existing entries into the new table
        for (int i = 0; i < table.length; i++) {
            LinkedList bucket = table[i];
            Node current = bucket.getFirstNode(); // Accessing first node directly
            while (current != null) {
                int newIndex = getIndex(current.getValue().getKey(), newCapacity);
                newTable[newIndex].add(current.getValue().getKey(), current.getValue().getValue());
                current = current.getNext();
            }
        }

        table = newTable;
    }

    /**
     * Calculate the index for a given key based on the current capacity.
     *
     * @param key The key to calculate the index for.
     * @param capacity The capacity of the table.
     * @return The index for the key.
     */
    private int getIndex(String key, int capacity) {
        return Math.abs(key.hashCode()) % capacity;
    }

    /**
     * Calculate the index for a given key based on the current table length.
     *
     * @param key The key to calculate the index for.
     * @return The index for the key.
     */
    private int getIndex(String key) {
        return getIndex(key, table.length);
    }

    /**
     * Get the size of the hash table (number of entries).
     *
     * @return The current size of the hash table.
     */
    public int size() {
        return size;
    }
    
    /**
     * Checks if the hash table is empty.
     *
     * @return true if the table is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Clears all entries from the hash table.
     */
    public void removeAll() {
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList(); // Replace each bucket with a new empty LinkedList
        }
        size = 0; // Reset the size to zero
    }

    /**
     * Retrieves all people stored in the hash table.
     *
     * @return An array of Person objects stored in the table.
     */
    public Person[] getAllPeople() {
        // Calculate the total number of people
        int totalPeople = 0;
        for (LinkedList bucket : table) {
            if (bucket != null) {
                totalPeople += bucket.getSize(); // Count elements in each bucket
            }
        }

        // Create an array of the required size
        Person[] peopleArray = new Person[totalPeople];

        // Populate the array with people from the hash table
        int index = 0;
        for (LinkedList bucket : table) {
            if (bucket != null) {
                Node current = bucket.getFirstNode();
                while (current != null) {
                    // Validate nodes and values before adding to the array
                    if (current.getValue() != null && current.getValue().getValue() != null) {
                        peopleArray[index++] = current.getValue().getValue();
                    } else {
                        System.out.println("Skipping null or invalid node in bucket");
                    }
                    current = current.getNext();
                }
            }
        }

        // If there were invalid entries, the array might not be fully filled
        if (index < totalPeople) {
            System.out.println("Resizing array due to skipped invalid entries");
            Person[] validPeople = new Person[index];
            System.arraycopy(peopleArray, 0, validPeople, 0, index);
            return validPeople;
        }

        return peopleArray;
    }

    /**
     * Returns an array of all values in the HashTable.
     *
     * @return An array of all values stored in the HashTable.
     */
    public Person[] values() {
        Person[] values = new Person[size]; // Create an array to store all values
        int index = 0;

        // Iterate through each bucket in the hash table
        for (LinkedList bucket : table) {
            if (bucket != null) { // Check if the bucket is not null
                Node current = bucket.getFirstNode(); // Get the first node of the bucket
                while (current != null) {
                    values[index++] = current.getValue().getValue(); // Add the Person value to the array
                    current = current.getNext(); // Move to the next node in the list
                }
            }
        }

        return values; // Return the array of values
    }

    /**
     * Retrieves all keys stored in the hash table.
     *
     * @return An array of keys stored in the hash table.
     */
    public String[] getKeys() {
        // Create an array to store the keys
        String[] keys = new String[size];
        int index = 0;

        // Iterate through each bucket in the table
        for (LinkedList bucket : table) {
            if (bucket != null) { // Check if the bucket is not null
                Node current = bucket.getFirstNode(); // Get the first node of the bucket
                while (current != null) {
                    keys[index++] = current.getValue().getKey(); // Add the key to the array
                    current = current.getNext(); // Move to the next node in the list
                }
            }
        }

        return keys; // Return the array of keys
    }

    /**
     * Method to remove a person by key from the hash table.
     *
     * @param key The key of the entry to remove.
     * @return True if the entry was removed, false otherwise.
     */
    public boolean remove(String key) {
        int index = getIndex(key);
        LinkedList bucket = table[index];
        Node current = bucket.getFirstNode();
        while (current != null) {
            if (current.getValue().getKey().equals(key)) {
                Person removedValue = current.getValue().getValue();
                bucket.remove(current.getValue().getKey()); // Remove node with the key
                size--;
                notifyListeners(); // Notify listeners after the update

                return true;
            }
            current = current.getNext();
        }
        return false; // Key not found
    }
}
