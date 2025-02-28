package backoffice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import com.exemplo.backoffice.model.Usuario;
import com.exemplo.backoffice.model.GrupoUsuario;
import com.exemplo.backoffice.dto.LoginRequest;
import com.exemplo.backoffice.repository.UsuarioRepository;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        if (usuario.getGrupo() == null || (usuario.getGrupo() != GrupoUsuario.ADMINISTRADOR && usuario.getGrupo() != GrupoUsuario.ESTOQUISTA)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenhaHash())) {
            return ResponseEntity.status(401).body("Senha incorreta");
        }

        if (!usuario.isHabilitado()) {
            return ResponseEntity.status(403).body("Usuário desabilitado");
        }

        session.setAttribute("usuario", usuario);
        return ResponseEntity.ok("Login realizado com sucesso");
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
