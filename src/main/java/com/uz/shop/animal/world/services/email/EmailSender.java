package com.uz.shop.animal.world.services.email;

public interface EmailSender {
    void send(String email, String body, Boolean activation);
}
