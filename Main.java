import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private GraphicsContext g;

    private final int w = 900, h = 900;

    @Override
    public void start(Stage stage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root, w, h, Color.BLACK);
        Canvas canvas = new Canvas(w, h);
        root.getChildren().add(canvas);
        GraphicsContext g = canvas.getGraphicsContext2D();
        stage.setTitle("Circles!");
        Functions s = new Functions(w, h);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                switch (key) {
                    case R:
                        break;
                    case O:
                        break;
                    case P:
                        break;
                    case X:
                        System.exit(0);
                        break;
                }
            }
        });

        stage.setScene(scene);
//        stage.setFullScreen(true);
        stage.show();

        new AnimationTimer() {
            @Override public void handle(long currentNanoTime) {
                g.clearRect(0, 0, w, h);
                s.update();
                s.draw(g);
            }
        }.start();
    }
}