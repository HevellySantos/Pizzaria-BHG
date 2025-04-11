package br.PI.Pizzaria.encriptar;

import br.PI.Pizzaria.modelCliente.Cliente;
import br.PI.Pizzaria.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EncryptSenhasClientes implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public EncryptSenhasClientes(ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<Cliente> clientes = clienteRepository.findAll();

        for (Cliente cliente : clientes) {
            String senha = cliente.getSenha();
            if (!senha.startsWith("$2")) { // Verifica se já está encriptada
                cliente.setSenha(passwordEncoder.encode(senha));
                clienteRepository.save(cliente);
                System.out.println("Senha encriptada para o cliente: " + cliente.getEmail());
            }
        }

        System.out.println("Senhas de clientes atualizadas!");
    }
}
