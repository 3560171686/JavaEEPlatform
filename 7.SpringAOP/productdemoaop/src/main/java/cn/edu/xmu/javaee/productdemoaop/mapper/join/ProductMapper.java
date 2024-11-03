package cn.edu.xmu.javaee.productdemoaop.mapper.join;

import cn.edu.xmu.javaee.productdemoaop.dao.bo.Product;
import cn.edu.xmu.javaee.productdemoaop.mapper.manual.po.ProductAllPo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.OnSalePo;
import cn.edu.xmu.javaee.productdemoaop.mapper.generator.po.ProductPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select({
            "SELECT p.id AS product_id, p.goods_id, p.sku_sn, p.name, p.original_price, p.weight,",
            "p.barcode, p.unit, p.origin_place, p.commission_ratio, p.free_threshold, p.status,",
            "p.creator_id, p.creator_name, p.modifier_id, p.modifier_name, p.gmt_create, p.gmt_modified,",
            "o.id AS on_sale_id, o.price, o.begin_time, o.end_time, o.quantity, o.max_quantity,",
            "o.creator_id AS on_sale_creator_id, o.creator_name AS on_sale_creator_name,",
            "o.modifier_id AS on_sale_modifier_id, o.modifier_name AS on_sale_modifier_name,",
            "o.gmt_create AS on_sale_gmt_create, o.gmt_modified AS on_sale_gmt_modified",
            "FROM goods_product p",
            "LEFT JOIN goods_onsale o ON p.id = o.product_id",
            "WHERE p.id = #{id,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="product_id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="sku_sn", property="skuSn", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="original_price", property="originalPrice", jdbcType=JdbcType.BIGINT),
            @Result(column="weight", property="weight", jdbcType=JdbcType.BIGINT),
            @Result(column="barcode", property="barcode", jdbcType=JdbcType.VARCHAR),
            @Result(column="unit", property="unit", jdbcType=JdbcType.VARCHAR),
            @Result(column="origin_place", property="originPlace", jdbcType=JdbcType.VARCHAR),
            @Result(column="commission_ratio", property="commissionRatio", jdbcType=JdbcType.INTEGER),
            @Result(column="free_threshold", property="freeThreshold", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            // 将销售信息映射到 onSaleList
            @Result(property="onSaleList", javaType=List.class, column="on_sale_id",
                    many=@Many(select="cn.edu.xmu.javaee.productdemoaop.mapper.manual.ProductAllMapper.selectOnSaleInfo")),
            // 将关联产品映射到 otherProduct
            @Result(property="otherProduct", javaType=List.class, column="goods_id",
                    many=@Many(select="cn.edu.xmu.javaee.productdemoaop.mapper.join.ProductMapper.selectOtherProducts"))
    })
    ProductAllPo getProductAllPoById(Long id);

    @Select({
            "SELECT p.id AS id, p.sku_sn, p.name, p.original_price, p.weight, p.barcode,",
            "p.unit, p.origin_place, p.commission_ratio, p.free_threshold, p.status, p.creator_id,",
            "p.creator_name, p.modifier_id, p.modifier_name, p.gmt_create, p.gmt_modified,",
            "o.id AS on_sale_id, o.price AS on_sale_price, o.begin_time AS on_sale_begin_time,",
            "o.end_time AS on_sale_end_time, o.quantity AS on_sale_quantity, o.max_quantity AS on_sale_max_quantity,",
            "o.creator_id AS on_sale_creator_id, o.creator_name AS on_sale_creator_name,",
            "o.modifier_id AS on_sale_modifier_id, o.modifier_name AS on_sale_modifier_name,",
            "o.gmt_create AS on_sale_gmt_create, o.gmt_modified AS on_sale_gmt_modified",
            "FROM goods_product p",
            "LEFT JOIN goods_onsale o ON p.id = o.product_id",
            "WHERE (p.name LIKE CONCAT('%', #{name}, '%') OR",
            "p.goods_id IN (",
            "SELECT goods_id FROM goods_product WHERE name LIKE CONCAT('%', #{name}, '%'))) ",
            "AND o.begin_time <= NOW() AND o.end_time > NOW()"
    })
    List<ProductAllPo> findProductByName_join(String name);

    @Select({
            "SELECT id, goods_id, sku_sn, name, original_price, weight, barcode, unit, origin_place",
            "FROM goods_product",
            "WHERE goods_id = #{goodsId} AND id != #{productId}"
    })
    List<ProductPo> selectOtherProducts(Long goodsId, Long productId);
}
