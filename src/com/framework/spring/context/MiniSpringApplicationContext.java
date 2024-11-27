package com.framework.spring;

import com.framework.spring.annotation.Autowired;
import com.framework.spring.annotation.Component;
import com.framework.spring.annotation.ComponentScan;
import com.framework.spring.annotation.Scope;
import com.framework.spring.bean.BeanDefinition;
import com.framework.spring.bean.BeanNameAware;
import com.framework.spring.bean.BeanPostProcessor;
import com.framework.spring.bean.InitializingBean;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MiniSpringApplicationContext {
    private Class config;

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();
    private ArrayList<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public MiniSpringApplicationContext(Class config) {
        this.config = config;

        //扫描component容器
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan annotations = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String path = annotations.value();  //获取到扫描路径 com.framework.service
            path = path.replace(".","/");

            ClassLoader classLoader = MiniSpringApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
//            System.out.println(resource.toString());

            File file = new File(resource.getFile());
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath(); //获取文件的绝对路径
                    if (fileName.endsWith(".class")){
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("\\", ".");
                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Component.class)){

                                if (BeanPostProcessor.class.isAssignableFrom(clazz)){
                                    BeanPostProcessor instance = (BeanPostProcessor)clazz.newInstance();
                                    beanPostProcessorList.add(instance);
                                }

                                //获取bean的名称
                                String beanName = clazz.getAnnotation(Component.class).value();
                                if (beanName.equals("")){
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }

                                //生成beanDefinition
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);
                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope annotation = clazz.getAnnotation(Scope.class);
                                    String scope = annotation.value();
                                    beanDefinition.setScope(scope);
                                }else{
                                    beanDefinition.setScope("singleton");
                                }
                                beanDefinitionMap.put(beanName,beanDefinition);
                            }

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        }


        //实例化单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())){
                //创建bean
                System.out.println("创建单例bean:"+beanName+"成功");
                Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object instance = clazz.getConstructor().newInstance();

            //依赖注入
            for (Field f : clazz.getDeclaredFields()) { //给所有加了@Autowired的属性赋值
                if (f.isAnnotationPresent(Autowired.class)) {
                    f.setAccessible(true); //改成可反射
                    f.set(instance,getBean(f.getName()));
                }
            }

            //beanName回调
            if (instance instanceof BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            //AOP 面向切面 前置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessBeforeInitialization(beanName,instance);
            }

            //初始化
            if (instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            //AOP 面向切面 后置处理器
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getBean(String beanName){
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition==null){
            throw new NullPointerException();
        }else{
            String scope = beanDefinition.getScope();

            if ("singleton".equals(scope)){
                //单例bean
                Object bean = singletonObjects.get(beanName);
                if (bean == null){
                    bean = createBean(beanName, beanDefinition);
                    singletonObjects.put(beanName,bean);
                }
                return bean;
            }else{
                //多例bean
                System.out.println("创建多例bean:"+beanName+"成功");
                return createBean(beanName,beanDefinition);
            }
        }
    }
}
