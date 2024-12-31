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

package walkingkooka.spreadsheet.dominokit.file;

import elemental2.dom.FileReader;
import elemental2.dom.ProgressEvent;
import walkingkooka.Binary;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.fetcher.Fetcher;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link File} that uses a {@link Binary} to hold the file content. This will be the {@link File} used within tests.
 */
final class FileElemental extends File {

    static FileElemental parseElemental(final String value) {
        Objects.requireNonNull(value, "value");

        return new FileElemental(
                null,
                value // base64
        );
    }

    static FileElemental withFile0(final elemental2.dom.File file) {
        return new FileElemental(
                Objects.requireNonNull(file, "file"),
                null // base64
        );
    }

    private FileElemental(final elemental2.dom.File file,
                          final String base64) {
        this.file = file;
        this.base64 = base64;

        // base64 missing use FileReader to read as dataUrl and
        if (null != file && null == base64) {
            final FileReader fileReader = new FileReader();
            fileReader.onload = e -> this.fileReaderOnLoad(
                    (ProgressEvent<FileReader>) e
            );
            fileReader.readAsDataURL(file);
        }

        // if file is missing, let Fetcher decode 64 into a Blob
    }

    private Object fileReaderOnLoad(final ProgressEvent<FileReader> event) {
        FileReader fileReader = (FileReader) event.target;


        final String dataUrl = fileReader.result.asString();
        final int startOfBase64 = dataUrl.indexOf(",");
        this.base64 = dataUrl.substring(startOfBase64 + 1);

        return null;
    }

    /**
     * Getter only intended to be called by {@link Fetcher}.
     * <br>
     * The file may be absent if {@link #parse(String)} was used to create this instance.
     */
    @Override
    public Optional<elemental2.dom.File> file() {
        return Optional.ofNullable(this.file);
    }

    // HasUrlFragment...................................................................................................

    /**
     * Assumes the base64 encoding when required has completed and is available.
     */
    @Override
    public UrlFragment urlFragment() {
        if(null == this.base64) {
            throw new IllegalStateException("Missing base64 form of file " + this.file.name);
        }
        return UrlFragment.with(this.base64);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.file.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof FileElemental && this.equals0((FileElemental) other);
    }

    private boolean equals0(final FileElemental other) {
        boolean equals = false;

        // not a perfect equals because it is possible that both instances may different properties set.
        final elemental2.dom.File file = this.file;
        if(null != file) {
            equals = file.equals(other.file);
        }

        final String base64 = this.base64;
        if(null != base64) {
            equals = equals && base64.equals(other.base64);
        }

        return equals;
    }

    @Override
    public String toString() {
        return this.file.toString();
    }

    private elemental2.dom.File file;

    private String base64;
}
