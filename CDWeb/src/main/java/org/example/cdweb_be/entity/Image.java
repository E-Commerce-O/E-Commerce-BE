package org.example.cdweb_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
//@NoArgsConstructor
public class Image {
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String imageName;
    @Lob
    byte[] imageData;
    public Image() {
        this.id = generateIdWithExtension();
    }

    private String generateIdWithExtension() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString() + ".jpg"; // Kết hợp UUID với chuỗi .jpg
    }
}
