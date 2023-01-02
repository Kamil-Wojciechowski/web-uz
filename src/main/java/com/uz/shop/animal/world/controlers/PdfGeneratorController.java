package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.services.pdf.PdfGeneratorService;
import com.uz.shop.animal.world.utils.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
*/
@RequestMapping("/api/v1/pdf")
@RestController
public class PdfGeneratorController {
    @Autowired
    PdfGeneratorService pdfGeneratorService;

    /*
GetMapping - Request GET - Zbieranie informacji - 200
PostMapping - Request POST - Tworzenie elementów - 201 / 400
PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
 */
    private void prepareResponse(HttpServletResponse response, String filePrefix) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        String currentDateFormatted = dateFormat.format(new Date());

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePrefix + "_" + currentDateFormatted + ".pdf");
    }

    @GetMapping("label")
    public ResponseEntity<?> createLabel(HttpServletResponse response, @RequestParam("id_order") long idOrder) {
        if (!this.pdfGeneratorService.generateLabel(response, idOrder)) {
            return new ResponseEntity<String>(Dictionary.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        this.prepareResponse(response, Dictionary.Document.INVOICE);
        return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
    }

    @GetMapping("/invoice")
    public ResponseEntity<?> invoice(HttpServletResponse response, @RequestParam("id_order") long idOrder) {
        if (!this.pdfGeneratorService.generateInvoice(response, idOrder)) {
            return new ResponseEntity<String>(Dictionary.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        this.prepareResponse(response, Dictionary.Document.INVOICE);
        return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
    }
}
