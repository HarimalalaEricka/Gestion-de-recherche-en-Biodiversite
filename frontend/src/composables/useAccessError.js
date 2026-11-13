import { useRouter } from 'vue-router'

export function useAccessError() {
    const router = useRouter()

    // Ton api.js lève "API error 404: ...", on lit le code dans le message
    // (si tu ajoutes err.status dans api.js, il sera utilisé en priorité)
    function statutDe(e) {
        if (e?.status) return e.status
        const m = /API error (\d+)/.exec(e?.message || '')
        return m ? Number(m[1]) : null
    }

    // Retourne true si l'erreur a été gérée par une redirection
    function gererAcces(e) {
        const statut = statutDe(e)

        if (statut === 404) {
            router.replace('/erreurs/page-introuvable')
            return true
        }
        if (statut === 403) {
            router.replace('/erreurs/acces-refuse')
            return true
        }
        return false
    }

    return { gererAcces }
}