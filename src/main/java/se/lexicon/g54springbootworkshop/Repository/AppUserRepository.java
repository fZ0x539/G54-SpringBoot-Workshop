package se.lexicon.g54springbootworkshop.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.g54springbootworkshop.Entity.AppUser;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
    AppUser findByUsername(String username);
    List<AppUser> findByRegDateBetween(LocalDate startDate, LocalDate endDate);
    AppUser findByUserDetails_Id(int id);
    AppUser findByUserDetailsEmailIgnoreCase(String email);
}
