import request from '@/utils/request'

// 查询读者类别列表
export function listReadertype(query) {
  return request({
    url: '/library/readertype/list',
    method: 'get',
    params: query
  })
}

// 查询读者类别详细
export function getReadertype(rdType) {
  return request({
    url: '/library/readertype/' + rdType,
    method: 'get'
  })
}

// 新增读者类别
export function addReadertype(data) {
  return request({
    url: '/library/readertype',
    method: 'post',
    data: data
  })
}

// 修改读者类别
export function updateReadertype(data) {
  return request({
    url: '/library/readertype',
    method: 'put',
    data: data
  })
}

// 删除读者类别
export function delReadertype(rdType) {
  return request({
    url: '/library/readertype/' + rdType,
    method: 'delete'
  })
}
