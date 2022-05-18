package cl.tbd.ejemplo1.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.tbd.ejemplo1.models.Region;
import cl.tbd.ejemplo1.repositories.RegionRepository;

@RestController
@CrossOrigin
public class RegionService {
    private final RegionRepository regionRepository;

    RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @GetMapping("/regions")
    public ResponseEntity<List<Region>> getAllRegions() {
        System.out.println("getAllRegions()");
        try {
            List<Region> regions = new ArrayList<>();
            Iterable<Region> records = regionRepository.getAllRegions();
            records.forEach(regions::add);
            return ResponseEntity.ok().body(regions);
        } catch (Exception e) {
            System.out.println("Error :" + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}