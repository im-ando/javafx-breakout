module jp.imagemagic.breakout {
    requires javafx.controls;
    requires javafx.fxml;
    exports jp.imagemagic.breakout to javafx.graphics;
    opens jp.imagemagic.breakout to javafx.fxml;
}