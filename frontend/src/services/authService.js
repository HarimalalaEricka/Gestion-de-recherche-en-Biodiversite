import { request, setToken, removeToken } from './api.js'


export async function login(email, motDePasse) {
  const { data } = await request('/auth/login', {
    method: 'POST',
    body: { email, motDePasse },
    auth: false
  })
  setToken(data.token)
  return data 
}

export function logout() {
  removeToken()
  window.location.href = '/login'
}

export async function getCurrentUser() {
  const { data } = await request('api/auth/me')
  return data
}
