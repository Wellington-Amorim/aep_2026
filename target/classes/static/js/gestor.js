const modal = document.getElementById('modalDespacho');
const btnFecharModal = document.getElementById('btnFecharModal');
const formDespacho = document.getElementById('formDespacho');
const novoStatus = document.getElementById('novoStatus');
const justificativa = document.getElementById('justificativa');
const btnSalvarAcao = document.getElementById('btnSalvarAcao');
const detalhesProtocoloModal = document.getElementById('detalhesProtocoloModal');

const filtroStatus = document.getElementById('filtroStatus');
const filtroPrioridade = document.getElementById('filtroPrioridade');
const btnAplicarFiltros = document.getElementById('btnAplicarFiltros');
const btnLimparFiltros = document.getElementById('btnLimparFiltros');

let linhaSelecionada = null;

document.addEventListener('DOMContentLoaded', async () => {
  const protocolos = await getProtocolos();
  atualizarTabelaGestor(protocolos);
});

function atualizarTabelaGestor(dados) {
  const tbody = document.querySelector('.table-wrapper tbody');
  renderizarTabela(tbody, dados, 'GESTOR');
  document.querySelectorAll('.btn-action').forEach(botao => {
    botao.addEventListener('click', abrirModal);
  });
}

async function aplicarFiltros() {
  const statusBuscado = filtroStatus.value;
  const prioBuscada = filtroPrioridade.value;
  
  const protocolos = await getProtocolos();
  
  let dadosFiltrados = protocolos.filter(p => {
    const statusStr = normalizar(p.status);
    const prioStr = p.prioridade ? normalizar(p.prioridade) : '';
    
    const matchStatus = statusBuscado === "" || statusStr === statusBuscado;
    const matchPrio = prioBuscada === "" || prioStr === prioBuscada;
    
    return matchStatus && matchPrio;
  });
  
  atualizarTabelaGestor(dadosFiltrados);
}

btnAplicarFiltros.addEventListener('click', aplicarFiltros);

btnLimparFiltros.addEventListener('click', async () => {
  filtroStatus.value = "";
  filtroPrioridade.value = "";
  const protocolos = await getProtocolos();
  atualizarTabelaGestor(protocolos);
});

async function abrirModal(e) {
  linhaSelecionada = e.target.closest('tr');
  const codigoProtocolo = linhaSelecionada.querySelector('td:first-child strong').innerText;
  const protocoloCompleto = await buscarProtocolo(codigoProtocolo);
  
  document.getElementById('tituloModalDespacho').innerText = `Análise: ${codigoProtocolo}`;
  detalhesProtocoloModal.innerHTML = montarHtmlDetalhes(protocoloCompleto);

  modal.classList.add('active');
  novoStatus.value = '';
  justificativa.value = '';
  validarFormulario();
}

function fecharModal() {
  modal.classList.remove('active');
  linhaSelecionada = null;
}

function validarFormulario() {
  const statusPreenchido = novoStatus.value !== '';
  const justificativaPreenchida = justificativa.value.trim().length >= 10;
  btnSalvarAcao.disabled = !(statusPreenchido && justificativaPreenchida);
  btnSalvarAcao.setAttribute('aria-disabled', btnSalvarAcao.disabled ? 'true' : 'false');
}

btnFecharModal.addEventListener('click', fecharModal);

modal.addEventListener('click', (e) => {
  if (e.target === modal) fecharModal();
});

novoStatus.addEventListener('change', validarFormulario);
justificativa.addEventListener('input', validarFormulario);

formDespacho.addEventListener('submit', async (e) => {
  e.preventDefault();
  if (!btnSalvarAcao.disabled && linhaSelecionada) {
    btnSalvarAcao.innerText = 'A guardar...';
    btnSalvarAcao.disabled = true;

    const codigoProtocolo = linhaSelecionada.querySelector('td:first-child strong').innerText;
    const textoSelecionado = novoStatus.options[novoStatus.selectedIndex].text;
    
    await atualizarStatusProtocolo(codigoProtocolo, textoSelecionado);
    await aplicarFiltros();
    
    btnSalvarAcao.innerText = 'Salvar Alteração';
    fecharModal();
  }
});