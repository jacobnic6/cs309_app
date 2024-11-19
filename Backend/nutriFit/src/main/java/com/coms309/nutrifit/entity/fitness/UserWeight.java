package com.coms309.nutrifit.entity.fitness;

import com.coms309.nutrifit.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SortNatural;
import org.springframework.data.web.SortDefault;

import java.time.LocalDate;

/**
 * The type User weight.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_weights")
public class UserWeight {


    //private int userId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double weight;


    @Column(unique = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate weightDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    /**
     * Instantiates a new User weight.
     *
     * @param weight     the weight
     * @param weightDate the weight date
     * @param user       the user
     */
    public UserWeight(double weight, LocalDate weightDate, User user) {
        this.weight = weight;
        this.user = user;
        this.weightDate = weightDate;
    }

    /**
     * Instantiates a new User weight.
     *
     * @param weight the weight
     * @param user   the user
     */
    public UserWeight(double weight, User user) {
        this.weight = weight;
        this.user = user;
        this.weightDate = LocalDate.now();
    }

    /**
     * Instantiates a new User weight.
     *
     * @param weight     the weight
     * @param weightDate the weight date
     */
    public UserWeight(double weight, LocalDate weightDate) {
        this.weight = weight;
        this.weightDate = weightDate;
    }


}
