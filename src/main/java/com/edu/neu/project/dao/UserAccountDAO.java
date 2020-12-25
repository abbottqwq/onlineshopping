package com.edu.neu.project.dao;

import com.edu.neu.project.authority.AuthorityEnum;
import com.edu.neu.project.entity.UserAccount;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAccountDAO extends AbstractHibernateDao<UserAccount> {
    public UserAccount findByUsername(String username) {
        Session session = this.getCurrentSession();
        Criteria crit = session.createCriteria(UserAccount.class);
        crit.add(Restrictions.eq("username", username));
        return (UserAccount) crit.uniqueResult();
    }

    public List<UserAccount> findAllByAuthority(AuthorityEnum authorityEnum) {
        Session session = this.getCurrentSession();
        Criteria crit = session.createCriteria(UserAccount.class);
        crit.add(Restrictions.eq("authority", authorityEnum));
        return crit.list();
    }
}
