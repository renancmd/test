import { checkEmail, checkName, checkPassword, checkConfirmPassword } from "/assets/js/authValidation.js";

const btnRegister = document.getElementById("register");

const email = document.getElementById("email");
const name = document.getElementById("name");
const password = document.getElementById("password");
const confirmPassword = document.getElementById("confirmPassword");

// Cadastra usuário
btnRegister.addEventListener("click", e => {
    e.preventDefault();

    let isValid = true;

    if (!checkEmail(email.value)) {
        document.getElementById("emailError").innerText = "Email inválido. Use um provedor como gmail ou icloud e termine com .com";
        isValid = false;
    } else {
        document.getElementById("emailError").textContent = "";
    }

    if (!checkName(name.value)) {
        document.getElementById("nameError").innerText = "O nome deve começar com letra maiúscula, conter apenas letras e ter no mínimo 3 caracteres.";
        isValid = false;
    } else {
        document.getElementById("nameError").innerText = "";
    }

    if (!checkPassword(password.value)) {
        document.getElementById("passwordError").innerText = "A senha deve ter entre 8 e 50 caracteres, com pelo menos uma letra maiúscula, uma minúscula, um número e um símbolo.";
        isValid = false;
    } else {
        document.getElementById("passwordError").innerText = "";
    }

    if (!checkConfirmPassword(password.value, confirmPassword.value)) {
        document.getElementById("confirmPasswordError").innerText = "As senhas não coincidem.";
        isValid = false;
    } else {
        document.getElementById("confirmPasswordError").innerText = "";
    }

    if (!isValid) return;

    fetch("/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            email: email.value,
            name: name.value,
            password: password.value
        })
    })
        .then(res => {
            if (res.ok) {
                return res.text();
            } else {
                throw new Error("Falha ao cadastrar");
            }
        })
        .then(token => {
            window.location.href = "/login/login.html";
        })
});
