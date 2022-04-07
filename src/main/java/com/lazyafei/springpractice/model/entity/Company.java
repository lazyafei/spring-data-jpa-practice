package com.lazyafei.springpractice.model.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Data
@ToString(exclude = {"users","relationships"})
@Entity
@Table(name = "company")
@Where(clause = "is_delete=0")
@SQLDelete(sql = "update company c set c.is_delete=b'1' where c.id=?")
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_ID_SEQ")
    @SequenceGenerator(name = "COMPANY_ID_SEQ", sequenceName = "COMPANY_ID_SEQ", allocationSize = 1)
    protected Integer id;

    @Column(name = "tenant_id")
    private Integer tenantId;

    @Column
    private String name;//公司名称

    @Column
    private String code;//公司代码

    @Column
    private String avatar;//公司头像

    @Column
    private String remark;//公司代码

    @Column(name = "create_user_id")
    private Integer createUserId;

    @Column(name = "last_update_user_id")
    private Integer lastUpdateUserId;

}
