package cl.tbd.ejemplo1.repositories;
import java.util.List;
import cl.tbd.ejemplo1.models.Dog;

public interface DogRepository {
    public int countDogs();
    public List<Dog> getAllDogs();
    public List<Dog> getDogsByRegion(int region_id);
    public Dog createDog(Dog dog);
    public String getJson();
}
