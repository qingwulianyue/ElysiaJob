package com.elysia.elysiajob.command.interfaces;

import com.elysia.elysiajob.command.annotations.CommandRegister;
import com.elysia.elysiajob.command.wrappers.CommandExecuteInfo;
import com.elysia.elysiajob.command.wrappers.CommandInvoker;
import com.elysia.elysiajob.command.wrappers.CommandWrapped;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommandHandler {

    private final CommandWrapped mainCommand;

    public Map<CommandWrapped, Map<String, CommandWrapped>> getSubCommandChainMap() {
        return subCommandChainMap;
    }

    // 构造器
    // 加载mainCommand和本类中的子指令
    public AbstractCommandHandler() {
        CommandRegister annotation = this.getClass()
                .getAnnotation(CommandRegister.class);
        if (annotation != null){
            this.mainCommand = new CommandWrapped(this,annotation,null);
        }else{
            this.mainCommand = null;
        }
        addSubCommand(this);
    }

    // 指令父子表
    // key -> value
    // 父 -> List<子>
    private final Map<CommandWrapped, Map<String,CommandWrapped>> subCommandChainMap = new HashMap<>();

    // 读取并添加handler中的指令
    public AbstractCommandHandler addSubCommand(AbstractCommandHandler... handlers){
        // TODO

        List<CommandWrapped> commandWrappedList = new ArrayList<>();

        for (AbstractCommandHandler handler : handlers){
            Class<? extends AbstractCommandHandler> clz = handler.getClass();
            for (Method method : clz.getDeclaredMethods()){
                CommandRegister annotation = method.getAnnotation(CommandRegister.class);
                if (annotation != null){
                    Method tabMethod = null;
                    if (!annotation.tabExecuteMethod().isEmpty()){
                        try {
                            tabMethod = clz.getMethod(annotation.tabExecuteMethod(), CommandExecuteInfo.class);
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    commandWrappedList.add(new CommandWrapped(handler,annotation,method,tabMethod));
                }
            }
        }

        List<CommandWrapped> sortedWrappedCommands = commandWrappedList
                .stream()
                .sorted(Comparator.comparingInt(it -> it.getCommandRegister().level()))
                .collect(Collectors.toList());


        for (CommandWrapped wrapped : sortedWrappedCommands){
            subCommandChainMap.put(wrapped,new HashMap<>());
            String parentName = wrapped.getCommandRegister().parentName();
            if (!parentName.isEmpty()){
                int parentLevel = wrapped.getCommandRegister().level() - 1;
                CommandWrapped commandWrapped = subCommandChainMap.keySet()
                        .stream()
                        .filter(it -> it.getCommandRegister().name().equals(parentName) && it.getCommandRegister().level() == parentLevel)
                        .findAny().orElse(null);
                if (commandWrapped != null){
                    Map<String, CommandWrapped> map = subCommandChainMap.get(commandWrapped);
                    map.put(wrapped.getCommandRegister().name(),wrapped);
                }
            }

        }
        return this;

    }

    // 指令分段读取
    // 返回每层有效的指令
    public Map<Integer,CommandWrapped> getCommandWrapped(CommandExecuteInfo info){
        String[] args = info.getArgs();
        Map<Integer,CommandWrapped> wrappedMap = new HashMap<>();
        int level = 1;
        for (String nameOrAlias : args){
            int finalLevel = level;
            CommandWrapped commandWrapped = this.subCommandChainMap.keySet()
                    .stream()
                    .filter(
                            it ->
                            (it.getCommandRegister().name().equalsIgnoreCase(nameOrAlias) || ArrayUtils.contains(it.getCommandRegister().aliases(), nameOrAlias))
                                    && finalLevel == it.getCommandRegister().level()
                                    && (it.getCommandRegister().parentName().isEmpty() || wrappedMap.get(finalLevel - 1).getCommandRegister().name().equalsIgnoreCase(it.getCommandRegister().parentName()))
                    )
                    .findAny().orElse(null);
            if (commandWrapped != null){
                wrappedMap.put(level,commandWrapped);
            }
            level++;
        }
        return wrappedMap;
    }


    // 在Bukkit中注册指令
    public void register(JavaPlugin plugin){

        CommandRegister annotation = this.getClass().getAnnotation(CommandRegister.class);

        if (annotation == null){
            return;
        }



        try {
            Constructor<PluginCommand> declaredConstructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            declaredConstructor.setAccessible(true);

            try {
                PluginCommand command = declaredConstructor.newInstance(annotation.name(), plugin);

                command.setAliases(new ArrayList<>(Arrays.asList(annotation.aliases())));
                if (!annotation.permission().isEmpty()){
                    command.setPermission(annotation.permission());
                }

                command.setDescription(annotation.desc());
                command.setUsage(annotation.usage());

                command.setExecutor(new CommandInvoker(this));

                Field field;
                try {

                    field = SimplePluginManager.class.getDeclaredField("commandMap");
                    field.setAccessible(true);
                    CommandMap commandMap = (CommandMap) field.get(plugin.getServer().getPluginManager());

                    commandMap.register(annotation.name(),command);


                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }

            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // 可覆写
    // 发送帮助信息方法
    public void sendHelpMessage(CommandExecuteInfo info,CommandWrapped wrapped){

        info.sendMessage(" ");
        info.sendMessage("  &6命令: &f" + wrapped.getCommandRegister().name());
        info.sendMessage("  &6别名: &f" + Arrays.toString(wrapped.getCommandRegister().aliases()));
        info.sendMessage("  &6描述: &f" + wrapped.getCommandRegister().desc());
        info.sendMessage("  &6用法: &f" + wrapped.getCommandRegister().usage()
                .replace("@parent-command",wrapped.getCommandRegister().parentName().isEmpty() ? mainCommand.getCommandRegister().name() : wrapped.getCommandRegister().parentName())
                .replace("@command",wrapped.getCommandRegister().name())
                .replace("@main-command",mainCommand.getCommandRegister().name())
        );
    }

    // 可覆写
    // Args为空时执行
    public void executeWithNoArg(CommandExecuteInfo info){
        info.sendMessage(mainCommand.getCommandRegister().name() + " &6命令帮助");
        info.sendMessage(" ");

        if (info.getArgs().length == 0){
            for (CommandWrapped wrapped : getHighestCommandWrapped()){
                sendHelpMessage(info,wrapped);
            }
        }else if (info.getArgs().length == 1){
            getHighestCommandWrapped().stream()
                    .filter(it -> it.getCommandRegister().name().toLowerCase().startsWith(info.getArgs()[0].toLowerCase()))
                    .forEach(it -> sendHelpMessage(info,it));
        }else{
            Map<Integer, CommandWrapped> commandWrappedMap = getCommandWrapped(info);
            
            if (!commandWrappedMap.isEmpty()){
                Integer max = commandWrappedMap.keySet()
                        .stream()
                        .max(Comparator.comparingInt(it -> it))
                        .orElse(null);
                CommandWrapped lastCommandWrapped = commandWrappedMap.get(max);
                sendHelpMessage(info,lastCommandWrapped);
            }
        }
        info.sendMessage(" ");

    }

    // 可覆写
    // 没有匹配的指令时执行
    public void executeWithNoMatchSubCommand(CommandExecuteInfo info){
        executeWithNoArg(info);
    }

    // 获取最高层子指令 (level == 1)
    public List<CommandWrapped> getHighestCommandWrapped(){
        return subCommandChainMap.keySet()
                .stream()
                .filter(it -> it.getCommandRegister().level() == 1)
                .collect(Collectors.toList());
    }
    public List<CommandWrapped> getLevelCommandWrapped(String parent, int level){
        return subCommandChainMap.keySet()
                .stream()
                .filter(it -> it.getCommandRegister().level() == level && it.getCommandRegister().parentName().equalsIgnoreCase(parent))
                .collect(Collectors.toList());
    }

}
