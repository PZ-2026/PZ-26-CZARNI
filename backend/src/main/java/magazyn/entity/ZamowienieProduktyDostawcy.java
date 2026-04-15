package magazyn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(ZamowienieProduktyDostawcyId.class)
@Table(name = "zamowienia_produkty_dostawcy_laczaca")
public class ZamowienieProduktyDostawcy {

    @Id
    @Column(name = "id_produktu")
    private Integer idProduktu;

    @Id
    @Column(name = "id_zamowienia")
    private Integer idZamowienia;

    private Integer ilosc;
}