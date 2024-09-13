package onetoone.Laptops;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import onetoone.Users.User;

/**
 * @author Vivek Bengre
 */


@Setter
@Getter
@Entity
public class Laptop {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private double cpuClock;
    private int cpuCores;
    private int ram;
    private String manufacturer;
    private int cost;

    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * @JsonIgnore is to assure that there is no infinite loop while returning either user/laptop objects (laptop->user->laptop->...)
     */
    @OneToOne
    @JsonIgnore
    private User user;

    public Laptop(double cpuClock, int cpuCores, int ram, String manufacturer, int cost) {
        this.cpuClock = cpuClock;
        this.cpuCores = cpuCores;
        this.ram = ram;
        this.manufacturer = manufacturer;
        this.cost = cost;
    }

    public Laptop() {
    }

    // =============================== Getters and Setters for each field ================================== //

}
