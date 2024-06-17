/**
 * Author: Antônio Oscar Gehrke
 */

package br.com.sicrediApp.sicrediApp.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        try {
            // Busca o usuário pelo nome de usuário e senha
            Optional<Usuario> usuarioAutenticado = usuarioRepository.findByUsuarioAndSenha(usuario.getUsuario(), usuario.getSenha());

            if (usuarioAutenticado.isPresent()) {
                return ResponseEntity.ok(usuarioAutenticado.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar a solicitação");
        }
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Verifica se já existe um usuário com o mesmo nome
            boolean usuarioJaExistente = usuarioRepository.existsByUsuario(usuario.getUsuario());
            
            if (usuarioJaExistente) {
                // Usuário já cadastrado, retornar um aviso para o front-end
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }
            
            // Caso não exista, salva o novo usuário
            Usuario novoUsuario = usuarioRepository.save(new Usuario(usuario.getUsuario(), usuario.getSenha()));
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (Exception e) {
            // Caso ocorra algum erro interno
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{usuario}")
    public ResponseEntity<Usuario> buscarUsuarioPorUsuario(@PathVariable("usuario") String usuario) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsuario(usuario);

        if (usuarioEncontrado.isPresent()) {
            return new ResponseEntity<>(usuarioEncontrado.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
