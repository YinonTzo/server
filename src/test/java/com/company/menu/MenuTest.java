package com.company.menu;

import com.company.commands.Command;
import com.company.commands.menu.Menu;
import com.company.common.messages.CLIToServer.BaseCLIToServer;
import com.company.common.messages.serverToCLI.BaseServerToCLI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MenuTest {

    public static final String COMMAND1 = "command1";
    public static final String COMMAND2 = "command2";

    private Menu menu;
    private Map<String, Command> commands;
    Command command1;
    Command command2;

    @BeforeEach
    void setUp() {
        command1 = mock(Command.class);
        when(command1.isKeepRunningCommand()).thenReturn(true);
        command2 = mock(Command.class);
        when(command2.isKeepRunningCommand()).thenReturn(false);

        commands = new HashMap<>();
        commands.put(COMMAND1, command1);
        commands.put(COMMAND2, command2);

        menu = new Menu(commands);
    }

    @Test
    void executeCommandFound() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer(COMMAND1);
        BaseServerToCLI expectedResponse = new BaseServerToCLI();
        expectedResponse.setType(COMMAND1);

        when(command1.execute(cliRequest)).thenReturn(expectedResponse);

        //when
        BaseServerToCLI actualResponse = menu.execute(cliRequest);

        //then
        assertEquals(expectedResponse, actualResponse);
        assertTrue(menu.isRun());
    }

    @Test
    void executeCommandNotFound() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer("someCommand");

        //when
        BaseServerToCLI response = menu.execute(cliRequest);

        //then
        assertEquals("CommandNotFoundCommand", response.getType());
        assertTrue(menu.isRun());
    }

    @Test
    void isRun() {
        //given
        BaseCLIToServer cliRequest = new BaseCLIToServer(COMMAND2);
        BaseServerToCLI expectedResponse = new BaseServerToCLI();
        expectedResponse.setType(COMMAND2);

        when(command2.execute(cliRequest)).thenReturn(expectedResponse);

        //when
        BaseServerToCLI actualResponse = menu.execute(cliRequest);

        //then
        assertEquals(expectedResponse, actualResponse);
        assertFalse(menu.isRun());
    }
}