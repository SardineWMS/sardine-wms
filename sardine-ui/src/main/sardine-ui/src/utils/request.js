import fetch from 'dva/fetch';
// const Ajax = require("robe-ajax");

function parseJSON(response) {
  return response.json();
}

function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }

  const error = new Error(response.statusText);
  error.response = response;
  throw error;
}

/*export default function request(url, options) {
  return fetch(url, options)
    .then(checkStatus)
    .then(parseJSON)
    .then((data) => ({ data }))
    .catch((err) => ({ err }));
}*/

export default function request(url, options) {
/*  if (options.cross) {
    return Ajax.getJSON("http://query.yahooapis.com/v1/public/yql", {
      q: "select * from json where url=\'"+url+"?"+Ajax.param(options.data)+"\'",
      format: "json"
    })
  } else {*/
    return fetch(url, options)
    .then(checkStatus)
    .then(parseJSON)
    .then((data) => ({ data }))
    .catch((err) => ({ err }));
//}
}

