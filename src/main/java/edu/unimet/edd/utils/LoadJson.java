package edu.unimet.edd.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Loads a JSON file and adds the data to a LinkedList of Person objects.
 */
public class LoadJson {

    private LinkedList peopleList;

    public LoadJson() {
        // Inicializamos la LinkedList vacía
        peopleList = new LinkedList();
    }

    /**
     * Loads data from the provided JSON file and stores each person in the
     * LinkedList.
     *
     * @param filePath the path to the JSON file
     * @throws FileNotFoundException if the file is not found
     */
    public void load(String filePath) throws FileNotFoundException {
        // Leemos el archivo JSON
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        StringBuilder jsonContent = new StringBuilder();

        // Cargamos todo el contenido del archivo
        while (scanner.hasNextLine()) {
            jsonContent.append(scanner.nextLine());
        }
        scanner.close();

        // Convertimos el contenido del archivo a un objeto JSON
        JSONObject jsonObject = new JSONObject(jsonContent.toString());

        // Aquí asumo que los datos de las personas están en un array llamado "people"
        JSONArray peopleArray = jsonObject.getJSONArray("people");

        // Iteramos sobre el array y agregamos las personas a la LinkedList
        for (int i = 0; i < peopleArray.length(); i++) {
            JSONObject personJson = peopleArray.getJSONObject(i);

            // Creamos un objeto Person a partir de los datos del JSON
            String name = personJson.getString("name");
            String title = personJson.optString("title", ""); // Título opcional
            String alias = personJson.optString("alias", ""); // Alias opcional
            String father = personJson.optString("father", null); // Padre opcional
            String mother = personJson.optString("mother", null); // Madre opcional
            String deathDate = personJson.optString("death_date", null); // Fecha de muerte opcional

            // Creamos la persona
            Person person = new Person(name, title, alias, father, mother, deathDate);

            // Agregamos la persona a la LinkedList
            peopleList.add(person);
        }
    }

    /**
     * Prints the list of people stored in the LinkedList.
     */
    public void printPeople() {
        peopleList.print();
    }

    // Getter para obtener la LinkedList si es necesario
    public LinkedList getPeopleList() {
        return peopleList;
    }
}
