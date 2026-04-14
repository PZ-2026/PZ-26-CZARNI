package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "zamowienia_produkty_klienci_laczaca")
public class ZamowienieProduktyKlienci {

    @EmbeddedId
    private ZamowienieProduktyKlienciId id = new ZamowienieProduktyKlienciId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idZamowienia")
    @JoinColumn(name = "id_zamowienia")
    private ZamowienieKlienta zamowienie;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProduktu")
    @JoinColumn(name = "id_produktu")
    private Produkt produkt;

    @Column(nullable = false)
    private Integer ilosc;

    @Column(name = "cena_w_dniu_zakupu", precision = 12, scale = 2)
    private BigDecimal cenaWDniuZakupu;
}