package coms309.services;

import coms309.people.Person;
import coms309.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
//connects controller and repo. Responsible for logic

@Service
public class PersonService
    {
        @Autowired
        PersonRepository personRepository;

        public String createPerson(Person person)
            {
                if (personRepository.findByFirstAndLast(person.getFirstName(), person.getLastName()) == null)
                    {
                        personRepository.saveAndFlush(person);
                        return "New person " + person.getFirstName() + " created";
                    } else
                    {
                        updatePerson(person.getFirstName(), person);
                        return person.getFirstName() + " already exists";
                    }
            }

        public Person updatePerson(String firstName, Person p)
            {
                personRepository.updatePersonByFullName(p.getAddress(),
                                                        p.getTelephone(),
                                                        p.getFirstName(),
                                                        p.getLastName());
                return personRepository.findByFirstAndLast(p.getFirstName(), p.getLastName());
            }

        public Person findPerson(String firstName, String lastName)
            {
                return personRepository.findByFirstAndLast(firstName, lastName);
            }

        public List<Person> deletePerson(String firstName, String lastName)
            {
                personRepository.delete(personRepository.findByFirstAndLast(firstName, lastName));
                return getPeopleList();
            }

        public List<Person> getPeopleList()
            {
                return personRepository.findAll();
            }
    }
