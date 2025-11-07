package br.univille.pagfut.repository;

import br.univille.pagfut.domain.pix.KeyType;
import br.univille.pagfut.domain.pix.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
    Optional<PixKey> findByKeyValueAndKeyType(String keyValue, KeyType keyType);
}
