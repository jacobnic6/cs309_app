package coms309.people;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
/**
 * Provides the Definition/Structure for the people row
 *
 * @author Nicholas Jacobs
 */


@Entity
public class Person {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer personId;
    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    public Person(){}

    public Person(String firstName, String lastName, String address, String telephone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public Integer getPersonId()
        {
            return personId;
        }

    public void setPersonId(Integer personId)
        {
            this.personId = personId;
        }
    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone;
    }
}
