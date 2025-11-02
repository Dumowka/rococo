package guru.qa.rococo.config;

public interface Callbacks {

  interface Android {
    String login = "/callback";
    String logout = "/logout_callback";
    String init = "/start";
  }

  interface Web {
    String login = "/login";
    String logout = "/logout";
    String init = "/";
  }
}
