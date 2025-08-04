package game2048rendering;

/**
 * 表示 2048 游戏棋盘上一个带数字的方块图像。
 * @author P. N. Hilfinger
 */
public class Tile {

    /**
     * 创建一个值为 VALUE 的新方块，位置为 (x, y)。
     * 构造方法为私有，因此所有方块只能通过工厂方法 create 创建。
     */
    private Tile(int value, int x, int y) {
        this._value = value;
        this._x = x;
        this._y = y;
        this._next = null;
        this._merged  = false;
    }

    /** 返回该方块是否已经被合并过。 */
    public boolean wasMerged() {
        return _merged;
    }

    /** 设置该方块的合并状态。 */
    void setMerged(boolean merged) {
        this._merged = merged;
    }

    /** 返回该方块当前的 y 坐标。 */
    int y() {
        return _y;
    }

    /** 返回该方块当前的 x 坐标。 */
    int x() {
        return _x;
    }

    /** 返回该方块的值（由构造器提供）。 */
    public int value() {
        return _value;
    }

    /**
     * 返回该方块的下一个状态。
     * 在移动或合并之前，它的“后继”就是它自己。
     */
    Tile next() {
        return _next == null ? this : _next;
    }

    /** 设置该方块在移动或合并之后的后继状态。 */
    void setNext(Tile otherTile) {
        _next = otherTile;
    }

    /** 在 (x, y) 位置创建一个值为 VALUE 的新方块。 */
    public static Tile create(int value, int x, int y) {
        return new Tile(value, x, y);
    }

    /**
     * 返回该方块与其后继方块之间在行或列方向上的距离。
     * 如果没有后继方块，则返回 0。
     */
    int distToNext() {
        if (_next == null) {
            return 0;
        } else {
            return Math.max(Math.abs(_y - _next.y()),
                    Math.abs(_x - _next.x()));
        }
    }

    @Override
    public String toString() {
        return String.format("Tile %d at position (%d, %d)", value(), x(), y());
    }

    /** 方块的值。 */
    private final int _value;

    /** 方块在棋盘上的最后位置（x 坐标）。 */
    private final int _x;

    /** 方块在棋盘上的最后位置（y 坐标）。 */
    private final int _y;

    /** 标志该方块是否已经合并过。 */
    private boolean _merged;

    /** 后继方块：指我将移动到或与之合并的那个方块。 */
    private Tile _next;
}
