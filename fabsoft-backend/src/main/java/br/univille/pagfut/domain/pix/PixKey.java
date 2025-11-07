package br.univille.pagfut.domain.pix;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class PixKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    KeyType keyType;

    @Column(unique = true)
    String keyValue;
    String recipientName;
    String recipientCity;
}
