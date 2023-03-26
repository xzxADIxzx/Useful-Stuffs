package useful.text;

import arc.func.Cons;
import arc.func.Cons2;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;
import useful.Action;
import useful.Formatter;
import useful.Interface;
import useful.State;
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

            view.show();
            return view;
        });
    }

    public class TextInputView extends View {
        public String title = "";
        public String content = "";

        public int textLength = 32;
        public String defaultText = "";
        public boolean numeric = false;

        public TextInputAction result = (input, text) -> {};
        public TextInputAction closed = (input, text) -> {};

        public TextInputView(Player player, State state, Interface<TextInputView>.View previous) {
            super(player, state, previous);
        }

        @Override
        public void show() {
            Call.textInput(player.con, id, title, content, textLength, defaultText, numeric);
        }

        public TextInputView title(String title, Object... values) {
            this.title = Formatter.format(title, player, values);
            return this;
        }

        public TextInputView content(String content, Object... values) {
            this.content = Formatter.format(content, player, values);
            return this;
        }

        public TextInputView defaultText(String defaultText, Object... values) {
            this.defaultText = Formatter.format(defaultText, player, values);
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

        public TextInputView result(TextInputAction result) {
            this.result = result;
            return this;
        }

        public TextInputView result(Cons<String> result) {
            return result((input, text) -> result.get(text));
        }

        public TextInputView closed(TextInputAction closed) {
            this.closed = closed;
            return this;
        }

        public TextInputView closed(Runnable runnable) {
            return closed((input, text) -> runnable.run());
        }

        @FunctionalInterface
        public interface TextInputAction extends Action<TextInputView> {

            static TextInputAction of(TextInputAction action) {
                return action;
            }

            void get(TextInputView input, String text);

            @Override
            default void get(TextInputView input) {
                get(input, null);
            }
        }
    }
}