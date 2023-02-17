package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.ToStringTesting;
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

public abstract class SpreadsheetHistoryHashTokenTokenTestCase<T extends SpreadsheetHistoryHashToken> implements ClassTesting<T>, ToStringTesting {

    SpreadsheetHistoryHashTokenTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(),
                expected
        );
    }

    final void urlFragmentAndCheck(final HasUrlFragment fragment,
                                   final String expected) {
        this.checkEquals(
                UrlFragment.with(expected),
                fragment.urlFragment()
        );
    }

    abstract T createSpreadsheetHistoryHashToken();

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
