package models;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;


public class Animal {
    public int id;
    public String name;

    public static final String TYPE = "Normal";

    public Animal(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public int getId() {
        return id;
    }
    @Override
    public boolean equals(Object otherAnimal) {
        if(!(otherAnimal instanceof Animal)) {
            return false;
        } else {
            Animal newAnimal = (Animal) otherAnimal;
            return this.getName().equals(newAnimal.getName());
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO animals (name,type) VALUES (:name, :type)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("type", TYPE)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<Animal> all() {
        return null;
//        try(Connection con = DB.sql2o.open()) {
//            String sql = "SELECT * FROM animals;";
//            return con.createQuery(sql)
//                    .executeAndFetch(Animal.class);
//        }
    }

    public static List<Animal> allNormal() {
        return null;
//        String sql = "SELECT * FROM animals where type = 'Normal'";
//        try(Connection con = DB.sql2o.open()) {
//            return con.createQuery(sql)
//                    .throwOnMappingFailure(false)
//                    .executeAndFetch(Animal.class);
//        }
    }

    public static Animal find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM animals WHERE id=:id;";
            Animal animal = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Animal.class);
            return animal;
        }
    }

    public void updateName(String name) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "UPDATE animals SET name=:name WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .executeUpdate();
        }
    }

    public void delete() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM animals WHERE id=:id;";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }

    public List<Sighting> getSightings() {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM sightings WHERE animal_id=:id;";
            List<Sighting> sightings = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetch(Sighting.class);
            return sightings;
        }
    }

}