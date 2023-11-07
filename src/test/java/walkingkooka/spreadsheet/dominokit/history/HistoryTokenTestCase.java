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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HistoryTokenTestCase<T extends HistoryToken> implements ClassTesting<T>, HashCodeEqualsDefinedTesting2<T>, ToStringTesting<T> {

    final static SpreadsheetId ID = SpreadsheetId.with(0x123);

    final static SpreadsheetName NAME = SpreadsheetName.with("SpreadsheetName456");

    HistoryTokenTestCase() {
        super();
    }

    // clearAction......................................................................................................

    final void clearActionAndCheck() {
        final HistoryToken token = this.createHistoryToken();

        assertSame(
                token,
                token.clearAction()
        );
    }

    final void clearActionAndCheck(final HistoryToken token,
                                   final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.clearAction(),
                () -> token + " clearAction"
        );
    }

    // setIdAndName.....................................................................................................

    @Test
    public final void testSetIdAndNameNullIdFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdAndName(
                        null,
                        NAME
                )
        );
    }

    @Test
    public final void testSetIdAndNameNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createHistoryToken().setIdAndName(
                        ID,
                        null
                )
        );
    }

    final void setIdAndNameAndCheck(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final HistoryToken expected) {
        this.setIdAndNameAndCheck(
                this.createHistoryToken(),
                id,
                name,
                expected
        );
    }

    final void setIdAndNameAndCheck(final HistoryToken token,
                                    final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setIdAndName(
                        id,
                        name
                ),
                () -> token + " id=" + id + " name=" + name
        );
    }

    // setMetadataPropertyName..........................................................................................

    @Test
    public final void testSetMetadataPropertyNameWithNullFails() {
        final T token = this.createHistoryToken();
        assertThrows(
                NullPointerException.class,
                () -> token.setMetadataPropertyName(null)
        );
    }

    final void setMetadataPropertyNameAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName) {
        this.setMetadataPropertyNameAndCheck(
                this.createHistoryToken(),
                propertyName
        );
    }

    final void setMetadataPropertyNameAndCheck(final HistoryToken token,
                                               final SpreadsheetMetadataPropertyName<?> propertyName) {
        assertSame(
                token,
                token.setMetadataPropertyName(propertyName)
        );
    }

    final void setMetadataPropertyNameAndCheck(final SpreadsheetMetadataPropertyName<?> propertyName,
                                               final HistoryToken expected) {
        this.setMetadataPropertyNameAndCheck(
                this.createHistoryToken(),
                propertyName,
                expected
        );
    }

    final void setMetadataPropertyNameAndCheck(final HistoryToken token,
                                               final SpreadsheetMetadataPropertyName<?> propertyName,
                                               final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setMetadataPropertyName(propertyName),
                () -> token + " setMetadataPropertyName " + propertyName
        );
    }

    // setPatternKind...................................................................................................

    @Test
    public final void testSetPatternKindNullFails() {
        final T token = this.createHistoryToken();
        assertThrows(
                NullPointerException.class,
                () -> token.setPatternKind(null)
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final HistoryToken expected) {
        this.setPatternKindAndCheck(
                token,
                Optional.empty(),
                expected
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final SpreadsheetPatternKind patternKind,
                                      final HistoryToken expected) {
        this.setPatternKindAndCheck(
                token,
                Optional.of(patternKind),
                expected
        );
    }

    final void setPatternKindAndCheck(final HistoryToken token,
                                      final Optional<SpreadsheetPatternKind> patternKind,
                                      final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setPatternKind(patternKind),
                () -> token + " setPatternKind " + patternKind
        );
    }

    // setSave..........................................................................................................

    @Test
    public final void testSetSaveNullFails() {
        final T token = this.createHistoryToken();
        assertThrows(
                NullPointerException.class,
                () -> token.setSave(null)
        );
    }

    final void setSaveAndCheck(final HistoryToken token,
                               final String save,
                               final HistoryToken expected) {
        this.checkEquals(
                expected,
                token.setSave(save),
                () -> token + " setSave " + save
        );
    }

    // equals...........................................................................................................

    @Test
    public final void testEqualsDifferentType() {
        this.checkNotEquals(
                new FakeHistoryToken() {
                    @Override
                    public UrlFragment urlFragment() {
                        return UrlFragment.SLASH;
                    }
                }
        );
    }

    @Test
    public final void testEqualsDifferentTypeSameUrgent() {
        final T token = this.createHistoryToken();

        this.checkNotEquals(
                token,
                new FakeHistoryToken() {
                    @Override
                    public UrlFragment urlFragment() {
                        return token.urlFragment();
                    }
                }
        );
    }

    final void urlFragmentAndCheck(final String expected) {
        this.urlFragmentAndCheck(
                this.createHistoryToken(),
                expected
        );
    }

    final void urlFragmentAndCheck(final HasUrlFragment fragment,
                                   final String expected) {
        this.urlFragmentAndCheck(
                fragment,
                UrlFragment.with(expected)
        );
    }

    final void urlFragmentAndCheck(final HasUrlFragment fragment,
                                   final UrlFragment expected) {
        this.checkEquals(
                expected,
                fragment.urlFragment(),
                () -> "urlFragment of " + fragment
        );
    }

    abstract T createHistoryToken();

    @Override
    public final T createObject() {
        return this.createHistoryToken();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
