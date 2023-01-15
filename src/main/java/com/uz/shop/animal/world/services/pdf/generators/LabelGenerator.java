package com.uz.shop.animal.world.services.pdf.generators;

import com.lowagie.text.*;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uz.shop.animal.world.models.Address;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

@AllArgsConstructor
public class LabelGenerator extends BaseGenerator {
    private Address address;

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A7;
    }

    @Override
    protected Font getFont() {
        return new Font(Font.HELVETICA, 9);
    }

    @Override
    public void generate(HttpServletResponse response) {
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(this.document, response.getOutputStream());

            this.document.open();
            this.document.add(this.createCourierLabelWithBarcode(pdfWriter));
            this.document.add(this.createCompanyAndCustomer());
            this.document.close();
        } catch (Exception e) {}
    }

    private PdfPTable createCourierLabelWithBarcode(PdfWriter pdfWriter) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        Barcode128 barcode = new Barcode128();
        barcode.setCode(this.getRandomStringOfNumbers());
        Image image = barcode.createImageWithBarcode(pdfWriter.getDirectContent(), null, null);

        table.addCell(this.creator.getCell(this.creator.getParagraph("PackageCompany", true), null, Element.ALIGN_CENTER));
        table.addCell(this.creator.getCell(image, null));

        return table;
    }

    private PdfPTable createCompanyAndCustomer() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        table.addCell(this.creator.getCompanyData("Nadawca"));
        table.addCell(this.creator.getBlankCell());
        table.addCell(this.creator.getCustomerData("Odbiorca", this.address));

        return table;
    }

    public String getRandomStringOfNumbers() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            result.append(random.nextInt(10));
        }

        return result.toString();
    }
}
