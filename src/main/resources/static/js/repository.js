const API_URL = '/api/solicitacoes';
const API_USUARIOS = '/api/usuarios';

function sanitizar(str) {
  if (!str) return '';
  const div = document.createElement('div');
  div.appendChild(document.createTextNode(str));
  return div.innerHTML;
}

function normalizar(str) {
  if (!str) return '';
  return str.normalize('NFD').replace(/[\u0300-\u036f]/g, '').toUpperCase().replace(/\s/g, '_');
}

async function registrarUsuarioAPI(nome, email, senha) {
  const response = await fetch(`${API_USUARIOS}/registrar`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ nome, email, senha })
  });
  if (!response.ok) {
    throw new Error(await response.text());
  }
  return await response.json();
}

async function loginUsuarioAPI(email, senha) {
  const response = await fetch(`${API_USUARIOS}/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, senha })
  });
  if (!response.ok) {
    throw new Error(await response.text());
  }
  return await response.json();
}

async function getProtocolos() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error('Erro');
    return await response.json();
  } catch (error) {
    return [];
  }
}

async function adicionarProtocolo(protocolo) {
  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(protocolo)
    });
    return await response.json();
  } catch (error) {
    return null;
  }
}

async function atualizarStatusProtocolo(codigo, novoStatus) {
  try {
    await fetch(`${API_URL}/${codigo}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'text/plain' },
      body: novoStatus
    });
  } catch (error) {
  }
}

async function buscarProtocolo(codigo) {
  try {
    const response = await fetch(`${API_URL}/${codigo}`);
    if (response.ok) {
      return await response.json();
    }
    return null;
  } catch (error) {
    return null;
  }
}

function calcularSlaDinamico(protocolo) {
  const statusNormalizado = normalizar(protocolo.status);

  if (statusNormalizado === 'CONCLUIDO') {
    return '<span style="color: var(--priority-baixa); font-weight: bold;">Atendimento Concluído</span>';
  }
  if (statusNormalizado === 'REJEITADO') {
    return '<span style="color: var(--text-light); font-weight: bold;">Protocolo Encerrado</span>';
  }

  const diasTotais = parseInt(protocolo.sla);
  if (isNaN(diasTotais)) return sanitizar(protocolo.sla);

  const partesData = protocolo.data.split('/');
  if (partesData.length === 3) {
    const dataCriacao = new Date(partesData[2], partesData[1] - 1, partesData[0]);
    dataCriacao.setHours(0, 0, 0, 0);
    
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const diffTempo = hoje.getTime() - dataCriacao.getTime();
    const diasPassados = Math.floor(diffTempo / (1000 * 3600 * 24));
    const diasRestantes = diasTotais - diasPassados;

    if (diasRestantes < 0) {
      return `<span style="color: var(--priority-alta); font-weight: bold;">Atrasado (${Math.abs(diasRestantes)} dias além do prazo)</span>`;
    } else if (diasRestantes === 0) {
      return `<span style="color: var(--priority-media); font-weight: bold;">Vence hoje</span>`;
    } else {
      return `<strong>${diasRestantes} dia(s) restante(s)</strong> <span class="text-light-info">(Total: ${sanitizar(protocolo.sla)})</span>`;
    }
  }
  return sanitizar(protocolo.sla);
}

function formatarStatus(status) {
  if (!status) return 'badge-aberto';
  const s = normalizar(status);
  if (s === 'EM_ANDAMENTO') return 'badge-andamento';
  if (s === 'CONCLUIDO') return 'badge-concluido';
  if (s === 'REJEITADO') return 'badge-rejeitado';
  return 'badge-aberto';
}

function montarHtmlDetalhes(protocolo) {
  if (!protocolo) {
    return `<div class="box-erro">Protocolo não encontrado.</div>`;
  }
  return `
    <div class="box-destaque d-flex-col">
      <p><strong>Protocolo:</strong> ${sanitizar(protocolo.codigo)}</p>
      <p><strong>Categoria:</strong> ${sanitizar(protocolo.categoria)}</p>
      <p><strong>Prioridade:</strong> ${sanitizar(protocolo.prioridade)}</p>
      <p><strong>Status:</strong> ${sanitizar(protocolo.status)}</p>
      <p><strong>Data de Abertura:</strong> ${sanitizar(protocolo.data)}</p>
      <p><strong>Acompanhamento de Prazo:</strong> ${calcularSlaDinamico(protocolo)}</p>
      <hr class="hr-divisor">
      <p><strong>Descrição:</strong><br><span class="text-light-info">${sanitizar(protocolo.descricao)}</span></p>
    </div>
  `;
}

function renderizarTabela(tbody, protocolos, tipoTela) {
  if (!tbody) return;
  tbody.innerHTML = '';
  
  protocolos.forEach(p => {
    const tr = document.createElement('tr');
    
    let badgeClassPrioridade = 'bg-baixa';
    if (p.prioridade && p.prioridade.includes('Alta')) badgeClassPrioridade = 'bg-alta';
    else if (p.prioridade && p.prioridade.includes('Média')) badgeClassPrioridade = 'bg-media';

    const badgeClassStatus = formatarStatus(p.status);
    const codigoLimpo = sanitizar(p.codigo);

    let html = `
      <td><strong>${codigoLimpo}</strong></td>
      <td>${sanitizar(p.categoria)}</td>
      <td><span class="badge ${badgeClassPrioridade}">${p.prioridade ? sanitizar(p.prioridade.split(' ')[0]) : ''}</span></td>
    `;

    if (tipoTela === 'HOME') {
      const desc = sanitizar(p.descricao || '');
      html += `<td>${desc.length > 45 ? desc.substring(0, 45) + '...' : desc}</td>`;
    } else if (tipoTela === 'CIDADAO') {
      html += `
        <td><span class="badge ${badgeClassStatus}">${sanitizar(p.status || 'Aberto')}</span></td>
        <td>${sanitizar(p.data || '')}</td>
        <td><button class="btn btn-outline btn-sm btn-detalhes" aria-label="Ver detalhes do protocolo ${codigoLimpo}">Ver Detalhes</button></td>
      `;
    } else if (tipoTela === 'GESTOR') {
      html += `
        <td><span class="badge ${badgeClassStatus}">${sanitizar(p.status || 'Aberto')}</span></td>
        <td>${sanitizar(p.data || '')}</td>
        <td><button class="btn btn-primary btn-sm btn-action" aria-label="Analisar protocolo ${codigoLimpo}">Analisar</button></td>
      `;
    }

    tr.innerHTML = html;
    tbody.appendChild(tr);
  });
}