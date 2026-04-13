package com.libraryms.service;
import com.google.zxing.*; import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix; import com.google.zxing.qrcode.QRCodeWriter;
import com.libraryms.model.BarCode; import com.libraryms.model.Book;
import com.libraryms.repository.BarCodeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.*; import java.nio.file.*; import java.util.*; import java.util.Base64;

@Service @Transactional
public class BarCodeService {
    private final BarCodeRepository barCodeRepository;
    @Value("${app.qr.output-dir:qrcodes/}") private String outputDir;
    public BarCodeService(BarCodeRepository r){ this.barCodeRepository=r; }

    public byte[] generateBarCodeForBook(Book book) throws Exception {
        String data = String.format("{\"bookId\":%d,\"title\":\"%s\",\"isbn\":\"%s\"}", book.getId(), book.getTitle(), book.getIsbn());
        byte[] bytes = generateBytes(data, 300, 300);
        Path dir = Paths.get(outputDir); Files.createDirectories(dir);
        Path fp = dir.resolve("book_"+book.getId()+".png"); Files.write(fp, bytes);
        BarCode bc = barCodeRepository.findByBookId(book.getId()).orElse(BarCode.builder().book(book).build());
        bc.setQrData(data); bc.setFilePath(fp.toString()); bc.setCode("BOOK-"+book.getId());
        barCodeRepository.save(bc); return bytes;
    }
    public byte[] getBarCodeForBook(Long bookId) throws Exception {
        BarCode bc = barCodeRepository.findByBookId(bookId).orElseThrow(()->new RuntimeException("Barcode not found"));
        return Files.readAllBytes(Paths.get(bc.getFilePath()));
    }
    public String getBarCodeBase64(Long bookId) throws Exception {
        return "data:image/png;base64,"+Base64.getEncoder().encodeToString(getBarCodeForBook(bookId));
    }
    private byte[] generateBytes(String data, int w, int h) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType,Object> hints = new HashMap<>(); hints.put(EncodeHintType.MARGIN,1);
        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, w, h, hints);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix,"PNG",out); return out.toByteArray();
    }
}
