package src.Model;

public class Locations {
    public Locations(SingleLocation[] data) {
        this.data = data;
    }

    private SingleLocation[] data;

    public SingleLocation[] getData() {
        return data;
    }

    public void setData(SingleLocation[] data) {
        this.data = data;
    }
}
