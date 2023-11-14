package application;

public class ProjectDataController {
    private int id;
    private String name;
    private String description;
    private String date;

    public ProjectDataController(int id, String name, String description, String date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
