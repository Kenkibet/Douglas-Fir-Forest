import models.*;
import spark.ModelAndView;

import org.apache.log4j.BasicConfigurator;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) throws Exception {
        staticFileLocation("public");
        String appFile = "app.vtl";

        BasicConfigurator.configure();
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 3000;
        }
        port(port);

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("rangers", Ranger.all());
            model.put("template", "templates/main.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });

        get("/ranger/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("template", "templates/add-new-ranger.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });

        post("/ranger", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String firstName = req.queryParams("inputFirstName");
            String lastName = req.queryParams("inputLastName");
            String badgeNumber = req.queryParams("inputBadgeNumber");

            if (!(firstName.trim().isEmpty() || lastName.trim().isEmpty() || badgeNumber.trim().isEmpty())) {

                Ranger ranger = new Ranger(firstName, lastName, Integer.parseInt(badgeNumber));
                ranger.save();
            } else {
                System.out.println("Please fill in all the fields");
            }
            res.redirect("/");

            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });

        get("/ranger/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Ranger ranger = Ranger.find(Integer.parseInt(req.params(":id")));
            model.put("ranger", ranger);
            // model.put("sightings",ranger.AllMySightings());

            model.put("template", "templates/ranger.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });

        get("/ranger/:id/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            Ranger ranger = Ranger.find(Integer.parseInt(req.params(":id")));
            model.put("ranger", ranger);


            model.put("animals", Animal.all());
            model.put("locations", Location.all());


            model.put("sightings", Sighting.find(Integer.parseInt(req.params(":id"))));
            model.put("template", "templates/record-sighting.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


        post("/sighting", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String name = req.queryParams("name");
            int animal = Integer.parseInt(req.queryParams("animal"));
            String location = req.queryParams("location");

            Sighting sighting = new Sighting(animal, location, name);
            sighting.save();

            res.redirect("/");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


        get("/animals", (req, res) -> {
            Map<String, Object> model = new HashMap<>();


            model.put("animals", Animal.all());
            model.put("endangeredAnimals", EndangeredAnimal.allEndangeredAnimals());

            model.put("template", "templates/animals.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


        post("/animals/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String name = req.queryParams("inputName");
            String health = req.queryParams("healthInput");
            String age = req.queryParams("ageInput");
            if (req.queryParams("endangeredInput") != null) {
                if (!(name.trim().isEmpty() || health.trim().isEmpty() || age.trim().isEmpty())) {

                    EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
                    endangeredAnimal.save();
                } else {
                    System.out.println("Please fill in all the fields");
                }
            } else {
                if (!(name.trim().isEmpty())) {
                    Animal animal = new Animal(name);
                    animal.save();
                } else {
                    System.out.println("Please fill in all the fields");
                }


            }
            res.redirect("/animals");

            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


        get("/sightings", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("sightings", Sighting.all());

            model.put("template", "templates/sightings.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


        get("/locations", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("locations", Location.all());

            model.put("template", "templates/locations.vtl");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });

        post("/locations", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String locationName = req.queryParams("name");

            if (!(locationName.trim().isEmpty())) {

                Location location = new Location(locationName);
                location.save();
            }
            res.redirect("/locations");
            return new ViewEngine().render(
                    new ModelAndView(model, appFile)
            );
        });


    }
}
