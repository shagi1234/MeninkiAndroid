package tm.store.meninki.data;

public class CharactersDto {
    private String name;
    private int item_count;
    private String type;

    public CharactersDto(String name, int item_count, String type) {
        this.name = name;
        this.item_count = item_count;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItem_count() {
        return item_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }
}
