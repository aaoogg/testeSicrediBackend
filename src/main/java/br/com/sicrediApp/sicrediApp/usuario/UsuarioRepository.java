package br.com.sicrediApp.sicrediApp.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuario(String usuario);
    
    boolean existsByUsuario(String usuario);
    
    Optional<Usuario> findByUsuarioAndSenha(String usuario, String senha);
}