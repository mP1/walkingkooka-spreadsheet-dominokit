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

package walkingkooka.spreadsheet.dominokit.value.textstyle.length;

import org.dominokit.domino.ui.forms.validations.ValidationResult;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentDelegator;
import walkingkooka.text.HasText;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;

/**
 * A text box that accepts text entry and validates it as a {@link Length}.
 */
public final class LengthComponent implements ValueTextBoxComponentDelegator<LengthComponent, Length<?>> {

    public static LengthComponent with(final TextStylePropertyName<Length<?>> textStylePropertyName) {
        return new LengthComponent(
            Objects.requireNonNull(textStylePropertyName, "textStylePropertyName")
        );
    }

    private LengthComponent(final TextStylePropertyName<Length<?>> textStylePropertyName) {
        this.textBox = ValueTextBoxComponent.with(
            textStylePropertyName::parseValue,
            HasText::text
        );
        this.textBox.setValidator(
            (Optional<String> text) -> {
                ValidationResult validationResult;
                try {
                    textStylePropertyName.parseValue(
                        text.orElse("")
                    );
                    validationResult = ValidationResult.valid();
                } catch (final UnsupportedOperationException rethrow) {
                    throw rethrow;
                } catch (final RuntimeException cause) {
                    validationResult = ValidationResult.invalid(cause.getMessage());
                }
                return validationResult;
            }
        ).clearIcon();
    }

    // ValueTextBoxComponentDelegator...................................................................................

    @Override
    public ValueTextBoxComponent<Length<?>> valueTextBoxComponent() {
        return this.textBox;
    }

    private final ValueTextBoxComponent<Length<?>> textBox;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }
}