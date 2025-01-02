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
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.UploadBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

import java.util.Optional;

/**
 * Wraps a {@link UploadBox}.
 */
public final class SpreadsheetUploadFileComponent extends SpreadsheetUploadFileComponentLike {

    public static SpreadsheetUploadFileComponent empty(final String id) {
        return new SpreadsheetUploadFileComponent()
                .setId(id);
    }

    private SpreadsheetUploadFileComponent() {
        super();
        this.uploadBox = new UploadBox();
    }

    @Override
    public String id() {
        return this.uploadBox.getId();
    }

    @Override
    public SpreadsheetUploadFileComponent setId(final String id) {
        this.uploadBox.setId(id);
        return this;
    }

    @Override
    public Optional<BrowserFile> value() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetUploadFileComponent setValue(final Optional<BrowserFile> value) {
        return this;
    }

    @Override 
    public boolean isDisabled() {
        return this.uploadBox.isDisabled();
    }

    @Override
    public SpreadsheetUploadFileComponent setDisabled(final boolean disabled) {
        this.uploadBox.setDisabled(disabled);
        return this;
    }

    @Override 
    public SpreadsheetUploadFileComponent addChangeListener(final ChangeListener<Optional<BrowserFile>> listener) {
        this.uploadBox.addChangeListener(null);
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent addFocusListener(final EventListener listener) {
        this.uploadBox.addEventListener(
                EventType.focus,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent focus() {
        this.uploadBox.focus();
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent setCssText(final String css) {
        this.element().style.cssText = css;
        return this;
    }

    @Override 
    public HTMLFieldSetElement element() {
        return this.uploadBox.element();
    }

    private final UploadBox uploadBox;
}
