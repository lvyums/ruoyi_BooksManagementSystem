import request from '@/utils/request'

// 查询借阅记录列表
export function listBorrow(query) {
  return request({
    url: '/library/borrow/list',
    method: 'get',
    params: query
  })
}

// 查询借阅记录详细
export function getBorrow(BorrowID) {
  return request({
    url: '/library/borrow/' + BorrowID,
    method: 'get'
  })
}

// 新增借阅记录
export function addBorrow(data) {
  return request({
    url: '/library/borrow',
    method: 'post',
    data: data
  })
}

// 修改借阅记录
export function updateBorrow(data) {
  return request({
    url: '/library/borrow',
    method: 'put',
    data: data
  })
}

// 删除借阅记录
export function delBorrow(BorrowID) {
  return request({
    url: '/library/borrow/' + BorrowID,
    method: 'delete'
  })
}
// 办理借书
export function lendBook(rdID, bkID) {
  return request({
    url: '/library/borrow/lend', // <--- 必须对应 Controller 里的 @PostMapping("/lend")
    method: 'post',
    params: { rdID, bkID }       // <--- 对应 Controller 里的 @RequestParam
  })
}
// 办理续借
export function renewBook(borrowID) {
  return request({
    url: '/library/borrow/renew/' + borrowID,
    method: 'post'
  })
}

// 办理还书
export function returnBook(borrowID, isLost) {
  return request({
    url: '/library/borrow/return',
    method: 'post',
    params: { borrowID, isLost }
  })
}
