package models;

import org.sql2o.Connection;

import java.util.List;

public class Location {
    private int id;
    private String name;

    public Location(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static List<Location> all() {
        String sql = "SELECT * FROM locations";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Location.class);
        }
    }

    public void save() {
        try(Connection con = DB.sql2o.open())  {
            String sql = "INSERT INTO locations (name) VALUES (:name)";
            this.id = (int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static Location find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM locations where id=:id";
            Location location= con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Location.class);
            return location;
        }
    }

    @Override
    public boolean equals(Object otherLocation){
        if (!(otherLocation instanceof Location)) {
            return false;
        } else {
            Location newLocation = (Location) otherLocation;
            return this.getId() == newLocation.id;
        }
    }

}