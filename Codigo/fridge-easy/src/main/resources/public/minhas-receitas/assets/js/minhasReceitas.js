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
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Nível de dificuldade:</label>
          <label><input type="radio" name="nivelDificuldade" value="Fácil" required /> Fácil</label>
          <label><input type="radio" name="nivelDificuldade" value="Médio" /> Médio</label>
          <label><input type="radio" name="nivelDificuldade" value="Difícil" /> Difícil</label>
        </div>
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Filtros:</label>
          <label><input type="checkbox" name="filtro" value="Porções" /> Porções</label>
          <label><input type="checkbox" name="filtro" value="Sobremesas" /> Sobremesas</label>
          <label><input type="checkbox" name="filtro" value="Dietas" /> Dietas</label>
          <label><input type="checkbox" name="filtro" value="Refeições" /> Refeições</label>
          <label><input type="checkbox" name="filtro" value="Vegetariano" /> Vegetariano</label>
          <label><input type="checkbox" name="filtro" value="Vegano" /> Vegano</label>
        </div>
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Ingredientes:</label>
          <input type="text" id="ingredient-search" placeholder="Buscar ingrediente..." autocomplete="off" />
          <div id="ingredient-suggestions" style="background:#fff;border:1px solid #bbb;max-height:120px;overflow-y:auto;display:none;position:relative;z-index:10;"></div>
          <div id="selected-ingredients" style="margin-top:0.5rem;display:flex;flex-direction:column;gap:0.5rem;"></div>
        </div>
        <label><input name="favorito" type="checkbox" /> Favorito</label>
        <button type="submit">Salvar</button>
        <button type="button" id="close-modal">Cancelar</button>
      </form>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById('close-modal').onclick = () => modal.remove();
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });

  // Ingredientes autocomplete e seleção múltipla
  let allIngredients = [];
  let selectedIngredients = [];
  const searchInput = modal.querySelector('#ingredient-search');
  const suggestionsBox = modal.querySelector('#ingredient-suggestions');
  const selectedBox = modal.querySelector('#selected-ingredients');

  fetch('/ingredients')
    .then(res => res.json())
    .then(data => { allIngredients = data; });

  searchInput.addEventListener('input', function () {
    const value = this.value.trim().toLowerCase();
    suggestionsBox.innerHTML = '';
    if (!value) { suggestionsBox.style.display = 'none'; return; }
    const filtered = allIngredients.filter(ing => ing.name.toLowerCase().includes(value) && !selectedIngredients.some(sel => sel.name === ing.name));
    if (filtered.length === 0) { suggestionsBox.style.display = 'none'; return; }
    filtered.forEach(ing => {
      const div = document.createElement('div');
      div.textContent = ing.name;
      div.style.cursor = 'pointer';
      div.style.padding = '0.3rem 0.7rem';
      div.addEventListener('click', () => {
        selectedIngredients.push({ ...ing, quantidade: '', medida: '' });
        renderSelectedIngredients();
        suggestionsBox.style.display = 'none';
        searchInput.value = '';
      });
      suggestionsBox.appendChild(div);
    });
    suggestionsBox.style.display = 'block';
  });

  function renderSelectedIngredients() {
    selectedBox.innerHTML = '';
    selectedIngredients.forEach((ing, idx) => {
      const row = document.createElement('div');
      row.style.display = 'flex';
      row.style.alignItems = 'center';
      row.style.gap = '0.5rem';
      row.style.marginBottom = '0.2rem';
      row.style.flexWrap = 'wrap';
      const tag = document.createElement('span');
      tag.textContent = ing.name;
      tag.style.background = '#889a85';
      tag.style.color = '#fff';
      tag.style.padding = '0.2rem 0.7rem';
      tag.style.borderRadius = '12px';
      tag.style.display = 'inline-flex';
      tag.style.alignItems = 'center';
      tag.style.gap = '0.3rem';
      tag.style.fontSize = '0.95em';
      const removeBtn = document.createElement('button');
      removeBtn.textContent = 'x';
      removeBtn.style.background = 'none';
      removeBtn.style.border = 'none';
      removeBtn.style.color = '#fff';
      removeBtn.style.cursor = 'pointer';
      removeBtn.onclick = () => {
        selectedIngredients.splice(idx, 1);
        renderSelectedIngredients();
      };
      tag.appendChild(removeBtn);
      // Campo quantidade
      const qtdInput = document.createElement('input');
      qtdInput.type = 'number';
      qtdInput.min = '0';
      qtdInput.placeholder = 'Qtd.';
      qtdInput.style.width = '70px';
      qtdInput.style.padding = '0.2rem 0.4rem';
      qtdInput.value = ing.quantidade;
      qtdInput.oninput = (e) => { selectedIngredients[idx].quantidade = e.target.value; };
      // Campo medida
      const medidaInput = document.createElement('input');
      medidaInput.type = 'text';
      medidaInput.placeholder = 'Medida';
      medidaInput.style.width = '70px';
      medidaInput.style.padding = '0.2rem 0.4rem';
      medidaInput.value = ing.medida;
      medidaInput.oninput = (e) => { selectedIngredients[idx].medida = e.target.value; };
      row.appendChild(tag);
      row.appendChild(qtdInput);
      row.appendChild(medidaInput);
      selectedBox.appendChild(row);
    });
  }

  document.getElementById('create-recipe-form').onsubmit = async function (e) {
    e.preventDefault();
    const data = new FormData(this);
    // Serializa filtros múltiplos
    const filtros = Array.from(this.querySelectorAll('input[name="filtro"]:checked')).map(cb => cb.value);
    data.delete('filtro');
    filtros.forEach(f => data.append('filtro', f));
    // Serializa ingredientes para receita
    data.append('ingredientes', JSON.stringify(selectedIngredients.map(i => i.name)));
    // Cria receita
    const res = await fetch('/receitas', {
      method: 'POST',
      body: new URLSearchParams([...data]),
    });
    if (!res.ok) { alert('Erro ao criar receita'); return; }
    // Tenta obter o id da receita criada
    let receitaId;
    try {
      const receitas = await fetch('/receitas').then(r => r.json());
      const nome = this.querySelector('input[name="nome"]').value;
      receitaId = receitas.reverse().find(r => r.nome === nome)?.id;
    } catch { receitaId = null; }
    if (!receitaId) { alert('Receita criada, mas não foi possível vincular ingredientes.'); modal.remove(); location.reload(); return; }
    // Cria receita_ingrediente para cada ingrediente selecionado
    for (const ing of selectedIngredients) {
      const ingredienteObj = allIngredients.find(i => i.name === ing.name);
      if (!ingredienteObj) continue;
      await fetch('/receita-ingrediente', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id_receita: receitaId,
          id_ingrediente: ingredienteObj.id || ingredienteObj.id_ingrediente || ingredienteObj.idIngrediente || ingredienteObj.nome || ingredienteObj.name, // tente id, se não nome
          quantidade: ing.quantidade || 1,
          medida: ing.medida || ''
        })
      });
    }
    alert('Receita criada com sucesso!');
    modal.remove();
    location.reload();
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
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Nível de dificuldade:</label>
          <label><input type="radio" name="nivelDificuldade" value="Fácil" required ${receita.nivelDificuldade === 'Fácil' ? 'checked' : ''} /> Fácil</label>
          <label><input type="radio" name="nivelDificuldade" value="Médio" ${receita.nivelDificuldade === 'Médio' ? 'checked' : ''} /> Médio</label>
          <label><input type="radio" name="nivelDificuldade" value="Difícil" ${receita.nivelDificuldade === 'Difícil' ? 'checked' : ''} /> Difícil</label>
        </div>
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Filtros:</label>
          <label><input type="checkbox" name="filtro" value="Porções" ${receita.filtro && receita.filtro.includes('Porções') ? 'checked' : ''} /> Porções</label>
          <label><input type="checkbox" name="filtro" value="Sobremesas" ${receita.filtro && receita.filtro.includes('Sobremesas') ? 'checked' : ''} /> Sobremesas</label>
          <label><input type="checkbox" name="filtro" value="Dietas" ${receita.filtro && receita.filtro.includes('Dietas') ? 'checked' : ''} /> Dietas</label>
          <label><input type="checkbox" name="filtro" value="Refeições" ${receita.filtro && receita.filtro.includes('Refeições') ? 'checked' : ''} /> Refeições</label>
          <label><input type="checkbox" name="filtro" value="Vegetariano" ${receita.filtro && receita.filtro.includes('Vegetariano') ? 'checked' : ''} /> Vegetariano</label>
          <label><input type="checkbox" name="filtro" value="Vegano" ${receita.filtro && receita.filtro.includes('Vegano') ? 'checked' : ''} /> Vegano</label>
        </div>
        <div style="margin-bottom: 1rem;">
          <label style="display:block; margin-bottom:0.3rem;">Ingredientes:</label>
          <input type="text" id="ingredient-search" placeholder="Buscar ingrediente..." autocomplete="off" />
          <div id="ingredient-suggestions" style="background:#fff;border:1px solid #bbb;max-height:120px;overflow-y:auto;display:none;position:relative;z-index:10;"></div>
          <div id="selected-ingredients" style="margin-top:0.5rem;display:flex;flex-direction:column;gap:0.5rem;"></div>
        </div>
        <label><input name="favorito" type="checkbox" ${receita.favorito ? 'checked' : ''}/> Favorito</label>
        <button type="submit">Salvar</button>
        <button type="button" id="close-modal">Cancelar</button>
      </form>
    </div>
  `;
  document.body.appendChild(modal);

  document.getElementById('close-modal').onclick = () => modal.remove();
  modal.addEventListener('click', e => { if (e.target === modal) modal.remove(); });

  // Ingredientes autocomplete e seleção múltipla
  let allIngredients = [];
  let selectedIngredients = [];
  const searchInput = modal.querySelector('#ingredient-search');
  const suggestionsBox = modal.querySelector('#ingredient-suggestions');
  const selectedBox = modal.querySelector('#selected-ingredients');

  fetch('/ingredients')
    .then(res => res.json())
    .then(data => { allIngredients = data; });

  searchInput.addEventListener('input', function () {
    const value = this.value.trim().toLowerCase();
    suggestionsBox.innerHTML = '';
    if (!value) { suggestionsBox.style.display = 'none'; return; }
    const filtered = allIngredients.filter(ing => ing.name.toLowerCase().includes(value) && !selectedIngredients.some(sel => sel.name === ing.name));
    if (filtered.length === 0) { suggestionsBox.style.display = 'none'; return; }
    filtered.forEach(ing => {
      const div = document.createElement('div');
      div.textContent = ing.name;
      div.style.cursor = 'pointer';
      div.style.padding = '0.3rem 0.7rem';
      div.addEventListener('click', () => {
        selectedIngredients.push(ing);
        renderSelectedIngredients();
        suggestionsBox.style.display = 'none';
        searchInput.value = '';
      });
      suggestionsBox.appendChild(div);
    });
    suggestionsBox.style.display = 'block';
  });

  function renderSelectedIngredients() {
    selectedBox.innerHTML = '';
    selectedIngredients.forEach((ing, idx) => {
      const tag = document.createElement('span');
      tag.textContent = ing.name;
      tag.style.background = '#889a85';
      tag.style.color = '#fff';
      tag.style.padding = '0.2rem 0.7rem';
      tag.style.borderRadius = '12px';
      tag.style.display = 'inline-flex';
      tag.style.alignItems = 'center';
      tag.style.gap = '0.3rem';
      tag.style.fontSize = '0.95em';
      const removeBtn = document.createElement('button');
      removeBtn.textContent = 'x';
      removeBtn.style.background = 'none';
      removeBtn.style.border = 'none';
      removeBtn.style.color = '#fff';
      removeBtn.style.cursor = 'pointer';
      removeBtn.onclick = () => {
        selectedIngredients.splice(idx, 1);
        renderSelectedIngredients();
      };
      tag.appendChild(removeBtn);
      // Campo quantidade
      const qtdInput = document.createElement('input');
      qtdInput.type = 'number';
      qtdInput.min = '0';
      qtdInput.placeholder = 'Qtd.';
      qtdInput.style.width = '70px';
      qtdInput.style.padding = '0.2rem 0.4rem';
      qtdInput.value = ing.quantidade;
      qtdInput.oninput = (e) => { selectedIngredients[idx].quantidade = e.target.value; };
      // Campo medida
      const medidaInput = document.createElement('input');
      medidaInput.type = 'text';
      medidaInput.placeholder = 'Medida';
      medidaInput.style.width = '70px';
      medidaInput.style.padding = '0.2rem 0.4rem';
      medidaInput.value = ing.medida;
      medidaInput.oninput = (e) => { selectedIngredients[idx].medida = e.target.value; };
      tag.appendChild(qtdInput);
      tag.appendChild(medidaInput);
      selectedBox.appendChild(tag);
    });
  }

  // Pré-seleciona ingredientes da receita
  const ingredientesReceita = receita.ingredientes ? JSON.parse(receita.ingredientes) : [];
  selectedIngredients = allIngredients.filter(ing => ingredientesReceita.includes(ing.name));
  renderSelectedIngredients();

  document.getElementById('edit-recipe-form').onsubmit = function (e) {
    e.preventDefault();
    const data = new FormData(this);
    // Serializa filtros múltiplos
    const filtros = Array.from(this.querySelectorAll('input[name="filtro"]:checked')).map(cb => cb.value);
    data.delete('filtro');
    filtros.forEach(f => data.append('filtro', f));
    // Serializa ingredientes
    data.append('ingredientes', JSON.stringify(selectedIngredients.map(i => i.name)));
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
grid.addEventListener('click', function (e) {
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
  btnCreate.addEventListener('click', function (e) {
    // Evita conflito com o botão de ingredientes
    if (btnCreate.id !== 'btnIngredients') {
      e.preventDefault();
      openCreateRecipeModal();
    }
  });
}