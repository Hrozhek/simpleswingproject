package utils;

import javax.swing.SpringLayout;
import java.awt.Container;

public final class SpringLayoutUtils {
    private static final int SPRING_LAYOUT_CONSTRAINT = 10;
    private SpringLayoutUtils() {}
    public static void putBordersBetweenFarElements(SpringLayout layout, Container from, Container to, boolean isFirst) {
        String border = SpringLayout.SOUTH;
        if (isFirst)
            border = SpringLayout.NORTH;
        layout.putConstraint(border, from, SPRING_LAYOUT_CONSTRAINT, border, to);
    }

    public static void putVerticalLayoutConstraints(SpringLayout layout, Container from, Container to) {
        layout.putConstraint(SpringLayout.NORTH, from, SPRING_LAYOUT_CONSTRAINT, SpringLayout.SOUTH, to);
    }
}
