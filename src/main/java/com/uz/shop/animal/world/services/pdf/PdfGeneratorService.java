package com.uz.shop.animal.world.services.pdf;

import com.uz.shop.animal.world.models.Order;
import com.uz.shop.animal.world.models.OrderUnit;
import com.uz.shop.animal.world.models.User;
import com.uz.shop.animal.world.models.enums.UserType;
import com.uz.shop.animal.world.repository.OrderRepository;
import com.uz.shop.animal.world.repository.OrderUnitRepository;
import com.uz.shop.animal.world.services.pdf.generators.InvoiceGenerator;
import com.uz.shop.animal.world.services.pdf.generators.LabelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static com.uz.shop.animal.world.utils.Dictionary.ITEM_NOT_FOUND;

@Service
public class PdfGeneratorService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderUnitRepository orderUnitRepository;

    public boolean generateInvoice(HttpServletResponse response, long idOrder) {
        Order order = orderRepository.findById(idOrder).orElseThrow(() -> {
            throw new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null);
        });
        Collection<OrderUnit> units = orderUnitRepository.findByOrderId(idOrder);

        if (!this.checkAccess(order)) {
            return false;
        }

        InvoiceGenerator generator = new InvoiceGenerator(order, units);
        generator.generate(response);

        return true;
    }

    public boolean generateLabel(HttpServletResponse response, long idOrder) {
        Order order = orderRepository.findById(idOrder).orElseThrow(() -> {
            throw new RestClientResponseException(ITEM_NOT_FOUND, 404, HttpStatus.NOT_FOUND.name(), null, null, null);
        });

        if (!this.checkAccess(order)) {
            return false;
        }

        LabelGenerator generator = new LabelGenerator(order.getAddress());
        generator.generate(response);

        return true;
    }

    private boolean checkAccess(Order order) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        return order != null && (UserType.ROLE_ADMIN == user.getUserType() || user.getId() == order.getAddress().getUser().getId());
    }
}
