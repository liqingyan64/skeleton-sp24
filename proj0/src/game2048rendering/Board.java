package game2048rendering;

import java.util.Arrays;
import java.util.Formatter;

/**
 * @author hug
 */
public class Board {
    /** 当前棋盘上的内容（每个格子是一个 Tile 或 null）。 */
    private final Tile[][] _values;
    /** 当前视角中，哪个方向被视为“北方”。 */
    private Side _viewPerspective;

    public Board(int size) {
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
    }

    /** 将棋盘视角调整为将指定方向 S 视为“北方”。 */
    public void setViewingPerspective(Side s) {
        _viewPerspective = s;
    }

    /**
     * 创建一个棋盘，RAWVALUES 表示棋盘上每个方块的值（0 表示无方块），
     * 默认视角为北方。
     */
    public Board(int[][] rawValues) {
        int size = rawValues.length;
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
        for (int x = 0; x < size; x += 1) {
            for (int y = 0; y < size; y += 1) {
                int value = rawValues[size - 1 - y][x];
                Tile tile;
                if (value == 0) {
                    tile = null;
                } else {
                    tile = Tile.create(value, x, y);
                }
                _values[x][y] = tile;
            }
        }
    }

    /** 返回棋盘的边长（大小）。 */
    public int size() {
        return _values.length;
    }

    /**
     * 返回 (x, y) 位置的 Tile，以指定方向 side 为“上方”视角时的值。
     */
    private Tile vtile(int x, int y, Side side) {
        return _values[side.x(x, y, size())][side.y(x, y, size())];
    }

    /**
     * 返回位于 (x, y) 处的当前方块（以当前视角 _viewPerspective）。
     * 如果该位置为空，返回 null。
     */
    public Tile tile(int x, int y) {
        return vtile(x, y, _viewPerspective);
    }

    /** 清空整个棋盘，所有格子设为 null。 */
    public void clear() {
        for (Tile[] column : _values) {
            Arrays.fill(column, null);
        }
    }

    /** 向棋盘上添加一个方块 Tile t。 */
    public void addTile(Tile t) {
        _values[t.x()][t.y()] = t;
    }

    /**
     * 将 TILE 放置在当前视角下的 (x, y) 位置上。
     *
     * (0, 0) 是左下角。
     *
     * 如果是合并动作，会将该 tile 的 merged 状态设为 true。
     */
    public void move(int x, int y, Tile tile) {
        int px = _viewPerspective.x(x, y, size());
        int py = _viewPerspective.y(x, y, size());

        Tile tile1 = vtile(x, y, _viewPerspective);
        _values[tile.x()][tile.y()] = null;

        // 移动或合并方块。需要调用 setNext 以便实现动画过渡效果。
        Tile next;
        if (tile1 == null) {
            next = Tile.create(tile.value(), px, py);
        } else {
            if (tile.value() != tile1.value()) {
                throw new IllegalArgumentException("尝试合并两个不同的方块值: " + tile + " 和 " + tile1);
            }
            next = Tile.create(2 * tile.value(), px, py);
            tile1.setNext(next);
        }
        tile.setMerged(tile1 != null);
        next.setMerged(tile.wasMerged());
        tile.setNext(next);
        _values[px][py] = next;
    }

    /** 将棋盘上所有方块的 merged 状态重置为 false。 */
    public void resetMerged() {
        for (int x = 0; x < size(); x += 1) {
            for (int y = 0; y < size(); y += 1) {
                if (_values[x][y] != null){
                    _values[x][y].setMerged(false);
                }
            }
        }
    }

    /** 返回棋盘的字符串表示（用于调试）。 */
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
        return out.toString();
    }
}
