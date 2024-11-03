package cn.edu.xmu.javaee.productdemoaop.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.productdemoaop.dao.ProductDao;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.dao.bo.User;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * 获取某个商品信息，仅展示相关内容
     *
     * @param id 商品id
     * @return 商品对象
     */
    @Transactional(rollbackFor = {BusinessException.class})
    public Product retrieveProductByID(Long id, boolean all) throws BusinessException {
        logger.debug("findProductById: id = {}, all = {}", id, all);
        return productDao.retrieveProductByID(id, all);
    }

    /**
     * 用商品名称搜索商品
     *
     * @param name 商品名称
     * @return 商品对象
     */
    @Transactional
    public List<Product> retrieveProductByName(String name, boolean all) throws BusinessException {
        return productDao.retrieveProductByName(name, all);
    }

    /**
     * 新增商品
     * @param product 新商品信息
     * @return 新商品
     */
    @Transactional
    public Product createProduct(Product product, User user) throws BusinessException {
        return productDao.createProduct(product, user);
    }

    /**
     * 修改商品
     * @param product 修改商品信息
     */
    @Transactional
    public void modifyProduct(Product product, User user) throws BusinessException {
        productDao.modiProduct(product, user);
    }

    /** 删除商品
     * @param id 商品id
     * @return 删除是否成功
     */
    @Transactional
    public void deleteProduct(Long id) throws BusinessException {
        productDao.deleteProduct(id);
    }

    @Transactional
    public Product findProductById_manual(Long id) throws BusinessException {
        logger.debug("findProductById_manual: id = {}", id);
        return productDao.findProductByID_manual(id);
    }

    /**
     * 用商品名称搜索商品
     *
     * @param name 商品名称
     * @return 商品对象
     */
    @Transactional
    public List<Product> findProductByName_manual(String name) throws BusinessException {
        return productDao.findProductByName_manual(name);
    }

    /**
     * 使用 join 查询根据商品id获取商品列表
     *
     * @param id 商品名称
     * @return 商品对象列表
     */

    @Transactional
    public Product getProductAllPoById(Long id) throws BusinessException {
        logger.debug("getProductAllPoById: id = {}", id);
        return productDao.getProductAllPoById(id);
    }

    /**
     * 用商品名称搜索商品
     *
     * @param name 商品名称
     * @return 商品对象
     */

    @Transactional
    public List<Product> findProductByName_join(String name) throws BusinessException {
        logger.debug("findProductByName_join: name = {}", name);

        // 调用 DAO 方法获取 ProductAllPo 列表
        List<ProductAllPo> allPoList = productDao.findProductByName_join(name);

        // 转换为 Product 列表
        List<Product> productList = allPoList.stream()
                .map(this::convertToProduct) // 调用转换方法
                .collect(Collectors.toList());

        return productList;
    }

    private Product convertToProduct(ProductAllPo productAllPo) {
        Product product = new Product();
        // 设置基本属性
        product.setId(productAllPo.getId());
        product.setSkuSn(productAllPo.getSkuSn());
        product.setName(productAllPo.getName());
        product.setOriginalPrice(productAllPo.getOriginalPrice());
        product.setWeight(productAllPo.getWeight());
        product.setBarcode(productAllPo.getBarcode());
        product.setUnit(productAllPo.getUnit());
        product.setOriginPlace(productAllPo.getOriginPlace());
        product.setCommissionRatio(productAllPo.getCommissionRatio());
        product.setFreeThreshold(productAllPo.getFreeThreshold());
        product.setStatus(productAllPo.getStatus());
        product.setGmtCreate(productAllPo.getGmtCreate());
        product.setGmtModified(productAllPo.getGmtModified());
        return product;
    }



}
