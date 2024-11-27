"# mini-spring-framework" 

一个简易版本的spring 里面实现了 IOC AOP
主要代码在MiniSpringApplicationContext中
同时也解决了循环依赖的问题 

里面主要借鉴了spring的三级缓存 不过我并没有用到第三级缓存通过lambda构造代理对象
而是通过第二级缓存直接生成实例化对象bean