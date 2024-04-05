package hr.fer.oprpp2.webserver.workers;

import hr.fer.oprpp2.webserver.IWebWorker;
import hr.fer.oprpp2.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Class representing a worker for generating a png circle.
 */
public class CircleWorker implements IWebWorker {

    @Override
    public void processRequest(RequestContext context) throws Exception {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bim.createGraphics();
        g2d.setColor(new Color(255, 155, 195));
        g2d.fillOval(0, 0, 200, 200);
        g2d.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bim, "png", bos);
            context.setMimeType("image/png");
            context.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
