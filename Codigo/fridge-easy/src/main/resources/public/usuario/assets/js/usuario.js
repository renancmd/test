const inputName = document.getElementById("name");
const inputEmail = document.getElementById("email");
const btnLogout = document.getElementById("logout");
const token = localStorage.getItem("token");

function showDeleteModal() {
  document.getElementById("delete-modal").classList.remove("hidden");
}

function logout() {
  localStorage.removeItem("token");
  window.location.href = "/login/login.html";
}

// Exibe os dados do usuário
fetch("/user", {
  method: "GET",
  headers: {
    "Authorization": "Bearer " + token
  }
})
  .then(res => res.json())
  .then(data => {
    inputName.value = data.name;
    inputEmail.value = data.email;
    console.log(data);
  });

// Ativa e desativa a botão para editar os dados
function toggleEdit(fieldId) {
  const input = document.getElementById(fieldId);
  const button = input.nextElementSibling;
  const isDisabled = input.disabled;

  input.disabled = !isDisabled;
  button.textContent = isDisabled ? "Salvar" : "Alterar";

  if (!isDisabled) {
    // Enviar a alteração
    const body = new URLSearchParams();
    body.append(fieldId, input.value); // Apenas o campo alterado

    fetch("/user", {
      method: "PUT",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "Authorization": "Bearer " + token
      },
      body: body
    })
      .then(response => {
        if (!response.ok) throw new Error("Erro ao atualizar");
        return response.text();
      })
      .then(msg => {
        document.getElementById("modal-message").innerText = msg + " Você será deslogado, entre com seus novos dados!";
        document.getElementById("modal").classList.remove("hidden");
      })
      .catch(err => alert("Erro: " + err.message));
  }
}

function togglePreferences() {
  const checkboxes = document.querySelectorAll("#preferences-options input[type='checkbox']");
  const button = document.querySelector(".preferences .edit-btn");
  const isDisabled = checkboxes[0].disabled;

  checkboxes.forEach(cb => cb.disabled = !isDisabled);
  button.textContent = isDisabled ? "Salvar" : "Alterar";

  if (!isDisabled) {
    const selected = Array.from(checkboxes)
      .filter(cb => cb.checked)
      .map(cb => cb.id);
    console.log("Preferências salvas:", selected);
  }
}

document.getElementById("confirm-delete").addEventListener("click", () => {
  fetch("/user", {
    method: "DELETE",
    headers: {
      "Authorization": "Bearer " + token
    }
  })
    .then(response => {
      if (!response.ok) throw new Error("Erro ao excluir conta.");
      return response.text();
    })
    .then(msg => {
      document.getElementById("modal-message").innerText = msg + " Você será deslogado.";
      document.getElementById("modal").classList.remove("hidden");
    })
    .catch(err => alert("Erro: " + err.message));

  document.getElementById("delete-modal").classList.add("hidden");
});

// Desloga usuário a partir do botão "Sair"
btnLogout.addEventListener("click", () => {
  logout();
});

document.getElementById("modal-button").addEventListener("click", () => {
  document.getElementById("modal").classList.add("hidden");
  logout();
});

document.getElementById("cancel-delete").addEventListener("click", () => {
  document.getElementById("delete-modal").classList.add("hidden");
});
