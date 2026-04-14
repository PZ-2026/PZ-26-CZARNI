package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dostawcy")
public class Dostawca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nazwa_dostawcy", nullable = false, columnDefinition = "text")
    private String nazwaDostawcy;

    @Column(nullable = false, columnDefinition = "text")
    private String adres;

    @Column(length = 15, nullable = false)
    private String telefon;
}