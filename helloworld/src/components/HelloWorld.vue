<template>
  <section>
    <el-button type='primary' @click='uploadFileBtnClicked'>上传本地文件</el-button>
    <el-dialog title='上传本地文件' width="40%" :visible.sync='dialogVisible' :before-close="handleClose"
      :close-on-click-modal='false'>
      <el-upload ref='upload' action='a' multiple accept=".xls,.xlsx" :file-list='fileList' :auto-upload="false"
        :on-change="handleChange" :http-request="uploadFile" :before-upload="beforeUpload">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <div slot="tip" class="el-upload__tip">只能上传excel文件</div>
      </el-upload>
    </el-dialog>
  </section>
</template>
<script>
import app from '../common/js/app'
import apiDomain from '../common/js/apiDomain'
export default {
  data: function () {
    return {
      onceUploadCount: 10, // 一次上传文件的数量
      fileDataList: [],
      paramList: [],
      uploadedFileCount: 0, // 已经上传的文件的数量
      username: 'admin',
      dialogVisible: false,
      fileList: []
    }
  },
  name: 'app',
  mounted () {
    this.initWebSocket()
  },
  destroyed () {
    this.closeWebSocket()
  },
  methods: {
    initWebSocket () {
      if ('WebSocket' in window) {
        this.websocket = new WebSocket('ws://' + apiDomain.ip + '/websocket')
        // 连接错误
        this.websocket.onerror = this.setErrorMessage
        // 连接成功
        this.websocket.onopen = this.setOnopenMessage
        // 收到消息的回调
        this.websocket.onmessage = this.setOnmessageMessage
        // 连接关闭的回调
        this.websocket.onclose = this.setOncloseMessage
        // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = this.onbeforeunload
      } else {
        alert('当前浏览器 Not support websocket')
      }
    },
    setErrorMessage () {
    },
    setOnopenMessage () {
      this.send(this.username + '_' + this.uuid())
    },
    setOnmessageMessage (event) {
      this.handleFileUploadResult(event.data)
    },
    setOncloseMessage () {
    },
    onbeforeunload () {
      this.websocket.close()
    },
    // websocket发送消息
    send (msg) {
      this.websocket.send(msg)
    },
    closeWebSocket () {
      if (this.websocket) {
        this.websocket.close()
      }
    },
    handleFileUploadResult (result) {
      const data = JSON.parse(result)
      const msg = data.msg
      var param = null
      this.paramList.forEach(element => {
        if (msg === element.file.name) {
          param = element
        }
      })
      if (data.code === 0) {
        this.uploadedFileCount++
        if (this.uploadedFileCount % this.onceUploadCount === 0) {
          this.appendFileAndSendFile(this.uploadedFileCount)
        }
        this.$message({
          message: data.msg + ' 上传成功',
          type: 'success'
        })
        param.onSuccess()
      } else {
        param.onError()
        this.$message({
          message: data.msg + ' 上传失败',
          type: 'error'
        })
        this.uploadedFileCount++
        if (this.uploadedFileCount % this.onceUploadCount === 0) {
          this.appendFileAndSendFile(this.uploadedFileCount)
        }
      }
    },
    uuid () {
      var s = []
      var hexDigits = '0123456789abcdef'
      for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1)
      }
      s[14] = '4' // bits 12-15 of the time_hi_and_version field to 0010
      s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1) // bits 6-7 of the clock_seq_hi_and_reserved to 01
      s[8] = s[13] = s[18] = s[23] = '-'

      var uuid = s.join('')
      return uuid
    },
    // 上传文件按钮被点击后，显示上传文件的提示框
    uploadFileBtnClicked () {
      this.dialogVisible = true
    },
    submitUpload () {
      this.paramList = []
      this.fileDataList = []
      this.uploadedFileCount = 0
      if (this.fileList.length === 0) {
        this.$message({
          message: '请先选取文件',
          type: 'warning'
        })
      } else {
        this.$refs.upload.submit()
        if (this.paramList.length === 0) {
          return
        }
        this.appendFileAndSendFile(0)
      }
    },
    appendFileAndSendFile (uploadedFileCount) {
      var loopTimes = this.onceUploadCount + uploadedFileCount
      if (loopTimes > this.fileDataList.length) {
        loopTimes = this.fileDataList.length
      }
      const fileData = new FormData()
      for (let i = uploadedFileCount; i < loopTimes; i++) {
        const element = this.fileDataList[i]
        fileData.append('files', element)
      }
      app.uploadFile('upload_initialdata_file', fileData).then(data => {
      }).catch(response => {
      })
    },
    // 上传文件
    uploadFile (param) {
      this.paramList.push(param)
      this.fileDataList.push(param.file)
    },
    beforeUpload (file) {
      const isLt10M = file.size / 1024 / 1024 < 10 // 这里做文件大小限制
      if (!isLt10M) {
        this.$message({
          message: '上传文件须小于 10MB!',
          type: 'error'
        })
        this.uploadedFileCount++
      }
      const isGreaterThan110k = file.size / 1024 > 110 // 这里做文件大小限制
      if (!isGreaterThan110k) {
        this.$message({
          message: '上传文件数据量过少!',
          type: 'error'
        })
        this.uploadedFileCount++
      }
      return isLt10M && isGreaterThan110k
    },
    // 显示上传的文件
    handleChange (file, fileList) {
      this.fileList = fileList
    },
    close () {
      this.fileList = []
    },
    handleClose (done) {
      this.close()
      done()
    }
  }
}
</script>
<style >

</style>
