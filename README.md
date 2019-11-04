## Huhu
该项目是一个基于SpringCloud微服务搭建的问答平台，类似知乎等。目前业务一共拆分成了5个services，分别是
auth-service、mail-service、qa-service、user-service、search-service。
理论上每个服务都应该有独立的数据库，但实际上类似auth-service和user-service这种强相关的服务共用一个用户数据库也是可以的，
否则会出现一些问题。（例如数据不一致，导致认证安全方面的问题等）

这5个服务都是可以独立部署的，有些功能可能以来其他的服务。但这并不影响服务的独立部署能力，仅影响业务。（通过服务熔断和服务降级等手段可以解决功能缺失的问题）


### auth-service
认证服务，也可以理解是认证中心，所有的需要访问权限的接口都必须要先到该用户中心进行用户认证，否则将服务访问接口。
认证服务同时也实现了SSO，及单点登录。

认证服务利用了Spring cloud OAuth2 + Jwt Token的方式来构建，Spring cloud OAuth2比较完整的实现了OAuth2协议，仅需要一些简单的配置我们就可以完成功能。
Jwt是一种有签名机制的token，比随机生成的token会更加安全。且在Jwt中可以携带一些信息，被称作负载payload，即使这样，最好也不要在把一些隐私信息放在token中。

认证服务的基本流程如下：

> 以用户服务为例

[![KjaIQx.md.png](https://s2.ax1x.com/2019/11/03/KjaIQx.md.png)](https://imgchr.com/i/KjaIQx)

> token是否安全一直是一个问题，其实没有绝对的安全，token是存储在客户端本地的，如果这个token都被劫持了，实际上用户的本地计算机已经不存在什么安全性了。


### user-service
用户服务，属于业务类型的服务，几乎每个系统都具备的服务，包含基本的用户注册、查询用户信息、更新用户信息，绑定手机、邮箱等功能。

根据业务的需求不同，其功能会有所差异，例如本项目中由于存在关注和被关注等功能，所以包含了关注、取消关注、获取关注人数目及其对应的简要信息等功能。

### qa-service
问答服务，也是属于业务类型的服务，主要有三个实体，question和answer以及comment，一个question下可以有多个answer和comment，一个answer下可以有
多个comment。

该服务包含了点赞功能的实现，点赞是一个需要保证强一致性的功能，且考虑到点赞通常也具备高并发的特性。
为了保持一致性以及良好的并发性，本项目利用了redis来实现。点赞数据的增加首先会请求到redis服务器，之后隔一段时间（不会很长，2s左右）将数据刷新到mysql
数据库中做持久化存储。

这种方式的好处在于实现简单，依赖redis的高性能以及并发安全性（类似setnx等原子操作可以保持并发性）。坏处在于一段时间后刷新到mysql数据库的压力可能会
暴增，从而导致系统响应缓慢甚至crash。

其他的功能就都是常规的功能，不再赘述。

### search-service
搜索服务，搜索也是很重要的一个特性，本项目利用elasticsearch开源搜索引擎来构建服务。

基本的思想是，ES初始化的时候将数据库中已有的数据批量导入ES（当然之前已经建立好cluster、index等），
而后续需要ES和Mysql同步，当Mysql数据发生改变时，必须及时刷新到ES中，保证ES中数据的实效，防止用户搜索得到过期的数据。实现的手段是监听binlog，
如果binlog发生了改变，立即将修改映射到ES中（具体的实现还是比较麻烦的，详细的请查看代码）。

最后，搜索服务还需要做"联想"，这也是利用ES的suggest API来实现的，suggest API以来分词，为了支持中文，所以需要添加中文分词器，是的suggest支持
中文。除此之外，还可以添加拼音分词器，使得用户输入拼音也可以做"联想"。

### mail-service
邮件服务，由于各个服务都可能需要以邮件的形式通知用户一些讯息，所以将发送邮件单独抽离出来作为一个服务可能会比较好。

独立出来之后，邮件服务可以做更多的灵活的操作，例如多个邮件模板之间的切换等。

以同步的方式发送邮件是一件比较耗时的操作，所以在发送邮件的实现中可以采用异步的方式来发送。异步的方式有很多，最简单的就是利用线程池，其他的就不多说了。

### gateway
网关，理论上上述的几个服务的API都不应该暴露到公网，尤其是IP等重要的信息。以及这几个服务一般都是集群化的，如果
在每个服务之前都单独做一个负载均衡，难免有些冗余了。

基于以上原因，加入了网关来做统一的调度。网关可以隐藏服务的ip地址、端口等信息，而使用服务名来调用服务，
这样将前端和后端服务做了一层隔离。前端不知道也没必要知道究竟后端发生了什么，只需要知道你调用了对应的后端服务，
后端返回给你响应的信息就行。

除此之外，网关还可以做统一的身份校验、负载均衡等。网上相关文章很多，不再赘述了。

## 小结
以上便是本项目的基本概述，如有建议或者意见，请随时联系我，感谢。