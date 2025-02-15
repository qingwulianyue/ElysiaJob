package com.elysia.elysiajob.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface CommandRegister {

    // 指令名称
    String name();
    // 指令别名
    String[] aliases() default {};
    // 执行/Tab所需权限
    String permission() default "";
    // 描述
    String desc();
    // 用法
    String usage();

    // 指令层次,1就是主命令下的最高层命令
    int level() default 1;
    // parentName 代表父命令名称
    String parentName() default "";
    // tab执行方法,必须在同类中且参数必须只能有一个CommandExecuteInfo
    String tabExecuteMethod() default "";

}
