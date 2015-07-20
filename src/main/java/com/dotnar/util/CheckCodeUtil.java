package com.dotnar.util;

import org.patchca.color.ColorFactory;
import org.patchca.filter.predefined.*;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.WordFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Random;

public class CheckCodeUtil {
	public static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
	private static Random random = new Random();
	private static BASE64Encoder encoder = new BASE64Encoder();

	static {
		cs.setColorFactory(new ColorFactory() {
//			@Override
			public Color getColor(int x) {
				int[] c = new int[3];
				int i = random.nextInt(c.length);
				for (int fi = 0; fi < c.length; fi++) {
					if (fi == i) {
						c[fi] = random.nextInt(71);
					} else {
						c[fi] = random.nextInt(256);
					}
				}
				return new Color(c[0], c[1], c[2]);
			}
		});
	}
	
	public String crimg(final String code){
    	WordFactory wf = new WordFactory() {
//			@Override
			public String getNextWord() {
				return code;
			}
		};
		cs.setWordFactory(wf);
		
        switch (random.nextInt(5)) {
            case 0:
                cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
                break;
            case 1:
                cs.setFilterFactory(new MarbleRippleFilterFactory());
                break;
            case 2:
                cs.setFilterFactory(new DoubleRippleFilterFactory());
                break;
            case 3:
                cs.setFilterFactory(new WobbleRippleFilterFactory());
                break;
            case 4:
                cs.setFilterFactory(new DiffuseRippleFilterFactory());
                break;
        }
		BufferedImage bi;
		try {
			bi = cs.getCaptcha().getImage();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "png", baos);
			byte[] bytes = baos.toByteArray();
//			System.out.println("data:image/png;base64,"+ encoder.encodeBuffer(bytes).trim());
			return "data:image/png;base64,"
					+ encoder.encodeBuffer(bytes).trim();
		}catch (Exception e){
			return null;
		}
//        setResponseHeaders(response);
//        EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
//        return response.getOutputStream();
    }
   
	
	
	 protected void setResponseHeaders(HttpServletResponse response) {
	        response.setContentType("image/png");
	        response.setHeader("Cache-Control", "no-cache, no-store");
	        response.setHeader("Pragma", "no-cache");
	        long time = System.currentTimeMillis();
	        response.setDateHeader("Last-Modified", time);
	        response.setDateHeader("Date", time);
	        response.setDateHeader("Expires", time);
	    }
}
