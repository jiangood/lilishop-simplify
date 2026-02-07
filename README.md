# 背景
公司就我一个程序员了,服务器资源也紧张，客户也少。

目标是将lilishop项目改为单体，方便小型商户场景的开发、运维。 


初始化git地址
https://gitee.com/beijing_hongye_huicheng/lilishop/commit/8fcc5ebfd3d6bf125b58b07b5223853473adcd62




# 记录下怎么修改的
部分工作由trae ai完成

## 先合并代码
## 去掉redis，改为内存缓存
## 整合安全认证模块
##去掉mq
 ai：去掉mq，改为内存实现
- 去掉xxl-job
## 去掉es
ai： 
加入jpa依赖
新开窗口：去掉elasticsearch，直接查数据库即可
     
