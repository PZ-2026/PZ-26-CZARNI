package magazyn.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ZamowienieKlientaDTO {
    private Integer id;
    private OffsetDateTime data;
    private String imieKlienta;
    private String nazwiskoKlienta;
    private Integer status;
    private List<PozycjaZamowieniaDTO> produkty;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PozycjaZamowieniaDTO {
        private Integer produktId;
        private String nazwaProduktu;
        private Integer ilosc;
        private String kodKreskowy;
    }
}
