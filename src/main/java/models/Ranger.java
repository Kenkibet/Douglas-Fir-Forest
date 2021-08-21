package models;

import org.sql2o.Connection;

import java.util.List;

public class Ranger {
    private String firstName;
    private String lastName;
    private int badgeNumber;
    private int id;

    public Ranger(String firstName, String lastName, int badgeNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.badgeNumber = badgeNumber;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public int getBadgeNumber() {
        return badgeNumber;
    }


    public int getId() {
        return id;
    }

    public static List<Ranger> all() {
        String create = "CREATE TABLE animals(id SERIAL PRIMARY KEY,health varchar, age varchar, type varchar,name varchar);\n" +
                "CREATE TABLE locations(id SERIAL PRIMARY KEY, name varchar);\n" +
                "CREATE TABLE rangers(id SERIAL PRIMARY KEY, firstname varchar, lastname varchar , badgenumber int);\n" +
                "CREATE TABLE sightings(id SERIAL PRIMARY KEY, ranger varchar , location varchar, animalid int);\n";
        String sql = "SELECT * FROM rangers";
        try (Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Ranger.class);
        }
    }

    public void save() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO rangers (firstname, lastname, badgenumber) VALUES (:firstName, :lastName, :badgeNumber)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("firstName", this.firstName)
                    .addParameter("lastName", this.lastName)
                    .addParameter("badgeNumber", this.badgeNumber)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static Ranger find(int id) {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM rangers where id=:id";
            Ranger ranger = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Ranger.class);
            return ranger;
        }
    }

    @Override
    public boolean equals(Object otherRanger) {
        if (!(otherRanger instanceof Ranger)) {
            return false;
        } else {
            Ranger newRanger = (Ranger) otherRanger;
            return this.getId() == newRanger.id;
        }
    }

    public List<Sighting> getSightings() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM sightings;";
            return con.createQuery(sql)
                    .executeAndFetch(Sighting.class);
        }
    }

    public void delete() {
        try (Connection con = DB.sql2o.open()) {
            String sql = "DELETE FROM rangers WHERE id = :id;";
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        }
    }
}