const form = document.getElementById('solicitacaoForm');
const categoriaInput = document.getElementById('categoria');
const categoryCards = document.querySelectorAll('.category-card');
const prioridade = document.getElementById('prioridade');
const localizacao = document.getElementById('localizacao');
const anonimo = document.getElementById('anonimo');
const descricao = document.getElementById('descricao');
const alertAnonimo = document.getElementById('alertAnonimo');
const charCounter = document.getElementById('charCounter');
const btnSubmit = document.getElementById('btnSubmit');
const dadosPessoaisArea = document.getElementById('dadosPessoaisArea');
const solNome = document.getElementById('solNome');
const solEmail = document.getElementById('solEmail');
const slaInfo = document.getElementById('slaInfo');
const slaTexto = document.getElementById('slaTexto');

const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

if (isLoggedIn) {
  dadosPessoaisArea.classList.add('d-none');
}

function selecionarCategoria(card) {
  categoryCards.forEach(c => {
    c.classList.remove('selected');
    c.setAttribute('aria-checked', 'false');
  });
  card.classList.add('selected');
  card.setAttribute('aria-checked', 'true');
  categoriaInput.value = card.getAttribute('data-value');
  validateForm();
}

categoryCards.forEach(card => {
  card.addEventListener('click', () => selecionarCategoria(card));
  
  card.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      selecionarCategoria(card);
    }
  });
});

function calcularSlaEstimadoFront(prio) {
  if (prio.includes('Alta')) return '5 dias';
  if (prio.includes('Média') || prio.includes('Media')) return '15 dias';
  return '30 dias';
}

function validateForm() {
  const isAnonimo = anonimo.checked;
  const textLength = descricao.value.trim().length;
  const hasCategoria = categoriaInput.value !== '';
  const hasPrioridade = prioridade.value !== '';
  const hasLocalizacao = localizacao.value.trim() !== '';
  
  let hasPessoal = true;
  
  if (isAnonimo || isLoggedIn) {
    solNome.disabled = true;
    solEmail.disabled = true;
  } else {
    solNome.disabled = false;
    solEmail.disabled = false;
    hasPessoal = solNome.value.trim() !== '' && solEmail.value.trim() !== '';
  }

  if (hasPrioridade) {
    slaTexto.innerText = calcularSlaEstimadoFront(prioridade.value);
    slaInfo.classList.remove('d-none');
  } else {
    slaInfo.classList.add('d-none');
  }

  let isValid = false;

  if (isAnonimo) {
    alertAnonimo.classList.add('visible');
    charCounter.textContent = `${textLength} / 20 caracteres necessários`;
    charCounter.style.color = textLength < 20 ? 'var(--priority-alta)' : 'var(--text-light)';
    isValid = hasCategoria && hasPrioridade && hasLocalizacao && hasPessoal && textLength >= 20;
    descricao.setAttribute('aria-invalid', textLength < 20 ? 'true' : 'false');
  } else {
    alertAnonimo.classList.remove('visible');
    charCounter.textContent = `${textLength} caracteres`;
    charCounter.style.color = 'var(--text-light)';
    isValid = hasCategoria && hasPrioridade && hasLocalizacao && hasPessoal && textLength > 0;
    descricao.setAttribute('aria-invalid', textLength === 0 ? 'true' : 'false');
  }

  btnSubmit.disabled = !isValid;
  btnSubmit.setAttribute('aria-disabled', !isValid ? 'true' : 'false');
}

prioridade.addEventListener('change', validateForm);
localizacao.addEventListener('input', validateForm);
anonimo.addEventListener('change', validateForm);
descricao.addEventListener('input', validateForm);
solNome.addEventListener('input', validateForm);
solEmail.addEventListener('input', validateForm);

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  if (!btnSubmit.disabled) {
    btnSubmit.innerText = 'Enviando...';
    btnSubmit.disabled = true;

    const nomeEnvio = isLoggedIn ? sessionStorage.getItem('usuarioNome') : solNome.value;
    const emailEnvio = isLoggedIn ? sessionStorage.getItem('usuarioEmail') : solEmail.value;

    const novoProtocolo = {
      categoria: categoriaInput.value,
      prioridade: prioridade.options[prioridade.selectedIndex].text,
      descricao: descricao.value,
      localizacao: localizacao.value,
      anonimo: anonimo.checked,
      nomeCidadao: nomeEnvio,
      emailCidadao: emailEnvio
    };

    const respostaSalva = await adicionarProtocolo(novoProtocolo);
    
    if (respostaSalva) {
      alert(`Sucesso! O seu protocolo é: ${respostaSalva.codigo}`);
      if (isLoggedIn) {
        window.location.href = 'cidadao.html';
      } else {
        window.location.href = 'index.html';
      }
    } else {
      alert('Erro ao conectar ao servidor. Verifique se o Spring Boot está ativo.');
      btnSubmit.innerText = 'Gerar Protocolo e Enviar';
      btnSubmit.disabled = false;
    }
  }
});