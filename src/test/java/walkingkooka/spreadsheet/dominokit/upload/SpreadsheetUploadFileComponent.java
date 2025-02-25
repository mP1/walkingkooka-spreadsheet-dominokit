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

package walkingkooka.spreadsheet.dominokit.upload;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a
 */
public final class SpreadsheetUploadFileComponent extends SpreadsheetUploadFileComponentLike {

    public static SpreadsheetUploadFileComponent empty(final String id) {
        return new SpreadsheetUploadFileComponent()
            .setId(id);
    }

    private SpreadsheetUploadFileComponent() {
        super();
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public SpreadsheetUploadFileComponent setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.id = id;
        return this;
    }

    private String id;

    @Override
    public Optional<BrowserFile> value() {
        return this.value;
    }

    @Override
    public SpreadsheetUploadFileComponent setValue(final Optional<BrowserFile> value) {
        this.value = value;
        return this;
    }

    private Optional<BrowserFile> value = Optional.empty();

    @Override
    public SpreadsheetUploadFileComponent setLabel(final String label) {
        this.label = Objects.requireNonNull(label, "label");
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label = "";

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    @Override
    public SpreadsheetUploadFileComponent setHelperText(final Optional<String> text) {
        this.helperText = Objects.requireNonNull(text, "text");
        return this;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetUploadFileComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled = false;

    @Override
    public SpreadsheetUploadFileComponent addChangeListener(final ChangeListener<Optional<BrowserFile>> listener) {
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent focus() {
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent setCssText(final String css) {
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent setCssProperty(final String name,
                                                         final String value) {
        return this;
    }

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }
}
