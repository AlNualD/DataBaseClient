package database;

public class Player {
    private String id_code;
    private String name;
    private String status;
    private int characters_amount;
    public  Player(String id_code, String name, String status, int characters_amount) {
        this.characters_amount  = characters_amount;
        this.id_code = id_code;
        this.name = name;
        this.status = status;
    }

    public String getId_code() {
        return id_code;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public int getCharacters_amount() {
        return characters_amount;
    }
}
