package com.app.optics.services;

import com.app.optics.models.Message;
import com.app.optics.models.MessageExample;
import com.app.optics.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MessageService {
    private static final String name = "nick_sukhanov";
    private static final String password = "rn16tJtI";
    private static final String sender = "TEST-SMS";
    private final MessageRepository messageRepository;
    private final MessageExampleService messageExampleService;

    public void addMessages(){
        messageRepository.save(new Message("6551601708", "Добрый день!", LocalDate.now(), 4));
        messageRepository.save(new Message("6551601839", "Второе сообщение для теста", LocalDate.now(), 4));
    }

    public String getLensesText(String name){
        MessageExample messageExample = messageExampleService.get();
        return messageExample.getLenses().replaceFirst("\\{}", name);
    }

    private static String getAuthStrEnc() {
        String authString = name + ":" + password;
        return Base64.getEncoder().encodeToString(authString.getBytes());
    }
// Send message, phone: 79811944787, text: Добрый день!, date: 2024-06-04
// send - status: accepted, id: 6551601708
// Send message, phone: 79811944787, text: Второе сообщение для теста, date: 2024-06-05
// send - status: accepted, id: 6551601839
    public String send(String phone, String text) {
        text = URLEncoder.encode(text, StandardCharsets.UTF_8);
        phone = "%2B" + phone;

        String address = "/messages/v2/send/?phone=" + phone + "&text=" + text + "&sender=" + sender;
        String result = "ok;123";// createRequest(address);
        String status = result.split(";")[0];
        String id = result.split(";")[1];
        System.out.println("/send - status: " + status + ", id: " + id);
        return id;
    }

    public void status(String messageId) {
        String address = "/messages/v2/status/?id=" + messageId;
        String result = createRequest(address);
        String id = result.split(";")[0];
        String status = result.split(";")[1];
        System.out.println("/status - id: " + id + ", status: " + status);
    }

    public String balance() {
        String address = "/messages/v2/balance/";
        String result = createRequest(address);
        String type = result.split(";")[0];
        String balance = result.split(";")[1];
        String credit = result.split(";")[2];
        System.out.println("/send - type: " + type + ", balance: " + balance + ", credit: " + credit);
        return balance;
    }

    public String createRequest(String address) {
        try {
            URL url = new URL("http", "api.smsfeedback.ru", 80, address);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", getAuthStrEnc());
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                sb.append(charArray, 0, numCharsRead);
            }

            return sb.toString();
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void sendSms() {

    }


}
