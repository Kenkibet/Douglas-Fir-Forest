package models;

import org.sql2o.*;

import java.util.ArrayList;
import java.util.List;

public class EndangeredAnimal extends Animal {
    public static final String TYPE = "Endangered";
    private String health;
    private String age;

    public EndangeredAnimal(String name, String health, String age) {
        super(name);
        this.name = name;
        this.id = id;
        this.health = health;
        this.age = age;
    }

    public String getHealth() {
        return health;
    }

    public String getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object otherEndangeredAnimal) {
        if (!(otherEndangeredAnimal instanceof EndangeredAnimal)) {
            return false;
        } else {
            EndangeredAnimal newEndangeredAnimal = (EndangeredAnimal) otherEndangeredAnimal;
            return this.getName().equals(newEndangeredAnimal.getName()) && this.getHealth().equals(newEndangeredAnimal.getHealth()) && this.getAge().equals(newEndangeredAnimal.getAge());
        }
    }

    public void save() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO animals (name, type, health ,age) VALUES (:name, :type, :health , :age)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", super.getName())
                    .addParameter("type", TYPE)
                    .addParameter("health", this.health)
                    .addParameter("age", this.age)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<EndangeredAnimal> allEndangeredAnimals() {
        return null;
//        String sql = "SELECT * FROM animals where type='Endangered'";
//        try (Connection con = DB.sql2o.open()) {
//            return con.createQuery(sql).executeAndFetch(EndangeredAnimal.class);
//        }
    }

    public static EndangeredAnimal find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM animals where id=:id";
            EndangeredAnimal endangeredAnimal= con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(EndangeredAnimal.class);
            return endangeredAnimal;
        }
    }

    public void updateHealth(int id, String health) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "UPDATE animals SET health=:health WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("health", health)
                    .executeUpdate();
        }
    }

    public void updateAge(String age) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "UPDATE endangered_animals SET age=:age WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("age", age)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public List<Sighting> getSightings() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM sightings WHERE animal_id=:id;";
            List<Sighting> sightings = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Sighting.class);
            return sightings;
        }
    }


}