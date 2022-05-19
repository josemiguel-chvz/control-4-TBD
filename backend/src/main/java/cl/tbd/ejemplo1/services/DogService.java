package cl.tbd.ejemplo1.services;

import cl.tbd.ejemplo1.models.Dog;
import cl.tbd.ejemplo1.repositories.DogRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class DogService {

    private final DogRepository dogRepository;

    DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @GetMapping("/dogs")
    public List<Dog> getAllDogs(@RequestParam(value="region_id", required = false) String region_id) {
        try {
            List<Dog> dogs = new ArrayList<>();
            Iterable<Dog> records;
            if (region_id == null) {
                System.out.println("getAllDogs()");
                records = dogRepository.getAllDogs();
            } else {
                System.out.println("getDogsByRegion()");
                records = dogRepository.getDogsByRegion(Integer.parseInt(region_id));
            }
            records.forEach(dogs::add);
            return dogs;
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
            return null;
        }
    }

    @GetMapping("/dogs/nearby")
    public List<Dog> getNearbyDogs(
        @RequestParam(value="latitude", required = true) Float latitude,
        @RequestParam(value="longitude", required = true) Float longitude,
        @RequestParam(value="limit", required = true) Integer limit
        ) {
            try {
                List<Dog> dogs = new ArrayList<>();
                Iterable<Dog> records = dogRepository.getNearbyDogs(latitude, longitude, limit);
                records.forEach(dogs::add);
                return dogs;
            } catch (Exception e) {
                System.out.println("Error :" + e.getMessage());
                return null;
            }
        }

    @GetMapping("/dogs/count")
    public String countDogs(){
        int total = dogRepository.countDogs();
        return String.format("Tienes %s perros!!", total);
    }

    @PostMapping("/dogs")
    @ResponseBody
    public Dog createDog(@RequestBody Dog dog){
        Dog result = dogRepository.createDog(dog);
        return result;
    }
}
