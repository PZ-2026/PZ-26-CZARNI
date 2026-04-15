package magazyn.entity;

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
public class ZamowienieProduktyKlienciId implements Serializable {

    @Column(name = "id_zamowienia")
    private Integer idZamowienia;

    @Column(name = "id_produktu")
    private Integer idProduktu;
}