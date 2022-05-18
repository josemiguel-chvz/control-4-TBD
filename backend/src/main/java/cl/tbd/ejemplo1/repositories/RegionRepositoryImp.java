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
            final String query = "SELECT distinct on ( cod_regi) " +
                                    "gid as id, cod_regi as cod_reg, nom_reg as name, superficie as area " +
                                    "FROM division_regional " +
                                    "WHERE cod_regi != 0 and st_isempty(geom) = false " +
                                    "ORDER BY cod_regi asc;";
            return conn.createQuery(query)
                        .executeAndFetch(Region.class);
        } catch (Exception e) {
            System.out.println("Error en repository: "+e.getMessage());
            return null;
        }
    }
}
