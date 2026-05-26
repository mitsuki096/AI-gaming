package tictactoe;

import java.util.*;

/**
 * 教材用のゲーム木（深さ 3）。
 *
 * <pre>
 *               A           ← max の手番（根）
 *              / \
 *             B   C         ← min の手番
 *            /|   |\
 *           D E   F G       ← 葉。末端評価値だけが意味を持つ
 *          3.0 2.0 1.0 4.0
 * </pre>
 *
 * 期待結果: max( min(3.0, 2.0), min(1.0, 4.0) ) = max(2.0, 1.0) = 2.0
 *
 * <h2>unit1 への橋渡し</h2>
 * このクラスは、unit1 で扱うオセロの世界を最小限まで簡略化した抽象例である。
 * <pre>
 *   unit0 の概念         ↔  unit1 (オセロ) の概念
 *   ─────────────────────────────────────────────
 *   Node                ↔  ap26.Board       (世界の状態)
 *   Move                ↔  ap26.Move        (状態遷移の指示)
 *   node.perform(move)  ↔  board.placed(move)        (状態遷移を適用)
 *   node.getMoves()     ↔  board.findLegalMoves(c)   (合法手の列挙)
 *   node.isGoal()       ↔  board.isEnd()             (終局判定)
 *   Eval.value(node)    ↔  MyEval.value(board)       (末端評価)
 * </pre>
 * ゲーム木探索の本質はこの 6 要素にあり、オセロ・チェス・将棋など多くの
 * 二人完全情報ゲームが、この同じ枠組みで実装できる。本ファイルはその
 * 「枠組みそのもの」を最小例で示すための教材である。
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
class Node {
  final int[] cells;//0を空、1をマル、-1をバツとする
  int turn;//手番。1をマル、-1をバツとする
  static final int[][] LINES = {
  {0,1,2},{3,4,5},{6,7,8},
  {0,3,6},{1,4,7},{2,5,8},
  {0,4,8},{2,4,6}};//勝利条件の8パターン。これが葉の代わりとなる

  //Mapの代わり。Mapと比べ、値を持たない(それはそう)
  Node(){
    this.cells = new int[9];
    this.turn = 1;
  }

  Node(int[] cells, int turn) {
    this.cells = cells;
    this.turn = turn;
  }

  int getTurn() { return this.turn; }

  /**
   * 指定された手を適用して得られる新しい状態を返す。
   * <p>unit1 では {@code board.placed(move)} に対応する。
   * ある手を得たら、現在の盤面をコピーしてその手を適用した(moveに投げて)新しい盤面を返す。
   */
  Node perform(Move move) {
    int[] newCells = Arrays.copyOf(this.cells, this.cells.length);
    newCells[move.idx] = this.turn;
    return new Node(newCells, -this.turn);
  }

  /**
   * 指せる手のリスト。
   * <p>unit1 では {@code board.findLegalMoves(color)} に対応する。
   * 盤面を見て、空いているマスに置く手を列挙する。
   */
  List<Move> getMoves() {
    List<Move> result = new ArrayList<>();
    for (int i = 0; i < this.cells.length; i++) {
      if (this.cells[i] == 0) {
        result.add(new Move(i));
      }
    }
    return result;
  }

  /**
   * 終局か。
   * <p>unit1 では {@code board.isEnd()} に対応する。
   * unit1 オセロと恐らく同じように「双方とも合法手がない」「どちらかが勝利」で終局を判定する。
   */
  boolean isGoal() {
    return winner() != 0 || getMoves().isEmpty();
  }

  /**
   * 勝者判定: 1 = マル の勝ち, -1 = バツ の勝ち, 0 = なし
   * ノードの葉の代わり。
   */
  int winner() {
    for (int[] l : LINES){
      int s = this.cells[l[0]] + this.cells[l[1]] + this.cells[l[2]];
      if (s == 3) return Integer.MAX_VALUE;
      if (s == -3) return Integer.MIN_VALUE;
    }
    return 0;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int r = 0; r < 3; r++) {
      for (int c = 0; c < 3; c++) {
        int v = this.cells[r * 3 + c];
        char ch = v == 1 ? '◌' : (v == -1 ? '×' : '.');
        sb.append(ch);
        if (c < 2) sb.append(' ');
      }
      if (r < 2) sb.append('\n');
    }
    sb.append("  turn=").append(this.turn);
    return sb.toString(); // 今の盤面と手番を文字列化（3列ごと改行）
  }
}

/**
 * 状態遷移の指示。
 * <p>unit1 の {@link ap26.Move} に対応する抽象。unit0 では「行き先のノード名」
 * だけを持つ最小実装だが、unit1 ではマス位置 {@code index} と石の色 {@code color}
 * を持つ。
 *
 * <p>状態 (Node) と状態遷移 (Move) を別クラスに分けるのは、ゲーム木探索の
 * 基本構造で「世界の状態」と「世界を変える指示」を分離するためである。
 */
class Move {
  /** 行き先のノード名。unit1 では index + color に相当する。*/
  final int idx;
  Move(int idx) { this.idx = idx; }
  int row() { return idx / 3; }//行はindexを3で割った商 ex) idx = 2 -> row = 0
  int col() { return idx % 3; }//列はindexを3で割った余り ex) idx = 2 -> col = 2
  @Override public String toString() { return String.format("(%d,%d)", row(), col()); }
}

/**
 * ノードの評価関数。
 * <p>unit1 の {@code MyEval} に対応する抽象。unit0 では事前に決めた葉の値を
 * 返すだけだが、unit1 では盤面の各マスに重みをかけて合計するなど、より
 * 複雑な計算を行う。
 *
 * <p>評価関数自体はインスタンス間で状態を持たないため、{@code shared} という
 * 単一インスタンスを使い回す（シングルトン）。これにより各探索ノードで
 * {@code new Eval()} する無駄を省ける。
 */
class Eval {
  static final Eval shared = new Eval();

  float value(Node node) {
    int w = node.winner();
    if (w == Integer.MAX_VALUE) return 1.0f;    // マルの勝ち
    if (w == Integer.MIN_VALUE) return -1.0f;  // バツの勝ち
    if (node.getMoves().isEmpty()) return 0.0f; // 引き分け

    float score = 0f;
    for (int[] l : Node.LINES){
      int sum = node.cells[l[0]] + node.cells[l[1]] + node.cells[l[2]];
      if (sum == 2) score += 0.5f;   //自分が2つ並んでいると強い
      else if (sum == -2) score -= 0.5f;
      else if (sum == 1) score += 0.1f;
      else if (sum == -1) score -= 0.1f;
    }
    return score;
  }
}
