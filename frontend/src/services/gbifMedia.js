import { getItems, insertItem } from './api'

const GBIF_API = (import.meta.env.VITE_GBIF_API_URL || '').replace(/\/$/, '')

// Les colonnes texte ont peut-être une limite (souvent 255)
const coupe = (s, n = 250) => (s ? String(s).slice(0, n) : null)

// Photos d'un taxon chez GBIF (images uniquement)
export async function fetchGbifImages(gbifKey, max = 6) {
  if (!GBIF_API || !gbifKey) return []

  const res = await fetch(`${GBIF_API}/species/${gbifKey}/media?limit=20`)
  if (!res.ok) throw new Error(`GBIF HTTP ${res.status}`)

  const { results = [] } = await res.json()

  return results
    .filter((r) => r.type === 'StillImage' && /^https?:\/\//.test(r.identifier ?? ''))
    .slice(0, max)
}

// Médias déjà enregistrés pour ce taxon
export async function listerMediasTaxon(taxonID) {
  const { items } = await getItems(
    'medias',
    { 'taxon.taxonID': taxonID },
    { page: 1, perPage: 50 }
  )
  // Sécurité tant que le filtre backend n'est pas corrigé
  return items.filter((m) => m.taxon?.taxonID === taxonID)
}

// Enregistre les photos GBIF dans la table media, rattachées au taxon
export async function importerMediasTaxon(taxonID, gbifKey, max = 6) {
  const images = await fetchGbifImages(gbifKey, max)

  const resultats = await Promise.allSettled(
    images.map(async (r) => {
      const payload = {
        accessURI: r.identifier,
        typeMedia: 'IMAGE',
        caption: coupe(r.title),
        captureDevice: null,
        comments: coupe(
          [
            r.creator && `Auteur : ${r.creator}`,
            r.rightsHolder && `Titulaire : ${r.rightsHolder}`,
            r.license && `Licence : ${r.license}`,
            'Source : GBIF'
          ].filter(Boolean).join(' · ')
        ),
        taxon: { taxonID },
        occurrence: null
      }

      const cree = await insertItem('medias', payload)
      return { ...payload, ...cree }
    })
  )

  for (const r of resultats) {
    if (r.status === 'rejected') console.warn('Média GBIF non enregistré :', r.reason)
  }

  return resultats.filter((r) => r.status === 'fulfilled').map((r) => r.value)
}

// Importe seulement si le taxon n'a encore aucun média
export async function assurerMediasTaxon(taxonID, gbifKey) {
  const existants = await listerMediasTaxon(taxonID)
  if (existants.length > 0) return existants
  return importerMediasTaxon(taxonID, gbifKey)
}