package tictactoe;
public class Main {
        public static void main(String[] args) {
        // 1. 初期状態（空の盤面）の生成
        Node currentNode = new Node();

        // 2. プレイヤーの準備（ここではメンバーAとメンバーBのAI）
        Player playerA = new GenPlayer();
        Player playerB = new AlphaBetaPlayer();

        // 3. ゲームループ
        while (!currentNode.isGoal()) {
            // 現在の盤面を描画（標準出力など）
            System.out.println();
            System.out.println(currentNode);

            Move nextMove;
            if (currentNode.getTurn() == 1) {
                // 先手の番：プレイヤーAに次の手を考えさせる
                nextMove = playerA.think(currentNode);
            } else {
                // 後手の番：プレイヤーBに次の手を考えさせる
                nextMove = playerB.think(currentNode);
            }

            // 盤面を更新（新しい状態の生成）
            currentNode = currentNode.perform(nextMove);
        }

        // 4. 終局後の処理（勝敗判定と結果表示）
        //printBoard(currentNode);
        float finalScore = Eval.shared.value(currentNode);
        
        if (finalScore > 0) {
            System.out.println(playerA + "先手(A)の勝ち！");
        } else if (finalScore < 0) {
            System.out.println(playerB + "後手(B)の勝ち！");
        } else {
            System.out.println("引き分け！");
        }
    } 
}
