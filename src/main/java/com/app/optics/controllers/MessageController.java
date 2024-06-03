package com.app.optics.controllers;

import com.app.optics.dto.MessageMapper;
import com.app.optics.dto.MessageProduct;
import com.app.optics.models.AppState;
import com.app.optics.models.Customer;
import com.app.optics.models.Message;
import com.app.optics.models.MessageExample;
import com.app.optics.models.products.Lenses;
import com.app.optics.repositories.CustomerRepository;
import com.app.optics.repositories.MessageRepository;
import com.app.optics.repositories.products.LensesRepository;
import com.app.optics.services.AppService;
import com.app.optics.services.CustomerService;
import com.app.optics.services.MessageExampleService;
import com.app.optics.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.app.optics.util.Constants.HOME;
import static com.app.optics.util.Constants.TO_PRODUCTS;

@Controller
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final CustomerService customerService;
    private final AppService appService;
    private final MessageRepository messageRepository;
    private final LensesRepository lensesRepository;
    private final CustomerRepository customerRepository;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final MessageExampleService messageExampleService;

    @GetMapping("/history")
    public String history(Model model) {
        appService.setModelState(AppState.HISTORY_MESSAGES, model);
        model.addAttribute("my_header", "История сообщений");
        List<MessageProduct> messages = messageRepository.findAllByOrderByDateAsc()
                .stream().map(messageMapper::modelToMessage).toList();
        System.out.println(messages);
        model.addAttribute("messages", messages);
        return HOME;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        appService.setModelState(AppState.MESSAGE_PANEL, model);
        model.addAttribute("my_header", "Панель");

        model.addAttribute("balance", messageService.balance());

        model.addAttribute("messageExample", messageExampleService.get());
        return HOME;
    }

    @PostMapping("/panel")
    public String postSend(@ModelAttribute MessageExample messageExample) {
        messageExampleService.save(messageExample);
        return "redirect:/message/panel";
    }


    @GetMapping("/personal")
    public String pageSend(Model model) {
        Message message = null;
        if (customerService.isCurrent())
            message = new Message();

        appService.setModelState(AppState.SEND_MESSAGE, model);
        model.addAttribute("message", message);
        return HOME;
    }

    @PostMapping("/personal")
    public String postSend(@ModelAttribute Message message) {
        if (!customerService.isCurrent())
            return TO_PRODUCTS;

        String phone = customerService.getCurrent().getPhone().replaceAll("\\+", "").replaceAll(" ", "");
        System.out.println("Send message, phone: " + phone + ", text: " + message.getText() + ", date: " + message.getDate());
        String serviceId = messageService.send(phone, message.getText());
        message.setServiceId(serviceId);
        messageRepository.save(message);
        return TO_PRODUCTS;
    }

    @GetMapping("/lenses")
    public String lenses(Model model) {
        appService.setModelState(AppState.PRODUCT_MESSAGES, model);

        List<MessageProduct> messageProducts = findLenses().stream().map(messageMapper::lensesToMessage).toList();
        model.addAttribute("my_header", "Покупка линз");
        model.addAttribute("products", messageProducts);
        System.out.println(messageProducts);
        return HOME;
    }

    @GetMapping("/lenses/send/{id}")
    public String lensesSend(@PathVariable int id) {
        List<MessageProduct> messageProducts = findLenses().stream().map(messageMapper::lensesToMessage).toList();
        Lenses lenses = lensesRepository.getReferenceById(id);
        Customer customer = customerRepository.getReferenceById(lenses.getCustomerId());
        lenses.setSendMessage(true);
        Message message = new Message(messageService.getLensesText(customer.getName()), LocalDate.now(), lenses.getCustomerId());

        String phone = customer.getPhone().replaceAll("\\+", "").replaceAll(" ", "");
        System.out.println("Send message, phone: " + phone + ", text: " + message.getText() + ", date: " + message.getDate());
        String serviceId = messageService.send(phone, message.getText());
        message.setServiceId(serviceId);
        messageRepository.save(message);
        return "redirect:/message/lenses";
    }

    private List<Lenses> findLenses() {
        Set<Integer> customers = new HashSet<>();
        List<Lenses> lenses = lensesRepository.findAllBySendMessage(false);
        List<Lenses> newLenses = new ArrayList<>();
        for (Lenses lens : lenses) {
            if (!customers.contains(lens.getCustomerId())) {
                customers.add(lens.getCustomerId());
                newLenses.add(lens);
            }
        }
        newLenses.sort((a, b) -> b.getReceptionDate().compareTo(a.getReceptionDate()));
        return newLenses;
    }

}








