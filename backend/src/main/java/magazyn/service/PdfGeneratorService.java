package magazyn.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import magazyn.entity.Uzytkownik;
import magazyn.entity.ZamowienieKlienta;
import magazyn.entity.ZamowienieProduktyKlienci;
import magazyn.repository.ZamowienieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;


@Service
public class PdfGeneratorService {

    @Autowired
    private ZamowienieRepository zamowienieRepository;

    public byte[] generateInvoice(Integer idZamowienia) throws Exception {
        // 1. Pobranie danych z bazy
        ZamowienieKlienta zamowienie = zamowienieRepository.findByIdWithProducts(idZamowienia)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zamówienia o ID: " + idZamowienia));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        // Marginesy: góra, prawo, dół, lewo
        Document document = new Document(pdf);
        document.setMargins(20, 36, 36, 36);

        // 2. Czcionka z polskimi znakami
        byte[] fontBytes = getClass().getClassLoader().getResourceAsStream("fonts/Roboto-VariableFont_wdth,wght.ttf").readAllBytes();
        PdfFont font = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);
        PdfFont bold = PdfFontFactory.createFont(fontBytes, PdfEncodings.IDENTITY_H);

        // --- NAGŁÓWEK ---
        document.add(new Paragraph("Data wystawienia: " + zamowienie.getData().toLocalDate())
                .setTextAlignment(TextAlignment.RIGHT).setFont(font).setFontSize(10));

        document.add(new Paragraph("FAKTURA FV NR " + zamowienie.getId() + "/" +LocalDate.now().getMonthValue() + "/2026")
                .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER).setFont(font).setMarginTop(10));

        // --- TABELA SPRZEDAWCA / NABYWCA ---
        float[] headerWidths = {1, 1};
        Table headerTable = new Table(UnitValue.createPercentArray(headerWidths)).useAllAvailableWidth().setMarginTop(20);

        // Sprzedawca (Twoje dane)
        Cell sellerCell = new Cell().add(new Paragraph("Sprzedawca:")
                        .setBold().setFont(font).setFontSize(11))
                .add(new Paragraph("Nasza Firma Sp. z o.o.\nNIP: 9876543210\nUl. Magazynowa 10\n35-001 Rzeszów")
                        .setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);

        // Nabywca (Z Twojego JSON-a)
        Uzytkownik k = zamowienie.getKlient();
        Cell buyerCell = new Cell().add(new Paragraph("Nabywca:")
                        .setBold().setFont(font).setFontSize(11))
                .add(new Paragraph(k.getFirma() + "\nNIP: " + k.getNip() + "\n" + k.getImie() + " " + k.getNazwisko() + "\nTel: " + k.getTelefon())
                        .setFont(font).setFontSize(10))
                .setBorder(Border.NO_BORDER);

        headerTable.addCell(sellerCell);
        headerTable.addCell(buyerCell);
        document.add(headerTable);

        // --- TABELA PRODUKTÓW ---
        // Kolumny: Lp, Nazwa, J.m., Ilość, Cena Netto, VAT, Wartość Brutto
        float[] productWidths = {1, 4, 1, 1, 2, 1, 2};
        Table table = new Table(UnitValue.createPercentArray(productWidths)).useAllAvailableWidth().setMarginTop(20);

        String[] headers = {"Lp.", "Nazwa towaru", "J.m.", "Ilość", "Cena za szt.", "VAT", "Brutto"};
        for (String h : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(h).setBold().setFont(font).setFontSize(10))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        }

        BigDecimal sumaNettoTotal = BigDecimal.ZERO;
        BigDecimal sumaBruttoTotal = BigDecimal.ZERO;
        int lp = 1;

        for (ZamowienieProduktyKlienci pozycja : zamowienie.getPozycje()) {
            BigDecimal cenaNetto = pozycja.getCenaWDniuZakupu();
            BigDecimal ilosc = BigDecimal.valueOf(pozycja.getIlosc());
            BigDecimal wartoscNetto = cenaNetto.multiply(ilosc);
            BigDecimal wartoscBrutto = wartoscNetto.multiply(new BigDecimal("1.23")).setScale(2, RoundingMode.HALF_UP);

            sumaNettoTotal = sumaNettoTotal.add(wartoscNetto);
            sumaBruttoTotal = sumaBruttoTotal.add(wartoscBrutto);

            table.addCell(new Cell().add(new Paragraph(String.valueOf(lp++)).setFont(font).setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph(pozycja.getProdukt().getNazwaProduktu()).setFont(font).setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph(pozycja.getProdukt().getJednostka()).setFont(font).setFontSize(10))); // Bierze "szt" z JSONa
            table.addCell(new Cell().add(new Paragraph(ilosc.toString()).setFont(font).setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", cenaNetto) + " zł").setFont(font).setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph("23%").setFont(font).setFontSize(10)));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", wartoscBrutto) + " zł").setFont(font).setFontSize(10)));
        }
        document.add(table);

        // --- PODSUMOWANIE ---
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).setWidth(200).setHorizontalAlignment(HorizontalAlignment.RIGHT).setMarginTop(10);

        summaryTable.addCell(new Cell().add(new Paragraph("Suma Netto:").setFont(font).setFontSize(10)).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", sumaNettoTotal) + " zł").setFont(font).setFontSize(10)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        summaryTable.addCell(new Cell().add(new Paragraph("Suma VAT (23%):").setFont(font).setFontSize(10)).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", sumaBruttoTotal.subtract(sumaNettoTotal)) + " zł").setFont(font).setFontSize(10)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        summaryTable.addCell(new Cell().add(new Paragraph("RAZEM BRUTTO:").setBold().setFont(font).setFontSize(12)).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.format("%.2f", sumaBruttoTotal) + " zł").setBold().setFont(font).setFontSize(12)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        document.add(summaryTable);

        // Stopka
        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1})).useAllAvailableWidth();
        footerTable.setMarginTop(50);

        // Podpis wystawiającego
        Cell sellerSign = new Cell().add(new Paragraph("--------------------------------------------\npodpis osoby upoważnionej do wystawienia faktury")
                        .setFont(font).setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER);

        // Komórka: Podpis odbierającego
        Cell buyerSign = new Cell().add(new Paragraph("--------------------------------------------\npodpis osoby odbierającej")
                        .setFont(font).setFontSize(9).setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER);

        footerTable.addCell(sellerSign);
        footerTable.addCell(buyerSign);

        document.add(footerTable);

        document.close();
        return baos.toByteArray();
    }
}