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

import elemental2.dom.HTMLAnchorElement;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.net.Url;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public interface AnchorComponentDelegator<A extends AnchorComponent<A, T>, T> extends AnchorComponent<A, T>,
    HtmlComponentDelegator<HTMLAnchorElement, A> {

    @Override
    default boolean isDisabled() {
        return this.anchorComponent()
            .isDisabled();
    }

    @Override
    default A setDisabled(final boolean disabled) {
        this.anchorComponent()
            .setDisabled(disabled);
        return (A) this;
    }

    @Override
    default boolean isChecked() {
        return this.anchorComponent().isChecked();
    }

    @Override
    default A setChecked(final boolean checked) {
        this.anchorComponent()
            .setChecked(checked);
        return (A) this;
    }

    @Override
    default Url href() {
        return this.anchorComponent()
            .href();
    }

    @Override
    default A setHref(final Url url) {
        this.anchorComponent()
            .setHref(url);
        return (A) this;
    }

    @Override
    default String id() {
        return this.anchorComponent()
            .id();
    }

    @Override
    default A setId(final String id) {
        this.anchorComponent()
            .setId(id);
        return (A) this;
    }

    @Override
    default int tabIndex() {
        return this.anchorComponent()
            .tabIndex();
    }

    @Override
    default A setTabIndex(final int tabIndex) {
        this.anchorComponent()
            .setTabIndex(tabIndex);
        return (A) this;
    }

    @Override
    default String target() {
        return this.anchorComponent()
            .target();
    }

    @Override
    default A setTarget(final String target) {
        this.anchorComponent()
            .setTarget(target);
        return (A) this;
    }

    @Override
    default String textContent() {
        return this.anchorComponent()
            .textContent();
    }

    @Override
    default A setTextContent(final String text) {
        this.anchorComponent()
            .setTextContent(text);
        return (A) this;
    }

    @Override
    default String badge() {
        return this.anchorComponent().badge();
    }

    @Override
    default A setBadge(final String text) {
        this.anchorComponent().setBadge(text);
        return (A) this;
    }

    @Override
    default Optional<Icon<?>> iconBefore() {
        return this.anchorComponent()
            .iconBefore();
    }

    @Override
    default A setIconBefore(final Optional<Icon<?>> icon) {
        this.anchorComponent().setIconBefore(icon);
        return (A) this;
    }

    @Override
    default Optional<Icon<?>> iconAfter() {
        return this.anchorComponent().iconAfter();
    }

    @Override
    default A setIconAfter(final Optional<Icon<?>> icon) {
        this.anchorComponent().setIconAfter(icon);
        return (A) this;
    }

    @Override
    default A focus() {
        this.anchorComponent()
            .focus();
        return (A) this;
    }

    @Override
    default boolean isEditing() {
        return this.anchorComponent()
            .isEditing();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        // continuing the anchor tradition, delegate to the anchor#printTree and do not print this#class#simpleName
        this.anchorComponent()
            .printTree(printer);
    }

    /**
     * The {@link AnchorComponent} delegate target.
     */
    AnchorComponent<?, ?> anchorComponent();

    // HtmlComponent.............................................................................................

    @Override
    default HtmlComponent<HTMLAnchorElement, ?> htmlComponent() {
        return this.anchorComponent();
    }
}
