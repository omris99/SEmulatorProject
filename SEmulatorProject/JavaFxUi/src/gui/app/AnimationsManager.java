package gui.app;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationsManager {
    private static boolean isAnimationsDisabled = true;

    public static boolean isAnimationsDisabled() {
        return isAnimationsDisabled;
    }

    public static void setAnimationsDisabled(boolean animationsDisabled) {
        isAnimationsDisabled = animationsDisabled;
    }

    public static void playScaleIn(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        node.setScaleX(0);
        node.setScaleY(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), node);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public static void playFadeIn(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    public static void playRotateIn(Node node, double durationMillis, int angle) {
        if (isAnimationsDisabled) return;
        node.setRotate(0);
        javafx.animation.RotateTransition rt = new javafx.animation.RotateTransition(Duration.millis(durationMillis), node);
        rt.setFromAngle(0);
        rt.setToAngle(angle);
        rt.play();
    }
    public static void playBigger(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), node);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(1.15);
        st.setToY(1.15);
        st.setFromX(1.15);
        st.setFromY(1.15);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public static void playSmaller(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), node);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0.85);
        st.setToY(0.85);
        st.setFromX(0.85);
        st.setFromY(0.85);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public static void playRotateOut(Node node, double durationMillis, int angle) {
        if (isAnimationsDisabled) return;
        node.setRotate(0);
        javafx.animation.RotateTransition rt = new javafx.animation.RotateTransition(Duration.millis(durationMillis), node);
        rt.setFromAngle(0);
        rt.setToAngle(-angle);
        rt.play();
    }

    public static void playShake(Node node, double durationMillis, double angle) {
        if (isAnimationsDisabled) return;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(node.rotateProperty(), 0)),
                new KeyFrame(Duration.millis(durationMillis / 4), new KeyValue(node.rotateProperty(), angle)),
                new KeyFrame(Duration.millis(durationMillis / 2), new KeyValue(node.rotateProperty(), -angle)),
                new KeyFrame(Duration.millis(3 * durationMillis / 4), new KeyValue(node.rotateProperty(), angle)),
                new KeyFrame(Duration.millis(durationMillis), new KeyValue(node.rotateProperty(), 0))
        );
        timeline.play();
    }


    public static void playFadeOut(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        node.setOpacity(1);
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.play();
    }

    public static void playScaleOut(Node node, double durationMillis) {
        if (isAnimationsDisabled) return;
        node.setScaleX(1);
        node.setScaleY(1);
        ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), node);
        st.setFromX(1);
        st.setFromY(1);
        st.setToX(0);
        st.setToY(0);
        st.play();
    }
}
