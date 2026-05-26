public class Kota_Player implements Player {

    //探索の最大深さ
    public int depthLimit = 4;

    @Override
    public Move think(Node node) {
        return new Move(3);
    }
}