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


### TODO
