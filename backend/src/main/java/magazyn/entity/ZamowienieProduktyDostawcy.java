package magazyn.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Encja reprezentująca pozycję w zamówieniu zaopatrzeniowym.
 * Łączy zamówienie zaopatrzeniowca z produktem i przechowuje zamówioną ilość.
 */
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
