package br.com.sicrediApp.sicrediApp.pauta;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/pautas")
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;

    @PostMapping
    public Pauta createPauta(@RequestBody Pauta pauta) {
        return pautaRepository.save(pauta);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pauta> getPauta(@PathVariable Long id) {
        Optional<Pauta> pauta = pautaRepository.findById(id);
        return pauta.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/votar")
    public ResponseEntity<Void> votar(@PathVariable Long id, @RequestBody Map<String, String> votoData) {
        Optional<Pauta> pautaOpt = pautaRepository.findById(id);
        if (!pautaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pauta pauta = pautaOpt.get();
        if (!pauta.getPautaEmVotacao()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String voto = votoData.get("voto");
        if ("sim".equals(voto)) {
            pauta.setVotosSim(pauta.getVotosSim() + 1);
        } else if ("nao".equals(voto)) {
            pauta.setVotosNao(pauta.getVotosNao() + 1);
        } else {
            return ResponseEntity.badRequest().build();
        }
        pautaRepository.save(pauta);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/iniciar-votacao")
    @Transactional
    public ResponseEntity<Void> iniciarVotacao(@PathVariable Long id) {
        Optional<Pauta> pautaOpt = pautaRepository.findById(id);
        if (!pautaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pauta pauta = pautaOpt.get();
        pauta.setPautaEmVotacao(true);
        pautaRepository.save(pauta);

        // Iniciar uma nova thread para contar 60 segundos
        new Thread(() -> {
            try {
                Thread.sleep(60000); // Esperar 60 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Encerrar a votação após 60 segundos
            Optional<Pauta> pautaToUpdateOpt = pautaRepository.findById(id);
            if (pautaToUpdateOpt.isPresent()) {
                Pauta pautaToUpdate = pautaToUpdateOpt.get();
                pautaToUpdate.setPautaEmVotacao(false);
                pautaToUpdate.setVotacaoFinalizada(true);
                pautaRepository.save(pautaToUpdate);
            }
        }).start();

        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/encerrar-votacao")
    public ResponseEntity<Void> encerrarVotacao(@PathVariable Long id) {
        Optional<Pauta> pautaOpt = pautaRepository.findById(id);
        if (!pautaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pauta pauta = pautaOpt.get();
        if (!pauta.getPautaEmVotacao()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Votação não está em andamento
        }
        pauta.setPautaEmVotacao(false);
        pauta.setVotacaoFinalizada(true);
        pautaRepository.save(pauta);
        return ResponseEntity.ok().build();
    }
}
