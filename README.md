# 背景
公司就我一个程序员了,服务器资源也紧张，客户也少。

目标是将lilishop项目改为单体，方便小型商户场景的开发、运维。


初始化git地址
https://gitee.com/beijing_hongye_huicheng/lilishop/commit/8fcc5ebfd3d6bf125b58b07b5223853473adcd62


# TODO
我的数据库是mysql8，支持全文搜索。 使用mysql8的特性来替代 es


# 记录下怎么修改的
部分工作由trae ai完成

先合并代码
去掉redis，改为内存缓存
整合安全认证模块


去掉xxl-job
ai: 去掉xxl-job,改为springboot的任务调度


去掉es
 
移除elasticsearch，必要的实体使用jpa替代

加入jpa依赖


然后手动修改mysql8的全文搜索特性

 去掉mq
ai-plan：使用jpa实现一个简单的消息队列，目标是替换项目的mq， 要求类名简洁直观
     

去掉项目并未使用的shardingsphere依赖

去掉Quartz， 使用定时注解，（好像就敏感词用）

敏感词管理变为实时更新，去掉原先的Quartz+ 缓存


增加ui界面