package com.app.optics.repositories;

import com.app.optics.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    List<Photo> findByCustomerId(Integer id);

    void deleteAllByCustomerId(Integer id);
}
