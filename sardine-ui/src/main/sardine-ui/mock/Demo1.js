'use strict';

const qs = require('qs');
const mockjs = require('mockjs');

let tableListData = {};
if (!global.tableListData) {
  const data = mockjs.mock({
    'data|100': [{
      'id|+1': 1,
      name: '@cname',
      'age|11-99': 1,
      address: '@region'
    }],
    page: {
      total: 100,
      current: 1
    }
  });
  tableListData = data;
  global.tableListData = tableListData;
} else {
  tableListData = global.tableListData;
}

module.exports = {

  'GET /api/demo' (req, res) {
    console.log('HHAHAHHAHA');
    const page = qs.parse(req.query);
    const pageSize = page.pageSize || 20;
    const currentPage = page.page || 1;

    let data;
    let newPage;

    let newData = tableListData.data.concat();

    if (page.field) {
      const d = newData.filter(function (item) {
        return item[page.field].indexOf(decodeURI(page.keyword)) > -1;
      })

      data = d.slice((currentPage - 1) * pageSize, currentPage * pageSize);

      newPage = {
        current: currentPage * 1,
        total: d.length
      }
    } else {
      data = tableListData.data.slice((currentPage - 1) * pageSize, currentPage * pageSize);
      tableListData.page.current = currentPage * 1;
      newPage = {
        current: tableListData.page.current,
        total: tableListData.page.total
      }
    }
    res.json({success: true, data, page: newPage});
  }, 

};