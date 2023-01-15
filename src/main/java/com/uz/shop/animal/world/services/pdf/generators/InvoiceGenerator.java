package com.uz.shop.animal.world.services.pdf.generators;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.models.Product;
import com.uz.shop.animal.world.utils.Dictionary;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

@AllArgsConstructor
public class InvoiceGenerator extends BaseGenerator {
    private Order order;

    private Collection<OrderUnit> units;

    @Override
    protected Rectangle getPageSize() {
        return PageSize.A4;
    }

    @Override
    protected Font getFont() {
        return new Font(Font.HELVETICA, 12);
    }

    @Override
    public void generate(HttpServletResponse response) {
        try {
            PdfWriter.getInstance(this.document, response.getOutputStream());

            document.open();
            document.add(this.createHeader());
            document.add(this.createNumberAndDates());
            document.add(this.createCompanyAndCustomer());
            document.add(this.createOrderList());
            document.close();
        } catch (Exception e) {}
    }

    private PdfPTable createHeader() throws IOException {
        Image image = Image.getInstance("resources/images/logo.png");
        int[] border = new int[] {0, 0, 1, 0};

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(new float[] {395, 50, 150}, PageSize.A4);
        table.addCell(this.creator.getCell(border));
        table.addCell(this.creator.getCell(image, border, Element.ALIGN_RIGHT));
        table.addCell(this.creator.getCell(Dictionary.Company.NAME.toUpperCase(), border));

        return table;
    }

    private PdfPTable createNumberAndDates() {
        String currentDateFormatted = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        StringBuilder content = new StringBuilder();
        content.append("Faktura FV-").append(this.order.getId());
        PdfPCell cellInvoiceNumber = this.creator.getCell(content.toString());

        content.setLength(0);
        content.append("Data sprzedaży: ").append(currentDateFormatted);
        PdfPCell cellSaleDate = this.creator.getCell(content.toString(), Element.ALIGN_RIGHT);

        content.setLength(0);
        content.append("Data wystawienia: ").append(currentDateFormatted);
        PdfPCell cellInvoiceDate = this.creator.getCell(content.toString(), Element.ALIGN_RIGHT);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(cellInvoiceNumber);
        table.addCell(cellSaleDate);
        table.addCell(this.creator.getBlankCell());
        table.addCell(cellInvoiceDate);

        return table;
    }

    private PdfPTable createCompanyAndCustomer() {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        table.addCell(this.creator.getBlankCell());
        table.addCell(this.creator.getCompanyData("Sprzedawca"));
        table.addCell(this.creator.getBlankCell());
        table.addCell(this.creator.getCustomerData("Nabywca", order.getAddress()));
        table.addCell(this.creator.getBlankCell());

        return table;
    }

    private PdfPTable createOrderList() {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        int[] borderLabel = new int[] {1, 0, 1, 0};

        table.addCell(this.creator.getCell("Nazwa", borderLabel));
        table.addCell(this.creator.getCell("Ilość", borderLabel, Element.ALIGN_CENTER));
        table.addCell(this.creator.getCell("Netto", borderLabel, Element.ALIGN_RIGHT));
        table.addCell(this.creator.getCell("VAT", borderLabel, Element.ALIGN_RIGHT));
        table.addCell(this.creator.getCell("Brutto", borderLabel, Element.ALIGN_RIGHT));

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        float sumNett = 0;
        float sumTax = 0;
        float sumGross = 0;

        for (OrderUnit unit : this.units) {
            Product product = unit.getProduct();


            float nett = (float) (unit.getOldPrice() * 1f);
            float tax = nett * 0.23f;
            float gross = nett + tax;

            sumNett += nett;
            sumTax += tax;
            sumGross += gross;

            table.addCell(this.creator.getCell(unit.getOldProductName()));
            table.addCell(this.creator.getCell(String.valueOf(unit.getAmount()), Element.ALIGN_CENTER));
            table.addCell(this.creator.getCell(decimalFormat.format(nett), Element.ALIGN_RIGHT));
            table.addCell(this.creator.getCell(decimalFormat.format(tax), Element.ALIGN_RIGHT));
            table.addCell(this.creator.getCell(decimalFormat.format(gross), Element.ALIGN_RIGHT));
        }

        int[] borderSum = new int[] {2, 0, 0, 0};
        table.addCell(this.creator.getCell(this.creator.getParagraph("Razem", true), borderSum));
        table.addCell(this.creator.getCell(this.creator.getParagraph(String.valueOf(units.size()), true), borderSum, Element.ALIGN_CENTER));
        table.addCell(this.creator.getCell(this.creator.getParagraph(decimalFormat.format(sumNett), true), borderSum, Element.ALIGN_RIGHT));
        table.addCell(this.creator.getCell(this.creator.getParagraph(decimalFormat.format(sumTax), true), borderSum, Element.ALIGN_RIGHT));
        table.addCell(this.creator.getCell(this.creator.getParagraph(decimalFormat.format(sumGross), true), borderSum, Element.ALIGN_RIGHT));

        return table;
    }
}
