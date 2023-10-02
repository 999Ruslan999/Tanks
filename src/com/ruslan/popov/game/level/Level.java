package com.ruslan.popov.game.level;

import com.ruslan.popov.game.Game;
import com.ruslan.popov.graphics.TextureAtlas;
import com.ruslan.popov.utils.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {


    public static final int SCALE = 8;

    public static final int TITLE_SCALE = 8;
    public static final int TITLE_IN_GAME_SCALE = 2;
    public static final int SCALED_TITLE_SIZE = TITLE_SCALE * TITLE_IN_GAME_SCALE;
    public static final int TITLES_IN_WIDTH = Game.WIDTH / SCALED_TITLE_SIZE;
    public static final int TITLES_IN_HEIGHT = Game.HEIGHT / SCALED_TITLE_SIZE;







    private Integer[][] tileMap;

    private Map<TileType,Tile> tiles;
    private List<Point> grassCords;
    public Level(TextureAtlas atlas) {
        tileMap = new Integer[TITLES_IN_WIDTH][TITLES_IN_HEIGHT];
        tiles = new HashMap<TileType, Tile>();

        tiles.put(TileType.BRICK, new Tile(atlas.cut(32 * TITLE_SCALE, 0 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.BRICK));

        tiles.put(TileType.METAL, new Tile(atlas.cut(32 * TITLE_SCALE, 2 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.METAL));

        tiles.put(TileType.WATER, new Tile(atlas.cut(32 * TITLE_SCALE, 4 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.WATER));

        tiles.put(TileType.GRASS, new Tile(atlas.cut(34 * TITLE_SCALE, 4 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.GRASS));

        tiles.put(TileType.ICE, new Tile(atlas.cut(36 * TITLE_SCALE, 4 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.ICE));
        tiles.put(TileType.EMPTY, new Tile(atlas.cut(36 * TITLE_SCALE, 6 * TITLE_SCALE, TITLE_SCALE, TITLE_SCALE),
                TITLE_IN_GAME_SCALE, TileType.EMPTY));

          tileMap = Utils.levelParser("res/level.lvl");
          grassCords = new ArrayList<Point>();
        for (int i = 0; i < tileMap.length; i++) {
            for (int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if(tile.type() == TileType.GRASS)
                    grassCords.add(new Point(j * SCALED_TITLE_SIZE, i * SCALED_TITLE_SIZE));


            }
        }



    }

    public void update() {

    }

    public void render(Graphics2D g) {

        for (int i = 0; i < tileMap.length; i++) {
            for(int j = 0; j < tileMap[i].length; j++) {
                Tile tile = tiles.get(TileType.fromNumeric(tileMap[i][j]));
                if(tile.type() != TileType.GRASS)
                tile.render(g, j * SCALED_TITLE_SIZE, i * SCALED_TITLE_SIZE);
            }
        }

    }

    public void renderGrass(Graphics2D g) {

        for (Point p : grassCords) {
            tiles.get(TileType.GRASS).render(g, p.x, p.y);
            }
        }

    }


