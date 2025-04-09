package se.lexicon.g54springbootworkshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import se.lexicon.g54springbootworkshop.Entity.AppUser;
import se.lexicon.g54springbootworkshop.Entity.Details;
import se.lexicon.g54springbootworkshop.Repository.AppUserRepository;
import se.lexicon.g54springbootworkshop.Repository.DetailsRepository;

import java.time.LocalDate;

@Component
public class AppCommandLineRunner implements CommandLineRunner {
    private final AppUserRepository appUserRepository;
    private final DetailsRepository detailsRepository;

    public AppCommandLineRunner(AppUserRepository appUserRepository, DetailsRepository detailsRepository) {
        this.appUserRepository = appUserRepository;
        this.detailsRepository = detailsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Details johnDetails = detailsRepository.save(new Details("john.doe@example.com", "John Doe", LocalDate.of(1990, 2,5)));
        Details janeDetails = detailsRepository.save(new Details("jane.smith@example.com", "Jane SMith", LocalDate.of(1985,8,22)));

//        AppUser empty = new AppUser();

        AppUser john = appUserRepository.save(new AppUser("johndoe", "password123", LocalDate.now(), johnDetails));
        AppUser jane =  appUserRepository.save(new AppUser("janesmith", "password123", LocalDate.now(), janeDetails));

        printInfo("Test");
        System.out.println(john);
        printInfo("John getters:");
        System.out.println(john.getId());



//        printInfo("--- AppUserRepository ---");
//        printInfo("Find by username (johndoe): " +
//                appUserRepository.findByUsername("johndoe"));
//        System.out.println("Find by registration date range: " +
//                appUserRepository.findByRegDateBetween(LocalDate.now().minusDays(2), LocalDate.now()));
//
//        System.out.println("Find by details ID: " +
//                appUserRepository.findByUserDetailsId(johnDetails.getId()));


    }

    private void printInfo(String message){
        System.out.println("\u001B[34m" + message + "\u001B[0m");
    }
}
