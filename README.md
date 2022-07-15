# SpringBootListener
利用切面、注解、反射实现SpringBoot的事件监听


测试结果如下

![image](https://user-images.githubusercontent.com/92293323/179234024-f8bd014e-e073-4872-97dd-c5ba39178afe.png)


# 前言
当某个事件需要被监听的时候，我们需要去做其他的事前，最简单的方式就是将自己的业务 方法追加到该事件之后。
但是当有N多个这样的需求的时候我们都这样一个个去添加修改事件的源码吗？
**这篇文章将告诉你如何用一个注解，就可以将你的业务代码通过切面的方式添加到事件的前后，而不需要修改事件的代码**

# 效果图
如下图所示，add方法内并没有调用其他的方法，但是其他方法仍然被执行了。
只要给监听方法加@AddEventListener()注解就可以让它在事件前后执行了
![在这里插入图片描述](https://img-blog.csdnimg.cn/72b9224146234e1b8e57f1647270502f.png)

![在这里插入图片描述](https://img-blog.csdnimg.cn/7001e32e2a3b413dbeda85c34b41454e.png)

# 监听原理
该方法是利用切面、注解、反射来实现SpringBoot的事件监听的
## 1.通过Aspect的切面，切入事件方法
首先使用Aspec的Around（也可以用before或者after，但是比较麻烦）注解，切入AddEvent的方法中，around注解的方法中，可以在事件方法的执行前后添加业务代码。但是我们不直接加入需要添加的业务，进入第二步骤。
## 2.利用反射获取被AddEventAop注解的类和方法
利用反射Class.forName(class)，获取被AddEventAop注解的类（当然你也可以修改一下，获取所有的类），该类哪个方法被AddEventListener注解了，就执行该方法，则监听执行成功。

```java
method.invoke(o, args);
```
## 注意（非常重要）

 - AddEventListener使用的类上，必须被AddEventAop注解了，否则反射的时候方法不会被执行。
 - 事件的类必须是bean，否则切面失败。
 - 监听方法和（被监听方法）事件方法的参数数量，类型，顺序必须一致，否则可能导致反射执行方法失败


##  核心源码
```java
    @Around("@annotation(event)")
    public Object addEventListener(ProceedingJoinPoint joinPoint, AddEventAop event) throws Throwable {
        Object[] args = joinPoint.getArgs();
        //存储需要在方法执行之后再执行的类
        List<Method> afterEventMethod = new ArrayList<>();

        //反射获取AddEventListener修饰的方法并执行
        //获取自定义注解的配置
        final Map<String, Object> beans = applicationContext.getBeansWithAnnotation(AddEventAop.class);
        for (String key : beans.keySet()) {
            //Spring 代理类导致Method无法获取,这里使用AopUtils.getTargetClass()方法
            Class<?> aClass = beans.get(key).getClass();
            String name = aClass.getName();
            //aop切面会导致方法注解丢失，在这里处理获取原类名
            if (name.contains("$$")){
                String[] names = name.split("\\$\\$");
                name=names[0];
                aClass = Class.forName(name);
            }
            Object o = aClass.newInstance();
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                //获取指定方法上的注解的属性
                AddEventListener annotation = method.getAnnotation(AddEventListener.class);
                if (annotation!=null){
                    //执行所有的注解了该类的方法
                    EventType value = annotation.value();
                    if (value.equals(EventType.BEFOREEVENT)){
                        method.invoke(o, args);
                    }else{
                       afterEventMethod.add(method);
                    }
                }
            }
        }

        //执行被切面的方法
        Object proceed = joinPoint.proceed(args);

        //执行需要在方法执行之后再执行的方法
        for (Method method : afterEventMethod) {
            Class<?> aClass = method.getDeclaringClass();
            Object o = aClass.newInstance();
            method.invoke(o, args);
        }
        return proceed;
    }


}

```


