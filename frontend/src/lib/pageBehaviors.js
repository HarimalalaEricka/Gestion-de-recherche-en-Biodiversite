// Comportements du template (ex-app.js) appliqués au contenu d'une vue :
// tableaux triables/filtrables, formulaires (validation + brouillon), concepteur de tableaux
// personnalisés, zones de dépôt, boutons data-demo / data-toast / data-confirm.
// Tout est limité à l'élément `root` (le <main> de la vue).
import { showToast, showConfirm } from './ui'
import { translate } from './i18n'

const TABLE_CONFIG_KEY = 'template:custom-table'

export function initPage(root, { page, route, router }) {
  const query = (selector, parent = root) => parent.querySelector(selector)
  const queryAll = (selector, parent = root) => [...parent.querySelectorAll(selector)]
  const storage = localStorage

  const go = (href) => {
    if (!href || href === '#') return
    if (/^https?:|^mailto:/.test(href)) location.href = href
    else router.push(href)
  }

  // ---------------------------------------------------------------- tableaux
  queryAll('table[data-table]').forEach((table) => {
    const filter = query('.table-filter', table.closest('.card'))

    if (filter) {
      filter.oninput = () => {
        const value = filter.value.toLowerCase()
        queryAll('tbody tr', table).forEach((row) => {
          row.hidden = !row.textContent.toLowerCase().includes(value)
        })
      }
    }

    queryAll('th', table).forEach((header, index) => {
      if (!header.textContent.trim()) return

      header.style.cursor = 'pointer'
      header.title = 'Trier'
      header.onclick = () => {
        const direction = (header.dataset.d = header.dataset.d == '1' ? '-1' : '1')

        queryAll('tbody tr', table)
          .sort(
            (rowA, rowB) =>
              rowA.cells[index].textContent.localeCompare(rowB.cells[index].textContent, 'fr', {
                numeric: true,
              }) * direction,
          )
          .forEach((row) => row.parentNode.append(row))
      }
    })
  })

  // Recherche globale de la barre du haut : /partage/explorer?q=...
  const searchQuery = [route.query.q].flat()[0]
  const externalFilter = query('.table-filter[data-q]')
  if (searchQuery && externalFilter) {
    externalFilter.value = searchQuery
    externalFilter.dispatchEvent(new Event('input'))
  }

  // ------------------------------------------------- tableaux personnalisés
  function inputForColumn(column, index) {
    const inputId = `table-field-${index}`
    const required = column.required ? ' required' : ''
    const requiredMark = column.required ? '<span class="text-danger"> *</span>' : ''
    const label = `${column.label || column.name}${column.unit ? ` (${column.unit})` : ''}`

    if (column.type === 'Liste') {
      return `<label class="form-label" for="${inputId}">${label}${requiredMark}</label>
        <select class="form-select" id="${inputId}" name="${column.name}"${required}>
          <option value="">Sélectionner...</option>
          ${(column.choices || []).map((choice) => `<option>${choice}</option>`).join('')}
        </select>`
    }

    if (column.type === 'Oui/non') {
      return `<div class="form-check form-switch mt-2">
        <input class="form-check-input" type="checkbox" role="switch"
               id="${inputId}" name="${column.name}">
        <label class="form-check-label" for="${inputId}">${label}</label>
      </div>`
    }

    const inputType =
      column.type === 'Entier' || column.type === 'Décimal'
        ? 'number'
        : column.type === 'Date'
          ? 'date'
          : column.type === 'Date et heure'
            ? 'datetime-local'
            : 'text'
    const step = column.type === 'Décimal' ? ' step="any"' : ''

    return `<label class="form-label" for="${inputId}">${label}${requiredMark}</label>
      <input class="form-control" type="${inputType}" id="${inputId}"
             name="${column.name}"${step}${required}>`
  }

  function setupTableEntryForm() {
    const entryGrid = query('#table-entry-grid')
    if (!entryGrid) return

    let tableConfig
    try {
      tableConfig = JSON.parse(storage.getItem(TABLE_CONFIG_KEY) || '{}')
    } catch (error) {
      console.error('Impossible de charger la structure du tableau.', error)
      return
    }

    const columns = tableConfig.columns?.length
      ? tableConfig.columns
      : [
          { name: 'nom-de-l-espece', label: "Nom de l'espèce", type: 'Texte', unit: '', choices: [], required: true },
          { name: 'hauteur-du-nid', label: 'Hauteur du nid', type: 'Décimal', unit: 'm', choices: [], required: false },
          { name: 'nombre-d-ufs', label: "Nombre d'œufs", type: 'Entier', unit: '', choices: [], required: false },
          { name: 'nid-actif', label: 'Nid actif', type: 'Oui/non', unit: '', choices: [], required: false },
          { name: 'date-de-decouverte', label: 'Date de découverte', type: 'Date', unit: '', choices: [], required: true },
          { name: 'etat-du-nid', label: 'État du nid', type: 'Liste', unit: '', choices: ['Intact', 'Endommagé', 'Abandonné'], required: false },
        ]
    entryGrid.innerHTML = columns.length
      ? columns.map((column, index) => `<div class="col-md">${inputForColumn(column, index)}</div>`).join('')
      : '<p class="text-body-secondary mb-0">Ajoutez d’abord des colonnes à ce tableau.</p>'
  }

  function setupTableGridDesigner() {
    const builder = query('#table-builder')
    const form = query('form.needs-validation')
    if (!builder || !form) return

    const columnsRow = query('#table-columns', builder)
    const rowsBody = query('#table-rows', builder)
    let columnIndex = 0

    const dataTypes = ['Texte', 'Entier', 'Décimal', 'Oui/non', 'Date', 'Date et heure', 'Liste']
    const typeOptions = dataTypes.map((type) => `<option value="${type}">${type}</option>`).join('')

    const cellInput = (type, name) => {
      if (type === 'Oui/non') {
        return `<input class="form-check-input" type="checkbox" name="${name}" aria-label="${name}">`
      }
      if (type === 'Liste') {
        return `<select class="form-select form-select-sm" name="${name}">
          <option value="">Choisir...</option>
        </select>`
      }
      const inputType =
        type === 'Entier' || type === 'Décimal'
          ? 'number'
          : type === 'Date'
            ? 'date'
            : type === 'Date et heure'
              ? 'datetime-local'
              : 'text'
      const step = type === 'Décimal' ? ' step="any"' : ''
      return `<input class="form-control form-control-sm" type="${inputType}" name="${name}"${step}>`
    }

    const rowAction = () => `
      <td class="table-builder-actions">
        <button type="button" class="btn btn-sm btn-outline-danger"
                data-remove-table-row aria-label="Supprimer la ligne"
                title="Supprimer la ligne">
          <i class="bi bi-trash"></i>
        </button>
      </td>`

    const refreshRows = () => {
      queryAll('th[data-column]', columnsRow).forEach((header) => {
        const key = header.dataset.column
        const typeSelect = query('[data-column-type]', header)
        const type = typeSelect.value
        const choicesInput = query('[data-column-choices]', header)
        const choices = choicesInput.value
          .split(';')
          .map((choice) => choice.trim())
          .filter(Boolean)
        choicesInput.disabled = type !== 'Liste'

        queryAll(`td[data-column="${key}"]`, rowsBody).forEach((cell) => {
          const currentValue = query('input, select', cell)?.value || ''
          cell.innerHTML = cellInput(type, key)
          const input = query('input, select', cell)
          if (input && input.tagName === 'SELECT') {
            choices.forEach((choice) => input.insertAdjacentHTML('beforeend', `<option>${choice}</option>`))
          }
          if (input && input.type !== 'checkbox') input.value = currentValue
        })
      })
    }

    const addColumn = () => {
      const index = columnIndex++
      const key = `column-${index}`
      const header = document.createElement('th')
      header.dataset.column = key
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
        </div>`
      columnsRow.insertBefore(header, query('[data-add-column]', columnsRow))

      queryAll('.table-builder-empty', rowsBody).forEach((cell) => cell.remove())
      queryAll('[data-table-row]', rowsBody).forEach((row) => {
        const cell = document.createElement('td')
        cell.dataset.column = key
        cell.innerHTML = cellInput('Texte', key)
        row.insertBefore(cell, query('.table-builder-actions', row))
      })
      refreshRows()
    }

    query('[data-add-column]', builder).onclick = addColumn
    query('[data-add-table-row]', form).onclick = () => {
      const row = document.createElement('tr')
      row.dataset.tableRow = ''
      queryAll('th[data-column]', columnsRow).forEach((header) => {
        const cell = document.createElement('td')
        cell.dataset.column = header.dataset.column
        cell.innerHTML = cellInput(query('[data-column-type]', header).value, header.dataset.column)
        row.append(cell)
      })
      row.insertAdjacentHTML('beforeend', rowAction())
      rowsBody.append(row)
    }

    builder.addEventListener('input', refreshRows)
    builder.addEventListener('change', refreshRows)
    builder.addEventListener('click', (event) => {
      const removeButton = event.target.closest('[data-remove-column]')
      if (removeButton) {
        const column = removeButton.closest('th[data-column]').dataset.column
        removeButton.closest('th').remove()
        queryAll(`td[data-column="${column}"]`, rowsBody).forEach((cell) => cell.remove())
        return
      }

      const removeRowButton = event.target.closest('[data-remove-table-row]')
      if (removeRowButton) removeRowButton.closest('tr').remove()
    })

    form.addEventListener(
      'submit',
      () => {
        const columns = queryAll('th[data-column]', columnsRow)
          .map((header) => ({
            name: query('[data-column-name]', header).value.trim(),
            label: query('[data-column-name]', header).value.trim(),
            type: query('[data-column-type]', header).value,
            choices: query('[data-column-choices]', header)
              .value.split(';')
              .map((choice) => choice.trim())
              .filter(Boolean),
            required: false,
          }))
          .filter((column) => column.name)
        storage.setItem(TABLE_CONFIG_KEY, JSON.stringify({ columns }))
      },
      { once: true },
    )
  }

  setupTableGridDesigner()
  setupTableEntryForm()

  // ------------------------------------------------------------- formulaires
  queryAll('form.needs-validation').forEach((form) => {
    const draftKey = `draft:${page}`

    try {
      const draft = JSON.parse(storage.getItem(draftKey) || '{}')
      ;[...form.elements].forEach((element) => {
        if (element.name && draft[element.name] != null && element.type != 'file') {
          element.value = draft[element.name]
        }
      })
    } catch (error) {
      console.error('Impossible de charger le brouillon du formulaire.', error)
    }

    form.addEventListener('input', () => {
      const draft = {}
      ;[...form.elements].forEach((element) => {
        if (element.name && element.type != 'file') draft[element.name] = element.value
      })
      storage.setItem(draftKey, JSON.stringify(draft))

      const status = query('[data-autosave]', form)
      if (status) {
        status.textContent =
          translate('Brouillon enregistré à') +
          ' ' +
          new Date().toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' })
      }
    })

    form.addEventListener('submit', (event) => {
      event.preventDefault()

      if (!form.checkValidity()) {
        form.classList.add('was-validated')
        showToast('Vérifiez les champs en rouge', 'danger')
        return
      }

      const nextPage = event.submitter && event.submitter.dataset.next
      // showToast('Enregistré (démonstration)')
      storage.removeItem(draftKey)

      if (nextPage) setTimeout(() => go(nextPage), 700)
    })
  })

  // ---------------------------------------------------------- zones de dépôt
  queryAll('.dropzone input[type=file]').forEach((input) => {
    input.onchange = () => {
      query('.files', input.closest('.dropzone')).innerHTML = [...input.files]
        .map(
          (file) => `
            <li>${file.name}
              <span class="text-body-secondary">(${Math.round(file.size / 1024)} Ko)</span>
            </li>`,
        )
        .join('')
    }
  })

  // ------------------------------------- data-demo / data-toast / data-confirm
  root.addEventListener('click', (event) => {
    const action = event.target.closest('[data-demo],[data-toast],[data-confirm]')
    if (!action) return

    const href = action.getAttribute('href')

    if (action.hasAttribute('data-demo')) {
      event.preventDefault()
      showToast('Action de démonstration : pas encore branchée', 'secondary')
    } else if (action.dataset.toast) {
      event.preventDefault()
      showToast(action.dataset.toast)
      if (href && href != '#') setTimeout(() => go(href), 700)
    } else if (action.dataset.confirm) {
      event.preventDefault()
      showConfirm(action.dataset.confirm, () => {
        showToast('Action confirmée (démonstration)', 'secondary')
        if (href && href != '#') setTimeout(() => go(href), 700)
      })
    }
  })
}
