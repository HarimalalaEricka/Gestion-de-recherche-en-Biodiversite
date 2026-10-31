const BASE_URL = import.meta.env.VITE_API_URL
const TOKEN_KEY = 'token'

function joinApiPath(...parts) {
  return parts
    .filter(p => p !== undefined && p !== null && p !== '')
    .map((p, i) => {
      const s = String(p)
      return i === 0 ? s.replace(/\/+$/, '') : s.replace(/^\/+|\/+$/g, '')
    })
    .join('/')
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}

export function isAuthenticated() {
  const token = getToken()
  if (!token) return false
  try {
    const payload = JSON.parse(atob(token.split('.')[1]))
    return payload.exp * 1000 > Date.now()
  } catch {
    return false
  }
}

export async function request(path, { method = 'GET', body, query, auth = true } = {}) {
  const url = new URL(joinApiPath(BASE_URL, path), window.location.origin)

  if (query) {
    Object.entries(query).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        url.searchParams.append(key, value)
      }
    })
  }

  const headers = { Accept: 'application/json' }
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (auth) {
    const token = getToken()
    if (token) headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(url, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined
  })

  // Token expiré ou invalide : on déconnecte et on renvoie au login
  if (response.status === 401 && auth) {
    removeToken()
    window.location.href = '/login'
    throw new Error('Session expirée, veuillez vous reconnecter')
  }

  if (!response.ok) {
    const text = await response.text()
    throw new Error(`API error ${response.status}: ${text}`)
  }

  const text = await response.text()
  if (!text) return { data: null, response }
  try {
    return { data: JSON.parse(text), response }
  } catch {
    return { data: text, response }
  }
}

export async function getItems(item, filters = {}, params = {}) {
  const query = { ...filters, ...params }

  // Pagination : page (à partir de 1) / perPage → page (à partir de 0) / size de Spring
  if (query.page !== undefined || query.perPage !== undefined) {
    const page = query.page || 1
    const perPage = query.perPage || 10
    query.page = page - 1
    query.size = perPage
    delete query.perPage
  }

  const { data, response } = await request(item, { query })

  // Lecture flexible de la liste
  let items = []
  if (Array.isArray(data)) items = data
  else if (Array.isArray(data?.content)) items = data.content   // Page Spring
  else if (Array.isArray(data?.data)) items = data.data
  else if (Array.isArray(data?.results)) items = data.results

  // Total : Page Spring, sinon header X-Total-Count, sinon taille de la liste
  const total =
    data?.totalElements ??
    parseInt(response.headers.get('X-Total-Count')) ??
    items.length

  return { items, total: Number.isNaN(total) ? items.length : total }
}

export async function getItemById(itemType, itemId) {
  const { data } = await request(joinApiPath(itemType, itemId))
  return data
}

export async function insertItem(itemUrl, data) {
  const { data: result } = await request(itemUrl, { method: 'POST', body: data })
  return result
}

export async function updateItem(itemUrl, data, idItem) {
  const { data: result } = await request(joinApiPath(itemUrl, idItem), {
    method: 'PUT',
    body: data
  })
  return result
}

export async function patchItem(itemUrl, data, idItem) {
  const { data: result } = await request(joinApiPath(itemUrl, idItem), {
    method: 'PATCH',
    body: data
  })
  return result
}

export async function deleteItem(itemType, itemId) {
  const { data } = await request(joinApiPath(itemType, itemId), { method: 'DELETE' })
  return data
}