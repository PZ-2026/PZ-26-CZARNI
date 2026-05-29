package magazyn.service;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import magazyn.entity.Dostawca;
import magazyn.entity.ZamowienieProduktyDostawcy;
import magazyn.entity.ZamowienieZaopatrzeniowca;
import magazyn.repository.ProduktRepository;
import magazyn.repository.ZamowieniaZaopatrzeniowiecRepository;
import magazyn.repository.ZamowienieProduktyDostawcyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Serwis odpowiedzialny za generowanie dokumentów PDF dla zamówień zaopatrzeniowych.
 * Umożliwia stworzenie oficjalnego potwierdzenia zamówienia wysyłanego do dostawcy.
 */
@Slf4j
@Service
public class PdfZaopatrzenieService {

    @Autowired
    private ZamowieniaZaopatrzeniowiecRepository repository;

    @Autowired
    private ZamowienieProduktyDostawcyRepository pozycjeRepository;

    @Autowired
    private ProduktRepository produktRepository;

    /**
     * Generuje dokument PDF dla zamówienia zaopatrzeniowego.
     * Dokument zawiera dane zamawiającego, dostawcy oraz listę zamówionych produktów z ilościami.
     *
     * @param idZamowienia identyfikator zamówienia zaopatrzeniowego
     * @return tablica bajtów zawierająca wygenerowany plik PDF
     * @throws Exception w przypadku błędów podczas generowania PDF lub braku danych w bazie
     */
    public byte[] generatePurchaseOrderPdf(Integer idZamowienia) throws Exception {
        log.info("Rozpoczęto generowanie PDF dla zamówienia zaopatrzeniowego nr: {}", idZamowienia);

        // 1. Pobranie danych zamówienia
        ZamowienieZaopatrzeniowca zamowienie = repository.findById(idZamowienia)
                .orElseThrow(() -> {
                    log.error("Nie znaleziono zamówienia o ID: {} w bazie danych", idZamowienia);
                    return new RuntimeException("Nie znaleziono zamówienia: " + idZamowienia);
                });

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.setMargins(20, 36, 36, 36);

        // 2. Czcionka z polskimi znakami
        byte[] fontBytes = getClass().getClassLoader().getResourceAsStream("fonts/Roboto-VariableFont_wdth,wght.ttf").readAllBytes();
        PdfFont font = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);

        // --- NAGŁÓWEK ---
        document.add(new Paragraph("Data zamówienia: " + zamowienie.getData().toLocalDate())
                .setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(10));

        document.add(new Paragraph("ZAMÓWIENIE DOSTAWCZE NR " + zamowienie.getId() + "/ZAO/2026")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER).setFont(font).setMarginTop(10));

        // --- TABELA STRONY TRANSAKCJI ---
        float[] headerWidths = {1, 1};
        Table headerTable = new Table(UnitValue.createPercentArray(headerWidths)).useAllAvailableWidth().setMarginTop(20);

        // Zamawiający (My)
        Cell receiverCell = new Cell().add(new Paragraph("Zamawiający (Odbiorca):")
                        .setBold().setFont(font).setFontSize(11))
                .add(new Paragraph("Nasza Firma Sp. z o.o.\nMagazyn Główny\nUl. Magazynowa 10\n35-001 Rzeszów")
                        .setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);

        // Dostawca
        Dostawca d = zamowienie.getDostawca();
        Cell supplierCell = new Cell().add(new Paragraph("Dostawca:")
                        .setBold().setFont(font).setFontSize(11))
                .add(new Paragraph(d.getNazwaDostawcy() + "\n" + d.getAdres())
                        .setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);

        headerTable.addCell(receiverCell);
        headerTable.addCell(supplierCell);
        document.add(headerTable);

        // --- TABELA PRODUKTÓW ---
        float[] productWidths = {1, 5, 2};
        Table table = new Table(UnitValue.createPercentArray(productWidths)).useAllAvailableWidth().setMarginTop(20);

        table.addHeaderCell(new Cell().add(new Paragraph("Lp.").setBold().setFont(font)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Nazwa towaru").setBold().setFont(font)).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Ilość").setBold().setFont(font)).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Pobieramy pozycje zamówienia
        List<ZamowienieProduktyDostawcy> pozycje = pozycjeRepository.findByIdZamowienia(idZamowienia);
        int lp = 1;

        for (ZamowienieProduktyDostawcy pozycja : pozycje) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(lp++)).setFont(font)));

            // Pobieranie nazwy produktu
            String nazwaProduktu = produktRepository.findById(pozycja.getIdProduktu())
                    .map(p -> p.getNazwaProduktu())
                    .orElseGet(() -> {
                        log.warn("Produkt o ID {} nie istnieje w bazie, a występuje w zamówieniu {}",
                                pozycja.getIdProduktu(), idZamowienia);
                        return "Nieznany produkt (ID: " + pozycja.getIdProduktu() + ")";
                    });

            table.addCell(new Cell().add(new Paragraph(nazwaProduktu).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(pozycja.getIlosc().toString() + " szt.").setFont(font)));
        }
        document.add(table);

        // Stopka ze statusem
        String statusText = (zamowienie.getStatus() == 2) ? "Zrealizowane" : "W trakcie realizacji";
        document.add(new Paragraph("\nStatus zamówienia: " + statusText)
                .setFont(font).setFontSize(10).setItalic());

        document.close();

        log.info("Pomyślnie wygenerowano plik PDF dla zamówienia {}", idZamowienia);
        return baos.toByteArray();
    }
}
