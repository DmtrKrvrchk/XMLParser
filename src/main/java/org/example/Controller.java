package org.example;

import java.io.File;

/**
 * Class to define a controller-component
 */
public class Controller {

    /** A view-component **/
    private final View view;

    /**
     * Constructor to create a controller-object
     * @param model a model-component
     * @param view a view-component
     */
    public Controller(Model model, View view) {
        this.view = view;
    }

    /**
     * Method to start an application
     */
    public void startApplication() {
        view.display();
    }

    public static void main(String[] args) {
        Model model = new Model(new File("src/main/resources/values.xml"));
        View view = new View("Patienten Values", "Value", "Scale", model.getDataset());
        Controller controller = new Controller(model, view);
        controller.startApplication();
    }
}