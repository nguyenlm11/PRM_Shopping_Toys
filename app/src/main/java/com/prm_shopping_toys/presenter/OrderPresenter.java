package com.prm_shopping_toys.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.prm_shopping_toys.R;
import com.prm_shopping_toys.api.OrderAPI;
import com.prm_shopping_toys.model.Cart;
import com.prm_shopping_toys.utils.ApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPresenter {
    private Context context;
    private OrderAPI orderAPI;

    public OrderPresenter(Context context) {
        this.context = context;
        this.orderAPI = ApiClient.getClient().create(OrderAPI.class);
    }

    public String createOrderBill(String userName, double totalPrice, List<Cart> items) {
        String filePath = null;
        try {
            filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/OrderBill.pdf";
            File file = new File(filePath);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                PdfWriter writer = new PdfWriter(fos);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);
                document.setMargins(20, 20, 20, 20);
                // Add a title for the invoice
                document.add(new Paragraph("Payment Invoice")
                        .setBold()
                        .setFontSize(20)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(10));
                // Add customer name
                document.add(new Paragraph("Customer name: " + userName).setMarginBottom(10));
                // Add order details section
                document.add(new Paragraph("Order details:").setBold().setFontSize(15).setMarginBottom(5));
                for (Cart item : items) {
                    String toyName = item.getToy().getName();
                    int quantity = item.getQuantity();
                    double price = item.getToy().getPrice();
                    // Format the price properly
                    String formattedPrice = String.format("%,.0f", price * quantity);
                    document.add(new Paragraph("x" + quantity + " - " + toyName + " - Price: " + formattedPrice + " VND"));
                }
                // Add a divider
                document.add(new Paragraph("__________________________________________________________________")
                        .setBold()
                        .setFontSize(15)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(10)
                        .setMarginBottom(10));
                // Add total amount
                String formattedTotalPrice = String.format("%,.0f", totalPrice);
                document.add(new Paragraph("Total amount: " + formattedTotalPrice + " VND").setBold());
                // Add QR code if available
                Bitmap qrBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.qrcode_bank);
                if (qrBitmap != null) {
                    byte[] qrBytes = getBytesFromBitmap(qrBitmap);
                    if (qrBytes != null) {
                        ImageData imageData = ImageDataFactory.create(qrBytes);
                        Image qrImage = new Image(imageData);
                        qrImage.setAutoScale(true);
                        qrImage.setMarginTop(20);
                        document.add(qrImage);
                    }
                }
                // Close the document properly
                document.close();
                // Upload the file to the server
                uploadOrderToServer(filePath, userName, totalPrice, items);
                return filePath;
            }
        } catch (IOException e) {
            Toast.makeText(context, "Error creating PDF", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    private void uploadOrderToServer(String filePath, String userName, double totalPrice, List<Cart> items) {
        File pdfFile = new File(filePath);
        if (pdfFile.exists()) {
            RequestBody pdfRequestBody = RequestBody.create(MediaType.parse("application/pdf"), pdfFile);
            MultipartBody.Part pdfPart = MultipartBody.Part.createFormData("bill_pdf", pdfFile.getName(), pdfRequestBody);

            RequestBody userIdBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getCurrentUserId()));
            RequestBody userNameBody = RequestBody.create(MediaType.parse("text/plain"), userName);
            RequestBody totalPriceBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(totalPrice));
            List<Item> itemList = new ArrayList<>();
            for (Cart item : items) {
                itemList.add(new Item(item.getToy().getName(), item.getQuantity(), item.getToy().getPrice()));
            }
            RequestBody itemsBody = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(itemList));

            orderAPI.createOrder(
                    userIdBody,
                    totalPriceBody,
                    itemsBody,
                    pdfPart
            ).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Order created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Error creating order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "Connection error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Invoice file not found", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private int getCurrentUserId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }

    public class Item {
        private String toy_name;
        private int quantity;
        private double price;

        public Item(String toy_name, int quantity, double price) {
            this.toy_name = toy_name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getToyName() {
            return toy_name;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
