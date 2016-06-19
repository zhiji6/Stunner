package com.hal.stun.cli;

public class Help {
  private static final String[] HELP_TEXT = {
    "  --tcp, -tcp  //  Run TCP STUN server (can be used with --udp)",
    "  --udp, -udp  //  Run UDP STUN server (can be used with --tcp)",
    "  --tcpport, -tport  //  Port on which to bind the TCP server",
    "  --udpport, -uport  //  Port on which to bind the TCP server",
    "  --help, -h  //  Display usage info",
    "  --threads, -t // Number of theads to use in handler threadpool"
  };
  private static final String HELP_HEADER = "\nUsage:\n\n$> java -jar Stunner.jar\n";

  public static String getHelpText() {
    String joinedHelpText = String.join("\n", HELP_TEXT);
    return HELP_HEADER + joinedHelpText;
  }
}
