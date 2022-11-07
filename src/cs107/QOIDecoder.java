package cs107;

import java.lang.reflect.Array;

import static cs107.Helper.Image;

/**
 * "Quite Ok Image" Decoder
 * @apiNote Third task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIDecoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */
    private QOIDecoder(){}

    // ==================================================================================
    // =========================== QUITE OK IMAGE HEADER ================================
    // ==================================================================================

    /**
     * Extract useful information from the "Quite Ok Image" header
     * @param header (byte[]) - A "Quite Ok Image" header
     * @return (int[]) - Array such as its content is {width, height, channels, color space}
     * @throws AssertionError See handouts section 6.1
     */
    public static int[] decodeHeader(byte[] header)
    {
        byte[] widthb = ArrayUtils.extract( header, 4, 4);
        byte[] heightb = ArrayUtils.extract( header, 8, 4);
        int width = ArrayUtils.toInt(widthb), height = ArrayUtils.toInt(heightb);
        int[] output = {width, height, (int)header[12], (int)header[13]};
        return output;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // =========================== ATOMIC DECODING METHODS ==============================
    // ==================================================================================

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param alpha (byte) - Alpha component of the pixel
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.1
     */
    public static int decodeQoiOpRGB(byte[][] buffer, byte[] input, byte alpha, int position, int idx)
    {
        for( int i = 0; i < 3; i++ )
            buffer[position][i] = input[idx + i];
        buffer[position][3] = alpha;
        return 3;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Store the pixel in the buffer and return the number of consumed bytes
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param input (byte[]) - Stream of bytes to read from
     * @param position (int) - Index in the buffer
     * @param idx (int) - Index in the input
     * @return (int) - The number of consumed bytes
     * @throws AssertionError See handouts section 6.2.2
     */
    public static int decodeQoiOpRGBA(byte[][] buffer, byte[] input, int position, int idx)
    {
        for( int i = 0; i < 4; i++ )
            buffer[position][i] = input[idx + i];
        return 4;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Create a new pixel following the "QOI_OP_DIFF" schema.
     * @param previousPixel (byte[]) - The previous pixel
     * @param chunk (byte) - A "QOI_OP_DIFF" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.4
     */
    public static byte[] decodeQoiOpDiff(byte[] previousPixel, byte chunk)
    {
        byte[] output;
        byte dr = 0b00_11_00_00, dg = 0b00_00_11_00, db = 0b00_00_00_11;
        dr &= chunk;
        dg &= chunk;
        db &= chunk;
        output = ArrayUtils.concat(ArrayUtils.wrap((byte)((dr >> 4) + previousPixel[0] - 2)));
        output = ArrayUtils.concat(output, ArrayUtils.wrap((byte)((dg >> 2) + previousPixel[1] - 2)));
        output = ArrayUtils.concat(output, ArrayUtils.wrap((byte)(db + previousPixel[2] - 2)));
        output = ArrayUtils.concat(output, ArrayUtils.wrap(previousPixel[3]));
        return output;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Create a new pixel following the "QOI_OP_LUMA" schema
     * @param previousPixel (byte[]) - The previous pixel
     * @param data (byte[]) - A "QOI_OP_LUMA" data chunk
     * @return (byte[]) - The newly created pixel
     * @throws AssertionError See handouts section 6.2.5
     */
    public static byte[] decodeQoiOpLuma(byte[] previousPixel, byte[] data)
    {
        byte [] output;
        byte dg = 0b00_11_11_11;
        byte dbg = 0b00_00_11_11;
        byte drg = 0b00_00_11_11;
        dg &= data[0] - 32;
        dbg &= data[1];
        drg &= data[1]>>4;
        byte dr = (byte)(dg + (drg));
        byte db = (byte)(dg + (dbg));
        output = ArrayUtils.concat(ArrayUtils.wrap((byte)(dr + previousPixel[0] - 8)));
        output = ArrayUtils.concat(output, ArrayUtils.wrap((byte)(dg + previousPixel[1])));
        output = ArrayUtils.concat(output, ArrayUtils.wrap((byte)(db + previousPixel[2] - 8)));
        output = ArrayUtils.concat(output, ArrayUtils.wrap(previousPixel[3]));
        return output;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Store the given pixel in the buffer multiple times
     * @param buffer (byte[][]) - Buffer where to store the pixel
     * @param pixel (byte[]) - The pixel to store
     * @param chunk (byte) - a QOI_OP_RUN data chunk
     * @param position (int) - Index in buffer to start writing from
     * @return (int) - number of written pixels in buffer
     * @throws AssertionError See handouts section 6.2.6
     */
    public static int decodeQoiOpRun(byte[][] buffer, byte[] pixel, byte chunk, int position)
    {
        byte countb = 0b00_11_11_11;
        chunk &= countb;
        int count = chunk;
        for( int i = 0; i <= count; i++ )
            buffer[position + i] = pixel;
        return count;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ========================= GLOBAL DECODING METHODS ================================
    // ==================================================================================

    /**
     * Decode the given data using the "Quite Ok Image" Protocol
     * @param data (byte[]) - Data to decode
     * @param width (int) - The width of the expected output
     * @param height (int) - The height of the expected output
     * @return (byte[][]) - Decoded "Quite Ok Image"
     * @throws AssertionError See handouts section 6.3
     */
    public static byte[][] decodeData(byte[] data, int width, int height)
    {

        return Helper.fail("Not Implemented");
    }

    /**
     * Decode a file using the "Quite Ok Image" Protocol
     * @param content (byte[]) - Content of the file to decode
     * @return (Image) - Decoded image
     * @throws AssertionError if content is null
     */
    public static Image decodeQoiFile(byte[] content){
        return Helper.fail("Not Implemented");
    }

}