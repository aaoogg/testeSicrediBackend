/**
 * Author: Antônio Oscar Gehrke
 */

package br.com.sicrediApp.sicrediApp.pauta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.sicrediApp.sicrediApp.voto.VotoController;
import br.com.sicrediApp.sicrediApp.voto.VotoService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/pautas")
@CrossOrigin(origins = "http://localhost:4200")
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotoService votoService;

    @PostMapping
    public Pauta createPauta(@RequestBody Pauta pauta) {
        return pautaRepository.save(pauta);
    }
    
    @GetMapping
    public List<Pauta> getPautas() {
        return pautaRepository.findAll();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Pauta> getPautaById(@PathVariable Long id) {
        Optional<Pauta> pauta = pautaRepository.findById(id);
        if (pauta.isPresent()) {
            return ResponseEntity.ok(pauta.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/votar")
    public ResponseEntity<String> votar(@PathVariable Long id, @RequestBody Map<String, Object> votoData) {
        Long usuarioId = Long.valueOf(votoData.get("usuarioId").toString());
        Boolean voto = Boolean.valueOf(votoData.get("voto").toString());

        Optional<Pauta> pautaOpt = pautaRepository.findById(id);
        if (!pautaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pauta pauta = pautaOpt.get();
        if (!pauta.getPautaEmVotacao()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A pauta não está em votação.");
        }

        return votoService.verificarERegistrarVoto(id, usuarioId, voto);
    }

    @PostMapping("/{id}/iniciar-votacao")
    @Transactional
    public ResponseEntity<Void> iniciarVotacao(@PathVariable Long id, @RequestParam(required = false) Integer tempoVotacao) {
        Optional<Pauta> pautaOpt = pautaRepository.findById(id);
        if (!pautaOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Pauta pauta = pautaOpt.get();
        pauta.setPautaEmVotacao(true);
        pautaRepository.save(pauta);

        // Verifica se tempoVotacao foi especificado, senão usa o padrão de 60 segundos
        int tempoEmSegundos = (tempoVotacao != null && tempoVotacao > 0) ? tempoVotacao * 60 : 60;

        // Iniciar uma nova thread para contar o tempo especificado
        new Thread(() -> {
            try {
                Thread.sleep(tempoEmSegundos * 1000); // Converte minutos para milissegundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // Encerrar a votação após o tempo especificado
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        pauta.setPautaEmVotacao(false);
        pauta.setVotacaoFinalizada(true);
        pautaRepository.save(pauta);
        return ResponseEntity.ok().build();
    }
}
