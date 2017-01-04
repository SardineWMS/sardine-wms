import request from '../utils/request';
import qs from 'qs';

export async function query(params) {
  return request('/api/demo', {
    method: 'get',
    body: qs.stringify(params),
  });
}