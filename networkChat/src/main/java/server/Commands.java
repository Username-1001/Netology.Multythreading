package server;

public enum Commands {
    EXIT("/exit");

    private String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
