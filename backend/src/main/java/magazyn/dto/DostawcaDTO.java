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
public class DostawcaDTO {

    private Integer id;

    @NotBlank(message = "Nazwa dostawcy jest wymagana")
    private String nazwaDostawcy;

    @NotBlank(message = "Adres jest wymagany")
    private String adres;

    @NotBlank(message = "Numer telefonu jest wymagany")
    private String telefon;
}
