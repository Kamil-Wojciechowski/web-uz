package com.uz.shop.animal.world.services.pdf;

import com.uz.shop.animal.world.services.pdf.generators.InvoiceGenerator;
import com.uz.shop.animal.world.services.pdf.generators.LabelGenerator;

import javax.servlet.http.HttpServletResponse;

public class PdfGeneratorService {
    public static void generateInvoice(HttpServletResponse response, int idOrder) {
        InvoiceGenerator generator = new InvoiceGenerator(idOrder);
        generator.generate(response);
    }

    public static void generateLabel(HttpServletResponse response, int idOrder) {
        LabelGenerator generator = new LabelGenerator(idOrder);
        generator.generate(response);
    }
}
