package edu.unimet.edd.utils;

import edu.unimet.edd.hash.HashTable;

/**
 * Represents a person in the genealogy tree.
 */
public class Person {

    private String name;
    private String title;
    private String nickname;
    private String father;
    private String mother;
    private String fate;
    private LinkedList children;
    private String ofHisName;

    /**
     * Constructs a new Person object.
     *
     * @param name The name of the person.
     * @param title The title held by the person.
     * @param nickname The nickname of the person.
     * @param father The name of the person's father.
     * @param mother The name of the person's mother.
     * @param fate The fate or notes about the person.
     * @param ofHisName The "Of his name" value for the person.
     * @param children A list of the person's children.
     */
    public Person(String name, String title, String nickname, String father, String mother, String fate, String ofHisName, LinkedList children) {
        this.name = name;
        this.title = title;
        this.nickname = nickname;
        this.father = father;
        this.mother = mother;
        this.fate = fate;
        this.ofHisName = ofHisName;
        this.children = children;
    }

    /**
     * Returns an iterator for the children list.
     *
     * @return An iterator for the children list.
     */
    public PersonIterator iterator() {
        return new PersonIterator();
    }

    /**
     * Iterator for iterating over the children.
     */
    public class PersonIterator implements Iterator<String> {

        private int currentIndex = 0;

        @Override
        public boolean hasNext() {
            return currentIndex < children.size();
        }

        @Override
        public String next() {
            if (hasNext()) {
                String child = children.get(currentIndex);
                currentIndex++;
                return child;
            }
            return null;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the father
     */
    public String getFather() {
        return father;
    }

    /**
     * @param father the father to set
     */
    public void setFather(String father) {
        this.father = father;
    }

    /**
     * @return the mother
     */
    public String getMother() {
        return mother;
    }

    /**
     * @param mother the mother to set
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    /**
     * @return the fate
     */
    public String getFate() {
        return fate;
    }

    /**
     * @param fate the fate to set
     */
    public void setFate(String fate) {
        this.fate = fate;
    }

    /**
     * @return the children
     */
    public LinkedList getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(LinkedList children) {
        this.children = children;
    }

    /**
     * @return the ofHisName
     */
    public String getOfHisName() {
        return ofHisName;
    }

    /**
     * @param ofHisName the ofHisName to set
     */
    public void setOfHisName(String ofHisName) {
        this.ofHisName = ofHisName;
    }

    /**
     * Updates the data of this Person object with new data from another Person
     * object. Only non-null and non-empty fields in the new Person are used for
     * the update.
     *
     * @param newPerson The Person object containing new data to update this
     * object.
     */
    public void updateData(Person newPerson) {
        if (!newPerson.getName().isEmpty()) {
            this.name = newPerson.getName();
        }
        if (newPerson.getTitle() != null && !newPerson.getTitle().isEmpty()) {
            this.title = newPerson.getTitle();
        }
        if (newPerson.getNickname() != null && !newPerson.getNickname().isEmpty()) {
            this.nickname = newPerson.getNickname();
        }
        if (newPerson.getFather() != null && !newPerson.getFather().isEmpty()) {
            this.father = newPerson.getFather();
        }
        if (newPerson.getMother() != null && !newPerson.getMother().isEmpty()) {
            this.mother = newPerson.getMother();
        }
        if (newPerson.getFate() != null && !newPerson.getFate().isEmpty()) {
            this.fate = newPerson.getFate();
        }
        if (newPerson.getOfHisName() != null && !newPerson.getOfHisName().isEmpty()) {
            this.ofHisName = newPerson.getOfHisName();
        }
        if (newPerson.getChildren() != null && newPerson.getChildren().size() > 0) {
            this.children = newPerson.getChildren();
        }
    }

    /**
     * Extracts the first name from a full name string.
     *
     * @param fullName The full name string.
     * @return The first name extracted from the full name.
     */
    private String getFirstName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split("\\s+");
        return parts[0]; // Returns the first part of the name
    }

    /**
     * Checks if a child is already present in the parent's list of children. If
     * a duplicate is found, it removes the existing child from both the
     * parent's children list and the HashTable.
     *
     * @param parent The parent Person object.
     * @param childName The name of the child to check.
     * @param hashTable The HashTable containing all the persons (for removal).
     * @return True if the child was a duplicate (and removed from both the list
     * and HashTable), false otherwise.
     */
    public String checkDuplicateChild(Person parent, String childName, HashTable hashTable) {
        if (parent == null) {
            return null; // No parent means no duplicates
        }

        String newChildFirstName = getFirstName(childName);
//        System.out.println("Trying to add: " + childName);

        LinkedList currentChildren = parent.getChildren();
//        System.out.println("Showing father: " + parent.getName());
        if (currentChildren == null) {
            return null; // No children list means no duplicates
        }

//        System.out.println("Showing children: ");
        for (int i = 0; i < currentChildren.size(); i++) {
//            System.out.println("lista de hijos: " + currentChildren.get(i));
        }

        // Check for duplicates
        for (int i = 0; i < currentChildren.size(); i++) {
            String existingChildFirstName = getFirstName(currentChildren.get(i));
            String currentChild = currentChildren.get(i);
            

            // If duplicate is found, remove the existing child from both the list and HashTable
            if (newChildFirstName.equalsIgnoreCase(existingChildFirstName)) {
//                System.out.println("Removed duplicate: " + currentChild.toLowerCase() + " his father is: " +  parent.getName());
                return currentChild.toLowerCase(); // A duplicate was found and removed from both the list and HashTable
            }
        }

        return null; // No duplicate found
    }

}
