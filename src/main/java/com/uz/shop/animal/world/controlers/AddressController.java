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

@EnableAutoConfiguration
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

    @Autowired
    private AddressService addresssService;

    @GetMapping
    public ResponseEntity<List<Address>> getAddress() { return addresssService.findByUser(); }

    @PostMapping
    public ResponseEntity<ObjectNode> postAddress(@Valid @RequestBody AddressRequest request) { return addresssService.createAddress(request); }

    @PatchMapping("/{addressId}")
    public ResponseEntity<ObjectNode> patchAddress(@PathVariable("addressId") Long addressId, @Valid @RequestBody AddressRequest request) { return addresssService.updateAddress(addressId, request); }

    @DeleteMapping("/{addressId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void patchAddress(@PathVariable("addressId") Long addressId) { addresssService.deleteAddress(addressId); }
}
