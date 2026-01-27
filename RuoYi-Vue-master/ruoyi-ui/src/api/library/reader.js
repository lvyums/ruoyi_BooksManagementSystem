import request from '@/utils/request'

// 查询读者信息列表
export function listReader(query) {
  return request({
    url: '/library/reader/list',
    method: 'get',
    params: query
  })
}

// 查询读者信息详细
export function getReader(rdID) {
  return request({
    url: '/library/reader/' + rdID,
    method: 'get'
  })
}

// 新增读者信息
export function addReader(data) {
  return request({
    url: '/library/reader',
    method: 'post',
    data: data
  })
}

// 修改读者信息
export function updateReader(data) {
  return request({
    url: '/library/reader',
    method: 'put',
    data: data
  })
}

// 删除读者信息
export function delReader(rdID) {
  return request({
    url: '/library/reader/' + rdID,
    method: 'delete'
  })
}

// 补办借书证
export function reissueReader(rdID) {
  return request({
    url: '/library/reader/reissue/' + rdID,
    method: 'post'
  })
}
