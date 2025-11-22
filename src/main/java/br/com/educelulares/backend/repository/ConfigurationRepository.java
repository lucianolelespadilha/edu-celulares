package br.com.educelulares.backend.repository;

import br.com.educelulares.backend.entity.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {

    Optional<Configuration> findByKeyName(String keyName);

    String keyName(String keyName);
}
