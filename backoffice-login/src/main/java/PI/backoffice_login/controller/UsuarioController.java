package PI.backoffice_login.controller;


import PI.backoffice_login.dto.LoginRequest;
import PI.backoffice_login.model.GrupoUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;



import java.util.Optional;

public class UsuarioController {
    @RestController
    @RequestMapping("/api")


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
