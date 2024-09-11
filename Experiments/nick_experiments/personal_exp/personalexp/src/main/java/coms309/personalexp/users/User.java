package coms309.personalexp.users;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User
    {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private String id;
        private String firstName;
        private String lastName;
        private String email;

        @Override
        public boolean equals(Object obj){
            if( this == obj){
                return true;
            }
            if (!(obj instanceof User))
                {
                   return false;
                }
            User user = (User) obj;
            return Objects.equals(this.id, user.id) && Objects.equals(this.firstName, user.firstName)
                    && Objects.equals(this.lastName, user.lastName) && Objects.equals(this.email, user.email);


        }


        public String getId()
            {
                return id;
            }

        public void setId(String id)
            {
                this.id = id;
            }

        public String getFirstName()
            {
                return firstName;
            }

        public void setFirstName(String firstName)
            {
                this.firstName = firstName;
            }

        public String getLastName()
            {
                return lastName;
            }

        public void setLastName(String lastName)
            {
                this.lastName = lastName;
            }

        public String getEmail()
            {
                return email;
            }

        public void setEmail(String email)
            {
                this.email = email;
            }





    }