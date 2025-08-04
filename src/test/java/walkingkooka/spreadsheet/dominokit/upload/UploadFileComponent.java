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
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * Wraps a
 */
public final class UploadFileComponent extends UploadFileComponentLike
    implements TestHtmlElementComponent<HTMLDivElement, UploadFileComponent> {

    public static UploadFileComponent empty(final String id) {
        return new UploadFileComponent()
            .setId(id);
    }

    private UploadFileComponent() {
        super();
    }

    @Override
    public String id() {
        return this.id;
    }

    @Override
    public UploadFileComponent setId(final String id) {
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
    public UploadFileComponent setValue(final Optional<BrowserFile> value) {
        this.value = value;
        return this;
    }

    private Optional<BrowserFile> value = Optional.empty();

    @Override
    public UploadFileComponent setLabel(final String label) {
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
    public UploadFileComponent setHelperText(final Optional<String> text) {
        this.helperText = Objects.requireNonNull(text, "text");
        return this;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public UploadFileComponent setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled = false;

    @Override
    public UploadFileComponent addContextMenuListener(final EventListener listener) {
        return this;
    }

    @Override
    public UploadFileComponent addChangeListener(final ChangeListener<Optional<BrowserFile>> listener) {
        return this;
    }

    @Override
    public UploadFileComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public UploadFileComponent focus() {
        return this;
    }
}
