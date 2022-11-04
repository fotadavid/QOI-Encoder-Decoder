package cs107;

import java.lang.reflect.Array;

/**
 * "Quite Ok Image" Encoder
 * @apiNote Second task of the 2022 Mini Project
 * @author Hamza REMMAL (hamza.remmal@epfl.ch)
 * @version 1.3
 * @since 1.0
 */
public final class QOIEncoder {

    /**
     * DO NOT CHANGE THIS, MORE ON THAT IN WEEK 7.
     */


    private QOIEncoder(){}

    // ==================================================================================
    // ============================ QUITE OK IMAGE HEADER ===============================
    // ==================================================================================

    /**
     * Generate a "Quite Ok Image" header using the following parameters
     * @param image (Helper.Image) - Image to use
     * @throws AssertionError if the colorspace or the number of channels is corrupted or if the image is null.
     *  (See the "Quite Ok Image" Specification or the handouts of the project for more information)
     * @return (byte[]) - Corresponding "Quite Ok Image" Header
     */
    public static byte[] qoiHeader(Helper.Image image){
        int [][] input = image.data();
        byte[] header = new byte[14];
        header[0] = (byte) 'q';
        header[1] = (byte) 'o';
        header[2] = (byte) 'i';
        header[3] = (byte) 'f';
        int height = input.length;
        int width = input[0].length;
        byte[] conv , conv1;
        conv = ArrayUtils.fromInt(height);
        conv1 = ArrayUtils.fromInt(width);
        for( int i = 0; i <= 3; i++ )
        {
            header[i + 4] = conv1[i];
            header[i + 8] = conv[i];
        }
        header[12] = image.channels();
        header[13] = image.color_space();
        return header;
    }

    // ==================================================================================
    // ============================ ATOMIC ENCODING METHODS =============================
    // ==================================================================================

