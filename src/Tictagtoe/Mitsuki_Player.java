package Tictagtoe;

public class Mitsuki_Player implements Player {
    public int depthLimit = 4;

    public float maxSearch(Node node, float alpha, float beta, int depth) {
        if (isTerminal(node, depth)) {
            return Eval.shared.value(node);
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
        if (isTerminal(node, depth)) {
            return Eval.shared.value(node);
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
        return node.isGoal() || depth >= this.depthLimit;
    }

    public Move think(Node node) {
        float bestValue = Float.NEGATIVE_INFINITY;
        Move bestMove = null;

        for (Move move : node.getMoves()) {
            Node nextNode = node.perform(move);

            float childValue = minSearch(nextNode, bestValue, Float.POSITIVE_INFINITY, 1);

            if (childValue > bestValue) {
                bestValue = childValue;
                bestMove = move;
            }
        }
        return bestMove;
    }
}
