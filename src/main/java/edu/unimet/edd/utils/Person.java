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
    private String ofHisName;
    private String eyesColor = null;
    private String hairColor = null;
    private String notes = null;
    private String wedTo = null;
    private PersonLinkedList children;

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
    public Person(String name, String title, String nickname, String father, String mother, String fate, String ofHisName, String eyesColor, String hairColor, String notes, String wedTo, PersonLinkedList children) {
        this.name = name;
        this.title = title;
        this.nickname = nickname;
        this.father = father;
        this.mother = mother;
        this.fate = fate;
        this.ofHisName = ofHisName;
        this.eyesColor = eyesColor;
        this.hairColor = hairColor;
        this.notes = notes;
        this.wedTo = wedTo;
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
            return currentIndex < getChildren().size();
        }

        @Override
        public String next() {
            if (hasNext()) {
                String child = getChildren().get(currentIndex);
                currentIndex++;
                return child;
            }
            return null;
        }
    }

    /**
     * Retrieves the details of this person in a formatted string if the given
     * name matches this person's name.
     *
     * @param name The name of the person to retrieve details for.
     * @return A formatted string containing the person's details, or null if
     * the name does not match.
     */
    public String getDetailsByName(String name) {
        if (this.getName().equalsIgnoreCase(name)) {
            StringBuilder details = new StringBuilder();
            details.append("Details of the person: \n");
            details.append("Name: ").append(this.getName()).append("\n");
            details.append("Title: ").append(this.getTitle() != null ? this.getTitle() : "None").append("\n");
            details.append("Nickname: ").append(this.getNickname() != null ? this.getNickname() : "None").append("\n");
            details.append("Father: ").append(this.getFather() != null ? this.getFather() : "Unknown").append("\n");
            details.append("Mother: ").append(this.getMother() != null ? this.getMother() : "Unknown").append("\n");
            details.append("Fate: ").append(this.getFate() != null ? this.getFate() : "Unknown").append("\n");
            details.append("Of His Name: ").append(this.getOfHisName() != null ? this.getOfHisName() : "Unknown").append("\n");
            details.append("Eyes color: ").append(this.getEyesColor() != null ? this.getEyesColor() : "Unknown").append("\n");
            details.append("Hair color: ").append(this.getHairColor() != null ? this.getHairColor() : "Unknown").append("\n");
            details.append("Notes: ").append(this.getNotes() != null ? this.getNotes() : "Unknown").append("\n");
            details.append("Wed to: ").append(this.getWedTo() != null ? this.getWedTo() : "Unknown").append("\n");

            
            details.append("Children: ");
            if (this.getChildren() != null && this.getChildren().size() > 0) {
                for (int i = 0; i < this.getChildren().size(); i++) {
                    details.append(this.getChildren().get(i));
                    if (i < this.getChildren().size() - 1) {
                        details.append(", ");
                    }
                }
            } else {
                details.append("None");
            }

            return details.toString();
        }
        return null; // Name does not match
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
            this.setName(newPerson.getName());
        }
        if (newPerson.getTitle() != null && !newPerson.getTitle().isEmpty()) {
            this.setTitle(newPerson.getTitle());
        }
        if (newPerson.getNickname() != null && !newPerson.getNickname().isEmpty()) {
            this.setNickname(newPerson.getNickname());
        }
        if (newPerson.getFather() != null && !newPerson.getFather().isEmpty()) {
            this.setFather(newPerson.getFather());
        }
        if (newPerson.getMother() != null && !newPerson.getMother().isEmpty()) {
            this.setMother(newPerson.getMother());
        }
        if (newPerson.getFate() != null && !newPerson.getFate().isEmpty()) {
            this.setFate(newPerson.getFate());
        }
        if (newPerson.getOfHisName() != null && !newPerson.getOfHisName().isEmpty()) {
            this.setOfHisName(newPerson.getOfHisName());
        }
        if (newPerson.getChildren() != null && newPerson.getChildren().size() > 0) {
            this.setChildren(newPerson.getChildren());
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

        PersonLinkedList currentChildren = parent.getChildren();
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
    public PersonLinkedList getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(PersonLinkedList children) {
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
     * @return the eyesColor
     */
    public String getEyesColor() {
        return eyesColor;
    }

    /**
     * @param eyesColor the eyesColor to set
     */
    public void setEyesColor(String eyesColor) {
        this.eyesColor = eyesColor;
    }

    /**
     * @return the hairColor
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * @param hairColor the hairColor to set
     */
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the wedTo
     */
    public String getWedTo() {
        return wedTo;
    }

    /**
     * @param wedTo the wedTo to set
     */
    public void setWedTo(String wedTo) {
        this.wedTo = wedTo;
    }

}
