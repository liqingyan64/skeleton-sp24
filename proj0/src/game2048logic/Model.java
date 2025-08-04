package game2048logic;

import game2048rendering.Board;
import game2048rendering.Side;
import game2048rendering.Tile;

import java.util.Formatter;

/** 2048 游戏的状态。
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** 当前棋盘的内容。 */
    private final Board board;
    /** 当前得分。 */
    private int score;

    /* 坐标系统：棋盘的第 x 列，第 y 行（x = 0, y = 0 是棋盘左下角）对应 board.tile(x, y)。要小心！ */

    /** 最大方块数值。 */
    public static final int MAX_PIECE = 2048;

    /** 一个新的 2048 游戏，棋盘大小为 SIZE，无方块，得分为 0。 */
    public Model(int size) {
        board = new Board(size);
        score = 0;
    }

    /** 一个新的 2048 游戏，RAWVALUES 包含了方块的值（为 0 表示该位置为空）。
     *  VALUES 以 (x, y) 方式索引，(0, 0) 对应左下角。用于测试。 */
    public Model(int[][] rawValues, int score) {
        board = new Board(rawValues);
        this.score = score;
    }

    /** 返回当前 (x, y) 位置上的 Tile，其中 0 <= x < size(), 0 <= y < size()。
     *  如果没有方块，则返回 null。用于测试。 */
    public Tile tile(int x, int y) {
        return board.tile(x, y);
    }

    /** 返回棋盘边长。 */
    public int size() {
        return board.size();
    }

    /** 返回当前得分。 */
    public int score() {
        return score;
    }

    /** 清空棋盘并重置得分。 */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** 向棋盘添加一个 TILE。添加的位置必须为空。 */
    public void addTile(Tile tile) {
        board.addTile(tile);
    }

    /** 如果游戏结束（没有可移动的格子，或出现 2048 方块），返回 true。 */
    public boolean gameOver() {
        return maxTileExists() || !atLeastOneMoveExists();
    }

    /** 返回当前模型的棋盘。 */
    public Board getBoard() {
        return board;
    }

    /** 如果棋盘上至少有一个空格，返回 true。
     *  空格用 null 表示。 */
    public boolean emptySpaceExists() {
        // TODO: Task 2. 完成此函数。
        for (int x =0 ;x<board.size();x++){
            for(int y = 0;y<board.size();y++){
                if (board.tile(x,y) == null){
                    return true;
                }
            }
        }
        return false;
    }

    /** 如果存在值为最大值（2048）的方块，返回 true。
     *  最大值由 MAX_PIECE 给出。使用 t.value() 获取 Tile 的数值。 */
    public boolean maxTileExists() {
        // TODO: Task 3. 完成此函数。
        for (int x =0 ;x<board.size();x++){
            for(int y =0;y<board.size();y++){
                Tile t = board.tile(x,y);
                if (t == null) continue;
                if (t.value() >= 2048){
                    return true;
                }
            }
        }
        return false;
    }

    /** 如果棋盘上还有合法移动方式，返回 true。
     *  有效移动方式包括：
     *  1. 存在至少一个空格。
     *  2. 存在两个相邻方块数值相同。 */
    public boolean atLeastOneMoveExists() {
        // TODO: 完成此函数。
        //设置两种条件，至少满足两种条件之一则能至少存在一个可移动

        //1.至少存在一个空格
        if (emptySpaceExists() == true){
            return true;
        }
        //2.存在两个相邻方块数值相同
        for (int x=0;x<board.size();x++){
            //竖着的
            for (int y=0;y<board.size()-1;y++){
                Tile curr = board.tile(x, y);
                Tile next = board.tile(x, y + 1);
                if (curr != null && next != null && curr.value() == next.value()) {
                    return true;
                }
            }
            //横着的
            for(int z=0;z<board.size()-1;z++){
                if (board.tile(z,x).value() == board.tile(z+1,x).value()){
                    return true;
                }
            }
        }

        return false;
    }

    /** 将位置 (x, y) 上的方块尽可能向上移动。
     *
     * 规则：
     * 1. 如果两个相邻方块在移动方向上数值相同，它们会合并为一个新方块，值为原来的两倍，得分也会增加该值。
     * 2. 每个方块每次最多只能合并一次。
     * 3. 如果三个方块相同，只合并靠近移动方向的前两个，最后一个不会合并。 */
    public void moveTileUpAsFarAsPossible(int x, int y) {
        Tile currTile = board.tile(x, y);
        int myValue = currTile.value();
        int targetY = y;

        // TODO: Task 5, 6, 10. 完成此函数。
    }

    /** 处理第 x 列中所有方块的向上移动。
     * 观察方向已经设置好，因此此处是向上合并。 */
    public void tiltColumn(int x) {
        // TODO: Task 7. 完成此函数。
    }

    /** 处理棋盘向 SIDE 方向的合并。 */
    public void tilt(Side side) {
        // TODO: Task 8 和 9. 完成此函数。
    }

    /** 将棋盘朝 SIDE 方向整体倾斜。 */
    public void tiltWrapper(Side side) {
        board.resetMerged();
        tilt(side);
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int y = size() - 1; y >= 0; y -= 1) {
            for (int x = 0; x < size(); x += 1) {
                if (tile(x, y) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(x, y).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (game is %s) %n", score(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Model m) && this.toString().equals(m.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
