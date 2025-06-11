const btnMinhasReceitas = document.getElementById("btnMinhasReceitas");
const btnReceitas = document.getElementById("btnReceitas");
const btnAddIngrediente = document.getElementById("btnAddIngrediente");
const modal = document.getElementById("addIngredientModal");
const closeModal = modal.querySelector(".close-modal");
const addIngredientForm = document.getElementById("addIngredientForm");

// Navegação
btnReceitas.addEventListener("click", e => window.location.href = "/receitas/receitas.html");
btnMinhasReceitas.addEventListener("click", e => window.location.href = "/minhas-receitas/minhas-receitas.html");

const openModal = () => {
    modal.classList.add("active");
    document.body.style.overflow = "hidden";
};

const closeModalFunction = () => {
    modal.classList.remove("active");
    document.body.style.overflow = ""; // Restore scrolling
    addIngredientForm.reset(); // Reset form
};

// Eventos do modal
btnAddIngrediente.addEventListener("click", openModal);
closeModal.addEventListener("click", closeModalFunction);

// Fehcar modal
modal.addEventListener("click", (e) => {
    if (e.target === modal) {
        closeModalFunction();
    }
});

// Modal de edição (reutiliza o de cadastro)
let isEditMode = false;
let editingIngredientName = null;

function openEditModal(ingredient) {
    isEditMode = true;
    editingIngredientName = ingredient.name;
    modal.classList.add("active");
    document.body.style.overflow = "hidden";
    // Preenche o formulário com os dados do ingrediente
    addIngredientForm.name.value = ingredient.name;
    addIngredientForm.category.value = ingredient.category;
    const [calories, weight] = ingredient.nutritional_value.split(',').map(str => str.trim());
    addIngredientForm.calories.value = calories;
    addIngredientForm.weight.value = weight;
}

// Atualiza o submit do formulário para diferenciar cadastro e edição
addIngredientForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const formData = new FormData(addIngredientForm);
    const ingredientData = {
        name: formData.get("name"),
        category: formData.get("category"),
        calories: formData.get("calories"),
        weight: formData.get("weight")
    };
    if (isEditMode) {
        // Atualizar ingrediente
        fetch("/update-ingredient", {
            method: "PUT",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({
                oldName: editingIngredientName,
                name: ingredientData.name,
                category: ingredientData.category,
                calories: ingredientData.calories,
                weight: ingredientData.weight
            })
        })
        .then(res => {
            if (res.ok) return res.text();
            throw new Error("Falha ao atualizar ingrediente");
        })
        .then(() => reloadIngredients())
        .catch(err => alert(err.message));
    } else {
        // Cadastrar novo ingrediente
        fetch("/create-ingredient", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({
                name: ingredientData.name,
                category: ingredientData.category,
                calories: ingredientData.calories,
                weight: ingredientData.weight
            })
        })
        .then(res => {
            if (res.ok) return res.text();
            throw new Error("Falha ao cadastrar ingrediente");
        })
        .then(() => reloadIngredients())
        .catch(err => alert(err.message));
    }
    closeModalFunction();
    isEditMode = false;
    editingIngredientName = null;
});

const reloadIngredients = () => {
    fetch("/ingredients")
        .then(res => res.json())
        .then(data => {
            const ingredientsGrid = document.getElementById('ingredientsGrid');
            ingredientsGrid.innerHTML = '';
            data.forEach(ingredient => {
                const card = createIngredientCard(ingredient);
                ingredientsGrid.appendChild(card);
            });
        });
};

// Função para criar o card de ingrediente com botões de editar e deletar
function createIngredientCard(ingredient) {
    const [calories, weight] = ingredient.nutritional_value.split(',').map(str => str.trim());
    const card = document.createElement('div');
    card.className = 'ingredient-card';
    card.innerHTML = `
        <div class="ingredient-card-content">
            <div class="card-header">
                <h3 class="ingredient-title">${ingredient.name}</h3>
                <div class="card-actions">
                    <button class="action-button edit" title="Editar"><i class="ph ph-pencil"></i></button>
                    <button class="action-button delete" title="Excluir"><i class="ph ph-trash"></i></button>
                </div>
            </div>
            <div class="ingredient-info">
                <p class="category">Categoria: ${ingredient.category}</p>
                <div class="nutritional-info">
                    <p>Valor Calórico: ${calories}</p>
                    <p>Peso: ${weight}</p>
                </div>
            </div>
        </div>
    `;
    // Botão editar
    card.querySelector('.edit').addEventListener('click', () => openEditModal(ingredient));
    // Botão deletar
    card.querySelector('.delete').addEventListener('click', () => {
        if (confirm(`Deseja realmente excluir o ingrediente "${ingredient.name}"?`)) {
            fetch(`/delete-ingredient?name=${encodeURIComponent(ingredient.name)}`, {
                method: "DELETE"
            })
            .then(res => {
                if (res.ok) return res.text();
                throw new Error("Falha ao deletar ingrediente");
            })
            .then(() => reloadIngredients())
            .catch(err => alert(err.message));
        }
    });
    return card;
}

// Atualiza a lista ao abrir a página
reloadIngredients();

// Ao abrir modal de adicionar, garante modo cadastro
btnAddIngrediente.addEventListener("click", () => {
    isEditMode = false;
    editingIngredientName = null;
    addIngredientForm.reset();
});
