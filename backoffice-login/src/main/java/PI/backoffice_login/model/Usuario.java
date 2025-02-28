package PI.backoffice_login.model;

import jakarta.persistence.*;

import PI.backoffice_login.model.GrupoUsuario;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@Entity
@Table(name = "usuarios")
public class Usuario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;

        @Column(unique = true, nullable = false)
        private String email;

        private String senhaHash;

        @Enumerated(EnumType.STRING)
        private GrupoUsuario grupo;

        private boolean habilitado;

        // Getters e Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenhaHash() {
            return senhaHash;
        }

        public void setSenhaHash(String senhaHash) {
            this.senhaHash = senhaHash;
        }

        public GrupoUsuario getGrupo() {
            return grupo;
        }

        public void setGrupo(GrupoUsuario grupo) {
            this.grupo = grupo;
        }

        public boolean isHabilitado() {
            return habilitado;
        }

        public void setHabilitado(boolean habilitado) {
            this.habilitado = habilitado;
        }
    }



}
