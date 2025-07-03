package sudoku_desafio_dio.sudoku.model;

import java.util.HashSet;
import java.util.Set;

public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;
    private final Set<Integer> draftValues = new HashSet<>();

    public Space(final int expected, final boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        if (fixed){
            actual = expected;
        }
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(final Integer actual) {
        if (!fixed) {
            this.actual = actual;
        }
    }

    public void clearSpace(){
        setActual(null);
        clearDraftValues();
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void addDraftValue(int value) {
        if (!fixed && value >= 1 && value <= 9) {
            draftValues.add(value);
        }
    }

    public void removeDraftValue(int value) {
        draftValues.remove(value);
    }

    public void clearDraftValues() {
        draftValues.clear();
    }

    public Set<Integer> getDraftValues() {
        return new HashSet<>(draftValues);
    }
}
