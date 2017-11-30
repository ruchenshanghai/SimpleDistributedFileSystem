package sdfs.namenode;


public class Entity {

    enum TYPE{
        FILE,DIR
    }

    private static final int NOT_EXIST_ID = -1;
    // id: -1 for not save to disk

    protected int id;
    protected TYPE type;
    protected String name;

    public Entity() {
//        System.out.println("no argument Entity constructor");
    }

    public Entity(int id, TYPE type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public static int getNotExistId() {
        return NOT_EXIST_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
