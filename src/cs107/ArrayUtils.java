package cs107;

/**
 * Utility class to manipulate arrays.
 * @apiNote First Task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class ArrayUtils {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private ArrayUtils(){}

    // ==================================================================================
    // =========================== ARRAY EQUALITY METHODS ===============================
    // ==================================================================================

    /**
     * Check if the content of both arrays is the same
     * @param a1 (byte[]) - First array
     * @param a2 (byte[]) - Second array
     * @return (boolean) - true if both arrays have the same content (or both null), false otherwise
     * @throws AssertionError if one of the parameters is null
     */
    public static boolean equals(byte[] a1, byte[] a2){
        if( a1.length != a2.length )
            return false;
        for( int i = 0; i <= a1.length - 1; i++ )
            if( a1[i] != a2[i] )
                return false;
        return true;
       //return Helper.fail("Not Implemented");
    }

    /**
     * Check if the content of both arrays is the same
     * @param a1 (byte[][]) - First array
     * @param a2 (byte[][]) - Second array
     * @return (boolean) - true if both arrays have the same content (or both null), false otherwise
     * @throws AssertionError if one of the parameters is null
     */
    public static boolean equals(byte[][] a1, byte[][] a2){
        if( a1 == null && a2 == null)
            return true;
        if( a1.length != a2.length )
            return false;
        else if( a1[1].length != a2[1].length )
            return false;
        for ( int i = 0; i <= a1.length-1; i++ )
            for( int j = 0; j <= a1[i].length - 1; j++ )
                if( a1[i][j] != a2[i][j] )
                    return false;
        return true;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ============================ ARRAY WRAPPING METHODS ==============================
    // ==================================================================================

    /**
     * Wrap the given value in an array
     * @param value (byte) - value to wrap
     * @return (byte[]) - array with one element (value)
     */
    public static byte[] wrap(byte value){
        byte[] wrapped = {value};
        return wrapped;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ========================== INTEGER MANIPULATION METHODS ==========================
    // ==================================================================================

    /**
     * Create an Integer using the given array. The input needs to be considered
     * as "Big Endian"
     * (See handout for the definition of "Big Endian")
     * @param bytes (byte[]) - Array of 4 bytes
     * @return (int) - Integer representation of the array
     * @throws AssertionError if the input is null or the input's length is different from 4
     */
    public static int toInt(byte[] bytes){
        assert bytes != null;
        assert bytes.length == 4;
        int num = 0;
        for( byte b : bytes )
            num = (num << 8) + (b & 0xFF);
        return num;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Separate the Integer (word) to 4 bytes. The Memory layout of this integer is "Big Endian"
     * (See handout for the definition of "Big Endian")
     * @param value (int) - The integer
     * @return (byte[]) - Big Endian representation of the integer
     */
    public static byte[] fromInt(int value)
    {
        byte[] bytes = new byte[4];
        for( int i = 3; i >= 0; i-- )
        {
            bytes[i] = (byte)(value & 0xFF);
            value = value >> 8;
        }
        return bytes;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ========================== ARRAY CONCATENATION METHODS ===========================
    // ==================================================================================

    /**
     * Concatenate a given sequence of bytes and stores them in an array
     * @param bytes (byte ...) - Sequence of bytes to store in the array
     * @return (byte[]) - Array representation of the sequence
     * @throws AssertionError if the input is null
     */
    public static byte[] concat(byte ... bytes){
        assert bytes != null;
        int i = 0;
        byte a[] = new byte[bytes.length];
        for( byte b : bytes ) {
            a[i] = b;
            i++;
        }
        return a;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Concatenate a given sequence of arrays into one array
     * @param tabs (byte[] ...) - Sequence of arrays
     * @return (byte[]) - Array representation of the sequence
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null.
     */
    public static byte[] concat(byte[] ... tabs){
        int j = 0;
        for( byte[] t : tabs )
            for( int i = 0; i < t.length; i++ )
                j++;
        byte[] a = new byte[j];
        j = 0;
        for( byte[] t : tabs )
            for( int i = 0; i < t.length; i++ )
            {
                a[j] = t[i];
                j++;
            }
        assert a != null;
        return a;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // =========================== ARRAY EXTRACTION METHODS =============================
    // ==================================================================================

    /**
     * Extract an array from another array
     * @param input (byte[]) - Array to extract from
     * @param start (int) - Index in the input array to start the extract from
     * @param length (int) - The number of bytes to extract
     * @return (byte[]) - The extracted array
     * @throws AssertionError if the input is null or start and length are invalid.
     * start + length should also be smaller than the input's length
     */
    public static byte[] extract(byte[] input, int start, int length){
        assert length >=0;
        assert start + length >= input.length;
        assert start >= 0 && start < input.length;
        assert input != null;

        byte[] a = new byte[length];
        for( int i = 0; i < length; i++ )
        {
            a[i] = input[start + i];
        }
        return a;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Create a partition of the input array.
     * (See handout for more information on how this method works)
     * @param input (byte[]) - The original array
     * @param sizes (int ...) - Sizes of the partitions
     * @return (byte[][]) - Array of input's partitions.
     * The order of the partition is the same as the order in sizes
     * @throws AssertionError if one of the parameters is null
     * or the sum of the elements in sizes is different from the input's length
     */
    public static byte[][] partition(byte[] input, int ... sizes) {
        assert input != null;
        assert sizes != null;
        int sum = 0;
        for( int size : sizes )
            sum += size;
        assert sum == input.length;

        int c = 0;
        int j = 0;
        byte[][] partitions = new byte [sizes.length][];
        for (int size : sizes)
        {
            partitions[j] = new byte[size];
            for (int i = 0; i < size; i++)
            {
                partitions[j][i] = input[c];
                c++;
            }
            j++;
        }
        return partitions;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ============================== ARRAY FORMATTING METHODS ==========================
    // ==================================================================================

    /**
     * Format a 2-dim integer array
     * where each dimension is a direction in the image to
     * a 2-dim byte array where the first dimension is the pixel
     * and the second dimension is the channel.
     * See handouts for more information on the format.
     * @param input (int[][]) - image data
     * @return (byte [][]) - formatted image data
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null
     */
    public static byte[][] imageToChannels(int[][] input)
    {
        assert input != null;
        int size = input[0].length;
        boolean flag = true;
        for( int i =0; i < input.length; i++) {
            assert input[i] != null;
            if (input[i].length != size)
                flag = false;
        }
        assert flag;

        int c = 0;
        byte aux, aux1;
        for( int i = 0; i < input.length; i++ )
        {
            for( int j = 0; j < input[i].length; j++ )
                c++;
        }
        byte[][] output = new byte[c][4];
        byte[][] outputfinal = new byte[c][4];
        c = 0;
        for( int i = 0; i < input.length; i++ )
        {
            for( int j = 0; j < input[i].length; j++ )
            {
                output[c] = fromInt(input[i][j]);
                byte [] tab1 = { output[c][1], output[c][2], output[c][3] };
                byte [] tab2 = { output [c][0] };
                outputfinal[c] = concat(tab1, tab2);
                c++;
            }

        }
        return outputfinal;
    }

    /**
     * Format a 2-dim byte array where the first dimension is the pixel
     * and the second is the channel to a 2-dim int array where the first
     * dimension is the height and the second is the width
     * @param input (byte[][]) : linear representation of the image
     * @param height (int) - Height of the resulting image
     * @param width (int) - Width of the resulting image
     * @return (int[][]) - the image data
     * @throws AssertionError if the input is null
     * or one of the inner arrays of input is null
     * or input's length differs from width * height
     * or height is invalid
     * or width is invalid
     */
    public static int[][] channelsToImage(byte[][] input, int height, int width)
    {
        assert input != null;
        for( int i =0; i < input.length; i++) {
            assert input[i] != null;
            assert input[i].length == 4;
        }
        assert input.length == height*width;
        int [][] output = new int [height][width];
        byte [] tab3 = new byte[4];
        int c = 0;
        int k = 0;
        for( int i = 0; i < height * width; i ++ )
        {
            if( c == width ) {
                c = 0;
                k++;
            }
            byte[] tab1 = { input[i][0], input[i][1], input[i][2] };
            byte[] tab2 = { input[i][3] };
            tab3 = concat( tab2, tab1 );
            output[k][c] = toInt(tab3);
            c++;
        }
        return output;
        //return Helper.fail("Not Implemented");
    }

}