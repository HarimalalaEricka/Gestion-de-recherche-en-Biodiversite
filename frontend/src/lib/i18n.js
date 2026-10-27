// Traduction français -> anglais.
// La clé du dictionnaire (i18n/en.json) est le texte français affiché dans les pages.
// Le changement de langue recharge la page, comme dans le template d'origine.
import en from '../i18n/en.json'

export const language = localStorage.getItem('lang') || 'fr'

export const translate = (text) => (language === 'en' && en[text]) || text

export function toggleLanguage() {
  localStorage.setItem('lang', language === 'en' ? 'fr' : 'en')
  location.reload()
}

const ATTRIBUTES = ['placeholder', 'title', 'aria-label', 'data-confirm', 'data-toast']

/** Traduit les textes et attributs d'un élément (les vues sont du HTML statique). */
export function translatePage(root) {
  if (language !== 'en') return

  const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT)
  const nodes = []
  let node
  while ((node = walker.nextNode())) nodes.push(node)

  nodes.forEach((textNode) => {
    const text = textNode.nodeValue.trim()
    if (text && en[text]) textNode.nodeValue = textNode.nodeValue.replace(text, en[text])
  })

  ;[root, ...root.querySelectorAll('*')].forEach((element) => {
    ATTRIBUTES.forEach((attribute) => {
      const value = element.getAttribute && element.getAttribute(attribute)
      if (value && en[value]) element.setAttribute(attribute, en[value])
    })
  })
}
