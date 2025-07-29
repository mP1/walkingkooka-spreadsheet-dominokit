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
import elemental2.dom.File;
import elemental2.dom.FileReader;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.ProgressEvent;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.icons.lib.Icons;
import org.dominokit.domino.ui.upload.DefaultFileUploadDecoration;
import org.dominokit.domino.ui.upload.FileItem;
import org.dominokit.domino.ui.upload.FileUpload;
import org.dominokit.domino.ui.upload.IsFilePreview;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.DataUrl;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.file.BrowserFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.style.ColorsCss.dui_fg_accent;
import static org.dominokit.domino.ui.style.SpacingCss.dui_font_size_12;
import static org.dominokit.domino.ui.style.SpacingCss.dui_m_b_4;

/**
 * Wraps a {@link FileUpload}.
 * <br>
 * Note that {@link #setValue(Optional)} may not be used to set a new selected file.
 */
public final class SpreadsheetUploadFileComponent extends SpreadsheetUploadFileComponentLike {

    public static SpreadsheetUploadFileComponent empty(final String id) {
        return new SpreadsheetUploadFileComponent()
            .setId(id);
    }

    private SpreadsheetUploadFileComponent() {
        super();
        this.defaultFileUploadDecoration = DefaultFileUploadDecoration.create()
            .setIcon(Icons.upload().addCss(dui_font_size_12, dui_fg_accent))
            .addCss(dui_m_b_4);
        this.label = "";
        this.helperText = Optional.empty();

        this.fileUpload = FileUpload.create(SpreadsheetUploadFileComponent::forFile)
            .setDecoration(this.defaultFileUploadDecoration)
            .manualUpload() // no auto upload
            .setMultiUpload(false)
            .setMaxAllowedUploads(1)
            .setShowPreview(true)
            .onAddFile(this::onFileItem);

        this.value = Optional.empty(); // no file
    }

    private static IsFilePreview<?> forFile(final FileItem fileItem,
                                            final FileUpload fileUpload) {
        return new DefaultFilePreview(
            fileItem,
            fileUpload
        );
    }

    @Override
    public String id() {
        return this.fileUpload.getId();
    }

    @Override
    public SpreadsheetUploadFileComponent setId(final String id) {
        this.fileUpload.setId(
            Objects.requireNonNull(id, "id")
        );
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    @Override
    public SpreadsheetUploadFileComponent setLabel(final String label) {
        Objects.requireNonNull(label, "label");

        this.label = label;
        this.defaultFileUploadDecoration.setTitle(label);
        this.label = label;

        return this;
    }

    private String label;

    @Override
    public Optional<String> helperText() {
        return this.helperText;
    }

    @Override
    public SpreadsheetUploadFileComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.defaultFileUploadDecoration.setDescription(
            text.orElse("")
        );
        this.helperText = text;

        return this;
    }

    private Optional<String> helperText;

    private final DefaultFileUploadDecoration defaultFileUploadDecoration;

    @Override
    public Optional<BrowserFile> value() {
        return this.value;
    }

    /**
     * Currently this component does not support setting a new {@link BrowserFile}.
     * Only the user may pick a file to be uploaded.
     */
    @Override
    public SpreadsheetUploadFileComponent setValue(final Optional<BrowserFile> value) {
        Objects.requireNonNull(value, "value");

        final Optional<BrowserFile> oldValue = this.value;
        if (false == oldValue.equals(value)) {
            if (value.isPresent()) {
                throw new UnsupportedOperationException();
            } else {
                this.value = value;
                this.fileUpload.removeFileItems();
            }

            this.fireChangeListeners(
                oldValue,
                value
            );
        }
        return this;
    }

    private Optional<BrowserFile> value;

    @Override
    public boolean isDisabled() {
        return this.fileUpload.isDisabled();
    }

    @Override
    public SpreadsheetUploadFileComponent setDisabled(final boolean disabled) {
        this.fileUpload.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent addChangeListener(final ChangeListener<Optional<BrowserFile>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.changeListener.add(listener);
        return this;
    }

    private void onFileItem(final FileItem fileItem) {
        final File file = fileItem.getFile();

        final FileReader fileReader = new FileReader();
        fileReader.onload = (final ProgressEvent<FileReader> progressEvent) ->
        {
            final Optional<BrowserFile> oldValue = this.value;
            final Optional<BrowserFile> newValue = Optional.of(
                BrowserFile.base64(
                    file.name,
                    DataUrl.parseData(
                        fileReader.result.asString()
                    ).data()
                )
            );
            this.value = newValue;

            this.fireChangeListeners(
                oldValue, // oldValue
                newValue
            );
            return null;
        };
        fileReader.readAsDataURL(file);

        fileItem.addRemoveHandler((removedFileIgnored) -> this.setValue(Optional.empty()));
    }

    private void fireChangeListeners(final Optional<BrowserFile> oldValue,
                                     final Optional<BrowserFile> newValue) {
        for (final ChangeListener<Optional<BrowserFile>> listener : this.changeListener) {
            listener.onValueChanged(
                oldValue,
                newValue
            );
        }
    }

    private final List<ChangeListener<Optional<BrowserFile>>> changeListener = Lists.array();

    @Override
    public SpreadsheetUploadFileComponent addFocusListener(final EventListener listener) {
        this.fileUpload.addEventListener(
            EventType.focus,
            listener
        );
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent focus() {
        this.fileUpload.element()
            .focus();
        return this;
    }

    @Override
    public boolean isEditing() {
        return HtmlElementComponent.hasFocus(this.element());
    }

    @Override
    public SpreadsheetUploadFileComponent setCssText(final String css) {
        this.element().style.cssText = css;
        return this;
    }

    @Override
    public SpreadsheetUploadFileComponent setCssProperty(final String name,
                                                         final String value) {
        this.element().style.setProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public HTMLDivElement element() {
        return this.fileUpload.element();
    }

    /**
     * The {@link FileUpload} that will accept dropped files and open a file browser when clicked.
     */
    private final FileUpload fileUpload;
}
