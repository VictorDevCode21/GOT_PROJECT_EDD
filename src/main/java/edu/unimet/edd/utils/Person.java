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
    private Integer generation = null;
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
     * @param eyesColor the person's eye color
     * @param hairColor the person's hair color
     * @param notes additional information about the person
     * @param wedTo name of the person's partner
     * @param generation number of the family generation to which the person belongs
     * @param children A list of the person's children.
     */
    public Person(String name, String title, String nickname, String father, String mother, String fate, String ofHisName, String eyesColor, String hairColor, String notes, String wedTo, Integer generation ,PersonLinkedList children) {
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
        this.generation = generation;
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

       /**
        * Checks if there are more children to iterate over.
        *
        * @return {@code true} if there are more children, {@code false} otherwise
        */
        @Override
        public boolean hasNext() {
            return currentIndex < getChildren().size();
        }

        
       /**
        * Retrieves the next child in the collection.
        * 
        * @return The name of the next child
        */
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

        PersonLinkedList currentChildren = parent.getChildren();
        if (currentChildren == null) {
            return null; // No children list means no duplicates
        }

        for (int i = 0; i < currentChildren.size(); i++) {
        }

        // Check for duplicates
        for (int i = 0; i < currentChildren.size(); i++) {
            String existingChildFirstName = getFirstName(currentChildren.get(i));
            String currentChild = currentChildren.get(i);

            // If duplicate is found, remove the existing child from both the list and HashTable
            if (newChildFirstName.equalsIgnoreCase(existingChildFirstName)) {
                return currentChild.toLowerCase(); // A duplicate was found and removed from both the list and HashTable
            }
        }

        return null; // No duplicate found
    }

    /**
     * Retrieves the name of the person.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the title of the person.
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the person.
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the nickname of the person.
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the person.
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Retrieves the father of the person.
     * @return the father
     */
    public String getFather() {
        return father;
    }

    /**
     * Sets the name of the person's father.
     * @param father the father to set
     */
    public void setFather(String father) {
        this.father = father;
    }

    /**
     * Retrieves the mother of the person.
     * @return the mother
     */
    public String getMother() {
        return mother;
    }

    /**
     * Sets the name of the person's mother.
     * @param mother the mother to set
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    /**
     * Retrieves the fate of the person.
     * @return the fate
     */
    public String getFate() {
        return fate;
    }

    /**
     * Sets the fate of the person.
     * @param fate the fate to set
     */
    public void setFate(String fate) {
        this.fate = fate;
    }

    /**
     * Retrieves the children of the person.
     * @return the children
     */
    public PersonLinkedList getChildren() {
        return children;
    }

    /**
     * Sets the children of the person.
     * @param children the children to set
     */
    public void setChildren(PersonLinkedList children) {
        this.children = children;
    }

    /**
     * Retrieves the "of His Name" title, indicating the lineage.
     * @return the ofHisName
     */
    public String getOfHisName() {
        return ofHisName;
    }

    /**
     * Sets the "of His Name" title for the person.
     * @param ofHisName the ofHisName to set
     */
    public void setOfHisName(String ofHisName) {
        this.ofHisName = ofHisName;
    }

    /**
     * Retrieves the eye color of the person.
     * @return the eyesColor
     */
    public String getEyesColor() {
        return eyesColor;
    }

    /**
     * Sets the eye color of the person.
     * @param eyesColor the eyesColor to set
     */
    public void setEyesColor(String eyesColor) {
        this.eyesColor = eyesColor;
    }

    /**
     * Retrieves the hair color of the person.
     * @return the hairColor
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * Sets the hair color of the person.
     * @param hairColor the hairColor to set
     */
    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * Retrieves additional notes about the person.
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes for the person.
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Retrieves the name of the person the individual is wed to.
     * @return the wedTo
     */
    public String getWedTo() {
        return wedTo;
    }

    /**
     * Sets the name of the person the individual is wed to.
     * @param wedTo the wedTo to set
     */
    public void setWedTo(String wedTo) {
        this.wedTo = wedTo;
    }

    /**
     * Retrieves the generation number of the person.
     * @return the generation
     */
    public Integer getGeneration() {
        return generation;
    }

    /**
     * Sets the generation number for the person.
     * @param generation the generation to set
     */
    public void setGeneration(Integer generation) {
        this.generation = generation;
    }

}
