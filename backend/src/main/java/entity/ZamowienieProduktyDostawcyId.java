package entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ZamowienieProduktyDostawcyId implements Serializable {

    @Column(name = "id_produktu")
    private Integer idProduktu;

    @Column(name = "id_zamowienia")
    private Integer idZamowienia;
}