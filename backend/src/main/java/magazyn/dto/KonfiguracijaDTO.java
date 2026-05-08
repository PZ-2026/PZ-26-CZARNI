package magazyn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KonfiguracijaDTO {

    private Integer id;

    @NotBlank(message = "Nazwa parametru jest wymagana")
    private String nazwaParametru;

    @NotBlank(message = "Wartość parametru jest wymagana")
    private String wartoscParametru;

    private String typParametru; // STRING, INTEGER, BOOLEAN, DECIMAL

    private String opis;

    private Boolean aktywna = true;
}
