package com.app.optics.repositories;

import com.app.optics.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByOrderByDateAsc();
}
