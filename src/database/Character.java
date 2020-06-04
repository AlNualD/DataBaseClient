package database;

public class Character {
    private String id_code;
    private String owner_id;
    private String name;
    private String c_class;
    private int lvl;
    public Character(String id_code, String owner_id, String name, String c_class, int lvl) {
        this.c_class = c_class;
        this.id_code = id_code;
        this.lvl = lvl;
        this.owner_id = owner_id;
        this.name = name;
    }

    public String getId_code() {
        return id_code;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getName() {
        return name;
    }

    public String getC_class() {
        return c_class;
    }

    public int getLvl() {
        return lvl;
    }
}
