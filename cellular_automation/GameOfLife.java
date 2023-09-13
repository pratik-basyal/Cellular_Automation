package main.java.cellular_automaton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.concurrent.TimeUnit;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
/**
 * Class that represents game of life automaton
 */
public class GameOfLife implements main.java.cellular_automaton.ICellularAutomaton {
  private String state;
  private int width;
  private int height;

  /**
   * Constructor
   */
  public GameOfLife(int width, int height, String initialState) {
    this.width = width;
    this.height = height;
    state = initialState;
  }

  /**
   * Get next state from  current state and neighbours
   */
  private char getNext(boolean live, int aliveNeighbours) {
    if (aliveNeighbours == 3)
      return '1';
    return live && (aliveNeighbours == 2 || aliveNeighbours == 3) ? '1' : '0';
  }

  /**
   * Count the alive neighbours
   */
  private int countAliveNeighbors(int i) {
    int aliveNeighbours = 0;
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        if (dx != 0 || dy != 0) {
          int x_ = (i % width + dx + width) % width;
          int y_ = (i / width + dy + height) % height;
          aliveNeighbours += state.charAt(y_ * width + x_) - '0';
        }
      }
    }
    return aliveNeighbours;
  }

  /**
   * Advance to next state
   */
  public void advance() {
    String nextState = "";
    for (int i = 0; i < state.length(); i++) {
      nextState += getNext(state.charAt(i) == '1', countAliveNeighbors(i));
    }
    state = nextState;
  }

  /**
   * Displays the cellular automaton
   */
  public void display(GridPane grid) {
    
    int gapSize = 5;
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
      Color color = cell == 0 ? Color.RED : Color.BLUE;
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