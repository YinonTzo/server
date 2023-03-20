package com.company.commands;

import com.company.services.ClientManagerService;
import com.company.services.ExecutionResultService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommandLoader {
    public static Map<String, Command> loadCommands(ClientManagerService clientManagerService,
                                                    ExecutionResultService executionResultService) {

        Map<String, Command> commands = new HashMap<>();

        // Load all command classes in the plugins directory
        File pluginsDir = new File(System.getProperty("user.dir") + "\\src\\main\\java\\com\\company\\plugins");
        File[] pluginFiles = pluginsDir.listFiles();
        if (pluginFiles != null) {
            for (File pluginFile : pluginFiles) {
                try {
                    String className = "com.company.plugins." +
                            pluginFile.getName().replace(".java", "");

                    Class<?> commandClass = Class.forName(className);

                    Constructor<?> constructor =
                            commandClass.getConstructor(ClientManagerService.class, ExecutionResultService.class);

                    Command command = (Command) constructor.newInstance(clientManagerService, executionResultService);
                    commands.put(command.getCommandName(), command);
                } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    log.error("An error occurred while load commands " + e);
                }
            }
        }
        return commands;
    }
}