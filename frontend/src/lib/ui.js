// Messages (toasts) et fenêtre de confirmation, construits à la demande.
import { Toast, Modal } from 'bootstrap'
import { translate, translatePage } from './i18n'

export function showToast(message, type = 'success') {
  let container = document.querySelector('#toasts')
  if (!container) {
    container = document.createElement('div')
    container.id = 'toasts'
    container.className = 'toast-container position-fixed bottom-0 end-0 p-3'
    document.body.append(container)
  }

  const toast = document.createElement('div')
  toast.className = `toast align-items-center text-bg-${type} border-0`
  toast.innerHTML = `
    <div class="d-flex">
      <div class="toast-body"></div>
      <button class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Fermer"></button>
    </div>`
  toast.querySelector('.toast-body').textContent = translate(message)
  container.append(toast)
  toast.addEventListener('hidden.bs.toast', () => toast.remove())
  new Toast(toast, { delay: 3000 }).show()
}

export function showConfirm(message, onConfirm) {
  let modal = document.querySelector('#cm')
  if (!modal) {
    document.body.insertAdjacentHTML(
      'beforeend',
      `<div class="modal fade" id="cm" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Confirmer</h5>
              <button class="btn-close" data-bs-dismiss="modal" aria-label="Fermer"></button>
            </div>
            <div class="modal-body" id="cmt"></div>
            <div class="modal-footer">
              <button class="btn btn-outline-secondary" data-bs-dismiss="modal">Annuler</button>
              <button class="btn btn-danger" id="cmok">Confirmer</button>
            </div>
          </div>
        </div>
      </div>`,
    )
    modal = document.querySelector('#cm')
    translatePage(modal)
  }

  document.querySelector('#cmt').textContent = translate(message)
  const instance = Modal.getOrCreateInstance(modal)
  document.querySelector('#cmok').onclick = () => {
    instance.hide()
    onConfirm()
  }
  instance.show()
}
