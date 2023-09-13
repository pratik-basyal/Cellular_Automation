package main.java.cellular_automaton;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;

/**
 * Class that represents langton's loop automaton
 *
 */
class LangtonAutomaton implements ICellularAutomaton {
  private int width;
  private int height;
  String state;
  HashMap<Integer, Integer> rules = new HashMap<Integer, Integer>();

  /**
   * Constructor
   */
  public LangtonAutomaton(int width, int height, String initialState,
                          ArrayList<Integer> rules) {
    this.width = width;
    this.height = height;
    state = initialState;
    for (int rule : rules) {
      this.rules.put(rule / 10, rule % 10);
    }
  }

  /**
   * Get the next state depending on current state and neighbors
   * (checks all combinations)
   *
   */
  int rules_get(int current, int neighbours) {
    int query = current * 10000 + neighbours;
    for (int i = 0; i < 4; i++) {
      query = current * 10000 + neighbours;
      if (rules.containsKey(query)) {
        return rules.get(query);
      }
      neighbours = (neighbours % 10) * 1000 + neighbours / 10;
    }
    return rules.get(query);
  }

  /**
   * Get the next state depending on current state and neighbors
   * (runs rules_get internally)
   *
   */
  public char getNext(int current, int neighbours) {
    return (char)('0' + rules_get(current, neighbours));
  }

  /**
   * Get the neighbours for the current cell at i
   *
   */
  public int getNeighbours(int i) {
    int x = i % width;
    int y = i / width;

    int n = state.charAt(x + width * ((y - 1 + height) % height)) - '0';
    int s = state.charAt(x + width * ((y + 1) % height)) - '0';
    int e = state.charAt((x + 1) % width + width * y) - '0';
    int w = state.charAt((x - 1 + width) % width + width * y) - '0';

    return (n * 1000 + e * 100 + s * 10 + w);
  }

  /**
   * Overrides for ICellularAutomaton interface that advances the cellular automaton
   *
   */
  public void advance() {
    String nextState = "";
    for (int i = 0; i < state.length(); i++) {
      nextState += getNext(state.charAt(i) - '0', getNeighbours(i));
    }
    state = nextState;
  }

  /**
   * Constructs a langton automaton from file
   *
   */
  public static LangtonAutomaton fromFile(String rule_filename,
                                          String init_filename)
      throws IOException {
    File rules_file = new File(rule_filename);
    Scanner rules_scanner = new Scanner(rules_file);
    ArrayList<Integer> rules = new ArrayList<Integer>();
    while (rules_scanner.hasNextLine()) {
      rules.add(rules_scanner.nextInt());
    }
    rules_scanner.close();

    File init_file = new File(init_filename);
    Scanner init_scanner = new Scanner(init_file);
    int width = init_scanner.nextInt();
    int height = init_scanner.nextInt();
    init_scanner.nextLine(); // flush stream
    String initialState = "";
    while (init_scanner.hasNextLine()) {
      initialState += init_scanner.nextLine();
    }
    init_scanner.close();

    return new LangtonAutomaton(width, height, initialState, rules);
  }


  /**
   * Override for ICellularAutomaton interface that displays the cellular automaton
   *
   */
  public void display(GridPane grid) {
   
    int gapSize = 1;
    grid.setHgap(gapSize);
    grid.setVgap(gapSize);


final AnimationTimer timer = new AnimationTimer() {
      private long prevUpdate = 0;
      private ArrayList<String> generations = new ArrayList<String>();
      @Override
      public void handle(final long now) {
        if (now - prevUpdate >= TimeUnit.SECONDS.toNanos(1)) {
grid.getChildren().clear();


    NumberBinding rectsAreaSize =
        Bindings.min(grid.heightProperty(), grid.widthProperty()).subtract(50);
    for (int i = 0; i < state.length(); i++) {
      int cell = state.charAt(i) - '0';
      Color color;
      if (cell == 0) {
        color = Color.RED;
      } else if (cell == 1) {
        color = Color.BLUE;
      } else if (cell == 2) {
        color = Color.GREEN;
      } else if (cell == 3) {
        color = Color.YELLOW;
      } else if (cell == 4) {
        color = Color.PURPLE;
      } else if (cell == 6) {
        color = Color.ORANGE;
      } else if (cell == 7) {
        color = Color.TURQUOISE;
      } else {
        color = Color.BLACK;
      }
      Rectangle rect = new Rectangle();
      rect.setFill(color);
      rect.heightProperty().bind(
          rectsAreaSize.divide(height).subtract(gapSize));
      rect.widthProperty().bind(rectsAreaSize.divide(width).subtract(gapSize));
      grid.add(rect, i % width, i / width);
    }
     advance();
          prevUpdate = now;
    }
  }
};
timer.start();
  }
}
