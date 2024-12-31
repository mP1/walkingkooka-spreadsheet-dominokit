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

import walkingkooka.Binary;
import walkingkooka.net.UrlFragment;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link File} that uses a {@link Binary} to hold the file content. This will be the {@link File} used within tests.
 */
final class FileBinary extends File {

    static FileBinary parseFileBinary(final String value) {
        Objects.requireNonNull(value, "value");

        return new FileBinary(
                Binary.with(
                        Base64.getDecoder()
                                .decode(value)
                )
        );
    }

    static FileBinary withBinary0(final Binary binary) {
        return new FileBinary(
                Objects.requireNonNull(binary, "binary")
        );
    }

    private FileBinary(final Binary binary) {
        this.binary = binary;
    }

    @Override
    public Optional<elemental2.dom.File> file() {
        throw new UnsupportedOperationException();
    }

    // HasUrlFragment...................................................................................................

    /**
     * Base64 encode the {@link #binary}.
     */
    @Override
    public UrlFragment urlFragment() {
        return UrlFragment.with(
                Base64.getEncoder()
                        .encodeToString(
                                this.binary.value()
                        )
        );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.binary.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof FileBinary && this.equals0((FileBinary) other);
    }

    private boolean equals0(final FileBinary other) {
        return this.binary.equals(other.binary);
    }

    @Override
    public String toString() {
        return this.binary.toString();
    }

    private final Binary binary;
}
