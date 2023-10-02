package com.ruslan.popov.graphics;

import com.ruslan.popov.utils.RecurseLoader;

import java.awt.image.BufferedImage;

public class TextureAtlas {

    BufferedImage image;

    public TextureAtlas(String imageName) {
        image = RecurseLoader.loadImage(imageName);
    }

    public BufferedImage cut(int x, int y, int w, int h) {
       return image.getSubimage(x, y, w, h);
    }

}
