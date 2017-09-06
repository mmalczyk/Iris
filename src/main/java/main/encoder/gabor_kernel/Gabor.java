package main.encoder.gabor_kernel;

import org.opencv.core.Mat;
import org.opencv.core.Size;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_64F;

public class Gabor {
    public static Mat getGaborKernel(Size ksize, double sigma, double theta,
                                     double lambd, double gamma, double psi, int ktype, boolean real) {
        double sigma_x = sigma;
        double sigma_y = sigma / gamma;
        int nstds = 3;
        int xmin, xmax, ymin, ymax;
        double c = Math.cos(theta), s = Math.sin(theta);

        if (ksize.width > 0)
            xmax = (int) ksize.width / 2;
        else
            xmax = (int) Math.round(Math.max(Math.abs((double) nstds * sigma_x * c), Math.abs((double) nstds * sigma_y * s)));

        if (ksize.height > 0)
            ymax = (int) ksize.height / 2;
        else
            ymax = (int) Math.round(Math.max(Math.abs((double) nstds * sigma_x * s), Math.abs((double) nstds * sigma_y * c)));

        xmin = -xmax;
        ymin = -ymax;

        assert (ktype == CV_32F || ktype == CV_64F);

        Mat kernel = new Mat(ymax - ymin + 1, xmax - xmin + 1, ktype);
        double scale = 1.;
        double ex = -0.5 / (sigma_x * sigma_x);
        double ey = -0.5 / (sigma_y * sigma_y);
        double cscale = Math.PI * 2. / lambd;

        for (int y = ymin; y <= ymax; y++)
            for (int x = xmin; x <= xmax; x++) {
                double xr = x * c + y * s;
                double yr = -x * s + y * c;
                double v;
                if (real)
                    v = getRealComponent(scale, ex, xr, ey, yr, cscale, psi);
                else
                    v = getImaginaryComponent(scale, ex, xr, ey, yr, cscale, psi);
                if (ktype == CV_32F)
                    kernel.put(ymax - y, xmax - x, (float) v);
                else
                    kernel.put(ymax - y, xmax - x, (double) v);
            }

        return kernel;
    }

    public static Mat getRealGaborKernel(Size ksize, double sigma, double theta,
                                         double lambd, double gamma, double psi, int ktype) {
        return getGaborKernel(ksize, sigma, theta, lambd, gamma, psi, ktype, true);
    }

    public static Mat getImaginaryGaborKernel(Size ksize, double sigma, double theta,
                                              double lambd, double gamma, double psi, int ktype) {
        return getGaborKernel(ksize, sigma, theta, lambd, gamma, psi, ktype, false);
    }

    public static double getRealComponent(double scale, double ex, double xr, double ey, double yr, double cscale, double psi) {
        return scale * Math.exp(ex * xr * xr + ey * yr * yr) * Math.cos(cscale * xr + psi);
    }

    public static double getImaginaryComponent(double scale, double ex, double xr, double ey, double yr, double cscale, double psi) {
        return scale * Math.exp(ex * xr * xr + ey * yr * yr) * Math.sin(cscale * xr + psi);
    }


}