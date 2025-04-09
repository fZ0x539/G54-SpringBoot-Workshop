package se.lexicon.g54springbootworkshop.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.g54springbootworkshop.Entity.Details;

import java.util.List;

@Repository
public interface DetailsRepository extends CrudRepository<Details, Integer> {
    Details findByEmail(String email);
    List<Details> findByNameContaining(String nameContains);
    List<Details> findByNameIgnoreCase(String name);
}
