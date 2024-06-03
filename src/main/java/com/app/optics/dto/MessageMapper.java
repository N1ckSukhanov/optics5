package com.app.optics.dto;

import com.app.optics.models.Customer;
import com.app.optics.models.Message;
import com.app.optics.models.products.Lenses;
import com.app.optics.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageMapper {
    private final CustomerRepository customerRepository;

    public MessageProduct lensesToMessage(Lenses lenses){
        Customer customer = customerRepository.getReferenceById(lenses.getCustomerId());
        return new MessageProduct(lenses.getId(), customer.getPhone(), customer.getName(), lenses.getReceptionDate(), "");
    }

    public MessageProduct modelToMessage(Message message){
        Customer customer = customerRepository.getReferenceById(message.getCustomerId());
        return new MessageProduct(message.getId(), customer.getPhone(), customer.getName(), message.getDate(), message.getText());
    }

}
