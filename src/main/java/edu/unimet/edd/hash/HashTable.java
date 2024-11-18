package edu.unimet.edd.hash;

import edu.unimet.edd.utils.Person;

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
        this.size = 0;
        this.loadFactor = 0.75; // Default load factor
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList(); // Initialize each bucket with an empty LinkedList
        }
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
     * Retrieves all people stored in the hash table.
     *
     * @return An array of Person objects stored in the table.
     */
    public Person[] getAllPeople() {
        // Calcular el tamaño total necesario
        int totalPeople = 0;
        for (LinkedList bucket : table) {
            if (bucket != null) {
                totalPeople += bucket.getSize(); // Contar elementos en cada bucket
            }
        }

        // Crear un arreglo estático del tamaño necesario
        Person[] peopleArray = new Person[totalPeople];

        // Llenar el arreglo con las personas de la tabla hash
        int index = 0;
        for (LinkedList bucket : table) {
            if (bucket != null) {
                Node current = bucket.getFirstNode();
                while (current != null) {
                    peopleArray[index++] = current.getValue().getValue(); // Agregar Persona al arreglo
                    current = current.getNext();
                }
            }
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

//    public void printTableContents() {
//        System.out.println("HashTable contents:");
//        for (Person person : table.getAllPeople()) {
//            System.out.println("- " + person.getName());
//        }
//    }
}