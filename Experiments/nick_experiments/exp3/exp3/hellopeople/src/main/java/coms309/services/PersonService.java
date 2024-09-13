package coms309.services;

import coms309.people.Person;
import coms309.repos.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

//connects controller and repo. Responsible for logic

@Service
public class PersonService
    {
        @Autowired
        PersonRepository personRepository;

        HashMap<String, Person> peopleList = new  HashMap<>();



        public HashMap<String, Person> getPeopleList()
            {
                return peopleList;
            }

        public String createPerson(Person person)
            {

                peopleList.put(person.getFirstName(), person);
               // personRepository.saveAndFlush(person);
                return "New person "+ person.getFirstName() + " created";
            }

        public Person findPerson(String firstName)
            {
               // personRepository.findByFirstName(firstName);
                return peopleList.get(firstName);
            }

        public Person updatePerson(String firstName, Person p)
            {
                List<Person> list = personRepository.findByFirstName(firstName);

               // peopleList.replace(firstName, p);

                return peopleList.get(firstName);
            }

        public HashMap<String, Person>  deletePerson(String firstName)
            {
                //personRepository.deletePerson(firstName);
                peopleList.remove(firstName);
                return peopleList;
            }
    }
