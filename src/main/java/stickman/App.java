package stickman;

import java.io.IOException;
import java.util.*;
import java.util.Map;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.JSONException;
import stickman.model.engine.GameEngine;
import stickman.model.engine.GameEngineImpl;
import stickman.model.config.ConfigParser;
import stickman.model.config.JsonConfigParser;
import stickman.view.GameWindow;

public class App extends Application {
//  private static ConfigParser config;
  private static List<ConfigParser> configParserList = new ArrayList<>();

  public static void main(String[] args) {
    List<String> configPathList = Arrays.asList("./level1.json", "./level2.json", "./level3.json");

    try {
      for (String configPath: configPathList) {
        configParserList.add(new JsonConfigParser(configPath));
      }
//      config = new JsonConfigParser(configPath);
    } catch (IOException e) {
      System.err.println("IO error when attempting to read configuration");
      e.printStackTrace();
      System.exit(1);
    } catch (JSONException e) {
      System.err.println(
          "Configuration is not well formed, please refer to example.json for an "
              + "example of a well formed configuration");
      e.printStackTrace();
      System.exit(2);
    }
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Map<String, String> params = getParameters().getNamed();

    GameEngine model = new GameEngineImpl(configParserList);
    model.startLevel();
    GameWindow window = new GameWindow(model, 640, 400);
    window.run();

    primaryStage.setTitle("Stickman");
    primaryStage.setScene(window.getScene());
    primaryStage.show();

    window.run();
  }
}
