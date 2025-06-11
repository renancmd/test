const btnAvaliar = document.getElementById("avaliar");

btnAvaliar.addEventListener("click", e => {
    e.preventDefault();

    fetch("/avaliacoes", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            nota: 4.5,
            comentario: "Muito boa!",
            id_usuario: 1,
            id_receita: 3
        })
    })
    .then(response => {
        if (response.ok) {
            console.log("Avaliação enviada com sucesso");
        } else {
            throw new Error("Erro ao enviar avaliação");
        }
    })
    .catch(error => console.error(error));
});