document.addEventListener('DOMContentLoaded', async () => {
  const navText = document.querySelector('.nav-text');
  const nomeLogado = sessionStorage.getItem('usuarioNome');
  
  if (navText && nomeLogado) {
    navText.innerText = `Olá, ${nomeLogado.split(' ')[0]}`;
  }

  await atualizarTabelaCidadao();
});

async function atualizarTabelaCidadao() {
  const tbody = document.querySelector('.table-wrapper tbody');
  const protocolos = await getProtocolos();
  renderizarTabela(tbody, protocolos, 'CIDADAO');
  
  document.querySelectorAll('.btn-detalhes').forEach(botao => {
    botao.addEventListener('click', async (e) => {
      const linha = e.target.closest('tr');
      const codigo = linha.querySelector('td:first-child strong').innerText;
      await exibirModalDetalhes(codigo);
    });
  });
}

const btnSair = document.getElementById('btnSair');
const btnNovoProtocolo = document.getElementById('btnNovoProtocolo');
const btnBuscar = document.getElementById('btnBuscar');
const inputBusca = document.getElementById('inputBusca');
const modalBusca = document.getElementById('modalBusca');
const btnFecharBusca = document.getElementById('btnFecharBusca');
const resultadoBuscaBody = document.getElementById('resultadoBuscaBody');

async function exibirModalDetalhes(termoBuscado) {
  const termo = termoBuscado.trim().toUpperCase();
  if (!termo) return;

  const encontrado = await buscarProtocolo(termo);
  resultadoBuscaBody.innerHTML = montarHtmlDetalhes(encontrado);
  modalBusca.classList.add('active');
}

if (btnSair) {
  btnSair.addEventListener('click', () => {
    sessionStorage.clear();
    window.location.href = 'index.html';
  });
}

if (btnNovoProtocolo) {
  btnNovoProtocolo.addEventListener('click', () => {
    window.location.href = 'solicitacao.html';
  });
}

if (btnBuscar) {
  btnBuscar.addEventListener('click', async () => {
    await exibirModalDetalhes(inputBusca.value);
  });
}

if (inputBusca) {
  inputBusca.addEventListener('keypress', (event) => {
    if (event.key === 'Enter') btnBuscar.click();
  });
}

if (btnFecharBusca) {
  btnFecharBusca.addEventListener('click', () => modalBusca.classList.remove('active'));
}

window.addEventListener('click', (e) => {
  if (e.target === modalBusca) modalBusca.classList.remove('active');
});