/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.dialog;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.dialogs.IsDialogHeight;
import org.dominokit.domino.ui.dialogs.IsDialogWidth;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;

public final class DialogComponent extends DialogComponentLike
    implements TestHtmlElementComponent<HTMLDivElement, DialogComponent> {

    /**
     * Factory that creates a new empty {@link DialogComponent}.
     */
    static DialogComponent with(final IsDialogWidth width,
                                final IsDialogHeight height,
                                final String id,
                                final boolean includeClose,
                                final DialogComponentContext context) {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(context, "context");

        return new DialogComponent(
            id,
            includeClose
        );
    }

    private DialogComponent(final String id,
                            final boolean includeClose) {
        this.id = id;
        this.title = "";
        this.includeClose = includeClose;
    }

    // width............................................................................................................

    @Override
    public int width() {
        throw new UnsupportedOperationException();
    }

    // height...........................................................................................................

    @Override
    public int height() {
        throw new UnsupportedOperationException();
    }

    // main/DialogComponent public members...................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open() {
        this.open = true;
    }

    @Override
    public void close() {
        this.open = false;
    }

    private boolean open;

    // title............................................................................................................

    @Override
    public DialogComponent setTitle(final String title) {
        this.title = title;
        return this;
    }

    @Override
    public String title() {
        return this.title;
    }

    private String title;

    // id...............................................................................................................

    private final String id;

    @Override
    public String id() {
        return this.id;
    }

    // isTitleIncludesClose.............................................................................................

    @Override
    public boolean isTitleIncludeClose() {
        return this.includeClose;
    }

    private final boolean includeClose;

    // appendChild......................................................................................................

    @Override
    public DialogComponent appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");
        this.children.add(child);
        return this;
    }

    private final List<IsElement<?>> children = Lists.array();

    // TreePrintable....................................................................................................

    @Override
    public void printTreeChildren(final IndentingPrinter printer) {
        for (final IsElement<?> child : this.children) {
            TreePrintable.printTreeOrToString(
                child,
                printer
            );
            printer.lineStart();
        }
    }
}
