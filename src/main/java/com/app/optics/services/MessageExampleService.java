package com.app.optics.services;

import com.app.optics.models.MessageExample;
import com.app.optics.repositories.MessageExampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageExampleService {
    private final MessageExampleRepository messageExampleRepository;

    public MessageExample get(){
        if (messageExampleRepository.findAll().isEmpty()){
            messageExampleRepository.save(new MessageExample("{}, купите линзы!", "{}, очки уже готовы!"));
        }
        return messageExampleRepository.findAll().getFirst();
    }

    public void save(MessageExample messageExample){
        messageExampleRepository.save(messageExample);
    }

}
