package src.gui;

import java.awt.*;

/**
 * Created by Se Easy on 24/11/2015.
 */
public class BigRacket extends Bonus {
    private String image = "image/big_raquette.png";
    private String imageH = "image/big_raquetteH.png";
    private Image oldImage;

    public BigRacket(PongItem pi){
        super();
        oldImage = pi.getImageItem();
    }

    public void startBonus(PongItem pi){
        System.out.println("je lance mon bonus");

        String im;
            Racket r = (Racket) pi;
            if (r.getNumber() < 3)
                im = image;
            else
                im = imageH;
        r.initImage(im);

    }

    public void stopBonus(PongItem pi){
        System.out.println("je remet l'ancienne image");
        System.out.println(oldImage);
        pi.setImageItem(oldImage);
    }


}
