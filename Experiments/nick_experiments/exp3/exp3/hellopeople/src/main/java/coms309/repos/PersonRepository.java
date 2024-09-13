package coms309.repos;

import coms309.people.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//Interacts with db
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>
    {
        Person findByFirstName(String firstName);

        @Query("select p from Person p where upper(p.firstName) = upper(?1) and upper(p.lastName) = upper(?2)")
        Person findByFirstAndLast(String firstName, String lastName); //

        @Transactional
        @Modifying
        @Query("""
                update Person p set p.address = :address, p.telephone = :telephone
                where upper(p.firstName) = upper(:firstName) and upper(p.lastName) = upper(:lastName)""")
        void updatePersonByFullName(@NonNull @Param("address") String address,
                                   @NonNull @Param("telephone") String telephone,
                                   @NonNull @Param("firstName") String firstName,
                                   @NonNull @Param("lastName") String lastName);

    }