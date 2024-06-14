package br.com.sicrediApp.sicrediApp.voto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.sicrediApp.sicrediApp.pauta.Pauta;
import br.com.sicrediApp.sicrediApp.pauta.PautaRepository;
import br.com.sicrediApp.sicrediApp.usuario.Usuario;
import br.com.sicrediApp.sicrediApp.usuario.UsuarioRepository;

@Service
public class VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ResponseEntity<String> verificarERegistrarVoto(Long pautaId, Long usuarioId, Boolean voto) {
        boolean jaVotou = votoRepository.existsByUsuarioIdAndPautaId(usuarioId, pautaId);

        if (jaVotou) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Você já votou nesta pauta.");
        } else {
            // Buscar o usuário e a pauta
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            Optional<Pauta> pautaOpt = pautaRepository.findById(pautaId);

            if (!usuarioOpt.isPresent() || !pautaOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Usuário ou pauta não encontrado.");
            }

            Usuario usuario = usuarioOpt.get();
            Pauta pauta = pautaOpt.get();

            // Registrar o voto
            Voto novoVoto = new Voto();
            novoVoto.setUsuario(usuario);
            novoVoto.setPauta(pauta);
            novoVoto.setVoto(voto);
            votoRepository.save(novoVoto);

            // Atualizar contadores na pauta
            if (voto) {
                pauta.setVotosSim(pauta.getVotosSim() + 1);
            } else {
                pauta.setVotosNao(pauta.getVotosNao() + 1);
            }
            pautaRepository.save(pauta);

            return ResponseEntity.status(HttpStatus.CREATED).body("Voto registrado com sucesso.");
        }
    }
}