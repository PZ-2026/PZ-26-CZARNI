package magazyn.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ZamowienieProduktyDostawcyId implements Serializable {
    private Integer idProduktu;
    private Integer idZamowienia;
}