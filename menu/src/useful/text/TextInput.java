package useful.text;

import arc.func.*;
import arc.util.Strings;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import useful.*;
import useful.State.StateKey;
import useful.text.TextInput.TextInputView;

public class TextInput extends Interface<TextInputView> {

    @Override
    public int register() {
        return Menus.registerTextInput((player, text) -> {
            var view = views.remove(player);
            if (view == null) return;

            if (text == null) view.closed.get(view);
            else view.result.get(view, text);
        });
    }

    @Override
    public TextInputView show(Player player, State state, View previous) {
        return views.get(player, () -> {
            var view = new TextInputView(player, state, previous);
            transformers.each(transformer -> transformer.get(view));

            if (player.con.mobile) {
                view.title = Strings.stripColors(Strings.stripGlyphs(view.title));
                view.content = Strings.stripColors(Strings.stripGlyphs(view.content));
            }

            Call.textInput(player.con, id, view.title, view.content, view.textLength, view.defaultText, view.numeric);
            return view;
        });
    }

    @Override
    public void hide(Player player) {}

    @Override
    public TextInput transform(Cons<TextInputView> transformer) {
        return (TextInput) super.transform(transformer);
    }

    @Override
    public <T1> TextInput transform(StateKey<T1> key, Cons2<TextInputView, T1> transformer) {
        return (TextInput) super.transform(view -> transformer.get(view, view.state.get(key)));
    }

    @Override
    public <T1, T2> TextInput transform(StateKey<T1> key1, StateKey<T2> key2, Cons3<TextInputView, T1, T2> transformer) {
        return (TextInput) super.transform(view -> transformer.get(view, view.state.get(key1), view.state.get(key2)));
    }

    public class TextInputView extends View {
        public String title = "";
        public String content = "";

        public int textLength = 32;
        public String defaultText = "";
        public boolean numeric = false;

        public Action2<TextInputView, String> result = Action2.none();
        public Action<TextInputView> closed = Action.none();

        public TextInputView(Player player, State state, Interface<TextInputView>.View previous) {
            super(player, state, previous);
        }

        public TextInputView title(String title, Object... values) {
            this.title = Bundle.format(title, player, values);
            return this;
        }

        public TextInputView content(String content, Object... values) {
            this.content = Bundle.format(content, player, values);
            return this;
        }

        public TextInputView defaultText(String defaultText, Object... values) {
            this.defaultText = Bundle.format(defaultText, player, values);
            return this;
        }

        public TextInputView textLength(int textLength) {
            this.textLength = textLength;
            return this;
        }

        public TextInputView numeric(boolean numeric) {
            this.numeric = numeric;
            return this;
        }

        public TextInputView result(Cons<String> result) {
            this.result = Action2.get(result);
            return this;
        }

        public TextInputView result(Action2<TextInputView, String> result) {
            this.result = result;
            return this;
        }

        public TextInputView closed(Runnable closed) {
            this.closed = Action.run(closed);
            return this;
        }

        public TextInputView closed(Action<TextInputView> closed) {
            this.closed = closed;
            return this;
        }
    }
}