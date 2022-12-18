package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.services.pdf.PdfGeneratorService;
import com.uz.shop.animal.world.utils.Dictionary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RequestMapping("/api/v1/pdf")
@RestController
public class PdfGeneratorController {
    private void prepareResponse(HttpServletResponse response, String filePrefix) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        String currentDateFormatted = dateFormat.format(new Date());

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePrefix + "_" + currentDateFormatted + ".pdf");
    }

    @GetMapping("/label")
    public void label(HttpServletResponse response, @RequestParam("order_id") int idOrder) {
        // TODO: #PDF - Dodać sprawdzenia czy order należy do użytkownika, który pyta o dokument / jest administratorem
        this.prepareResponse(response, Dictionary.Document.LABEL);
        PdfGeneratorService.generateLabel(response, idOrder);
    }

    @GetMapping("/invoice")
    public void invoice(HttpServletResponse response, @RequestParam("order_id") int idOrder) {
        // TODO: #PDF - Dodać sprawdzenia czy order należy do użytkownika, który pyta o dokument / jest administratorem
        this.prepareResponse(response, Dictionary.Document.INVOICE);
        PdfGeneratorService.generateInvoice(response, idOrder);
    }
}
