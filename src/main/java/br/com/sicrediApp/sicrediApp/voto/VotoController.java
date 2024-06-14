/**
 * Author: Antônio Oscar Gehrke
 */

package br.com.sicrediApp.sicrediApp.voto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
public class VotoController {

    private final VotoRepository votoRepository;

    @Autowired
    public VotoController(VotoRepository votoRepository) {
        this.votoRepository = votoRepository;
    }

    @PostMapping
    public ResponseEntity<String> verificarVoto(@RequestBody Voto voto) {
        boolean jaVotou = votoRepository.existsByUsuarioIdAndPautaId(voto.getUsuario().getId(), voto.getPauta().getId());

        if (jaVotou) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Você já votou nesta pauta.");
        } else {
            votoRepository.save(voto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Voto registrado com sucesso.");
        }
    }
}
