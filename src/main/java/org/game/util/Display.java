package org.game.util;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;

public class Display {
    Terminal terminal;
    public Display(){
        try {
            terminal = TerminalBuilder.terminal();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear_display(){
        terminal.puts(InfoCmp.Capability.clear_screen);
    }
}
