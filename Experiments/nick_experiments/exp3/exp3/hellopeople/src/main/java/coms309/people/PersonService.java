package coms309.people;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
public class PersonService
    {
        EntityManager entityManager;

        public PersonService(EntityManager entityManager)
            {
                this.entityManager = entityManager;
            }

        @Transactional
        public long savePerson(Person person){
            entityManager.persist(person);
            return person.getId();
        }
    }
