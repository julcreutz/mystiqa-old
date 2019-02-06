package game.main.state.play.map;

import game.main.positionable.entity.Chest;
import game.main.positionable.entity.Entity;
import game.main.positionable.entity.monster.*;
import game.main.positionable.tile.Tile;
import game.main.positionable.tile.connected.CaveBridge;
import game.main.positionable.tile.connected.CaveHole;
import game.main.positionable.tile.connected.CaveWall;
import game.main.positionable.tile.unconnected.CaveGround;
import game.main.positionable.tile.unconnected.CaveMushroom;
import game.main.positionable.tile.unconnected.CaveRock;
import game.main.positionable.tile.connected.CaveSpiderWeb;

public class Cave extends Map {
    public Cave() {
        minRooms = 13;
        maxRooms = 18;
        outerWall = CaveWall.class;
        innerWall = CaveHole.class;
        hole = CaveBridge.class;
        chest = Chest.class;
        minMonsters = 2;
        maxMonsters = 6;
        boss = SpiderQueen.class;
        bossTemplate = new Template(new char[][] {
                {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'},
                {'#', '#', ' ', ' ', ' ', ' ', ' ', '#', '#', '#'},
                {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
                {' ', ' ', ' ', ' ', 'b', ' ', ' ', ' ', ' ', ' '},
                {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                {'#', '#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                {'#', '#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#'},
                {'#', '#', '#', '#', ' ', ' ', '#', '#', '#', '#'}
        });
        templates = new Template[] {
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', ' ', ' ', '#', '#', '#', '#'},
                        {'#', '#', ' ', ' ', ' ', ' ', ' ', '#', '#', '#'},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                        {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                        {'#', '#', '#', ' ', ' ', ' ', '#', '#', ' ', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', '#', '#'}
                }),
                new Template(new char[][] {
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', ' ', '#'},
                        {'#', '#', '#', '#', '#', ' ', ' ', ' ', ' ', '#'},
                        {'#', ' ', '#', '#', '#', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                        {'#', ' ', ' ', ' ', ' ', ' ', ' ', '#', '#', '#'},
                        {'#', ' ', '#', '#', ' ', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'}
                }),
                new Template(new char[][] {
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', ' ', ' ', '#', '#', ' ', '#'},
                        {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '#'},
                        {' ', ' ', ' ', '+', '.', '+', '+', ' ', ' ', ' '},
                        {'#', ' ', '+', '+', '.', '+', '#', '#', ' ', '#'},
                        {'#', '#', '#', '+', ' ', ' ', '#', '#', ' ', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'}
                }),
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '+', '+', '+', '#', '#', '#'},
                        {'#', '#', '#', '+', '+', '+', '+', '+', '#', '#'},
                        {' ', ' ', ' ', ' ', '+', '+', '+', '+', ' ', '#'},
                        {'#', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {'#', ' ', '#', '#', ' ', ' ', ' ', ' ', ' ', '#'},
                        {'#', '#', '#', '#', ' ', ' ', ' ', ' ', '#', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', '#', '#'}
                }, Room.Direction.UP),
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '+', '+', '+', '#', '#', '#'},
                        {'#', '#', 'c', '+', '+', '+', '+', '+', '#', '#'},
                        {' ', ' ', ' ', '+', '+', '+', '+', '+', '+', '#'},
                        {'#', ' ', ' ', '.', '.', ' ', '+', '+', '+', '#'},
                        {'#', ' ', '+', '+', ' ', ' ', ' ', '+', '+', '#'},
                        {'#', '#', '#', '+', '+', ' ', ' ', '+', '#', '#'},
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'}
                }, Room.Direction.UP, Room.Direction.RIGHT),
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', 'c', ' ', ' ', '#', '+', '#'},
                        {'#', '#', '#', '#', '#', '+', '.', '+', ' ', '#'},
                        {'#', ' ', '+', '#', '#', '+', '.', ' ', ' ', ' '},
                        {' ', ' ', '+', '+', '+', ' ', ' ', ' ', ' ', '#'},
                        {'#', ' ', '.', '.', ' ', ' ', ' ', '#', '#', '#'},
                        {'#', '+', '+', '+', ' ', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'}
                }),
                new Template(new char[][] {
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', ' ', 'c', '#', '#', '#', '#'},
                        {'#', '#', '+', ' ', ' ', ' ', ' ', '#', '#', '#'},
                        {'#', '+', '+', '+', ' ', ' ', ' ', '#', '#', '#'},
                        {'#', '+', '+', '+', '.', '+', '#', '#', '#', '#'},
                        {'#', '+', '+', '+', '.', '+', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', ' ', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', '#', '#'}
                }, Room.Direction.LEFT, Room.Direction.RIGHT),
                new Template(new char[][] {
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'},
                        {'#', '#', ' ', ' ', ' ', 'c', '#', '#', '#', '#'},
                        {'#', ' ', ' ', '+', '#', '#', '#', '#', '#', '#'},
                        {'#', '+', '.', '+', '#', '#', '#', '+', ' ', '#'},
                        {'#', '+', '.', '+', '#', '#', '+', '+', ' ', ' '},
                        {'#', '#', ' ', ' ', ' ', '.', '.', ' ', ' ', '#'},
                        {'#', '#', '#', ' ', ' ', '+', '+', ' ', ' ', '#'},
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
                }, Room.Direction.LEFT, Room.Direction.DOWN),
                new Template(new char[][] {
                        {'#', '#', '#', '#', ' ', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', ' ', 'c', '#', '#', '#', '#'},
                        {'#', '#', ' ', ' ', ' ', '#', '#', '#', '#', '#'},
                        {'#', ' ', ' ', ' ', '#', '#', '#', '#', '#', '#'},
                        {' ', ' ', ' ', ' ', '#', '#', '#', '#', '#', '#'},
                        {'#', ' ', ' ', ' ', '#', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', ' ', '#', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
                }, Room.Direction.RIGHT, Room.Direction.DOWN),
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', ' ', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', ' ', ' ', '#', '#', 'c', '#'},
                        {'#', '#', '#', '#', '#', ' ', ' ', ' ', ' ', '#'},
                        {'#', '+', '#', '#', '#', ' ', ' ', ' ', ' ', ' '},
                        {'#', '+', '+', '+', '+', ' ', ' ', ' ', ' ', ' '},
                        {'#', '+', '+', '+', '+', '+', ' ', '#', '#', '#'},
                        {'#', '+', '#', '#', '+', '+', '#', '#', '#', '#'},
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
                }, Room.Direction.LEFT, Room.Direction.DOWN),
                new Template(new char[][] {
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                        {'#', '#', '#', '+', '+', '#', '#', '#', '#', '#'},
                        {'#', '#', '+', '+', '+', '+', '+', '#', 'c', '#'},
                        {' ', ' ', ' ', '+', '+', '+', '+', '+', ' ', '#'},
                        {'#', ' ', ' ', '.', '.', '.', '.', '.', ' ', ' '},
                        {'#', ' ', '+', '+', '+', '+', '+', '+', ' ', '#'},
                        {'#', '#', '#', '+', '#', '#', '#', '+', '+', '#'},
                        {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
                }, Room.Direction.UP, Room.Direction.DOWN),
        };

        grounds.addChoice(new Choice<Tile>(1, 1, 1, CaveGround.class));
        grounds.addChoice(new Choice<Tile>(0, 1, .75f, CaveSpiderWeb.class));
        grounds.addChoice(new Choice<Tile>(1, 1, .05f, CaveMushroom.class));
        grounds.addChoice(new Choice<Tile>(1, 1, .025f, CaveRock.class));

        monsters.addChoice(new Choice<Entity>(0f, 1f, 1, SpiderBaby.class));
        monsters.addChoice(new Choice<Entity>(0f, 1f, .5f, Spider.class));
        monsters.addChoice(new Choice<Entity>(1f, 0f, .5f, Bat.class));
        monsters.addChoice(new Choice<Entity>(0f, 1f, .33f, SpiderNest.class));
    }
}
