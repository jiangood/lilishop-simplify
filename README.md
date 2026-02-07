目标是将lilishop项目改为单体，方便小型商户部署

初始化git地址
https://gitee.com/beijing_hongye_huicheng/lilishop/commit/8fcc5ebfd3d6bf125b58b07b5223853473adcd62



# 记录下怎么修改的
- 先合并代码
- 去掉redis，改为内存缓存
- 整合安全认证模块
- 去掉mq
- 去掉xxl-job
- 去掉es