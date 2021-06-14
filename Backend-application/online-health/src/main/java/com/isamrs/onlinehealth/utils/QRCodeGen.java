package com.isamrs.onlinehealth.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;

public class QRCodeGen
{
    /*---------------------QR GENERATION-----------------------*/
    //static function that creates QR Code
    public static void gen(String data, String path, String charset, Map map, int h, int w) throws WriterException, IOException
    {
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }
    //main() method
    public static String generateQRcode(String data, String path) throws WriterException, IOException, NotFoundException
    {
        //Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        //generates QR code with Low level(L) error correction capability
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        //invoking the user-defined method that creates the QR code
        try {
            gen(data, path, charset, hashMap, 200, 200);//increase or decrease height and width accodingly
        }catch (Exception e){
            return "error";
        }
        //prints if the QR code is generated
       return "success";
    }

    public static String generate(String data, String path){
        String response = "";
        try {
            response = generateQRcode(data, path);
        }catch (Exception e){
            return "error";
        }
        return response;
    }
    /*---------------------QR GENERATION-----------------------*/

    /*------------------------QR READ--------------------------*/
    //user-defined method that reads the QR code
    public static String r(String path, String charset, Map map) throws FileNotFoundException, IOException, NotFoundException
    {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
        Result rslt = new MultiFormatReader().decode(binaryBitmap);
        return rslt.getText();
    }
    //main() method
    public static String readQRcode(String path) throws WriterException, IOException, NotFoundException {
        //Encoding charset to be used
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
        //generates QR code with Low level(L) error correction capability
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
           return r(path, charset, hintMap);
        }catch (Exception e){
            return "error";
        }
    }
    public static String read(String path){
        try {
            return readQRcode(path);
        }catch (Exception e){
            return "error";
        }
    }
    /*------------------------QR READ--------------------------*/
}
