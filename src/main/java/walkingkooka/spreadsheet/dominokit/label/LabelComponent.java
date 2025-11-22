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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.HTMLElement;
import org.dominokit.domino.ui.labels.Label;

import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.style.ColorsCss.dui_bg_accent;
import static org.dominokit.domino.ui.style.SpacingCss.dui_m_4;
import static org.dominokit.domino.ui.style.SpacingCss.dui_rounded_full;

public final class LabelComponent extends LabelComponentLike {

    public static LabelComponent empty() {
        return new LabelComponent();
    }

    private LabelComponent() {
        super();
        this.label = Label.create("")
            .addCss(dui_m_4, dui_bg_accent, dui_rounded_full);
    }

    @Override
    public LabelComponent setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.value = value;
        this.label.setText(
            value.orElse("")
        );
        return this;
    }

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value;

    // HtmlElementComponent.............................................................................................

    @Override
    public String id() {
        return this.label.getId();
    }

    @Override
    public LabelComponent setId(final String id) {
        this.label.setId(id);
        return this;
    }

    @Override
    public int width() {
        return this.label.element()
            .offsetWidth;
    }

    @Override
    public int height() {
        return this.label.element()
            .offsetHeight;
    }

    @Override
    public LabelComponent setCssText(final String css) {
        this.label.element()
            .style
            .cssText = css;
        return this;
    }

    @Override
    public LabelComponent setCssProperty(final String name,
                                         final String value) {
        this.label.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public LabelComponent removeCssProperty(String name) {
        this.label.removeCssProperty(name);
        return this;
    }

    @Override
    public HTMLElement element() {
        return this.label.element();
    }

    private final Label label;
}
