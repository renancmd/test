import { checkEmail, checkPassword } from "/assets/js/authValidation.js";

const btnLogin = document.getElementById("entrar");

const email = document.getElementById("email");
const password = document.getElementById("password");

// Loga o usuário
btnLogin.addEventListener("click", e => {
    e.preventDefault();

    let isValid = true;

    if (!checkEmail(email.value)) {
        document.getElementById("emailError").innerText = "Email inválido. Use um provedor como gmail ou icloud e termine com .com";
        isValid = false;
    } else {
        document.getElementById("emailError").textContent = "";
    }

    if (!checkPassword(password.value)) {
        document.getElementById("passwordError").innerText = "A senha deve ter entre 8 e 50 caracteres, com pelo menos uma letra maiúscula, uma minúscula, um número e um símbolo.";
        isValid = false;
    } else {
        document.getElementById("passwordError").innerText = "";
    }

    if (!isValid) return;

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            email: email.value,
            password: password.value
        })
    })
        .then(res => {
            if (res.ok) {
                return res.text();
            } else {
                throw new Error("Falha ao logar");
            }
        })
        .then(token => {
            localStorage.setItem("token", token);
            window.location.href = "/pagina-principal/index.html";
        })
        .catch(err => {
            document.getElementById("passwordError").innerText = "Email ou senha inválidos";
        });
});
