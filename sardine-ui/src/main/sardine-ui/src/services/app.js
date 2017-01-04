import request from '../utils/request';
import qs from 'qs'

export async function login(params) {
  return request('/api/login', {
    method: 'post',
    body : qs.stringify(params),
  })
}

export async function logout(params) {
  return request('/api/logout', {
    method: 'post',
    body : qs.stringify(params),
  })
}

export async function signIn(params) {
  return request('/api/signIn', {
    method: 'post',
    body : qs.stringify(params),
  })
}

export async function userInfo(params) {
  return request('/api/userInfo', {
    method: 'get',
    body : qs.stringify(params),
  })
}
