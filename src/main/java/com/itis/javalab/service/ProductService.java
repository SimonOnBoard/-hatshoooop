package com.itis.javalab.service;

import com.itis.javalab.dao.ProductDao;
import com.itis.javalab.dao.ProductDaoImpl;
import com.itis.javalab.models.Product;
import com.itis.javalab.models.ProductDTO;
import com.itis.javalab.dto.ShowProductDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private static ProductDao productDao;
    public static void loadConfig(String[] properties) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(properties[0], properties[1], properties[2]);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        productDao  = new ProductDaoImpl(connection);
    }

    public static String addnewProduct(JsonWorker jsonWorker) {
        Product product = jsonWorker.readProduct();
        productDao.save(product);
        return jsonWorker.prepareSuccessSaveMessage("Товар успешно сохранён");
    }

    public static synchronized String registrePayment(JsonWorker jsonWorker) {
        ProductDTO info = jsonWorker.readProductInfo();
        Optional<Product> product = productDao.findByName(info.getName());
        if(product.isPresent()){
            Product product1 = product.get();
            if(!product1.getEnded().booleanValue()){
                if(product1.getCount() >= info.getCount()){
                    if(!BalanceService.checkAvaliableBalance(jsonWorker,
                            product1.getPrice(),info.getCount())){
                        return jsonWorker.prepareCustomFailMessage("Недостаточно средств");
                    }
                    product1.setCount(product1.getCount() - info.getCount());
                    productDao.update(product1);
                    Long id = jsonWorker.jwt.getClaim("id").asLong();
                    LocalDateTime now = productDao.savePaymentAct(id, product1.getId(), info.getCount());
                    info.setDateTime(Timestamp.valueOf(now).getTime());
                    BalanceService.setBalance(jsonWorker, product1.getPrice(), info.getCount());
                    return jsonWorker.preparePaymentSuccessMessage(info);
                }
                else{
                    return jsonWorker.prepareCustomFailMessage("Столько товара нет, вы хотите купить слишком много," +
                            "или обладает неактуальными данными");
                }
            }
            else{
                return jsonWorker.prepareCustomFailMessage("Товар временно удалён из продажи");
            }
        }
        else{
            return jsonWorker.prepareCustomFailMessage("Товар не найден");
        }
    }

    public static String getProductListViaPagination(JsonWorker jsonWorker) {
        Long page = Long.parseLong(jsonWorker.getPayloadParam("number"));
        Long size = Long.parseLong(jsonWorker.getPayloadParam("size"));
        List<Product> products = productDao.findProductsOnPage(size,(page-1)*size);
        List<ShowProductDTO> products1 = new ArrayList<>();
        products.stream().forEach(product -> products1.add(new ShowProductDTO(
                product.getName(),product.getPrice(),product.getCount())));
        return jsonWorker.preparedListOfProducts(products1);
    }

    public static String removeProduct(JsonWorker jsonWorker) {
        ProductDTO info = jsonWorker.readProductInfo();
        Optional<Product> product = productDao.findByName(info.getName());
        if(product.isPresent()){
            Product product1 = product.get();
            product1.setEnded(true);
            productDao.update(product1);
            return jsonWorker.prepareSuccessSaveMessage("Товар успешно изъят из продажи, " +
                    "Вы можете просмотреть его в истории");
        }
        else{
            return jsonWorker.prepareCustomFailMessage("Товар не найден");
        }
    }
}
