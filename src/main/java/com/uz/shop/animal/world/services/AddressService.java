package com.uz.shop.animal.world.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uz.shop.animal.world.models.Address;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.repository.AddressRepository;
import com.uz.shop.animal.world.request.AddressRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;

import static com.uz.shop.animal.world.utils.Dictionary.ITEM_NOT_FOUND;

//Serwis odpowiadający za wszystkie biznesowe procesy dla danej klasy
@Service
@AllArgsConstructor
public class AddressService {

    @Autowired
    private final AddressRepository addressRepository;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    private User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    //Adresy po adresie email
    public ResponseEntity<List<Address>> findByUser() {
        return ResponseEntity.ok(new ArrayList<>(addressRepository.findByUserEmail(getAuthUser().getEmail())));
    }

    //Odpowiedzi z serwera dla Create oraz Update
    private ResponseEntity<ObjectNode> responses(Address address, Boolean isCreate) {
        ObjectNode tree = mapper.valueToTree(address);
        if(isCreate) {
            return ResponseEntity.status(HttpStatus.CREATED).body(tree);
        } else {
            return ResponseEntity.ok(tree);
        }
    }

    /*
    Tworzenie adresu.
    Pobierany jest zalogowany użytkownik.
    Zapisywany jest nowy adres oraz tworzony w bazie.
     */
    public ResponseEntity<ObjectNode> createAddress(AddressRequest request) {
        User user = getAuthUser();

        Address address = new Address(user, request.getFirstname(), request.getLastname(), request.getCompany(), request.getNip(), request.getMobileNumber(), request.getStreet(), request.getPostalCode(), request.getCity());

        address = addressRepository.save(address);

        return responses(address, true);
    }

        /*
            Wyszukanie adresu przez ID
         */
    private Address findAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() ->
                new RestClientResponseException(ITEM_NOT_FOUND, 400, HttpStatus.BAD_REQUEST.name(), null, null, null)
        );
    }

    /*
    Zwracanie adresu przez ID
     */
    public ResponseEntity<ObjectNode> getAddressById(Long id) {
        Address address = findAddressById(id);

        return responses(address, false);
    }


    /*
    Aktualizacja Adresu
    Pobierane zostają wszystkie elementy oraz podstawiane do obiektu, a następnie zapisywane w bazie.
     */
    public ResponseEntity<ObjectNode> updateAddress(Long id, AddressRequest request) {
        Address address = findAddressById(id);

        address.setFirstname(request.getFirstname());
        address.setLastname(request.getLastname());
        address.setCompany(request.getCompany());
        address.setNip(request.getNip());
        address.setMobileNumber(request.getMobileNumber());
        address.setStreet(request.getStreet());
        address.setPostalCode(request.getPostalCode());
        address.setCity(request.getCity());

        address = addressRepository.save(address);

        return responses(address, false);
    }

    /*
    Usuwanie adresu
    Adres jest wyszukiwany w adresie
     */
    public ResponseEntity.BodyBuilder deleteAddress(Long id) {
        Address address = findAddressById(id);

        addressRepository.delete(address);

        return ResponseEntity.status(HttpStatus.NO_CONTENT);
    }

}
