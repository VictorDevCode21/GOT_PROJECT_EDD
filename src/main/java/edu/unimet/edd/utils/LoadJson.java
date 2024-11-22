package edu.unimet.edd.utils;

import edu.unimet.edd.tree.Tree;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utility class to load genealogy data from a JSON string.
 */
public class LoadJson {

    /**
     * Loads the genealogy data from the given JSON string and populates the
     * tree.
     *
     * @param jsonContent The JSON content containing genealogy data.
     * @param tree The tree to populate with the genealogy data.
     */
    public void loadGenealogy(String jsonContent, Tree tree) {
        JSONObject jsonObject = new JSONObject(jsonContent);

        // Parse each house and its members
        for (String houseName : jsonObject.keySet()) {
            JSONArray houseArray = jsonObject.getJSONArray(houseName);

            // Load each person from the house
            for (int i = 0; i < houseArray.length(); i++) {
                JSONObject personEntry = houseArray.getJSONObject(i);

                String personName = personEntry.keys().next();
                JSONArray personDetails = personEntry.getJSONArray(personName);

                // Parse and add the person to the tree
                Person person = parsePersonDetails(personName, personDetails);
                tree.addPerson(person); // Add person to the tree
            }
        }

        // Establish parent-child relationships after all people are added
        for (String houseName : jsonObject.keySet()) {
            JSONArray houseArray = jsonObject.getJSONArray(houseName);

            for (int i = 0; i < houseArray.length(); i++) {
                JSONObject personEntry = houseArray.getJSONObject(i);

                String personName = personEntry.keys().next();
                JSONArray personDetails = personEntry.getJSONArray(personName);

                // Link parents and children within the tree
                linkRelationships(tree, personName, personDetails);
            }
        }
    }

    /**
     * Links the relationships (parent-child) in the tree after all persons are
     * loaded.
     *
     * @param tree The Tree object containing all persons.
     * @param personName The name of the person.
     * @param personDetails The JSON array containing the person's details.
     */
    private void linkRelationships(Tree tree, String personName, JSONArray personDetails) {
        Person person = tree.getPerson(personName);
        if (person == null) {
            return;
        }

        for (int i = 0; i < personDetails.length(); i++) {
            JSONObject detail = personDetails.getJSONObject(i);
            String key = detail.keys().next();

            if ("Father to".equals(key)) {
                JSONArray childrenArray = detail.getJSONArray(key);
                for (int j = 0; j < childrenArray.length(); j++) {
                    String childName = childrenArray.getString(j);
                    Person child = tree.getPerson(childName);
                    if (child != null) {
                        // Set father for each child
                        child.setFather(personName);
                    }
                }
            }
        }
    }

   /**
    * Parses the details of a person from the given JSON array.
   *
   * @param name The name of the person.
   * @param personDetails The JSON array containing the person's details.
   * @return A Person object with the parsed data.
   */
private Person parsePersonDetails(String name, JSONArray personDetails) {
    String title = null;
    String nickname = null;
    String father = null;
    String mother = null;
    String fate = null;
    String ofHisName = null;
    String wedTo = null;
    String ofEyes = null;
    String ofHair = null;
    String notes = null;
    LinkedList children = new LinkedList();

    for (int i = 0; i < personDetails.length(); i++) {
        JSONObject detail = personDetails.getJSONObject(i);
        String key = detail.keys().next();
        Object value = detail.get(key);

        switch (key) {
            case "Held title":
                title = (String) value;
                break;
            case "Known throughout as":
                nickname = (String) value;
                break;
            case "Born to":
                if (father == null) {
                    father = (String) value;
                } else {
                    mother = (String) value;
                }
                break;
            case "Father to":
                if (value instanceof JSONArray) {
                    JSONArray childrenArray = (JSONArray) value;
                    for (int j = 0; j < childrenArray.length(); j++) {
                        String childName = childrenArray.getString(j);
                        // Use iterator to check for duplicates in children
                        if (!isChildPresent(children, childName)) {
                            children.addString(childName);
                        }
                    }
                }
                break;
            case "Fate":
                fate = (String) value;
                break;
            case "Of his name":
                ofHisName = (String) value;
                break;
            case "Wed to":
                wedTo = (String) value;
                break;
            case "Of eyes":
                ofEyes = (String) value;
                break;
            case "Of hair":
                ofHair = (String) value;
                break;
            case "Notes":
                notes = (String) value;
                break;
            default:
                break;
        }
    }

    return new Person(name, title, nickname, father, mother, fate, ofHisName, children, wedTo, ofEyes, ofHair, notes);
}

    /**
     * Checks if a child is present in the list of children.
     *
     * @param children The list of children.
     * @param childName The name of the child to check.
     * @return True if the child is present, false otherwise.
     */
    private boolean isChildPresent(LinkedList children, String childName) {
        LinkedList.LinkedListIterator iterator = children.iterator(); // Use the correct iterator type
        while (iterator.hasNext()) {
            if (iterator.next().equals(childName)) {
                return true; // Child is already in the list
            }
        }
        return false; // Child is not in the list
    }
}