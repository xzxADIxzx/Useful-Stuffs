package useful.menu.impl;

import mindustry.gen.Player;
import useful.menu.view.Action;
import useful.menu.view.Menu;

public class ConfirmMenu extends Menu {
    public String confirm, deny;

    public ConfirmMenu set(String confirm, String deny) {
        this.confirm = confirm;
        this.deny = deny;

        return this;
    }

    public void show(Player player, String title, String content, Runnable confirmed, Object... values) {
        show(player, title, content, confirmed, () -> {}, values);
    }

    public void show(Player player, String title, String content, Runnable confirmed, Runnable denied, Object... values) {
        show(player, menu -> {
            menu.title(title);
            menu.content(content, values);

            menu.addOption(confirm, Action.run(confirmed));
            menu.addOption(deny, Action.run(denied));
        });
    }
}