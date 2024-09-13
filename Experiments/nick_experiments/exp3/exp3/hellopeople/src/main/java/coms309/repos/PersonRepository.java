package coms309.repos;

import coms309.people.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Interacts with db
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>
    {
        List<Person> findByFirstName(String firstName);
//
//        void updatePerson();
//
//        void deletePerson(String firstName);
    }