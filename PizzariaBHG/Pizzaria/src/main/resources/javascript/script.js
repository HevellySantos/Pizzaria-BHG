const API_URL = "http://localhost:8080/usuarios";

// Carregar usuários na inicialização
window.onload = buscarUsuarios;

function buscarUsuarios() {
    fetch(API_URL)
        .then(response => response.json())
        .then(data => {
            let tableBody = document.getElementById("usuariosTable");
            tableBody.innerHTML = "";
            data.forEach(usuario => {
                let row = `<tr>
                    <td>${usuario.nome}</td>
                    <td>${usuario.email}</td>
                    <td>${usuario.role}</td>
                    <td><button onclick="editarUsuario(${usuario.id})">Alterar</button></td>
                    <td><button onclick="alterarStatus(${usuario.id})">${usuario.role === 'INATIVO' ? 'Ativar' : 'Inativar'}</button></td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        });
}

function abrirCadastro() {
    document.getElementById("formContainer").style.display = "block";
}

function fecharCadastro() {
    document.getElementById("formContainer").style.display = "none";
}

function salvarUsuario() {
    let id = document.getElementById("userId").value;
    let nome = document.getElementById("nome").value;
    let email = document.getElementById("email").value;
    let cpf = document.getElementById("cpf").value;
    let senha = document.getElementById("senha").value;
    let confirmarSenha = document.getElementById("confirmarSenha").value;

    if (senha !== confirmarSenha) {
        alert("As senhas não coincidem!");
        return;
    }

    let usuario = { nome, email, cpf, senha, role: "ADMIN" };

    let method = id ? "PUT" : "POST";
    let url = id ? `${API_URL}/${id}` : API_URL;

    fetch(url, {
        method: method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(usuario)
    }).then(() => {
        fecharCadastro();
        buscarUsuarios();
    });
}

function editarUsuario(id) {
    fetch(`${API_URL}/${id}`)
        .then(response => response.json())
        .then(usuario => {
            document.getElementById("userId").value = usuario.id;
            document.getElementById("nome").value = usuario.nome;
            document.getElementById("email").value = usuario.email;
            document.getElementById("cpf").value = usuario.cpf;
            document.getElementById("senha").value = "";
            document.getElementById("confirmarSenha").value = "";
            abrirCadastro();
        });
}

function alterarStatus(id) {
    fetch(`${API_URL}/${id}/toggle-status`, { method: "PUT" })
        .then(() => buscarUsuarios());
}
