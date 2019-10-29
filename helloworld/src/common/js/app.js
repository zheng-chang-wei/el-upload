import axios from 'axios'
import allApi from './allApi'
import qs from 'qs'
axios.defaults.baseURL = 'api'
axios.defaults.withCredentials = true
axios.interceptors.response.use(res => {
  return res.data
}, error => {
  console.log('err')
  return Promise.reject(error)
})

export default {
  get (url, data) {
    return axios.get(allApi[url], { params: data })
  },
  post (url, data) {
    return axios.post(allApi[url], qs.stringify(data))
  },
  uploadFile (url, data) {
    return axios.post(allApi[url], data)
  },
  getUrl (url) {
    return allApi[url]
  }
}