    /**
     * Encode the given pixel using the QOI_OP_RGB schema
     * @param pixel (byte[]) - The Pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) - Encoding of the pixel using the QOI_OP_RGB schema
     */
    public static byte[] qoiOpRGB(byte[] pixel)
    {
        byte[] rgb = { pixel[0], pixel[1], pixel[2] };
        byte[] tag = { QOISpecification.QOI_OP_RGB_TAG };
        byte[] output;
        output = ArrayUtils.concat(tag, rgb);
        return output;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Encode the given pixel using the QOI_OP_RGBA schema
     * @param pixel (byte[]) - The pixel to encode
     * @throws AssertionError if the pixel's length is not 4
     * @return (byte[]) Encoding of the pixel using the QOI_OP_RGBA schema
     */
    public static byte[] qoiOpRGBA(byte[] pixel)
    {
        byte[] rgba = pixel;
        byte[] tag = { QOISpecification.QOI_OP_RGBA_TAG };
        byte[] output;
        output = ArrayUtils.concat(tag, rgba);
        return output;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Encode the index using the QOI_OP_INDEX schema
     * @param index (byte) - Index of the pixel
     * @throws AssertionError if the index is outside the range of all possible indices
     * @return (byte[]) - Encoding of the index using the QOI_OP_INDEX schema
     */
    public static byte[] qoiOpIndex(byte index)
    {
        return ArrayUtils.wrap(index);
        //return Helper.fail("Not Implemented");
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_DIFF schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpDiff(byte[] diff)
    {
        byte output = 0;
        byte[] outputfin;
        output |= QOISpecification.QOI_OP_DIFF_TAG;
        output |= diff[2] + 2;
        output |= (diff[1] + 2) << 2;
        output |= (diff[0] + 2) << 4;
        outputfin = ArrayUtils.wrap(output);
        return outputfin;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Encode the difference between 2 pixels using the QOI_OP_LUMA schema
     * @param diff (byte[]) - The difference between 2 pixels
     * @throws AssertionError if diff doesn't respect the constraints
     * or diff's length is not 3
     * (See the handout for the constraints)
     * @return (byte[]) - Encoding of the given difference
     */
    public static byte[] qoiOpLuma(byte[] diff)
    {
        byte[] outputfin = new byte[2];
        outputfin[0] |= QOISpecification.QOI_OP_LUMA_TAG;
        outputfin[0] |= ((diff[1] + 32));
        outputfin[1] |= (((diff[0] - diff[1]) + 8)<<4);
        outputfin[1] |= (((diff[2] - diff[1]) + 8));
        return outputfin;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Encode the number of similar pixels using the QOI_OP_RUN schema
     * @param count (byte) - Number of similar pixels
     * @throws AssertionError if count is not between 0 (exclusive) and 63 (exclusive)
     * @return (byte[]) - Encoding of count
     */
    public static byte[] qoiOpRun(byte count)
    {
        byte[] outputfinal = new byte[1];
        outputfinal[0] |= QOISpecification.QOI_OP_RUN_TAG;
        outputfinal[0] |= (count - 1);
        return outputfinal;
        //return Helper.fail("Not Implemented");
    }

    // ==================================================================================
    // ============================== GLOBAL ENCODING METHODS  ==========================
    // ==================================================================================

    /**
     * Encode the given image using the "Quite Ok Image" Protocol
     * (See handout for more information about the "Quite Ok Image" protocol)
     * @param image (byte[][]) - Formatted image to encode
     * @return (byte[]) - "Quite Ok Image" representation of the image
     */
    public static byte[] encodeData(byte[][] image)
    {
        byte[][] hashtab = new byte [64][4];
        byte[] lastpixel = QOISpecification.START_PIXEL;
        byte[] output = new byte[0];
        int count = 0;
        int length = image.length;
        byte index;
        for( int i = 0; i < image.length; i++ )
            {
                if( ArrayUtils.equals(lastpixel,image[i]) ) {
                    count++;
                    if (count == 62 || i == length - 1) {
                        output = ArrayUtils.concat(output, qoiOpRun((byte) (count)));
                        count = 0;
                    }
                } else {
                    if (count > 0) {
                        output = ArrayUtils.concat(output, qoiOpRun((byte) (count)));
                        count = 0;
                    }
                index = QOISpecification.hash(image[i]);
                if( ArrayUtils.equals(hashtab[index], image[i]) )
                        output = ArrayUtils.concat(output, qoiOpIndex(index));
                else {
                    hashtab[index] = image[i];
                    if (image[i][3] == lastpixel[3]) {
                        byte dr = (byte) (image[i][0] - lastpixel[0]);
                        //dr += 2;
                        byte dg = (byte) (image[i][1] - lastpixel[1]);
                        //dg += 2;
                        byte db = (byte) (image[i][2] - lastpixel[2]);
                        //db += 2;
                        if ((-3 < dr && dr < 2) && (-3 < dg && dg < 2) && (-3 < db && db < 2)) {
                            byte[] diff = {dr, dg, db};
                            output = ArrayUtils.concat(output, qoiOpDiff(diff));
                        } else if ((-33 < dg && dg < 32) && (-9 < (dr - dg) && (dr - dg) < 8) && (-9 < (db - dg) && (db - dg) < 8)) {
                            byte[] diff = {dr, dg, db};
                            output = ArrayUtils.concat(output, qoiOpLuma(diff));
                        } else output = ArrayUtils.concat(output, qoiOpRGB(image[i]));
                    } else output = ArrayUtils.concat(output, qoiOpRGBA(image[i]));
                }
            }
                lastpixel = image[i];
            }
        return output;
        //return Helper.fail("Not Implemented");
    }

    /**
     * Creates the representation in memory of the "Quite Ok Image" file.
     * @apiNote THE FILE IS NOT CREATED YET, THIS IS JUST ITS REPRESENTATION.
     * TO CREATE THE FILE, YOU'LL NEED TO CALL Helper::write
     * @param image (Helper.Image) - Image to encode
     * @return (byte[]) - Binary representation of the "Quite Ok File" of the image
     * @throws AssertionError if the image is null
     */
    public static byte[] qoiFile(Helper.Image image)
    {
        byte[] output = new byte[0];
        byte[][] chan = ArrayUtils.imageToChannels(image.data());
        output = ArrayUtils.concat(output, qoiHeader(image));
        output = ArrayUtils.concat(output, encodeData(chan));
        output = ArrayUtils.concat(output, QOISpecification.QOI_EOF);
        return output;
        //return Helper.fail("Not Implemented");
    }

}