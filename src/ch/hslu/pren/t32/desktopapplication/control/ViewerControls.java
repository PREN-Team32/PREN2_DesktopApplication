/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.pren.t32.desktopapplication.control;

import ch.hslu.pren.t32.desktopapplication.control.network.ConfigSender;
import ch.hslu.pren.t32.desktopapplication.control.network.ConnectionCheckerRunnable;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

/**
 *
 * @author Niklaus
 */
public class ViewerControls {

    private ResultLogger logger;
    private final ConnectionCheckerRunnable connectionChecker;
    private Thread connectionCheckerThread;
    private ConfigSender sender;

    //Time measurement
    private long zeitVorher;

    public ViewerControls(ConnectionCheckerRunnable connectionChecker) {
        this.connectionChecker = connectionChecker;
    }

    public void luminanceSliderStateChanged(JSlider luminanceSlider, JTextField luminanceThreshold) {
        float value = luminanceSlider.getValue();
        value = value / 100.00f;
        System.out.println(value + " f");
        luminanceThreshold.setText(value + " f");
    }

    public void connectMouseClicked(String ipAddress) {
        this.sender = new ConfigSender(ipAddress);
    }

    public void testrunMouseClicked(int luminanceThreshold, String pixelToCm, String visitedPixels, String width, String height) throws IOException {
        this.zeitVorher = System.currentTimeMillis();
        if (sender != null) {
            sender.setLuminanceThreshold(luminanceThreshold / 100f);
            sender.setPixelToCm(Double.parseDouble(pixelToCm));
            sender.setVisitedPixels(Integer.parseInt(visitedPixels));
            sender.setWidthToObserve(Integer.parseInt(width));
            sender.setHeightToObserve(Integer.parseInt(height));
            sender.sendConfig();
            startConnectionChecking();
        } else {
            JOptionPane.showMessageDialog(null, "Bitte stellen Sie erst die Verbindung her.");
        }
    }

    public void startMouseClicked(int luminanceThreshold, String pixelToCm, String visitedPixels, String width, String height) throws IOException {
        this.zeitVorher = System.currentTimeMillis();
        if (sender != null) {
            sender.setLuminanceThreshold(luminanceThreshold / 100f);
            sender.setPixelToCm(Double.parseDouble(pixelToCm));
            sender.setVisitedPixels(Integer.parseInt(visitedPixels));
            sender.setWidthToObserve(Integer.parseInt(width));
            sender.setHeightToObserve(Integer.parseInt(height));
            sender.setStartSignal(true);
            sender.sendConfig();
            startConnectionChecking();
        } else {
            JOptionPane.showMessageDialog(null, "Bitte stellen Sie erst die Verbindung her.");
        }
    }

    private void startConnectionChecking() {
        connectionChecker.setHostIP(sender.getHostIP());
        connectionCheckerThread = new Thread(connectionChecker);
        connectionCheckerThread.start();
    }
    
    public long getTotalTimeUsed(long zeitNachher) {
        return this.zeitVorher - zeitNachher;
    }
}
