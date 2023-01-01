package com.uz.shop.animal.world.controlers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.request.AddressRequest;
import com.uz.shop.animal.world.services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/*
Kontrolery służą do zarządzania ścieżkami zapytań oraz interakcją między zapytaniem a przebiegiem zapytania biznesowego
 */
@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /*
    GetMapping - Request GET - Zbieranie informacji - 200
    PostMapping - Request POST - Tworzenie elementów - 201 / 400
    PatchMapping - Request Patch - Aktualizacja elementów - 200 / 400 / 404
    DeleteMapping - Request Delete - Usunięcie elementów - 204 / 404
     */
    @GetMapping
    public ResponseEntity<List<Address>> getAddress() { return addressService.findByUser(); }

    @PostMapping
    public ResponseEntity<ObjectNode> postAddress(@Valid @RequestBody AddressRequest request) { return addressService.createAddress(request); }

    @GetMapping("/{addressId}")
    public ResponseEntity<ObjectNode> getAddress(@PathVariable Long addressId) { return addressService.getAddressById(addressId); }

    @PatchMapping("/{addressId}")
    public ResponseEntity<ObjectNode> patchAddress(@PathVariable("addressId") Long addressId, @Valid @RequestBody AddressRequest request) { return addressService.updateAddress(addressId, request); }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable("addressId") Long addressId) { addressService.deleteAddress(addressId); }
}
