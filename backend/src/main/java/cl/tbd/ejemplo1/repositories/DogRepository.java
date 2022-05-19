package cl.tbd.ejemplo1.repositories;
import java.util.List;
import cl.tbd.ejemplo1.models.Dog;

public interface DogRepository {
    public int countDogs();
    public List<Dog> getAllDogs();
    public List<Dog> getDogsByRegion(int region_id);
    public List<Dog> getNearbyDogs(float latitude, float longitude, int limit);
    public List<Dog> getDogsRadio(float latitude, float longitude, int radio);
    public Dog createDog(Dog dog);
    public String getJson();
}
