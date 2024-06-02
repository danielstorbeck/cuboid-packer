package packergui.helpticker;

public class Messages {
    /**
     * Hidden constructor
     */
    private Messages() {
        // not to be instanciated
    }

    static String BOX_LIST_MESSAGE = " To edit table cells double click on them, edit them, and then press return. "
            + " To add more lines to the table, right click on the present lines," + " or on the table's title. "
            + " To generate lines with random box definitions, go to the box generator tab. "
            + " To select lines just point, click and drag. " + " To delete selected lines, press control backspace. "
            + " A line turns red when the box does not fit into the container. ";

    static String BOX_GENERATOR_MESSAGE = " Adjust the no. of boxes, min. and max. edge lengths,"
            + " cubeness, and press the generate button. "
            + " Cubeness is the tendency of all edges to have equal length. ";

    static String BOX_DISPLAY_MESSAGE = " To generate the three-dimensional representation of the boxes"
            + " press the display button. " + " When the result is displayed, point, click and drag on the image"
            + " to rotate the container. ";

    static String PLEASE_WAIT_MESSAGE = " The graphical representation is being calculated. " + " Please wait. ";
}
