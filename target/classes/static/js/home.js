const btnBuscar = document.getElementById('btnBuscar');
const inputBusca = document.getElementById('inputBusca');
const btnEntrar = document.getElementById('btnEntrar');
const btnAddConta = document.getElementById('btnAddConta');
const btnNovoProtocolo = document.getElementById('btnNovoProtocolo');
const modalLogin = document.getElementById('modalLogin');
const modalCadastro = document.getElementById('modalCadastro');
const modalBusca = document.getElementById('modalBusca');
const btnFecharLogin = document.getElementById('btnFecharLogin');
const btnFecharCadastro = document.getElementById('btnFecharCadastro');
const btnFecharBusca = document.getElementById('btnFecharBusca');
const formLogin = document.getElementById('formLogin');
const formCadastro = document.getElementById('formCadastro');
const linkGestor = document.getElementById('linkGestor');
const linkCriarConta = document.getElementById('linkCriarConta');
const resultadoBuscaBody = document.getElementById('resultadoBuscaBody');

document.addEventListener('DOMContentLoaded', async () => {
  const tbody = document.querySelector('.table-wrapper tbody');
  const protocolos = await getProtocolos();
  renderizarTabela(tbody, protocolos, 'HOME');

  document.querySelectorAll('.btn-toggle-password').forEach(btn => {
    btn.addEventListener('click', function() {
      const wrapper = this.closest('.password-wrapper');
      const input = wrapper.querySelector('input');
      const isPassword = input.getAttribute('type') === 'password';
      
      input.setAttribute('type', isPassword ? 'text' : 'password');
      this.setAttribute('aria-pressed', isPassword ? 'true' : 'false');
      this.setAttribute('aria-label', isPassword ? 'Ocultar senha' : 'Mostrar senha');
      this.innerText = isPassword ? '🙈' : '👁️';
    });
  });
});

btnBuscar.addEventListener('click', async () => {
  const termo = inputBusca.value.trim().toUpperCase();
  if (!termo) return;

  const encontrado = await buscarProtocolo(termo);
  resultadoBuscaBody.innerHTML = montarHtmlDetalhes(encontrado);
  modalBusca.classList.add('active');
});

inputBusca.addEventListener('keypress', (event) => {
  if (event.key === 'Enter') btnBuscar.click();
});

if (btnEntrar) btnEntrar.addEventListener('click', () => modalLogin.classList.add('active'));
if (btnAddConta) btnAddConta.addEventListener('click', () => modalCadastro.classList.add('active'));

function fecharLimparLogin() {
  modalLogin.classList.remove('active');
  if(formLogin) {
    formLogin.reset();
    resetarToggleSenhas(formLogin);
  }
}

function fecharLimparCadastro() {
  modalCadastro.classList.remove('active');
  if(formCadastro) {
    formCadastro.reset();
    resetarToggleSenhas(formCadastro);
  }
}

function resetarToggleSenhas(formulario) {
  formulario.querySelectorAll('.password-wrapper input').forEach(input => {
    input.setAttribute('type', 'password');
  });
  formulario.querySelectorAll('.btn-toggle-password').forEach(btn => {
    btn.setAttribute('aria-pressed', 'false');
    btn.setAttribute('aria-label', 'Mostrar senha');
    btn.innerText = '👁️';
  });
}

if (btnFecharLogin) btnFecharLogin.addEventListener('click', fecharLimparLogin);
if (btnFecharCadastro) btnFecharCadastro.addEventListener('click', fecharLimparCadastro);
if (btnFecharBusca) btnFecharBusca.addEventListener('click', () => modalBusca.classList.remove('active'));

window.addEventListener('click', (e) => {
  if (e.target === modalLogin) fecharLimparLogin();
  if (e.target === modalCadastro) fecharLimparCadastro();
  if (e.target === modalBusca) modalBusca.classList.remove('active');
});

if (formLogin) {
  formLogin.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;
    const btnSubmit = formLogin.querySelector('button[type="submit"]');
    
    btnSubmit.innerText = 'Autenticando...';
    btnSubmit.disabled = true;

    try {
      const usuario = await loginUsuarioAPI(email, senha);
      sessionStorage.setItem('isLoggedIn', 'true');
      sessionStorage.setItem('usuarioNome', usuario.nome);
      sessionStorage.setItem('usuarioEmail', usuario.email);
      fecharLimparLogin();
      window.location.href = 'cidadao.html';
    } catch (error) {
      alert(error.message);
      btnSubmit.innerText = 'Entrar como Cidadão';
      btnSubmit.disabled = false;
    }
  });
}

if (formCadastro) {
  formCadastro.addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('cadNome').value;
    const email = document.getElementById('cadEmail').value;
    const senha = document.getElementById('cadSenha').value;
    const btnSubmit = formCadastro.querySelector('button[type="submit"]');

    const regexSenha = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    if (!regexSenha.test(senha)) {
      alert("A senha precisa ter no mínimo 8 caracteres, uma letra maiúscula, uma minúscula, um número e um caractere especial.");
      return;
    }

    btnSubmit.innerText = 'Criando conta...';
    btnSubmit.disabled = true;

    try {
      await registrarUsuarioAPI(nome, email, senha);
      alert('Cadastro realizado com sucesso! Faça login para continuar.');
      fecharLimparCadastro();
      modalLogin.classList.add('active');
      btnSubmit.innerText = 'Concluir Cadastro';
      btnSubmit.disabled = false;
    } catch (error) {
      alert(error.message);
      btnSubmit.innerText = 'Concluir Cadastro';
      btnSubmit.disabled = false;
    }
  });
}

if (linkGestor) {
  linkGestor.addEventListener('click', (e) => {
    e.preventDefault();
    window.location.href = 'gestor.html';
  });
}

if (linkCriarConta) {
  linkCriarConta.addEventListener('click', (e) => {
    e.preventDefault();
    fecharLimparLogin();
    modalCadastro.classList.add('active');
  });
}

if (btnNovoProtocolo) {
  btnNovoProtocolo.addEventListener('click', () => window.location.href = 'solicitacao.html');
}