package com.app.optics.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

@Entity
@Table(name = "photo")
@Data
@NoArgsConstructor
public class Photo {
    @Id
    @SequenceGenerator(name = "photo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_seq")
    private Integer id;

    private Integer customerId;

    private String name;

    private String type;

    @Lob
    private byte[] data;

    public Photo(String name, String type, byte[] data, Integer customerId) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.customerId = customerId;
    }

    public String getImage() {
        return String.format("data:%s;base64,%s", type, Base64.encodeBase64String(data));
    }
}
