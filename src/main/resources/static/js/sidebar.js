/* URL de base du backend  */
const API_URL = 'http://localhost:8080/api';

/* Récupère les infos de l'utilisateur connecté depuis sessionStorage */
function getUser() {
  return {
    id:    sessionStorage.getItem('bs_userId'),
    nom:   sessionStorage.getItem('bs_name')   || 'Utilisateur',
    email: sessionStorage.getItem('bs_email')  || ''
  };
}

/* Redirige vers login si pas connecté */
function verifierConnexion() {
  if (!sessionStorage.getItem('bs_userId')) {
    window.location.href = 'login.html';
  }
}

/* Génère les initiales depuis le nom  */
function getInitiales(nom) {
  if (!nom || typeof nom !== 'string') return '?';
  return nom.trim().split(/\s+/).map(n => n[0]).join('').toUpperCase().slice(0, 2) || '?';
}

/* Déconnexion */
function deconnecter() {
  sessionStorage.clear();
  window.location.href = 'login.html';
}

/* Injecte la sidebar dans l'élément #sidebar-slot */
function injecterSidebar(pageActive) {
  verifierConnexion();

  const user = getUser();

  const liens = [
    { id: 'dashboard',     label: 'Tableau de bord', href: 'dashboard.html',     icone: 'grille' },
    { id: 'transactions',  label: 'Transactions',    href: 'transactions.html',  icone: 'fleches' },
    { id: 'budgets',       label: 'Budgets',          href: 'budgets.html',       icone: 'portefeuille' },
    { id: 'previsions',    label: 'Prévisions',       href: 'previsions.html',    icone: 'graphe' },
    { id: 'rapports',      label: 'Rapports',         href: 'rapports.html',      icone: 'rapport' },
    { id: 'categories',    label: 'Catégories',       href: 'categories.html',    icone: 'etiquette' },
  ];

  const icones = {
    grille:      `<svg viewBox="0 0 16 16"><rect x="1" y="1" width="6" height="6" rx="1.5"/><rect x="9" y="1" width="6" height="6" rx="1.5"/><rect x="1" y="9" width="6" height="6" rx="1.5"/><rect x="9" y="9" width="6" height="6" rx="1.5"/></svg>`,
    fleches:     `<svg viewBox="0 0 16 16"><path d="M8 14V2M3 7l5-5 5 5M3 11l5 5 5-5"/></svg>`,
    portefeuille:`<svg viewBox="0 0 16 16"><rect x="1" y="4" width="14" height="10" rx="2"/><path d="M11 9.5a.5.5 0 100 1 .5.5 0 000-1z"/><path d="M5 4V3a2 2 0 012-2h4a2 2 0 012 2v1"/></svg>`,
    graphe:      `<svg viewBox="0 0 16 16"><path d="M2 14l4-6 3 4 2-3 3 5"/></svg>`,
    rapport:     `<svg viewBox="0 0 16 16"><rect x="3" y="2" width="10" height="12" rx="1.5"/><path d="M6 6h4M6 9h4M6 12h2"/></svg>`,
    etiquette:   `<svg viewBox="0 0 16 16"><path d="M9 2H4a2 2 0 00-2 2v4l6 6 6-6-5-6z"/><circle cx="5.5" cy="6.5" r="1"/></svg>`,
    deconnexion: `<svg viewBox="0 0 16 16"><path d="M10 3h3a1 1 0 011 1v8a1 1 0 01-1 1h-3M7 11l4-3-4-3M11 8H3"/></svg>`,
  };

  const liensHTML = liens.map(l => `
    <a href="${l.href}" class="nav-lien ${l.id === pageActive ? 'actif' : ''}">
      ${icones[l.icone]}
      ${l.label}
    </a>
  `).join('');

  const html = `
    <!-- Logo -->
    <div class="sidebar-logo">
      <img src="img/logo.png" alt="Logo Budgetly" onerror="this.style.display='none'"/>
      <div>
        <div class="sidebar-logo-nom">Budgetly</div>
        <div class="sidebar-logo-slogan">Gestion budgétaire</div>
      </div>
    </div>

    <!-- Navigation -->
    <nav class="sidebar-nav">
      <div class="nav-label">Menu</div>
      ${liensHTML}
    </nav>

    <!-- Pied : infos utilisateur -->
    <div class="sidebar-pied">
      <div class="sidebar-avatar">${getInitiales(user.nom)}</div>
      <div>
        <div class="sidebar-user-nom">${user.nom}</div>
        <div class="sidebar-user-role">Étudiant·e</div>
      </div>
      <button class="btn-deconnexion" onclick="deconnecter()" title="Se déconnecter">
        ${icones.deconnexion}
      </button>
    </div>
  `;

  const slot = document.getElementById('sidebar-slot');
  if (slot) slot.innerHTML = html;
}
