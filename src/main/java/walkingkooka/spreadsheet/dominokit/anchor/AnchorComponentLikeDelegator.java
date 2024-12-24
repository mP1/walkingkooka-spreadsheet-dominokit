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

package walkingkooka.spreadsheet.dominokit.anchor;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public interface AnchorComponentLikeDelegator<A extends AnchorComponentLike<A>> extends AnchorComponentLike<A>,
        HtmlElementComponentDelegator<HTMLAnchorElement, A> {

    @Override
    default boolean isDisabled() {
        return this.anchorComponentLike()
                .isDisabled();
    }

    @Override
    default A setDisabled(final boolean disabled) {
        this.anchorComponentLike()
                .setDisabled(disabled);
        return (A) this;
    }

    @Override
    default boolean isChecked() {
        return this.anchorComponentLike().isChecked();
    }

    @Override
    default A setChecked(final boolean checked) {
        this.anchorComponentLike()
                .setChecked(checked);
        return (A) this;
    }

    @Override
    default Url href() {
        return this.anchorComponentLike()
                .href();
    }

    @Override
    default A setHref(final Url url) {
        this.anchorComponentLike()
                .setHref(url);
        return (A) this;
    }

    @Override
    default String id() {
        return this.anchorComponentLike()
                .id();
    }

    @Override
    default A setId(final String id) {
        this.anchorComponentLike()
                .setId(id);
        return (A) this;
    }

    @Override
    default int tabIndex() {
        return this.anchorComponentLike()
                .tabIndex();
    }

    @Override
    default A setTabIndex(final int tabIndex) {
        this.anchorComponentLike()
                .setTabIndex(tabIndex);
        return (A) this;
    }

    @Override
    default String target() {
        return this.anchorComponentLike()
                .target();
    }

    @Override
    default A setTarget(final String target) {
        this.anchorComponentLike()
                .setTarget(target);
        return (A) this;
    }

    @Override
    default String textContent() {
        return this.anchorComponentLike()
                .textContent();
    }

    @Override
    default A setTextContent(final String text) {
        this.anchorComponentLike()
                .setTextContent(text);
        return (A) this;
    }

    @Override
    default Optional<Icon<?>> iconBefore() {
        return this.anchorComponentLike()
                .iconBefore();
    }

    @Override
    default A setIconBefore(final Optional<Icon<?>> icon) {
        this.anchorComponentLike().setIconBefore(icon);
        return (A) this;
    }

    @Override
    default Optional<Icon<?>> iconAfter() {
        return this.anchorComponentLike().iconAfter();
    }

    @Override
    default A setIconAfter(final Optional<Icon<?>> icon) {
        this.anchorComponentLike().setIconAfter(icon);
        return (A) this;
    }

    @Override default A addClickListener(final EventListener listener) {
        this.anchorComponentLike()
                .addClickListener(listener);
        return (A) this;
    }

    @Override
    default A addFocusListener(final EventListener listener) {
        this.anchorComponentLike()
                .addFocusListener(listener);
        return (A) this;
    }

    @Override
    default A addKeydownListener(final EventListener listener) {
        this.anchorComponentLike()
                .addKeydownListener(listener);
        return (A) this;
    }

    @Override
    default A addClickAndKeydownEnterListener(final EventListener listener) {
        this.anchorComponentLike()
                .addClickAndKeydownEnterListener(listener);
        return (A) this;
    }

    @Override
    default A focus() {
        this.anchorComponentLike()
                .focus();
        return (A) this;
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        // continuing the anchor tradition, delegate to the anchor#printTree and do not print this#class#simpleName
        this.anchorComponentLike()
                .printTree(printer);
    }

    /**
     * The {@link AnchorComponentLike} delegate target.
     */
    AnchorComponentLike<?> anchorComponentLike();

    // HtmlElementComponent.............................................................................................

    @Override
    default HtmlElementComponent<HTMLAnchorElement, ?> htmlElementComponent() {
        return this.anchorComponentLike();
    }
}
