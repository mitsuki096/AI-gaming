package tictactoe;

public class GenPlayer implements Player {

  public int depthLimit = 9;
  public int visited = 0;
  private Eval eval = new GenEval();

  public float maxSearch(Node node, float alpha, float beta, int depth) {
    this.visited++;
    if (isTerminal(node, depth)) {
      return eval.value(node);//GenPlayerでの評価関数を使用
    }

    for (Move move : node.getMoves()) {
      Node nextNode = node.perform(move);
      float childValue = minSearch(nextNode, alpha, beta, depth + 1);

      alpha = Math.max(alpha, childValue);

      if (alpha >= beta) {
        break;
      }
    }

    return alpha;
  }

  public float minSearch(Node node, float alpha, float beta, int depth) {
    this.visited++;
    if (isTerminal(node, depth)) {
      return eval.value(node);//GenPlayerでの評価関数を使用
    }

    for (Move move : node.getMoves()) {
      Node nextNode = node.perform(move);
      float childValue = maxSearch(nextNode, alpha, beta, depth + 1);

      beta = Math.min(beta, childValue);

      if (alpha >= beta) {
        break;
      }
    }

    return beta;
  }

  public boolean isTerminal(Node node, int depth) {
    return node.isGoal() || depth > this.depthLimit;
  }

  @Override
  public Move think(Node node) {
    float alpha = Float.NEGATIVE_INFINITY;
    float beta = Float.POSITIVE_INFINITY;
    float best;
    Move bestMove = null;
    // もし自分が先行（マル: turn=1）なら、評価値を最大化したい
    if (node.getTurn() == 1) {
      best = Float.NEGATIVE_INFINITY;
      for (Move m : node.getMoves()) {
        Node child = node.perform(m);
        // 次は相手なので minSearch
        float val = minSearch(child, alpha, beta, 1);
        if (val > best) {
          best = val;
          bestMove = m;
        }
        alpha = Math.max(alpha, val);
        if (alpha >= beta) break;
      }
    } 
    // もし自分が後攻（バツ: turn=-1）なら、評価値を最小化したい
    else {
      best = Float.POSITIVE_INFINITY;
      for (Move m : node.getMoves()) {
        Node child = node.perform(m);
        // 次は相手（先手）なので maxSearch を呼ぶ！
        float val = maxSearch(child, alpha, beta, 1);
        if (val < best) {  // 値が小さい（マイナスに大きい）ほど良い
          best = val;
          bestMove = m;
        }
        beta = Math.min(beta, val);
        if (alpha >= beta) break;
      }
    }
    return bestMove;
  }

  //追加
  class GenEval extends Eval {
    @Override
    protected float calculateScore(Node node) {
        float score = 0f;
        for (int[] l : Node.LINES){
            int sum = node.cells[l[0]] + node.cells[l[1]] + node.cells[l[2]];
            if (sum == 2) score += 50.0f;
            else if (sum == -2) score -= 100.0f;//ピンチに大きく注意するように
            else if (sum == 1) score += 0.1f;
            else if (sum == -1) score -= 0.1f;
        }
        return score;
    }
}
}
