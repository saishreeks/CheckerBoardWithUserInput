package ooad_assignment1;

public class RowColumnMapping {
    private int row;
    private int col;

    RowColumnMapping(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 31 * result + row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof RowColumnMapping)) {
            return false;
        }

        RowColumnMapping obj = (RowColumnMapping) o;

        return obj.row == row &&
                obj.col == col;
    }



}
