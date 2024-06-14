/**
 * Author: Antônio Oscar Gehrke
 */

package br.com.sicrediApp.sicrediApp.pauta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {
    Optional<Pauta> findByNome(String nome);
    boolean existsByNome(String nome);
}
