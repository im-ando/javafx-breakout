package jp.imagemagic.breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class FieldController implements Initializable {
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    private ViewUnit unit;

    public void mouseMove(MouseEvent e) {
        transAndDraw(unit.moveBar(e.getSceneX()));
    }

    public void mouseClick() {
        transAndDraw(unit.transStatus());
    }

    private void transAndDraw(UnaryOperator<State> trans) {
        unit.trans(trans);
        drawView();
    }

    private void drawView() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        unit.drawView(this::drawObject, status -> {
            final Optional<String> message = switch (status) {
                case Ready -> Optional.of("Breakout");
                case GameOver -> Optional.of("Game Over");
                case Clear -> Optional.of("Clear");
                default -> Optional.empty();
            };
            message.ifPresent(s -> {
                gc.setFill(Color.WHITE);
                gc.setFont(new Font(25));
                gc.fillText(s, canvas.getWidth() / 3, canvas.getHeight() / 2);
                gc.setFont(new Font(20));
                gc.fillText("Click To Start", canvas.getWidth() / 3 - 10, canvas.getHeight() / 2 + 40);
            });
        });


    }

    private void drawObject(Drawable d) {
        gc.setFill(d.fill());
        gc.setStroke(d.stroke());
        switch (d.drawType()) {
            case Rect -> {
                gc.fillRect(d.pos().x(), d.pos().y(), d.size().x(), d.size().y());
                gc.strokeRect(d.pos().x(), d.pos().y(), d.size().x(), d.size().y());
            }
            case Oval -> {
                gc.fillOval(d.pos().x(), d.pos().y(), d.size().x(), d.size().y());
                gc.strokeOval(d.pos().x(), d.pos().y(), d.size().x(), d.size().y());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gc = canvas.getGraphicsContext2D();
        unit = new ViewUnit(canvas.getWidth(), canvas.getHeight());
        final var timeline = new Timeline(new KeyFrame(new Duration(10), e -> transAndDraw(unit.moveBall())));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        drawView();
    }
}
