package tictactoe;

interface Player {
    // 現在の盤面(Node)を受け取り、打つべき一手(Move)を返す
    Move think(Node node);
}
