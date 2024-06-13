package br.com.sicrediApp.sicrediApp.voto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByUsuarioIdAndPautaId(Long usuarioId, Long pautaId);
}
