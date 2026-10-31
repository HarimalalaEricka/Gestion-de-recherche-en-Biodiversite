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

export async function forgotPassword(email) {
  const { data } = await request('auth/mot-de-passe-oublie', {
    method: 'POST',
    body: { email },
    auth: false
  })
  return data
}

export async function resetPassword(token, motDePasse) {
  const { data } = await request('auth/reinitialisation', {
    method: 'POST',
    body: { token, motDePasse },
    auth: false
  })
  return data
}