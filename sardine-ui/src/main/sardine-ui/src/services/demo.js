import request from '../utils/request';
import qs from 'qs';

export async function query(params) {
  return request(`/api/users?${qs.stringify(params)}`);
}

export async function login(params) {
  console.log('service ');
  return request(`/api/ia/authen/login?${qs.stringify(params)}`);
}

export async function register(params) {
  console.log('注册用户 ');
  return request('/api/register', {
    method: 'post',
    body: qs.stringify(params),
  });
}

export async function create(params) {
  return request('/api/users', {
    method: 'post',
    body: qs.stringify(params),
  });
}

export async function remove(params) {
  return request('/api/users', {
    method: 'delete',
    body: qs.stringify(params),
  });
}

export async function update(params) {
  return request('/api/users', {
    method: 'put',
    body: qs.stringify(params),
  });
}