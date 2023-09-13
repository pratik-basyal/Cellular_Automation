package main.java.cellular_automaton;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class that represents a one dimensional celluar automaton
 */
public class CellularAutomaton1D implements main.java.cellular_automaton.ICellularAutomaton {
  private String rule;
  private String state;
  private int height;
  private int width;

  /**
   * Constructor
   *
   */
  public CellularAutomaton1D(String rule, String initialState) {
    this.rule = rule;
    this.width = initialState.length();
    this.height = 1;
    state = initialState;
  }

  /**
   * Overrides for ICellularAutomaton interface that advances the cellular
   * automaton
   */
  public void advance() {
    String nextState = "";
    for (int i = 0; i < state.length(); i++) {
      int mid = state.charAt(i) - '0';
      int left = state.charAt((i - 1 + state.length()) % state.length()) - '0';
      int right = state.charAt((i + 1 + state.length()) % state.length()) - '0';
      int out = left * 4 + mid * 2 + right;
      nextState += rule.charAt(rule.length() - 1 - out);
    }
    state = nextState;
  }

  /**
   * Overrides for ICellularAutomaton interface that displays the cellular
   * automaton
   *
   */
  public void display(GridPane grid) {
    int gapSize = 2;
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
              Bindings.min(grid.heightProperty(), grid.widthProperty())
                  .subtract(50);
          generations.add(state);

          for (int j = 0; j < generations.size(); j++) {
            String gen = generations.get(j);
            for (int i = 0; i < gen.length(); i++) {
              int cell = gen.charAt(i) - '0';
              Color color = cell == 0 ? Color.RED : Color.BLUE;
              Rectangle rect = new Rectangle();
              rect.setFill(color);

              NumberBinding squareSize = Bindings.min(
                  rectsAreaSize.divide(generations.size()).subtract(gapSize),
                  rectsAreaSize.divide(gen.length()).subtract(gapSize));
              rect.heightProperty().bind(squareSize);
              rect.widthProperty().bind(squareSize);

              grid.add(rect, i, j);
            }
          }

          advance();
          prevUpdate = now;
        }
      }
    };

    timer.start();
  }
}
