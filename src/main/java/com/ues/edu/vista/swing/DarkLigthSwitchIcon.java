/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ues.edu.vista.swing;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.AnimatedIcon;
import com.formdev.flatlaf.util.ColorFunctions;
import com.formdev.flatlaf.util.UIScale;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;

/**
 *
 * @author jorge
 */
public class DarkLigthSwitchIcon implements AnimatedIcon {

    private int iconGap = 3;
    private int centerSpace = 5;

    // 1. Declaramos las variables pero NO las inicializamos (evita el error al inicio)
    private Icon darkIcon;
    private Icon lightIcon;

    private Color darkColor = new Color(80, 80, 80);
    private Color lightColor = new Color(230, 230, 230);

    // 2. Constructor: Aquí cargamos las imágenes de forma segura
    public DarkLigthSwitchIcon() {
        try {
            // En Maven, src/main/resources/iconos se convierte en /iconos en el classpath.
            // Si falla, capturamos el error para que no se cierre el diseñador.
            darkIcon = new FlatSVGIcon("iconos/dark.svg", 0.4f);
            lightIcon = new FlatSVGIcon("iconos/light.svg", 0.4f);
        } catch (Exception e) {
            System.err.println("Error cargando iconos SVG: " + e.getMessage());
            // Dejamos los iconos como null, el código de abajo sabrá manejarlo.
        }
    }

    public int getIconGap() {
        return iconGap;
    }

    public void setIconGap(int iconGap) {
        this.iconGap = iconGap;
    }

    public int getCenterSpace() {
        return centerSpace;
    }

    public void setCenterSpace(int centerSpace) {
        this.centerSpace = centerSpace;
    }

    public Icon getDarkIcon() {
        return darkIcon;
    }

    public void setDarkIcon(Icon darkIcon) {
        this.darkIcon = darkIcon;
    }

    public Icon getLightIcon() {
        return lightIcon;
    }

    public void setLightIcon(Icon lightIcon) {
        this.lightIcon = lightIcon;
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public void setDarkColor(Color darkColor) {
        this.darkColor = darkColor;
    }

    public Color getLightColor() {
        return lightColor;
    }

    public void setLightColor(Color lightColor) {
        this.lightColor = lightColor;
    }

    private float getBorderArc(Component com) {
        return FlatUIUtils.getBorderArc((JComponent) com);
    }

    @Override
    public int getAnimationDuration() {
        return 500;
    }

    @Override
    public void paintIconAnimated(Component c, Graphics g, int x, int y, float animatedValue) {
        // 3. SEGURIDAD: Si los iconos no cargaron, no intentamos pintar nada
        if (darkIcon == null || lightIcon == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        FlatUIUtils.setRenderingHints(g2);
        Color color = ColorFunctions.mix(darkColor, lightColor, animatedValue);
        int size = getIconHeight();
        int width = getIconWidth();
        float arc = Math.min(getBorderArc(c), size);
        float animatedX = x + (width - size) * animatedValue;

        g2.setColor(color);
        g2.fill(new RoundRectangle2D.Double(animatedX, y, size, size, arc, arc));
        float darkY = (y - size + (animatedValue * size));
        float lightY = y + animatedValue * size;
        g2.setClip(new Rectangle(x, y, width, size));
        paintIcon(c, (Graphics2D) g2.create(), animatedX, darkY, darkIcon, animatedValue);
        paintIcon(c, (Graphics2D) g2.create(), animatedX, lightY, lightIcon, 1f - animatedValue);
        g2.dispose();
    }

    private void paintIcon(Component c, Graphics2D g, float x, float y, Icon icon, float alpha) {
        int gap = UIScale.scale(iconGap);
        g.translate(x, y);
        g.setComposite(AlphaComposite.SrcOver.derive(alpha));
        icon.paintIcon(c, g, gap, gap);
        g.dispose();
    }

    @Override
    public float getValue(Component c) {
        return ((AbstractButton) c).isSelected() ? 1 : 0;
    }

    @Override
    public int getIconWidth() {

        if (darkIcon == null || lightIcon == null) {
            return 60;
        }
        return darkIcon.getIconWidth() + lightIcon.getIconWidth() + UIScale.scale(centerSpace) + UIScale.scale(iconGap) * 4;
    }

    @Override
    public int getIconHeight() {
        if (darkIcon == null || lightIcon == null) {
            return 26;
        }
        return Math.max(darkIcon.getIconHeight(), lightIcon.getIconHeight()) + UIScale.scale(iconGap) * 2;
    }

}
