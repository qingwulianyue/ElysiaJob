package com.elysia.elysiajob.command.wrappers;


import com.elysia.elysiajob.command.annotations.CommandRegister;
import com.elysia.elysiajob.command.interfaces.AbstractCommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 指令包装
public class CommandWrapped {

    // 由handler提供
    private final AbstractCommandHandler handler;
    // 指令设定信息
    private final CommandRegister commandRegister;
    // 执行方法
    private final Method method;
    // 执行Tab方法 (必须在同类中)
    private final Method tabMethod;

    @Override
    public String toString() {
        return "CommandWrapped{" +
                "commandRegister=" + commandRegister +
                ", method=" + method +
                ", tabMethod=" + tabMethod +
                '}';
    }

    public CommandWrapped(AbstractCommandHandler handler,CommandRegister commandRegister, Method method) {
        this.commandRegister = commandRegister;
        this.method = method;
        this.handler = handler;
        if (this.method != null){
            this.method.setAccessible(true);
        }
        this.tabMethod = null;
    }

    public CommandWrapped(AbstractCommandHandler handler,CommandRegister commandRegister, Method method,Method tabMethod) {
        this.commandRegister = commandRegister;
        this.method = method;
        this.handler = handler;
        if (this.method != null){
            this.method.setAccessible(true);
        }
        this.tabMethod = tabMethod;
        if (this.tabMethod != null){
            this.tabMethod.setAccessible(true);
        }
    }

    public void execute(CommandExecuteInfo info){
        if (!this.commandRegister.permission().isEmpty() && info.getSender().hasPermission(this.commandRegister.permission())){
            // TODO getHandler
            try {
                this.method.invoke(Modifier.isStatic(this.method.getModifiers()) ? null : handler,info);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public List<String> executeTab(CommandExecuteInfo info){
        if (!this.commandRegister.permission().isEmpty() && info.getSender().hasPermission(this.commandRegister.permission())){
            // TODO getHandler

            Map<String, CommandWrapped> map = handler.getSubCommandChainMap()
                    .get(this);
            if (!map.isEmpty()){
                return map.values()
                        .stream()
                        .map(it -> it.getCommandRegister().name())
                        .collect(Collectors.toList());
            }

            if (this.tabMethod == null){
                return Collections.emptyList();
            }

            try {
                return (List<String>) this.tabMethod.invoke(Modifier.isStatic(this.tabMethod.getModifiers()) ? null : handler,info);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return Collections.emptyList();
    }

    public CommandRegister getCommandRegister() {
        return commandRegister;
    }

    public Method getMethod() {
        return method;
    }
}
