const btnIngredients = document.getElementById("btnIngredients");

btnIngredients.addEventListener("click", e => window.location.href = "/ingredientes/ingredientes.html");

// Função para criar um card de receita
function createRecipeCard(receita) {
  const card = document.createElement('div');
  card.className = 'recipe-card';
  card.innerHTML = `
    <img src="${receita.imagem || '../pagina-principal/assets/imgs/torta-holandesa.jpg'}" alt="${receita.nome}" />
    <div class="recipe-card-content">
      <h3 class="recipe-title">${receita.nome}</h3>
      <div class="recipe-actions">
        <button class="action-button edit" title="Editar" data-id="${receita.id}">
          <i class="ph ph-pencil"></i>
        </button>
        <button class="action-button delete" title="Excluir" data-id="${receita.id}">
          <i class="ph ph-trash"></i>
        </button>
      </div>
    </div>
  `;
  return card;
}

// Carregar receitas ao abrir a página
window.addEventListener('DOMContentLoaded', () => {
  fetch('/receitas')
    .then(res => res.json())
    .then(receitas => {
      const grid = document.querySelector('.recipe-grid');
      grid.innerHTML = '';
      receitas.forEach(receita => {
        grid.appendChild(createRecipeCard(receita));
      });
    });
});

// Função para abrir modal de criação de receita
function openCreateRecipeModal() {
  const modal = document.createElement('div');
  modal.className = 'modal-bg';
  modal.innerHTML = `
    <div class="modal">
      <h2>Criar Receita</h2>
      <form id="create-recipe-form">
        <input name="nome" placeholder="Nome da receita" required />
        <textarea name="descricao" placeholder="Descrição"></textarea>
        <input name="imagem" placeholder="URL da imagem" />
        <input name="tempoPreparo" type="number" placeholder="Tempo de preparo (min)" required />
        <input name="nivelDificuldade" placeholder="Nível de dificuldade" required />
        <input name="filtro" placeholder="Filtro" />
        <label><input name="favorito" type="checkbox" /> Favorito</label>
        <button type="submit">Salvar</button>
        <button type="button" id="close-modal">Cancelar</button>
      </form>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById('close-modal').onclick = () => modal.remove();
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });

  document.getElementById('create-recipe-form').onsubmit = function(e) {
    e.preventDefault();
    const data = new FormData(this);
    fetch('/receitas', {
      method: 'POST',
      body: new URLSearchParams([...data]),
    })
      .then(res => res.text())
      .then(msg => {
        alert(msg);
        modal.remove();
        location.reload();
      });
  };
}

// Função para abrir modal de edição de receita
function openEditRecipeModal(receita) {
  const modal = document.createElement('div');
  modal.className = 'modal-bg';
  modal.innerHTML = `
    <div class="modal">
      <h2>Editar Receita</h2>
      <form id="edit-recipe-form">
        <input name="nome" placeholder="Nome da receita" required value="${receita.nome}" />
        <textarea name="descricao" placeholder="Descrição">${receita.descricao || ''}</textarea>
        <input name="imagem" placeholder="URL da imagem" value="${receita.imagem || ''}" />
        <input name="tempoPreparo" type="number" placeholder="Tempo de preparo (min)" required value="${receita.tempoPreparo}" />
        <input name="nivelDificuldade" placeholder="Nível de dificuldade" required value="${receita.nivelDificuldade || ''}" />
        <input name="filtro" placeholder="Filtro" value="${receita.filtro || ''}" />
        <label><input name="favorito" type="checkbox" ${receita.favorito ? 'checked' : ''}/> Favorito</label>
        <button type="submit">Salvar</button>
        <button type="button" id="close-modal">Cancelar</button>
      </form>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById('close-modal').onclick = () => modal.remove();
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });

  document.getElementById('edit-recipe-form').onsubmit = function(e) {
    e.preventDefault();
    const data = new FormData(this);
    fetch(`/receitas/${receita.id}`, {
      method: 'PUT',
      body: new URLSearchParams([...data]),
    })
      .then(res => res.text())
      .then(msg => {
        alert(msg);
        modal.remove();
        location.reload();
      });
  };
}

// Delegação de eventos para editar/excluir
const grid = document.querySelector('.recipe-grid');
grid.addEventListener('click', function(e) {
  if (e.target.closest('.delete')) {
    const id = e.target.closest('.delete').dataset.id;
    if (confirm('Deseja realmente excluir esta receita?')) {
      fetch(`/receitas/${id}`, { method: 'DELETE' })
        .then(res => res.text())
        .then(msg => {
          alert(msg);
          location.reload();
        });
    }
  } else if (e.target.closest('.edit')) {
    const id = e.target.closest('.edit').dataset.id;
    // Buscar dados da receita para edição
    fetch(`/receitas`)
      .then(res => res.json())
      .then(receitas => {
        const receita = receitas.find(r => r.id == id);
        if (receita) openEditRecipeModal(receita);
      });
  }
});

// Botão de criar receita
const btnCreate = document.querySelector('.button-container .primary-button');
if (btnCreate) {
  btnCreate.addEventListener('click', function(e) {
    // Evita conflito com o botão de ingredientes
    if (btnCreate.id !== 'btnIngredients') {
      e.preventDefault();
      openCreateRecipeModal();
    }
  });
}