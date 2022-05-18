package cl.tbd.ejemplo1.repositories;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import cl.tbd.ejemplo1.models.Region;

@Repository
public class RegionRepositoryImp implements RegionRepository {

    @Autowired
    private Sql2o sql2o;

    @Override
    public List<Region> getAllRegions() {
        try(Connection conn = sql2o.open()) {
            final String query = 
        } catch (Exception e) {
            //TODO: handle exception
        }
        // TODO Auto-generated method stub
        return null;
    }
}
