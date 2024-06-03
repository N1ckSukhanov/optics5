package com.app.optics.services;

import com.app.optics.models.Photo;
import com.app.optics.repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final CustomerService customerService;

    public void save(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Photo photo = new Photo(fileName, file.getContentType(),
                    file.getBytes(), customerService.getCurrentId());
            photoRepository.save(photo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Integer id) {
        photoRepository.deleteById(id);
    }

    public Photo getPhoto(Integer id) {
        return photoRepository.findById(id).get();
    }

    public List<Photo> getImagesByCurrent() {
        return photoRepository.findByCustomerId(customerService.getCurrentId());
    }
}
