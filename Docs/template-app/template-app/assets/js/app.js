(() => {
    const app = window.APP;
    const body = document.body;
    const root = body.dataset.root || '../../';
    const storage = localStorage;

    const query = (selector, parent = document) => parent.querySelector(selector);
    const queryAll = (selector, parent = document) => [...parent.querySelectorAll(selector)];
    const pageUrl = page => root + app.pages[page].href;

    const language = storage.lang || 'fr';
    const translations = (window.I18N || {}).en || {};
    const translate = text => language === 'en' && translations[text] || text;
    const themeIcon = theme => theme === 'dark' ? 'bi-sun' : 'bi-moon-stars';

    document.documentElement.dataset.bsTheme = storage.theme || 'light';
    document.documentElement.lang = language;

    const isOffline = () => storage.sim === '1' || !navigator.onLine;
    const pendingDrafts = () => +(storage.drafts || 3);

    function connectionBadge() {
        if (isOffline()) {
            return `<span class="badge rounded-pill text-bg-warning">
                <i class="bi bi-wifi-off"></i>
                ${translate('Hors ligne')} · ${pendingDrafts()} ${translate('en attente')}
            </span>`;
        }

        return `<span class="badge rounded-pill text-bg-success">
            <i class="bi bi-wifi"></i>
            ${translate('En ligne')}
        </span>`;
    }

    function showToast(message, type = 'success') {
        let container = query('#toasts');

        if (!container) {
            container = document.createElement('div');
            container.id = 'toasts';
            container.className = 'toast-container position-fixed bottom-0 end-0 p-3';
            body.append(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast align-items-center text-bg-${type} border-0`;
        toast.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">${translate(message)}</div>
                <button class="btn-close btn-close-white me-2 m-auto"
                        data-bs-dismiss="toast"
                        aria-label="Fermer"></button>
            </div>`;

        container.append(toast);
        new bootstrap.Toast(toast, { delay: 3000 }).show();
    }

    function showConfirmDialog(message, onConfirm) {
        let modal = query('#cm');

        if (!modal) {
            body.insertAdjacentHTML(
                'beforeend',
                `<div class="modal fade" id="cm" tabindex="-1">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirmer</h5>
                                <button class="btn-close"
                                        data-bs-dismiss="modal"
                                        aria-label="Fermer"></button>
                            </div>
                            <div class="modal-body" id="cmt"></div>
                            <div class="modal-footer">
                                <button class="btn btn-outline-secondary"
                                        data-bs-dismiss="modal">Annuler</button>
                                <button class="btn btn-danger"
                                        id="cmok">Confirmer</button>
                            </div>
                        </div>
                    </div>
                </div>`
            );
            modal = query('#cm');
        }

        translatePage(modal);
        query('#cmt').textContent = translate(message);

        const modalInstance = bootstrap.Modal.getOrCreateInstance(modal);
        query('#cmok').onclick = () => {
            modalInstance.hide();
            onConfirm();
        };
        modalInstance.show();
    }

    function translatePage(rootElement) {
        if (language !== 'en') {
            return;
        }

        const walker = document.createTreeWalker(rootElement, NodeFilter.SHOW_TEXT);
        const textNodes = [];
        let node;

        while (node = walker.nextNode()) {
            textNodes.push(node);
        }

        textNodes.forEach(textNode => {
            const text = textNode.nodeValue.trim();

            if (text && translations[text]) {
                textNode.nodeValue = textNode.nodeValue.replace(text, translations[text]);
            }
        });

        [rootElement, ...rootElement.querySelectorAll('*')].forEach(element => {
            ['placeholder', 'title', 'aria-label', 'data-confirm', 'data-toast'].forEach(attribute => {
                const value = element.getAttribute && element.getAttribute(attribute);

                if (value && translations[value]) {
                    element.setAttribute(attribute, translations[value]);
                }
            });
        });
    }

    window.App = {
        toast: showToast,
        confirmBox: showConfirmDialog,
        T: translate
    };

    function renderApplicationLayout(main) {
        const menu = app.menu.map(item => item.l
            ? `<div class="side-label">${item.l}</div>`
            : `<a class="side-item${item.p == body.dataset.active ? ' active' : ''}"
                    href="${pageUrl(item.p)}"
                    title="${item.t}">
                    <i class="bi bi-${item.i}"></i>
                    <span>${item.t}</span>
                    ${item.b ? `<span class="badge text-bg-warning ms-auto">${item.b}</span>` : ''}
                </a>`
        ).join('');

        const notifications = app.notifs.map(notification => `
            <li>
                <a class="dropdown-item small text-wrap" href="${pageUrl(27)}">
                    <i class="bi bi-${notification.i} me-2"></i>${notification.t}
                    <div class="text-body-secondary">${notification.d}</div>
                </a>
            </li>`
        ).join('');

        const sidebar = `
            <aside class="sidebar offcanvas-lg offcanvas-start" id="sb" tabindex="-1">
                <div class="brand">
                    <div class="mark">
                        <img src="${root}assets/img/logo.svg" alt="" width="64" height="64">
                    </div>
                    <div class="brand-t">
                        <b>${app.name}</b>
                        <span>${app.org}</span>
                    </div>
                </div>
                <nav class="side-nav">${menu}</nav>
            </aside>`;

        const topbar = `
            <header class="topbar">
                <div class="topbar-main">
                    <button class="btn btn-outline-secondary"
                            id="sidebar-toggle"
                            aria-label="Menu">
                        <i class="bi bi-layout-sidebar-inset"></i>
                    </button>
                    <form class="topsearch" action="${pageUrl(29)}" role="search">
                        <i class="bi bi-search"></i>
                        <input name="q"
                               class="form-control"
                               type="search"
                               placeholder="Rechercher une espèce, un lieu, un dataset..."
                               aria-label="Rechercher">
                    </form>
                    <span id="pill">${connectionBadge()}</span>

                    <button class="btn btn-outline-secondary"
                            id="theme"
                            aria-label="Mode sombre / clair">
                        <i class="bi ${themeIcon(storage.theme || 'light')}"></i>
                    </button>
                    <button class="btn btn-outline-secondary"
                            id="lang"
                            aria-label="Langue / Language">
                        <i class="bi bi-translate"></i>
                        ${language === 'en' ? 'FR' : 'EN'}
                    </button>
                    <div class="dropdown">
                        <button class="btn btn-outline-secondary position-relative"
                                data-bs-toggle="dropdown"
                                aria-label="Notifications">
                            <i class="bi bi-bell"></i>
                            <span class="badge text-bg-success position-absolute top-0 start-100
                                         translate-middle rounded-pill">
                                ${app.notifs.length}
                            </span>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow" style="width:320px">
                            ${notifications}
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item text-center small" href="${pageUrl(27)}">
                                    Tout voir
                                </a>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="dropdown profile-menu">
                        <button class="profile-trigger"
                                data-bs-toggle="dropdown">
                            <span class="avatar">${app.user.ini}</span>
                            <span class="profile-summary d-none d-md-flex">
                                <strong>${app.user.name}</strong>
                                <small>${app.user.role}</small>
                            </span>
                            <i class="bi bi-chevron-down profile-chevron"></i>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow profile-dropdown">
                            <li class="profile-dropdown-heading">
                                <span class="avatar">${app.user.ini}</span>
                                <span>
                                    <strong>${app.user.name}</strong>
                                    <small>${app.user.role}</small>
                                </span>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" href="${pageUrl(5)}">
                                    <i class="bi bi-person me-2"></i>Mon profil
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="${pageUrl(23)}">
                                    <i class="bi bi-cloud-arrow-up me-2"></i>Brouillons hors ligne
                                </a>
                            </li>
                            <li>
                                <button class="dropdown-item" id="sim">
                                    <i class="bi bi-wifi-off me-2"></i>Simuler hors ligne
                                </button>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" href="${pageUrl(1)}">
                                    <i class="bi bi-box-arrow-right me-2"></i>Déconnexion
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </header>`;

        const footer = `
            <footer class="app-foot">
                <span>${app.name} · <span>version</span> ${app.version}</span>
                <a href="${pageUrl(44)}">Aide</a>
                <a href="${pageUrl(45)}">Mentions légales</a>
                <a href="${pageUrl(45)}">Données et licences</a>
            </footer>`;

        body.innerHTML = `
            <div class="app-shell">
                ${sidebar}
                <div class="app-main">${topbar}${main.outerHTML}${footer}</div>
            </div>`;

        if (storage.collapsed === '1') {
            body.classList.add('collapsed');
        }

        query('#sidebar-toggle').onclick = () => {
            if (window.matchMedia('(min-width: 992px)').matches) {
                body.classList.toggle('collapsed');
                storage.collapsed = body.classList.contains('collapsed') ? '1' : '0';
                return;
            }

            bootstrap.Offcanvas.getOrCreateInstance(query('#sb')).toggle();
        };

        query('#sim').onclick = () => {
            storage.sim = storage.sim === '1' ? '0' : '1';
            query('#pill').innerHTML = connectionBadge();
            showToast(isOffline() ? 'Mode hors ligne simulé' : 'Retour en ligne', 'secondary');
        };

        addEventListener('online', () => query('#pill').innerHTML = connectionBadge());
        addEventListener('offline', () => query('#pill').innerHTML = connectionBadge());
    }

    const main = query('main#content');

    if (body.dataset.layout !== 'blank' && main) {
        renderApplicationLayout(main);
    }

    queryAll('table[data-table]').forEach(table => {
        const filter = query('.table-filter', table.closest('.card'));

        if (filter) {
            filter.oninput = () => {
                const value = filter.value.toLowerCase();

                queryAll('tbody tr', table).forEach(row => {
                    row.hidden = !row.textContent.toLowerCase().includes(value);
                });
            };
        }

        queryAll('th', table).forEach((header, index) => {
            if (!header.textContent.trim()) {
                return;
            }

            header.style.cursor = 'pointer';
            header.title = 'Trier';
            header.onclick = () => {
                const direction = header.dataset.d = header.dataset.d == '1' ? '-1' : '1';

                queryAll('tbody tr', table)
                    .sort((rowA, rowB) => rowA.cells[index].textContent.localeCompare(
                        rowB.cells[index].textContent,
                        'fr',
                        { numeric: true }
                    ) * direction)
                    .forEach(row => row.parentNode.append(row));
            };
        });
    });

    const searchQuery = new URLSearchParams(location.search).get('q');
    const externalFilter = query('.table-filter[data-q]');

    if (searchQuery && externalFilter) {
        externalFilter.value = searchQuery;
        externalFilter.dispatchEvent(new Event('input'));
    }

    const tableConfigKey = 'template:custom-table';

    function getTableColumns() {
        return queryAll('#cols tbody tr').map(row => ({
            name: query('[data-attr]', row)?.value.trim(),
            label: query('td:nth-child(2) input', row)?.value.trim(),
            type: query('[data-type]', row)?.value,
            unit: query('td:nth-child(4) input', row)?.value.trim(),
            choices: query('td:nth-child(5) input', row)?.value
                .split(';')
                .map(choice => choice.trim())
                .filter(Boolean),
            required: query('input[type=checkbox]', row)?.checked || false
        })).filter(column => column.name);
    }

    function inputForColumn(column, index) {
        const inputId = `table-field-${index}`;
        const required = column.required ? ' required' : '';
        const requiredMark = column.required ? '<span class="text-danger"> *</span>' : '';
        const label = `${column.label || column.name}${column.unit ? ` (${column.unit})` : ''}`;

        if (column.type === 'Liste') {
            return `<label class="form-label" for="${inputId}">${label}${requiredMark}</label>
                <select class="form-select" id="${inputId}" name="${column.name}"${required}>
                    <option value="">Sélectionner...</option>
                    ${column.choices.map(choice => `<option>${choice}</option>`).join('')}
                </select>`;
        }

        if (column.type === 'Oui/non') {
            return `<div class="form-check form-switch mt-2">
                <input class="form-check-input" type="checkbox" role="switch"
                       id="${inputId}" name="${column.name}">
                <label class="form-check-label" for="${inputId}">${label}</label>
            </div>`;
        }

        const inputType = column.type === 'Entier' || column.type === 'Décimal'
            ? 'number'
            : column.type === 'Date'
                ? 'date'
                : column.type === 'Date et heure'
                    ? 'datetime-local'
                    : 'text';
        const step = column.type === 'Décimal' ? ' step="any"' : '';

        return `<label class="form-label" for="${inputId}">${label}${requiredMark}</label>
            <input class="form-control" type="${inputType}" id="${inputId}"
                   name="${column.name}"${step}${required}>`;
    }

    function setupTableDesigner() {
        const columnsTable = query('#cols');
        const addColumnButton = query('[data-add-row="#cols"]');
        const tableForm = query('form.needs-validation');
        const preview = query('#table-preview');

        if (!columnsTable || !addColumnButton || !tableForm) {
            return;
        }

        const columnsBody = query('tbody', columnsTable);
        const createColumnRow = () => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td style="min-width:200px">
                    <input class="form-control" list="cat" data-attr
                           aria-label="Attribut" placeholder="Nom de la colonne">
                    <span class="badge mt-1" data-info></span>
                </td>
                <td><input class="form-control" type="text"
                           aria-label="Libellé dans ce tableau"></td>
                <td>
                    <select class="form-select" data-type aria-label="Type">
                        <option value="Texte">Texte</option>
                        <option value="Entier">Entier</option>
                        <option value="Décimal">Décimal</option>
                        <option value="Oui/non">Oui/non</option>
                        <option value="Date">Date</option>
                        <option value="Date et heure">Date et heure</option>
                        <option value="Liste">Liste</option>
                    </select>
                </td>
                <td><input class="form-control" type="text" aria-label="Unité"></td>
                <td><input class="form-control" type="text"
                           aria-label="Choix (type Liste)"
                           placeholder="Choix séparés par ;"></td>
                <td class="text-center">
                    <div class="form-check form-switch d-inline-block">
                        <input class="form-check-input" type="checkbox"
                               role="switch" aria-label="Obligatoire">
                    </div>
                </td>
                <td class="text-end">
                    <button type="button" class="btn btn-sm btn-outline-danger"
                            data-del-row aria-label="Supprimer">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>`;
            return row;
        };

        const refreshColumnState = row => {
            const type = query('[data-type]', row);
            const choices = query('td:nth-child(5) input', row);
            const catalogueOption = [...queryAll('#cat option')]
                .find(option => option.value === query('[data-attr]', row)?.value);
            const info = query('[data-info]', row);

            choices.disabled = type.value !== 'Liste';
            if (info) {
                info.textContent = catalogueOption ? 'Attribut existant' : 'Nouvel attribut';
                info.className = `badge mt-1 ${catalogueOption ? 'text-bg-success' : 'text-bg-secondary'}`;
            }
        };

        const refreshPreview = () => {
            if (preview) {
                const columns = getTableColumns();
                preview.innerHTML = columns.length
                    ? `<div class="row g-3">${columns.map((column, index) => `
                        <div class="col-md">${inputForColumn(column, index)}</div>
                    `).join('')}</div>`
                    : '<p class="text-body-secondary mb-0">Ajoutez une colonne pour prévisualiser le formulaire.</p>';
            }
        };

        addColumnButton.onclick = () => {
            const row = createColumnRow();
            columnsBody.append(row);
            refreshColumnState(row);
            refreshPreview();
            query('[data-attr]', row).focus();
        };

        columnsTable.addEventListener('click', event => {
            const deleteButton = event.target.closest('[data-del-row]');
            if (deleteButton) {
                deleteButton.closest('tr').remove();
                refreshPreview();
            }
        });

        columnsTable.addEventListener('input', event => {
            const row = event.target.closest('tr');
            if (row) {
                refreshColumnState(row);
                refreshPreview();
            }
        });

        columnsTable.addEventListener('change', event => {
            const row = event.target.closest('tr');
            if (row) {
                refreshColumnState(row);
                refreshPreview();
            }
        });

        queryAll('tbody tr', columnsTable).forEach(refreshColumnState);
        refreshPreview();
        tableForm.addEventListener('submit', () => {
            storage[tableConfigKey] = JSON.stringify({
                name: query('[name="nom-du-tableau"]', tableForm)?.value || 'Nouveau tableau',
                dataset: query('[name="dataset"]', tableForm)?.value || '',
                description: query('[name="description"]', tableForm)?.value || '',
                columns: getTableColumns()
            });
        }, { once: true });
    }

    function setupTableEntryForm() {
        const entryGrid = query('#table-entry-grid');

        if (!entryGrid) {
            return;
        }

        let tableConfig;
        try {
            tableConfig = JSON.parse(storage[tableConfigKey] || '{}');
        } catch (error) {
            console.error('Impossible de charger la structure du tableau.', error);
            return;
        }

        const columns = tableConfig.columns?.length
            ? tableConfig.columns
            : [
                { name: 'nom-de-l-espece', label: "Nom de l'espèce", type: 'Texte', unit: '', choices: [], required: true },
                { name: 'hauteur-du-nid', label: 'Hauteur du nid', type: 'Décimal', unit: 'm', choices: [], required: false },
                { name: 'nombre-d-ufs', label: "Nombre d'œufs", type: 'Entier', unit: '', choices: [], required: false },
                { name: 'nid-actif', label: 'Nid actif', type: 'Oui/non', unit: '', choices: [], required: false },
                { name: 'date-de-decouverte', label: 'Date de découverte', type: 'Date', unit: '', choices: [], required: true },
                { name: 'etat-du-nid', label: 'État du nid', type: 'Liste', unit: '', choices: ['Intact', 'Endommagé', 'Abandonné'], required: false }
            ];
        entryGrid.innerHTML = columns.length
            ? columns.map((column, index) => `
                <div class="col-md">${inputForColumn(column, index)}</div>
            `).join('')
            : '<p class="text-body-secondary mb-0">Ajoutez d’abord des colonnes à ce tableau.</p>';
    }

    function setupTableGridDesigner() {
        const builder = query('#table-builder');
        const form = query('form.needs-validation');

        if (!builder || !form) {
            return;
        }

        const columnsRow = query('#table-columns', builder);
        const rowsBody = query('#table-rows', builder);
        let columnIndex = 0;

        const dataTypes = ['Texte', 'Entier', 'Décimal', 'Oui/non', 'Date', 'Date et heure', 'Liste'];
        const typeOptions = dataTypes.map(type => `<option value="${type}">${type}</option>`).join('');

        const cellInput = (type, name) => {
            if (type === 'Oui/non') {
                return `<input class="form-check-input" type="checkbox" name="${name}"
                               aria-label="${name}">`;
            }

            if (type === 'Liste') {
                return `<select class="form-select form-select-sm" name="${name}">
                    <option value="">Choisir...</option>
                </select>`;
            }

            const inputType = type === 'Entier' || type === 'Décimal'
                ? 'number'
                : type === 'Date'
                    ? 'date'
                    : type === 'Date et heure'
                        ? 'datetime-local'
                        : 'text';
            const step = type === 'Décimal' ? ' step="any"' : '';

            return `<input class="form-control form-control-sm" type="${inputType}"
                           name="${name}"${step}>`;
        };

        const rowAction = () => `
            <td class="table-builder-actions">
                <button type="button" class="btn btn-sm btn-outline-danger"
                        data-remove-table-row aria-label="Supprimer la ligne"
                        title="Supprimer la ligne">
                    <i class="bi bi-trash"></i>
                </button>
            </td>`;

        const addColumn = () => {
            const index = columnIndex++;
            const key = `column-${index}`;
            const header = document.createElement('th');
            header.dataset.column = key;
            header.innerHTML = `
                <div class="table-column-editor">
                    <div class="d-flex align-items-center gap-1">
                        <input class="form-control form-control-sm" list="attribute-catalog"
                               data-column-name placeholder="Nom de colonne"
                               aria-label="Nom de colonne">
                        <select class="form-select form-select-sm column-type-select"
                                data-column-type aria-label="Type de colonne">
                            ${typeOptions}
                        </select>
                        <button type="button" class="btn btn-sm btn-link text-danger p-0"
                                data-remove-column aria-label="Supprimer la colonne"
                                title="Supprimer la colonne">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                    <input type="hidden" data-column-choices value="Option 1; Option 2">
                </div>`;
            columnsRow.insertBefore(header, query('[data-add-column]', columnsRow));

            queryAll('.table-builder-empty', rowsBody).forEach(cell => cell.remove());
            queryAll('[data-table-row]', rowsBody).forEach(row => {
                const cell = document.createElement('td');
                cell.dataset.column = key;
                cell.innerHTML = cellInput('Texte', key);
                row.insertBefore(cell, query('.table-builder-actions', row));
            });
            refreshRows();
        };

        const refreshRows = () => {
            queryAll('th[data-column]', columnsRow).forEach(header => {
                const key = header.dataset.column;
                const typeSelect = query('[data-column-type]', header);
                const type = typeSelect.value;
                const choicesInput = query('[data-column-choices]', header);
                const choices = choicesInput.value
                    .split(';').map(choice => choice.trim()).filter(Boolean);
                choicesInput.disabled = type !== 'Liste';

                queryAll(`td[data-column="${key}"]`, rowsBody).forEach(cell => {
                    const currentValue = query('input, select', cell)?.value || '';
                    cell.innerHTML = cellInput(type, key);
                    const input = query('input, select', cell);
                    if (input && input.tagName === 'SELECT') {
                        choices.forEach(choice => input.insertAdjacentHTML(
                            'beforeend', `<option>${choice}</option>`
                        ));
                    }
                    if (input && input.type !== 'checkbox') {
                        input.value = currentValue;
                    }
                });
            });
        };

        query('[data-add-column]', builder).onclick = addColumn;
        query('[data-add-table-row]', form).onclick = () => {
            const row = document.createElement('tr');
            row.dataset.tableRow = '';
            row.innerHTML = '';
            queryAll('th[data-column]', columnsRow).forEach(header => {
                const cell = document.createElement('td');
                cell.dataset.column = header.dataset.column;
                cell.innerHTML = cellInput(
                    query('[data-column-type]', header).value,
                    header.dataset.column
                );
                row.append(cell);
            });
            row.insertAdjacentHTML('beforeend', rowAction());
            rowsBody.append(row);
        };

        builder.addEventListener('input', refreshRows);
        builder.addEventListener('change', refreshRows);
        builder.addEventListener('click', event => {
            const removeButton = event.target.closest('[data-remove-column]');
            if (removeButton) {
                const column = removeButton.closest('th[data-column]').dataset.column;
                removeButton.closest('th').remove();
                queryAll(`td[data-column="${column}"]`, rowsBody).forEach(cell => cell.remove());
                return;
            }

            const removeRowButton = event.target.closest('[data-remove-table-row]');
            if (removeRowButton) {
                removeRowButton.closest('tr').remove();
            }
        });

        form.addEventListener('submit', () => {
            const columns = queryAll('th[data-column]', columnsRow).map(header => ({
                name: query('[data-column-name]', header).value.trim(),
                label: query('[data-column-name]', header).value.trim(),
                type: query('[data-column-type]', header).value,
                choices: query('[data-column-choices]', header).value
                    .split(';').map(choice => choice.trim()).filter(Boolean),
                required: false
            })).filter(column => column.name);
            storage[tableConfigKey] = JSON.stringify({ columns });
        }, { once: true });
    }

    setupTableDesigner();
    setupTableGridDesigner();
    setupTableEntryForm();

    queryAll('form.needs-validation').forEach(form => {
        const draftKey = `draft:${body.dataset.page}`;

        try {
            const draft = JSON.parse(storage[draftKey] || '{}');

            [...form.elements].forEach(element => {
                if (element.name && draft[element.name] != null && element.type != 'file') {
                    element.value = draft[element.name];
                }
            });
        } catch (error) {
            console.error('Impossible de charger le brouillon du formulaire.', error);
        }

        form.addEventListener('input', () => {
            const draft = {};

            [...form.elements].forEach(element => {
                if (element.name && element.type != 'file') {
                    draft[element.name] = element.value;
                }
            });

            storage[draftKey] = JSON.stringify(draft);

            const status = query('[data-autosave]', form);

            if (status) {
                status.textContent = translate('Brouillon enregistré à') + ' ' +
                    new Date().toLocaleTimeString('fr-FR', {
                        hour: '2-digit',
                        minute: '2-digit'
                    });
            }
        });

        form.addEventListener('submit', event => {
            event.preventDefault();

            if (!form.checkValidity()) {
                form.classList.add('was-validated');
                showToast('Vérifiez les champs en rouge', 'danger');
                return;
            }

            const nextPage = event.submitter && event.submitter.dataset.next;
            showToast('Enregistré (démonstration)');
            delete storage[draftKey];

            if (nextPage) {
                setTimeout(() => location.href = nextPage, 700);
            }
        });
    });

    queryAll('.dropzone input[type=file]').forEach(input => {
        input.onchange = () => {
            query('.files', input.closest('.dropzone')).innerHTML = [...input.files]
                .map(file => `
                    <li>${file.name}
                        <span class="text-body-secondary">
                            (${Math.round(file.size / 1024)} Ko)
                        </span>
                    </li>`
                )
                .join('');
        };
    });

    document.addEventListener('click', event => {
        const action = event.target.closest('[data-demo],[data-toast],[data-confirm]');

        if (!action) {
            return;
        }

        const href = action.getAttribute('href');
        const goToLink = () => {
            if (href && href != '#') {
                setTimeout(() => location.href = href, 700);
            }
        };

        if (action.hasAttribute('data-demo')) {
            event.preventDefault();
            showToast('Action de démonstration : pas encore branchée', 'secondary');
        } else if (action.dataset.toast) {
            event.preventDefault();
            showToast(action.dataset.toast);
            goToLink();
        } else if (action.dataset.confirm) {
            event.preventDefault();
            showConfirmDialog(action.dataset.confirm, () => {
                showToast('Action confirmée (démonstration)', 'secondary');
                goToLink();
            });
        }
    });

    if (body.dataset.layout === 'blank') {
        body.insertAdjacentHTML(
            'beforeend',
            `<div class="position-fixed top-0 end-0 m-3 d-flex gap-2">
                <button class="btn btn-sm btn-outline-secondary"
                        id="theme"
                        aria-label="Mode sombre / clair">
                    <i class="bi ${themeIcon(storage.theme || 'light')}"></i>
                </button>
                <button class="btn btn-sm btn-outline-secondary"
                        id="lang"
                        aria-label="Langue / Language">
                    <i class="bi bi-translate"></i>
                    ${language === 'en' ? 'FR' : 'EN'}
                </button>
            </div>`
        );
    }

    document.addEventListener('click', event => {
        if (event.target.closest('#lang')) {
            storage.lang = language === 'en' ? 'fr' : 'en';
            location.reload();
        }

        if (event.target.closest('#theme')) {
            storage.theme = document.documentElement.dataset.bsTheme === 'dark'
                ? 'light'
                : 'dark';
            document.documentElement.dataset.bsTheme = storage.theme;
            queryAll('#theme').forEach(button => {
                button.querySelector('i').className = `bi ${themeIcon(storage.theme)}`;
            });
        }
    });

    const titleParts = document.title.split(' · ');
    document.title = translate(titleParts[0]) + (titleParts[1] ? ` · ${titleParts[1]}` : '');
    translatePage(body);
})();
