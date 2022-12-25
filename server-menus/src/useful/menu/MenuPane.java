package useful.menu;

import java.util.Arrays;

public class MenuPane {
    public String title = "";
    public String content = "";

    public MenuOption[][] options = new MenuOption[0][0];

    public MenuPane setTitle(String title) {
        this.title = title;
        return this;
    }

    public MenuPane setContent(String content) {
        this.content = content;
        return this;
    }

    public MenuOption[][] getOptions() {
        return options;
    }

    public MenuPane setOptions(MenuOption[][] options) {
        this.options = options;
        return this;
    }

    public MenuOption getOption(int x, int y) {
        return options[y][x];
    }

    public MenuOption getOption(int id) {
        int i = 0;
        for (var row : options) {
            i += row.length;
            if (i > id)
                return row[id - i + row.length];
        }
        return null;
    }

    public MenuPane setOption(int x, int y, MenuOption option) {
        this.options[y][x] = option;
        return this;
    }

    public MenuOption[] getOptionRow(int y) {
        return options[y];
    }

    public MenuPane setOptionRow(int y, MenuOption... options) {
        this.options[y] = options;
        return this;
    }

    public MenuPane addOptionRow(MenuOption... options) {
        this.options = Arrays.copyOf(this.options, this.options.length + 1);
        this.options[options.length - 1] = options;
        return this;
    }
}