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

//                // Normalize the name before passing it to parsePersonDetails
//                personName = normalizeName(personName);  // Normalizing the person's name
                // Parse and add the person to the tree
                Person person = parsePersonDetails(personName, personDetails);
//                System.out.println("Adding person: " + person.getName());
//                System.out.println("Adding person: " + personName);
                tree.addPerson(person);
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
                        Person tempParent = new Person(name, title, nickname, father, mother, fate, ofHisName, children);

                        for (int j = 0; j < childrenArray.length(); j++) {
                            String childName = childrenArray.getString(j);
                            // Check for duplicates using isChildPresent
//                            if (!isChildPresent(tempParent, childName)) {
//                                System.out.println("Hola maldita sea ");
//                                children.addString(childName);
//                            }
                        }
                    }
                    break;
                case "Fate":
                    fate = (String) value;
                    break;
                case "Of his name":
                    ofHisName = (String) value;
                    break;
                default:
                    break;
            }
        }

        // Generate the full name using the new method and normalize it
        String fullName = getFullName(name, ofHisName);
        fullName = normalizeName(fullName); // Normalizing the full name

        // Now we ensure the nickname is normalized as well if it exists
        if (nickname != null) {
            nickname = normalizeName(nickname);
        };

//        System.out.println("Person: " + fullName + " Father: " + father);
        return new Person(fullName, title, nickname, father, mother, fate, ofHisName, children);
    }

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
}
