import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Functions {
    //                   GRAVITY        TIME STEP     AIR RESIST    RESTITUTION ENERGY
    private final double GRAV = 10000.0, TSTEP = 0.004, FRIC = 1, RESTI = 0.9;
    private final double FX = 0, FY = GRAV;
    private final int COUNT = 20000;
    private int w, h;
    private int rad;
    private double[] x, y, nx, ny;
    private double[] vx, vy, nvx, nvy;
    private double[] ax, ay;

    public Functions(int w, int h) {
        this.w = w;
        this.h = h;
        x = new double[COUNT];
        y = new double[COUNT];
        nx = new double[COUNT];
        ny = new double[COUNT];
        vx = new double[COUNT];
        vy = new double[COUNT];
        nvx = new double[COUNT];
        nvy = new double[COUNT];
        ax = new double[COUNT];
        ay = new double[COUNT];
        rad = 2;
        Random rand = new Random();
        double maxVel = 750;
        for (int i = 0; i < COUNT; i++) {
//            x[i] = 200;
//            y[i] = 200;
//            vx[i] = 1000;
//            vy[i] = 0;
            x[i] = rand.nextDouble() * (w - 2.0 * rad) + rad;
            y[i] = rand.nextDouble() * (h - 2.0 * rad) + rad;
            nx[i] = x[i];
            ny[i] = y[i];
//            vx[i] = 0;
//            vy[i] = 0;
            vx[i] = (rand.nextDouble() + 1) * maxVel * ((rand.nextDouble() > 0.5) ? 1.0 : -1.0);
            vy[i] = (rand.nextDouble() + 1) * maxVel * ((rand.nextDouble() > 0.5) ? 1.0 : -1.0);
            ax[i] = FX;
            ay[i] = FY;
        }
    }

    public void update() {
        //BALL COLLISIONS
//        for (int i = 0; i < COUNT - 1; i++) {
//            for (int j = i + 1; j < COUNT; j++) {
//                double dist = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
//                interForce(i, j);
//                if (dist < 2.0 * rad) {
//                    bump(i, j, dist);
//                    bounce(i, j);
//                }
//            }
//        }
        for (int i = 0; i < COUNT; i++) {
            if (x[i] < rad) nx[i] = rad;
            else if (x[i] > w - rad) nx[i] = w - rad;
            else if (!boundX(i)) { //NO BOUNDARY... NORMAL UPDATE
                nx[i] += vx[i] * TSTEP + 0.5 * ax[i] * TSTEP * TSTEP;
                vx[i] = vx[i] * FRIC + ax[i] * TSTEP;
                ax[i] = FX;
            }
            if (y[i] < rad) ny[i] = rad;
            else if (y[i] > h - rad) ny[i] = h - rad;
            else if (!boundY(i)){ //NO BOUNDARY... NORMAL UPDATE
                ny[i] += vy[i] * TSTEP + 0.5 * ay[i] * TSTEP * TSTEP;
                vy[i] = vy[i] * FRIC + ay[i] * TSTEP;
                ay[i] = FY;
            }
        }
        for (int i = 0; i < COUNT; i++) {
            x[i] = nx[i];
            y[i] = ny[i];
        }
    }

    public void draw(GraphicsContext g) {
        g.setLineWidth(rad);
//        g.setStroke(Color.GREY);
        for (int i = 0; i < COUNT; i++) {
            double hue = Math.sqrt(vx[i] * vx[i] + vy[i] * vy[i]) * TSTEP * 10.0;
            g.setStroke(Color.hsb(hue, 1.0, 1.0));
            g.strokeOval(x[i] - rad / 2.0, y[i] - rad / 2.0, rad, rad);
        }
    }


    /////////////////////////////////////////////////////////////////////////////
    public boolean boundX(int i) {
        boolean bounded = false;
        //x position update and collision
        if (x[i] + vx[i] * TSTEP + 0.5 * ax[i] * TSTEP * TSTEP < rad) { //LEFT BOUND
            if (ax[i] == 0) { //NO ACCELERATION
                nx[i] = rad - ((x[i] + vx[i] * TSTEP) - rad);
                vx[i] *= -1.0 * FRIC * RESTI;
            } else { //ACCELERATION
                double t1 = Math.abs(((-1.0 * vx[i]) - Math.sqrt(vx[i] * vx[i] - 2.0 * ax[i] * x[i] - rad)) / ax[i]);
                double v2 = (vx[i] + ax[i] * t1) * -1.0, t2 = TSTEP - t1;
                nx[i] = rad + v2 * t2 + 0.5 * ax[i] * t2 * t2;
                vx[i] = v2 * FRIC * RESTI + ax[i] * t2;
            }
            bounded = true;
        }
        if (x[i] + vx[i] * TSTEP + 0.5 * ax[i] * TSTEP * TSTEP > w - rad) { //RIGHT BOUND
            if (ax[i] == 0) { //NO ACCELERATION
                nx[i] = (w - rad) - ((x[i] + vx[i] * TSTEP) - w + rad);
                vx[i] *= -1.0 * FRIC * RESTI;
            } else { //ACCELERATION
                double t1 = Math.abs(((-1.0 * vx[i]) + Math.sqrt(vx[i] * vx[i] - 2.0 * ax[i] * (-1.0 * (w - x[i] - rad)))) / ax[i]);
                double v2 = (vx[i] + ax[i] * t1) * -1.0, t2 = TSTEP - t1;
                nx[i] = (w - rad) + v2 * t2 + 0.5 * ax[i] * t2 * t2;
                vx[i] = v2 * FRIC * RESTI + ax[i] * t2;
            }
            bounded = true;
        }
        return bounded;
    }
    public boolean boundY(int i) {
        boolean bounded = false;
        //y update and collision
        if (y[i] + vy[i] * TSTEP + 0.5 * ay[i] * TSTEP * TSTEP < rad) { //TOP BOUND
            if (ay[i] == 0) { //NO ACCELERATION
                ny[i] = rad - ((y[i] + vy[i] * TSTEP) - rad);
                vy[i] *= -1.0 * FRIC * RESTI;
            } else { //ACCELERATION
                double t1 = Math.abs(((-1.0 * vy[i]) - Math.sqrt(vy[i] * vy[i] - 2.0 * ay[i] * y[i] - rad)) / ay[i]);
                double v2 = (vy[i] + ay[i] * t1) * -1.0, t2 = TSTEP - t1;
                ny[i] = rad + v2 * t2 + 0.5 * ay[i] * t2 * t2;
                vy[i] = v2 * FRIC * RESTI + ay[i] * t2;
            }
            bounded = true;
        }
        if (y[i] + vy[i] * TSTEP + 0.5 * ay[i] * TSTEP * TSTEP > h - rad) { //BOTTOM BOUND
            if (ay[i] == 0) { //NO ACCELERATION
                ny[i] = (h - rad) - ((y[i] + vy[i] * TSTEP) - h - rad);
                vy[i] *= -1.0 * FRIC * RESTI;
            } else { //ACCELERATION
                double t1 = Math.abs(((-1.0 * vy[i]) + Math.sqrt(vy[i] * vy[i] - 2.0 * ay[i] * (-1.0 * (h - y[i] - rad)))) / ay[i]);
                double v2 = (vy[i] + ay[i] * t1) * -1.0, t2 = TSTEP - t1;
                ny[i] = (h - rad) + v2 * t2 + 0.5 * ay[i] * t2 * t2;
                vy[i] = v2 * FRIC * RESTI + ay[i] * t2;
            }
            bounded = true;
        }
        return bounded;
    }
    public void bump(int i, int j, double dist) {
        double difx = x[i] - x[j], dify = y[i] - y[j];
        double phi = Math.atan2(dify, difx);
        double bdifx = rad * 2.0 * Math.cos(phi), bdify = rad * 2.0 * Math.sin(phi);
        nx[i] += (bdifx - difx) / 2.0;
        ny[i] += (bdify - dify) / 2.0;
        nx[j] -= (bdifx - difx) / 2.0;
        ny[j] -= (bdify - dify) / 2.0;
    }
    public void bounce(int i, int j) {
        double difx = x[i] - x[j], dify = y[i] - y[j];
        double phi = Math.atan2(dify, difx);
        double t1 = Math.atan2(vy[i], vx[i]), t2 = Math.atan2(vy[j], vx[j]);
        double v1 = Math.sqrt(vx[i] * vx[i] + vy[i] * vy[i]), v2 = Math.sqrt(vx[j] * vx[j] + vy[j] * vy[j]);
        double v1x, v1y, v2x, v2y;
        v1x = v1 * Math.cos(t1 - phi);
        v1y = v1 * Math.sin(t1 - phi);
        v2x = v2 * Math.cos(t2 - phi);
        v2y = v2 * Math.sin(t2 - phi);
        vx[i] = (v2x * Math.cos(phi) - v1y * Math.sin(phi)) * RESTI;
        vy[i] = (v2x * Math.sin(phi) + v1y * Math.cos(phi)) * RESTI;
        vx[j] = (v1x * Math.cos(phi) - v2y * Math.sin(phi)) * RESTI;
        vy[j] = (v1x * Math.sin(phi) + v2y * Math.cos(phi)) * RESTI;
    }

    public void interForce(int i, int j) {
        double dx = x[i] - x[j], dy = y[i] - y[j], dist = Math.sqrt(dx * dx + dy * dy);
            double t = Math.atan2(dy, dx);
            double force = 1000000.0 / (dist * dist);
        if (dist > rad) {
            ax[i] += force * Math.cos(t);
            ay[i] += force * Math.sin(t);
            ax[j] -= force * Math.cos(t);
            ay[j] -= force * Math.sin(t);
        }
//        else if (dist < rad && dist >= 2){
//            ax[i] += force * Math.cos(t) / 100.0;
//            ay[i] += force * Math.sin(t) / 100.0;
//            ax[j] -= force * Math.cos(t) / 100.0;
//            ay[j] -= force * Math.sin(t) / 100.0;
//        }
    }
}