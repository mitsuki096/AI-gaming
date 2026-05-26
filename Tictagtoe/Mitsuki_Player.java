public class Mitsuki_Player implements Player{
    public int depthLimit = 4;

    public int visited = 0;

    public float maxSerch(Node node, float alpha, float beta, int depth){
        return alpha;
    }

    public Move think(Node node){
        return new Move();
    }
}
