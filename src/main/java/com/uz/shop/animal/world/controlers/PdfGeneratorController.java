package com.uz.shop.animal.world.controlers;

import com.uz.shop.animal.world.handlers.ApiError;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.services.pdf.PdfGeneratorService;
import com.uz.shop.animal.world.utils.Dictionary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "PDF")
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

    @Operation(description = "Pobranie etykiety przesyłki do zamówienia. Dostępne tylko z autoryzacją.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_PDF_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("label")
    public ResponseEntity<?> createLabel(HttpServletResponse response, @Parameter(description = "Identyfikator zamówienia", required = true) @RequestParam("id_order") long idOrder) {
        if (!this.pdfGeneratorService.generateLabel(response, idOrder)) {
            return new ResponseEntity<String>(Dictionary.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        this.prepareResponse(response, Dictionary.Document.INVOICE);
        return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
    }

    @Operation(description = "Pobranie faktury do zamówienia. Dostępne tylko z autoryzacją.", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = OrderUnit.class), mediaType = MediaType.APPLICATION_PDF_VALUE), responseCode = "200", description = "Pobrano"),
            @ApiResponse(content = @Content(schema = @Schema(implementation = ApiError.class), mediaType = MediaType.APPLICATION_JSON_VALUE), responseCode = "404", description = "Nie znaleziono elementu")})
    @GetMapping("/invoice")
    public ResponseEntity<?> invoice(HttpServletResponse response, @Parameter(description = "Identyfikator zamówienia", required = true) @RequestParam("id_order") long idOrder) {
        if (!this.pdfGeneratorService.generateInvoice(response, idOrder)) {
            return new ResponseEntity<String>(Dictionary.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        this.prepareResponse(response, Dictionary.Document.INVOICE);
        return new ResponseEntity<HttpServletResponse>(response, HttpStatus.OK);
    }
}
