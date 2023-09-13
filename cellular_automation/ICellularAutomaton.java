package main.java.cellular_automaton;
import javafx.scene.layout.GridPane;

/**
 * Interface for cellular automaton
 *
 */
public interface ICellularAutomaton {
  /**
   * Advance the cellular automaton
   *
   */
  public void advance();
  /**
   * Display the cellular automaton
   *
   */
  public void display(GridPane grid);
}
