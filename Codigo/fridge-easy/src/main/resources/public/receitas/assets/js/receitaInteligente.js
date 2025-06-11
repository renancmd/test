const API_URL = '/api/receita-ingrediente';

async function listarReceitaIngredientes() {
    const res = await fetch(API_URL);
    const dados = await res.json();
    console.log(dados);
}

async function buscarReceitaIngrediente(id) {
    const res = await fetch(`${API_URL}/${id}`);
    const dados = await res.json();
    console.log(dados);
}

async function criarReceitaIngrediente() {
    const novo = {
        id_receita: 1,
        id_ingrediente: 3,
        quantidade: 100,
        medida: 'GRAMAS'
    };

    const res = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(novo)
    });

    const dados = await res.json();
    console.log('Criado:', dados);
}

async function atualizarReceitaIngrediente(id) {
    const atualizado = {
        id_receita: 1,
        id_ingrediente: 3,
        quantidade: 150,
        medida: 'ML'
    };

    const res = await fetch(`${API_URL}/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(atualizado)
    });

    const dados = await res.json();
    console.log('Atualizado:', dados);
}


async function deletarReceitaIngrediente(id) {
    const res = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    console.log('Deletado:', id);
}
