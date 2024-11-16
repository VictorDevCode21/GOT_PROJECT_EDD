package edu.unimet.edd.utils;

/**
 * This class represents a person in a genealogical tree. A person has a name,
 * attributes, children, and fate.
 */
public class Person {

    private String name;
    private String[] attributes; // Array of attributes
    private String[] children;    // Array of children names
    private String fate;

    /**
     * Constructs a new Person with the given details.
     *
     * @param name The name of the person.
     * @param attributes The attributes of the person.
     * @param children The children of the person.
     * @param fate The fate of the person.
     */
    public Person(String name, String title, String alias, String father, String mother, String deathDate) {
        this.name = name;
        this.attributes = new String[]{title, alias};  // Combina title y alias como atributos
        this.children = new String[0];  // Asignar un arreglo vacío si no tienes hijos
        this.fate = deathDate;  // Utilizar deathDate como destino
    }

    /**
     * Returns the name of the person.
     *
     * @return The name of the person.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the attributes of the person.
     *
     * @return An array of attributes.
     */
    public String[] getAttributes() {
        return attributes;
    }

    /**
     * Returns the children of the person.
     *
     * @return An array of children's names.
     */
    public String[] getChildren() {
        return children;
    }

    /**
     * Returns the fate of the person.
     *
     * @return The fate of the person.
     */
    public String getFate() {
        return fate;
    }

    /**
     * Returns a string representation of the person.
     *
     * @return A string representing the person's name and attributes.
     */
    @Override
    public String toString() {
        return name + " - " + String.join(", ", attributes);
    }
}
