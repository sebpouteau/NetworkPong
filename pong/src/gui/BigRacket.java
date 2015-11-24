package src.gui;

import java.awt.*;

/**
 * Created by Se Easy on 24/11/2015.
 */
public class BigRacket extends Bonus {
    private String image = "image/big_raquette.png";
    private String imageH = "image/big_raquetteH.png";
    private String old;


    public BigRacket(PongItem pi){
        super();

    }

    public void startBonus(PongItem pi){
        System.out.println("je lance mon bonus");
        if(pi instanceof Racket) {
            String im;
            Racket r = (Racket) pi;
            if (r.getNumber() < 3)
                im = image;
            else
                im = imageH;
            r.initImage(im);
            if(r.getNumber() < 3)
                old = r.getImage();
            else
                old = r.getImageH();
}
    }

    public void stopBonus(PongItem pi){
        System.out.println("je remet l'ancienne image");
        pi.setImageItem(Toolkit.getDefaultToolkit().createImage(
                ClassLoader.getSystemResource(old)));

    }


}
