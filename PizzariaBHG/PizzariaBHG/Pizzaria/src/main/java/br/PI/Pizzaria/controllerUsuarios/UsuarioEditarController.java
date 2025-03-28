package br.PI.Pizzaria.controllerUsuarios;

import br.PI.Pizzaria.modelUsuarios.UpdateUsuarioDTO;
import br.PI.Pizzaria.modelUsuarios.Usuario;
import br.PI.Pizzaria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.PI.Pizzaria.modelUsuarios.Role;

@Controller
@RequestMapping("/usuarios")
public class UsuarioEditarController {

    @Autowired
    private UsuarioService usuarioService;

    // Método GET para carregar o formulário de edição
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id); // Método para buscar usuário por ID
        model.addAttribute("usuario", usuario);
        return "editarUsuario"; // Nome do template ou página de edição
    }

    // Método POST para atualizar o usuário
    @PostMapping("/editar/{id}")
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute UpdateUsuarioDTO dto,
                                   @RequestParam("role") Role role, RedirectAttributes redirectAttributes) {
        try {
            // Atualiza o role diretamente no DTO antes de passar para o serviço
            dto.setRole(role);
            Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, dto);

            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
            return "redirect:/usuarios"; // Redireciona para a listagem de usuários
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/usuarios/editar/" + id; // Redireciona para a página de edição se houver erro
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário.");
            return "redirect:/usuarios/editar/" + id; // Redireciona para a página de edição em caso de erro
        }
    }

}





