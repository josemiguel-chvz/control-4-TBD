package cl.tbd.ejemplo1.models;
import org.postgis.PGgeometry;

public class Region {
    private int id;
    private int cod_reg;
    private String name;
    private float area;
    private PGgeometry geom;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCod_reg() {
        return cod_reg;
    }

    public void setCod_reg(int cod_reg) {
        this.cod_reg = cod_reg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public PGgeometry getGeom() {
        return geom;
    }

    public void setGeom(PGgeometry geom) {
        this.geom = geom;
    }
}
